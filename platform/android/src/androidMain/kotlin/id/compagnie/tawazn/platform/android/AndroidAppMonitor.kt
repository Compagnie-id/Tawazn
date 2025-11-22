@file:OptIn(ExperimentalTime::class)

package id.compagnie.tawazn.platform.android

import android.app.AppOpsManager
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Process
import android.provider.Settings
import co.touchlab.kermit.Logger
import id.compagnie.tawazn.domain.model.AppCategory
import id.compagnie.tawazn.domain.model.AppInfo
import id.compagnie.tawazn.domain.model.AppUsage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * Android implementation of AppMonitor using UsageStatsManager API
 *
 * Key Android APIs used:
 * - UsageStatsManager: Track app usage (requires PACKAGE_USAGE_STATS permission)
 * - PackageManager: Get installed apps information
 * - AccessibilityService: Required for app blocking functionality
 *
 * Permissions required in AndroidManifest.xml:
 * - <uses-permission android:name="android.permission.PACKAGE_USAGE_STATS" />
 * - <uses-permission android:name="android.permission.QUERY_ALL_PACKAGES" />
 */
class AndroidAppMonitor(
    private val context: Context
) : AppMonitor {

    private val logger = Logger.withTag("AndroidAppMonitor")
    private val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as? UsageStatsManager
    private val packageManager = context.packageManager

    override suspend fun getInstalledApps(): List<AppInfo> = withContext(Dispatchers.IO) {
        try {
            val packages = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)

            packages.mapNotNull { appInfo ->
                try {
                    // Only include apps that have a launcher intent (can be launched by user)
                    val launchIntent = packageManager.getLaunchIntentForPackage(appInfo.packageName)
                    if (launchIntent == null) {
                        logger.d { "Skipping ${appInfo.packageName} - no launcher intent" }
                        return@mapNotNull null
                    }

                    val packageInfo = packageManager.getPackageInfo(appInfo.packageName, 0)

                    // An app is considered a "system app" only if:
                    // 1. It's in the system partition (FLAG_SYSTEM) AND
                    // 2. It has NEVER been updated (FLAG_UPDATED_SYSTEM_APP not set)
                    // This excludes pure system apps but includes pre-installed user apps like YouTube, Gmail
                    val isPureSystemApp = (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0 &&
                                         (appInfo.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 0

                    AppInfo(
                        packageName = appInfo.packageName,
                        appName = packageManager.getApplicationLabel(appInfo).toString(),
                        iconPath = null, // Icon handling would require additional implementation
                        category = categorizeApp(appInfo),
                        isSystemApp = isPureSystemApp,
                        installDate = Instant.fromEpochMilliseconds(packageInfo.firstInstallTime),
                        lastUpdated = Instant.fromEpochMilliseconds(packageInfo.lastUpdateTime)
                    )
                } catch (e: Exception) {
                    logger.e(e) { "Error getting info for package: ${appInfo.packageName}" }
                    null
                }
            }
        } catch (e: Exception) {
            logger.e(e) { "Error getting installed apps" }
            emptyList()
        }
    }

    override suspend fun getAppUsageStats(
        startDate: LocalDate,
        endDate: LocalDate
    ): List<AppUsage> = withContext(Dispatchers.IO) {
        if (!hasUsageStatsPermission()) {
            logger.w { "Usage stats permission not granted" }
            return@withContext emptyList()
        }

        try {
            val startTime = startDate.atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds()
            val endTime = endDate.atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds() + (24 * 60 * 60 * 1000)

            // Use UsageEvents for accurate tracking
            val usageEvents = usageStatsManager?.queryEvents(startTime, endTime)
            if (usageEvents == null) {
                logger.w { "UsageStatsManager returned null" }
                return@withContext emptyList()
            }

            val appUsageMap = mutableMapOf<String, MutableMap<LocalDate, UsageData>>()
            val event = UsageEvents.Event()

            while (usageEvents.hasNextEvent()) {
                usageEvents.getNextEvent(event)

                val packageName = event.packageName ?: continue
                val timestamp = Instant.fromEpochMilliseconds(event.timeStamp)
                val date = timestamp.toLocalDateTime(TimeZone.currentSystemDefault()).date

                val packageMap = appUsageMap.getOrPut(packageName) { mutableMapOf() }
                val usageData = packageMap.getOrPut(date) { UsageData() }

                when (event.eventType) {
                    UsageEvents.Event.ACTIVITY_RESUMED -> {
                        usageData.foregroundTime = event.timeStamp
                        usageData.launchCount++
                        usageData.lastTimeUsed = event.timeStamp
                    }
                    UsageEvents.Event.ACTIVITY_PAUSED -> {
                        if (usageData.foregroundTime > 0) {
                            usageData.totalTime += (event.timeStamp - usageData.foregroundTime)
                            usageData.foregroundTime = 0
                        }
                    }
                }
            }

            // Convert to AppUsage list
            appUsageMap.flatMap { (packageName, dateMap) ->
                dateMap.map { (date, usageData) ->
                    val appName = try {
                        val appInfo = packageManager.getApplicationInfo(packageName, 0)
                        packageManager.getApplicationLabel(appInfo).toString()
                    } catch (e: Exception) {
                        packageName
                    }

                    val now = kotlin.time.Clock.System.now()
                    AppUsage(
                        packageName = packageName,
                        appName = appName,
                        usageDate = date,
                        totalTimeInForeground = usageData.totalTime.milliseconds,
                        launchCount = usageData.launchCount,
                        lastTimeUsed = if (usageData.lastTimeUsed > 0) {
                            Instant.fromEpochMilliseconds(usageData.lastTimeUsed)
                        } else null,
                        createdAt = now,
                        updatedAt = now
                    )
                }
            }
        } catch (e: Exception) {
            logger.e(e) { "Error getting app usage stats" }
            emptyList()
        }
    }

    override fun hasUsageStatsPermission(): Boolean {
        return try {
            val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as? AppOpsManager
            val mode = appOps?.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                Process.myUid(),
                context.packageName
            )
            mode == AppOpsManager.MODE_ALLOWED
        } catch (e: Exception) {
            logger.e(e) { "Error checking usage stats permission" }
            false
        }
    }

    override fun requestUsageStatsPermission() {
        try {
            val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: Exception) {
            logger.e(e) { "Error requesting usage stats permission" }
        }
    }

    override suspend fun isAppRunning(packageName: String): Boolean = withContext(Dispatchers.IO) {
        if (!hasUsageStatsPermission()) return@withContext false

        try {
            val currentTime = System.currentTimeMillis()
            val startTime = currentTime - (5 * 60 * 1000) // Last 5 minutes

            val usageEvents = usageStatsManager?.queryEvents(startTime, currentTime)
            val event = UsageEvents.Event()

            var isInForeground = false

            while (usageEvents?.hasNextEvent() == true) {
                usageEvents.getNextEvent(event)

                if (event.packageName == packageName) {
                    when (event.eventType) {
                        UsageEvents.Event.ACTIVITY_RESUMED -> isInForeground = true
                        UsageEvents.Event.ACTIVITY_PAUSED -> isInForeground = false
                    }
                }
            }

            isInForeground
        } catch (e: Exception) {
            logger.e(e) { "Error checking if app is running" }
            false
        }
    }

    override suspend fun blockApp(packageName: String): Result<Unit> = runCatching {
        // Note: App blocking on Android typically requires:
        // 1. AccessibilityService to detect app launches
        // 2. UsageStatsManager to monitor current app
        // 3. Drawing overlays or using DeviceAdmin for enforcement
        //
        // This would require additional implementation with:
        // - AccessibilityService implementation
        // - Overlay permission (SYSTEM_ALERT_WINDOW)
        // - Or DeviceAdmin API for stronger enforcement

        logger.w { "App blocking requires AccessibilityService implementation" }
        throw UnsupportedOperationException("App blocking requires AccessibilityService setup")
    }

    override suspend fun unblockApp(packageName: String): Result<Unit> = runCatching {
        logger.i { "Unblocking app: $packageName" }
        // Implementation would remove from blocked list in AccessibilityService
    }

    private fun categorizeApp(appInfo: ApplicationInfo): AppCategory {
        // Basic categorization - could be enhanced with ML or API
        val packageName = appInfo.packageName.lowercase()

        return when {
            packageName.contains("facebook") || packageName.contains("twitter") ||
            packageName.contains("instagram") || packageName.contains("tiktok") ||
            packageName.contains("snapchat") -> AppCategory.SOCIAL_MEDIA

            packageName.contains("youtube") || packageName.contains("netflix") ||
            packageName.contains("spotify") || packageName.contains("hulu") -> AppCategory.ENTERTAINMENT

            packageName.contains("gmail") || packageName.contains("whatsapp") ||
            packageName.contains("telegram") || packageName.contains("messenger") -> AppCategory.COMMUNICATION

            packageName.contains("game") || packageName.contains("play") -> AppCategory.GAMES

            packageName.contains("news") || packageName.contains("reddit") -> AppCategory.NEWS

            packageName.contains("shop") || packageName.contains("amazon") -> AppCategory.SHOPPING

            packageName.contains("fitness") || packageName.contains("health") -> AppCategory.HEALTH_FITNESS

            packageName.contains("bank") || packageName.contains("pay") -> AppCategory.FINANCE

            else -> AppCategory.OTHER
        }
    }

    private data class UsageData(
        var totalTime: Long = 0,
        var launchCount: Int = 0,
        var foregroundTime: Long = 0,
        var lastTimeUsed: Long = 0
    )
}
