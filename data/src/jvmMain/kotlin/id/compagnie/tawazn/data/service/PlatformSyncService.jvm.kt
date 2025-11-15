package id.compagnie.tawazn.data.service

import co.touchlab.kermit.Logger
import id.compagnie.tawazn.domain.repository.AppRepository
import id.compagnie.tawazn.domain.repository.BlockedAppRepository
import id.compagnie.tawazn.domain.repository.UsageRepository
import id.compagnie.tawazn.platform.desktop.DesktopPlatformSync

/**
 * Desktop (JVM) implementation of PlatformSyncService
 * Supports Windows, macOS, and Linux
 */
actual class PlatformSyncService {

    private val logger = Logger.withTag("PlatformSyncService")
    private lateinit var platformSync: DesktopPlatformSync

    actual fun initialize(
        appRepository: AppRepository,
        blockedAppRepository: BlockedAppRepository,
        usageRepository: UsageRepository
    ) {
        logger.i { "Initializing Desktop platform sync service" }

        platformSync = DesktopPlatformSync(
            appRepository = appRepository,
            blockedAppRepository = blockedAppRepository,
            usageRepository = usageRepository
        )
    }

    actual suspend fun syncInstalledApps() {
        logger.i { "Syncing installed apps..." }
        platformSync.syncInstalledApps()
    }

    actual suspend fun syncUsageData(daysBack: Int) {
        logger.i { "Desktop usage tracking limited - active window monitoring only" }
        // Desktop usage tracking is handled by active app monitoring
        // Historical data is not available like on mobile platforms
    }

    actual suspend fun syncBlockedApps() {
        logger.i { "Syncing blocked apps..." }
        platformSync.syncBlockedApps()
    }

    actual suspend fun performFullSync() {
        logger.i { "Performing full Desktop platform sync..." }
        platformSync.performFullSync()
    }

    actual fun hasRequiredPermissions(): Boolean {
        // Desktop apps typically don't require special permissions
        // But some operations may require admin/root access
        return true
    }

    actual suspend fun requestPermissions(): Boolean {
        logger.i { "Desktop platform does not require special permissions" }
        // On desktop, permissions are typically handled at OS level
        // Some features may require running as administrator/root
        return true
    }

    actual fun startBackgroundServices() {
        logger.i { "Starting Desktop background services..." }

        // Start active app monitoring and blocking enforcement
        platformSync.startBackgroundServices()

        logger.i { "Desktop background services started" }
    }

    actual fun stopBackgroundServices() {
        logger.i { "Stopping Desktop background services..." }

        platformSync.stopBackgroundServices()

        logger.i { "Desktop background services stopped" }
    }

    actual fun getPlatformInfo(): Map<String, String> {
        val info = platformSync.getPlatformInfo()

        return info + mapOf(
            "javaVersion" to System.getProperty("java.version"),
            "javaVendor" to System.getProperty("java.vendor"),
            "userHome" to System.getProperty("user.home"),
            "userName" to System.getProperty("user.name")
        )
    }
}

/**
 * Factory function for Desktop
 */
actual fun createPlatformSyncService(): PlatformSyncService {
    return PlatformSyncService()
}
