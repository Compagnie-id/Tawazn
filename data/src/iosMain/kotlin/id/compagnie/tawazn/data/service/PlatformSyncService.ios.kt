package id.compagnie.tawazn.data.service

import co.touchlab.kermit.Logger
import id.compagnie.tawazn.domain.repository.AppRepository
import id.compagnie.tawazn.domain.repository.BlockedAppRepository
import id.compagnie.tawazn.domain.repository.UsageRepository
import id.compagnie.tawazn.platform.ios.IOSPlatformSync
import id.compagnie.tawazn.platform.ios.ScreenTimeApi
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * iOS implementation of PlatformSyncService
 *
 * Uses Koin dependency injection to get ScreenTimeApi and properly initialize IOSPlatformSync.
 */
actual class PlatformSyncService : KoinComponent {

    private val logger = Logger.withTag("PlatformSyncService")
    private lateinit var platformSync: IOSPlatformSync

    // Inject ScreenTimeApi from Koin
    private val screenTimeApi: ScreenTimeApi by inject()

    actual fun initialize(
        appRepository: AppRepository,
        blockedAppRepository: BlockedAppRepository,
        usageRepository: UsageRepository
    ) {
        logger.i { "Initializing iOS platform sync service" }

        // Create IOSPlatformSync with all required dependencies including ScreenTimeApi
        platformSync = IOSPlatformSync(
            screenTimeApi = screenTimeApi,
            appRepository = appRepository,
            blockedAppRepository = blockedAppRepository,
            usageRepository = usageRepository
        )

        logger.i { "iOS platform sync service initialized successfully" }
    }

    actual suspend fun syncInstalledApps() {
        logger.i { "iOS does not sync installed apps (privacy restriction)" }
        // iOS doesn't allow listing all installed apps
        // Apps are discovered through usage data instead
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
        logger.i { "Performing full iOS platform sync..." }
        platformSync.performFullSync()
    }

    actual fun hasRequiredPermissions(): Boolean {
        return platformSync.isAuthorized()
    }

    actual suspend fun requestPermissions(): Boolean {
        logger.i { "Requesting Family Controls authorization..." }

        return platformSync.ensureAuthorizedAndSetup()
    }

    actual fun startBackgroundServices() {
        logger.i { "Starting iOS background services..." }

        // Set up device activity monitoring
        kotlinx.coroutines.MainScope().launch {
            platformSync.setupMonitoring()
            syncBlockedApps()
        }

        logger.i { "iOS background services started" }
    }

    actual fun stopBackgroundServices() {
        logger.i { "Stopping iOS background services..." }
        // iOS doesn't need explicit stop - managed by system
        logger.i { "iOS background services stopped" }
    }

    actual fun getPlatformInfo(): Map<String, String> {
        val isAuthorized = platformSync.isAuthorized()

        return mapOf(
            "platform" to "iOS",
            "osVersion" to platform.UIKit.UIDevice.currentDevice.systemVersion,
            "model" to platform.UIKit.UIDevice.currentDevice.model,
            "familyControlsAuthorized" to isAuthorized.toString(),
            "screenTimeAvailable" to "true", // iOS 15+ required by KMP setup
            "usageTrackingReady" to isAuthorized.toString(),
            "blockingReady" to isAuthorized.toString()
        )
    }
}

/**
 * Factory function for iOS
 */
actual fun createPlatformSyncService(): PlatformSyncService {
    return PlatformSyncService()
}

private fun kotlinx.coroutines.MainScope(): kotlinx.coroutines.CoroutineScope {
    return kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main)
}
