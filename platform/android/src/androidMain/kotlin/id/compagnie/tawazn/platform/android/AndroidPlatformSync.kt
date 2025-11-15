package id.compagnie.tawazn.platform.android

import android.content.Context
import co.touchlab.kermit.Logger
import id.compagnie.tawazn.domain.repository.AppRepository
import id.compagnie.tawazn.domain.repository.BlockedAppRepository
import id.compagnie.tawazn.domain.repository.UsageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

/**
 * Synchronizes Android platform data with repositories
 *
 * This class bridges the gap between Android platform APIs and the app's data layer:
 * - Syncs usage data from UsageStatsManager to UsageRepository
 * - Syncs installed apps to AppRepository
 * - Updates blocked apps list in AccessibilityService
 */
class AndroidPlatformSync(
    private val context: Context,
    private val appRepository: AppRepository,
    private val blockedAppRepository: BlockedAppRepository,
    private val usageRepository: UsageRepository
) {
    private val logger = Logger.withTag("AndroidPlatformSync")
    private val appMonitor = AndroidAppMonitor(context)

    /**
     * Sync all installed apps to the database
     */
    suspend fun syncInstalledApps() = withContext(Dispatchers.IO) {
        try {
            logger.i { "Syncing installed apps..." }

            val installedApps = appMonitor.getInstalledApps()
            logger.i { "Found ${installedApps.size} installed apps" }

            // Upsert each app to the database
            installedApps.forEach { app ->
                appRepository.upsertApp(app)
            }

            logger.i { "Successfully synced ${installedApps.size} apps" }
        } catch (e: Exception) {
            logger.e(e) { "Failed to sync installed apps" }
        }
    }

    /**
     * Sync usage data from UsageStatsManager to database
     */
    suspend fun syncUsageData(daysBack: Int = 7) = withContext(Dispatchers.IO) {
        try {
            if (!appMonitor.hasUsageStatsPermission()) {
                logger.w { "Usage stats permission not granted" }
                return@withContext
            }

            logger.i { "Syncing usage data for last $daysBack days..." }

            val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
            val startDate = today.minus(daysBack, kotlinx.datetime.DateTimeUnit.DAY)

            val usageData = appMonitor.getAppUsageStats(startDate, today)
            logger.i { "Found ${usageData.size} usage records" }

            // Upsert usage data to database
            usageData.forEach { usage ->
                usageRepository.upsertUsage(usage)
            }

            logger.i { "Successfully synced ${usageData.size} usage records" }
        } catch (e: Exception) {
            logger.e(e) { "Failed to sync usage data" }
        }
    }

    /**
     * Update the blocked apps list in SharedPreferences
     * This is read by AppBlockingAccessibilityService
     */
    suspend fun syncBlockedApps() = withContext(Dispatchers.IO) {
        try {
            logger.i { "Syncing blocked apps to accessibility service..." }

            val blockedApps = blockedAppRepository.getBlockedApps().first()
            val packageNames = blockedApps.map { it.packageName }.toSet()

            logger.i { "Found ${packageNames.size} blocked apps" }

            // Save to SharedPreferences for AccessibilityService
            val prefs = context.getSharedPreferences("tawazn_blocked_apps", Context.MODE_PRIVATE)
            prefs.edit().putStringSet("blocked_packages", packageNames).apply()

            logger.i { "Successfully synced blocked apps" }
        } catch (e: Exception) {
            logger.e(e) { "Failed to sync blocked apps" }
        }
    }

    /**
     * Perform full sync of all data
     */
    suspend fun performFullSync() = withContext(Dispatchers.IO) {
        logger.i { "Performing full platform sync..." }

        syncInstalledApps()
        syncUsageData()
        syncBlockedApps()

        logger.i { "Full platform sync completed" }
    }

    /**
     * Check if all required permissions are granted
     */
    fun hasRequiredPermissions(): Boolean {
        val permissionHelper = PermissionHelper(context)
        val status = permissionHelper.getPermissionStatus()

        return status.usageTrackingReady && status.blockingReady
    }

    /**
     * Get permission status
     */
    fun getPermissionStatus(): PermissionStatus {
        val permissionHelper = PermissionHelper(context)
        return permissionHelper.getPermissionStatus()
    }
}

/**
 * Extension functions to make sync easier from other components
 */
suspend fun Context.syncAndroidPlatform(
    appRepository: AppRepository,
    blockedAppRepository: BlockedAppRepository,
    usageRepository: UsageRepository
) {
    val sync = AndroidPlatformSync(this, appRepository, blockedAppRepository, usageRepository)
    sync.performFullSync()
}
