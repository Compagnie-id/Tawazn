package id.compagnie.tawazn.platform.desktop

import co.touchlab.kermit.Logger
import id.compagnie.tawazn.domain.repository.AppRepository
import id.compagnie.tawazn.domain.repository.BlockedAppRepository
import id.compagnie.tawazn.domain.repository.UsageRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Duration.Companion.seconds

/**
 * Synchronizes desktop platform data with repositories
 *
 * This class manages synchronization for Windows, macOS, and Linux:
 * - Syncs installed applications to AppRepository
 * - Monitors running applications
 * - Enforces app blocking by terminating blocked processes
 * - Tracks active window/app for usage statistics
 *
 * Platform Detection:
 * - Windows: PowerShell and WMI
 * - macOS: AppleScript and system commands
 * - Linux: Process monitoring and desktop files
 */
class DesktopPlatformSync(
    private val appRepository: AppRepository,
    private val blockedAppRepository: BlockedAppRepository,
    private val usageRepository: UsageRepository
) {
    private val logger = Logger.withTag("DesktopPlatformSync")
    private val osName = System.getProperty("os.name").lowercase()

    private val windowsMonitor by lazy { WindowsAppMonitor() }
    private val macMonitor by lazy { MacOSAppMonitor() }
    private val genericMonitor by lazy { DesktopAppMonitor() }

    private var monitoringJob: Job? = null
    private var blockingEnforcementJob: Job? = null

    /**
     * Sync installed apps to database
     */
    suspend fun syncInstalledApps() = withContext(Dispatchers.IO) {
        try {
            logger.i { "Syncing installed apps for $osName..." }

            val apps = when {
                osName.contains("windows") -> windowsMonitor.getInstalledApps()
                osName.contains("mac") -> macMonitor.getInstalledApps()
                else -> genericMonitor.getInstalledApps()
            }

            logger.i { "Found ${apps.size} installed apps" }

            // Upsert to database
            apps.forEach { app ->
                appRepository.upsertApp(app)
            }

            logger.i { "Successfully synced ${apps.size} apps" }
        } catch (e: Exception) {
            logger.e(e) { "Failed to sync installed apps" }
        }
    }

    /**
     * Start monitoring active apps for usage tracking
     * This runs continuously in the background
     */
    fun startActiveAppMonitoring(intervalSeconds: Int = 5) {
        monitoringJob?.cancel()

        monitoringJob = CoroutineScope(Dispatchers.IO).launch {
            logger.i { "Starting active app monitoring (interval: ${intervalSeconds}s)..." }

            while (isActive) {
                try {
                    val activeApp = when {
                        osName.contains("windows") -> windowsMonitor.getActiveWindow()
                        osName.contains("mac") -> macMonitor.getActiveApp()
                        else -> null
                    }

                    if (activeApp != null) {
                        logger.d { "Active app: $activeApp" }
                        // TODO: Track usage time in database
                    }

                } catch (e: Exception) {
                    logger.e(e) { "Error monitoring active app" }
                }

                delay(intervalSeconds.seconds)
            }
        }
    }

    /**
     * Stop active app monitoring
     */
    fun stopActiveAppMonitoring() {
        monitoringJob?.cancel()
        monitoringJob = null
        logger.i { "Stopped active app monitoring" }
    }

    /**
     * Start enforcing app blocking
     * This runs continuously and terminates blocked apps
     */
    fun startBlockingEnforcement(checkIntervalSeconds: Int = 2) {
        blockingEnforcementJob?.cancel()

        blockingEnforcementJob = CoroutineScope(Dispatchers.IO).launch {
            logger.i { "Starting blocking enforcement (interval: ${checkIntervalSeconds}s)..." }

            while (isActive) {
                try {
                    val blockedApps = blockedAppRepository.getBlockedApps().first()
                    val blockedPackages = blockedApps.map { it.packageName }

                    if (blockedPackages.isNotEmpty()) {
                        enforceBlocking(blockedPackages)
                    }

                } catch (e: Exception) {
                    logger.e(e) { "Error enforcing app blocking" }
                }

                delay(checkIntervalSeconds.seconds)
            }
        }
    }

    /**
     * Stop blocking enforcement
     */
    fun stopBlockingEnforcement() {
        blockingEnforcementJob?.cancel()
        blockingEnforcementJob = null
        logger.i { "Stopped blocking enforcement" }
    }

    /**
     * Enforce blocking by terminating blocked apps
     */
    private suspend fun enforceBlocking(blockedPackages: List<String>) = withContext(Dispatchers.IO) {
        blockedPackages.forEach { packageName ->
            try {
                val isRunning = when {
                    osName.contains("windows") -> windowsMonitor.isProcessRunning(packageName)
                    osName.contains("mac") -> macMonitor.isAppRunning(packageName)
                    else -> genericMonitor.isAppRunning(packageName)
                }

                if (isRunning) {
                    logger.i { "Blocked app detected: $packageName, terminating..." }

                    val success = when {
                        osName.contains("windows") -> windowsMonitor.killProcess(packageName)
                        osName.contains("mac") -> macMonitor.forceQuitApp(packageName)
                        else -> false
                    }

                    if (success) {
                        logger.i { "Successfully terminated: $packageName" }
                    } else {
                        logger.w { "Failed to terminate: $packageName" }
                    }
                }
            } catch (e: Exception) {
                logger.e(e) { "Error checking/blocking app: $packageName" }
            }
        }
    }

    /**
     * Sync blocked apps from database (no active enforcement, just logging)
     */
    suspend fun syncBlockedApps() = withContext(Dispatchers.IO) {
        try {
            logger.i { "Syncing blocked apps..." }

            val blockedApps = blockedAppRepository.getBlockedApps().first()
            logger.i { "Found ${blockedApps.size} blocked apps" }

            // Log blocked apps (enforcement happens in background job)
            blockedApps.forEach { app ->
                logger.d { "Blocked: ${app.appName} (${app.packageName})" }
            }

        } catch (e: Exception) {
            logger.e(e) { "Failed to sync blocked apps" }
        }
    }

    /**
     * Perform full sync
     */
    suspend fun performFullSync() = withContext(Dispatchers.IO) {
        logger.i { "Performing full platform sync..." }

        syncInstalledApps()
        syncBlockedApps()

        logger.i { "Full platform sync completed" }
    }

    /**
     * Start all background services
     */
    fun startBackgroundServices() {
        logger.i { "Starting desktop background services..." }

        startActiveAppMonitoring(intervalSeconds = 5)
        startBlockingEnforcement(checkIntervalSeconds = 2)

        logger.i { "Desktop background services started" }
    }

    /**
     * Stop all background services
     */
    fun stopBackgroundServices() {
        logger.i { "Stopping desktop background services..." }

        stopActiveAppMonitoring()
        stopBlockingEnforcement()

        logger.i { "Desktop background services stopped" }
    }

    /**
     * Get platform information
     */
    fun getPlatformInfo(): Map<String, String> {
        return mapOf(
            "os" to osName,
            "osVersion" to System.getProperty("os.version"),
            "platform" to when {
                osName.contains("windows") -> "Windows"
                osName.contains("mac") -> "macOS"
                osName.contains("linux") -> "Linux"
                else -> "Unknown"
            },
            "architecture" to System.getProperty("os.arch"),
            "monitoring" to if (monitoringJob?.isActive == true) "active" else "inactive",
            "blocking" to if (blockingEnforcementJob?.isActive == true) "active" else "inactive"
        )
    }
}

/**
 * Desktop Platform Sync Notes:
 *
 * Windows:
 * - Uses PowerShell for app queries
 * - Uses taskkill for process termination
 * - Monitors active window via Win32 API
 *
 * macOS:
 * - Uses AppleScript for app control
 * - Uses killall for force quit
 * - Monitors active app via System Events
 *
 * Linux:
 * - Uses package managers (dpkg, rpm, flatpak, snap)
 * - Uses kill/pkill for termination
 * - Window manager integration varies
 *
 * Limitations:
 * - Desktop blocking is less reliable than mobile
 * - Users with admin rights can bypass
 * - Some apps may require elevated permissions to terminate
 * - Usage tracking is approximate (based on active window)
 *
 * Best Practices:
 * - Use reasonable check intervals (2-5 seconds)
 * - Handle permission errors gracefully
 * - Provide clear user feedback when blocking occurs
 * - Consider showing a blocking overlay instead of just killing
 */
