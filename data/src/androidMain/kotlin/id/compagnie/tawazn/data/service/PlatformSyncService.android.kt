package id.compagnie.tawazn.data.service

import android.content.Context
import co.touchlab.kermit.Logger
import id.compagnie.tawazn.domain.repository.AppRepository
import id.compagnie.tawazn.domain.repository.BlockedAppRepository
import id.compagnie.tawazn.domain.repository.UsageRepository
import id.compagnie.tawazn.platform.android.AndroidPlatformSync
import id.compagnie.tawazn.platform.android.PermissionHelper
import id.compagnie.tawazn.platform.android.UsageSyncWorker

/**
 * Android implementation of PlatformSyncService
 */
actual class PlatformSyncService(private val context: Context) {

    private val logger = Logger.withTag("PlatformSyncService")
    private lateinit var platformSync: AndroidPlatformSync
    private lateinit var permissionHelper: PermissionHelper

    actual fun initialize(
        appRepository: AppRepository,
        blockedAppRepository: BlockedAppRepository,
        usageRepository: UsageRepository
    ) {
        logger.i { "Initializing Android platform sync service" }

        platformSync = AndroidPlatformSync(
            context = context,
            appRepository = appRepository,
            blockedAppRepository = blockedAppRepository,
            usageRepository = usageRepository
        )

        permissionHelper = PermissionHelper(context)
    }

    actual suspend fun syncInstalledApps() {
        logger.i { "Syncing installed apps..." }
        platformSync.syncInstalledApps()
    }

    actual suspend fun syncUsageData(daysBack: Int) {
        logger.i { "Syncing usage data for last $daysBack days..." }
        platformSync.syncUsageData(daysBack)
    }

    actual suspend fun syncBlockedApps() {
        logger.i { "Syncing blocked apps..." }
        platformSync.syncBlockedApps()
    }

    actual suspend fun performFullSync() {
        logger.i { "Performing full Android platform sync..." }
        platformSync.performFullSync()
    }

    actual fun hasRequiredPermissions(): Boolean {
        return platformSync.hasRequiredPermissions()
    }

    actual suspend fun requestPermissions(): Boolean {
        logger.i { "Requesting Android permissions..." }

        val status = permissionHelper.getPermissionStatus()

        // Request missing permissions
        if (!status.hasUsageStats) {
            logger.i { "Requesting usage stats permission..." }
            permissionHelper.requestUsageStatsPermission()
            return false // User needs to grant in settings
        }

        if (!status.hasAccessibility) {
            logger.i { "Requesting accessibility permission..." }
            permissionHelper.requestAccessibilityPermission()
            return false // User needs to enable in settings
        }

        if (!status.hasOverlay) {
            logger.i { "Requesting overlay permission..." }
            permissionHelper.requestOverlayPermission()
            return false // User needs to grant in settings
        }

        logger.i { "All permissions granted" }
        return true
    }

    actual fun startBackgroundServices() {
        logger.i { "Starting Android background services..." }

        // Schedule periodic usage sync
        UsageSyncWorker.schedule(context)

        // Sync blocked apps immediately
        kotlinx.coroutines.MainScope().launch {
            syncBlockedApps()
        }

        logger.i { "Android background services started" }
    }

    actual fun stopBackgroundServices() {
        logger.i { "Stopping Android background services..." }

        // Cancel periodic sync
        UsageSyncWorker.cancel(context)

        logger.i { "Android background services stopped" }
    }

    actual fun getPlatformInfo(): Map<String, String> {
        val status = permissionHelper.getPermissionStatus()

        return mapOf(
            "platform" to "Android",
            "osVersion" to android.os.Build.VERSION.RELEASE,
            "sdkVersion" to android.os.Build.VERSION.SDK_INT.toString(),
            "manufacturer" to android.os.Build.MANUFACTURER,
            "model" to android.os.Build.MODEL,
            "usageStatsPermission" to status.hasUsageStats.toString(),
            "accessibilityPermission" to status.hasAccessibility.toString(),
            "overlayPermission" to status.hasOverlay.toString(),
            "allPermissionsGranted" to status.allGranted.toString(),
            "usageTrackingReady" to status.usageTrackingReady.toString(),
            "blockingReady" to status.blockingReady.toString()
        )
    }
}

/**
 * Factory function for Android
 */
actual fun createPlatformSyncService(): PlatformSyncService {
    throw IllegalStateException("Use createPlatformSyncService(context) for Android")
}

/**
 * Android-specific factory that requires Context
 */
fun createPlatformSyncService(context: Context): PlatformSyncService {
    return PlatformSyncService(context)
}

private fun kotlinx.coroutines.MainScope(): kotlinx.coroutines.CoroutineScope {
    return kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main)
}
