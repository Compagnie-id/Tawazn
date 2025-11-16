package id.compagnie.tawazn.platform.ios

import co.touchlab.kermit.Logger
import id.compagnie.tawazn.domain.model.AppUsage
import kotlinx.datetime.LocalDate

/**
 * iOS implementation using Screen Time API
 *
 * This implementation uses a helper interface that bridges to Swift code.
 * The actual Swift implementation is in ScreenTimeBridge.swift which wraps
 * the native iOS Screen Time APIs.
 *
 * Required Frameworks (add to Xcode project):
 * - FamilyControls.framework
 * - DeviceActivity.framework
 * - ManagedSettings.framework
 *
 * Required Entitlements:
 * - com.apple.developer.family-controls (in iosApp.entitlements)
 *
 * Info.plist additions:
 * - NSFamilyControlsUsageDescription: "We need this permission to help you manage screen time"
 *
 * To use this implementation:
 * 1. Make sure ScreenTimeBridge.swift is included in your iOS app target
 * 2. Initialize the helper from Swift: IOSAppMonitorHelper.initialize(bridge: ScreenTimeBridge.shared)
 * 3. The Kotlin code will use the initialized helper to call Swift methods
 */
actual class IOSAppMonitorImpl : IOSAppMonitor {

    private val logger = Logger.withTag("IOSAppMonitor")

    override suspend fun requestAuthorization(): Result<Unit> = runCatching {
        logger.i { "Requesting Family Controls authorization" }

        val helper = IOSAppMonitorHelper.instance
        if (helper == null) {
            logger.w { "IOSAppMonitorHelper not initialized - Screen Time features will not work" }
            logger.i { "To fix this, initialize from Swift: IOSAppMonitorHelper.initialize(...)" }
            // Return success but log warning - this allows the app to run without crashing
            return@runCatching
        }

        helper.requestAuthorization { success, error ->
            if (success) {
                logger.i { "Family Controls authorization granted" }
            } else {
                logger.e { "Family Controls authorization failed: ${error ?: "Unknown error"}" }
            }
        }
    }

    override fun isAuthorized(): Boolean {
        val helper = IOSAppMonitorHelper.instance
        if (helper == null) {
            logger.w { "IOSAppMonitorHelper not initialized" }
            return false
        }
        return helper.isAuthorized()
    }

    override suspend fun getAppUsageStats(
        startDate: LocalDate,
        endDate: LocalDate
    ): List<AppUsage> {
        logger.i { "Getting app usage stats from $startDate to $endDate" }

        // Note: iOS Screen Time API doesn't provide detailed usage stats programmatically
        // due to privacy restrictions. The DeviceActivity framework only provides
        // monitoring capabilities, not historical data access.
        //
        // For now, we return an empty list. To get usage data, you would need to:
        // 1. Set up DeviceActivityMonitor extension
        // 2. Implement callbacks in the extension
        // 3. Store the data locally when the extension is triggered

        logger.w { "iOS Screen Time API does not provide programmatic access to usage history due to privacy restrictions" }
        return emptyList()
    }

    override suspend fun shieldApp(bundleIdentifier: String): Result<Unit> = runCatching {
        logger.i { "Shielding app: $bundleIdentifier" }

        val helper = IOSAppMonitorHelper.instance
        if (helper == null) {
            logger.e { "IOSAppMonitorHelper not initialized" }
            throw IllegalStateException("IOSAppMonitorHelper not initialized")
        }

        val success = helper.shieldApp(bundleIdentifier)
        if (success) {
            logger.i { "Successfully shielded app: $bundleIdentifier" }
        } else {
            logger.e { "Failed to shield app: $bundleIdentifier - check authorization" }
            throw IllegalStateException("Failed to shield app - authorization may be required")
        }
    }

    override suspend fun unshieldApp(bundleIdentifier: String): Result<Unit> = runCatching {
        logger.i { "Unshielding app: $bundleIdentifier" }

        val helper = IOSAppMonitorHelper.instance
        if (helper == null) {
            logger.e { "IOSAppMonitorHelper not initialized" }
            throw IllegalStateException("IOSAppMonitorHelper not initialized")
        }

        val success = helper.unshieldApp(bundleIdentifier)
        if (success) {
            logger.i { "Successfully unshielded app: $bundleIdentifier" }
        } else {
            logger.e { "Failed to unshield app: $bundleIdentifier" }
            throw IllegalStateException("Failed to unshield app")
        }
    }

    override suspend fun setupActivityMonitoring(): Result<Unit> = runCatching {
        logger.i { "Setting up device activity monitoring" }

        val helper = IOSAppMonitorHelper.instance
        if (helper == null) {
            logger.e { "IOSAppMonitorHelper not initialized" }
            throw IllegalStateException("IOSAppMonitorHelper not initialized")
        }

        val success = helper.setupMonitoring()
        if (success) {
            logger.i { "Successfully set up activity monitoring" }
        } else {
            logger.e { "Failed to setup activity monitoring - check authorization" }
            throw IllegalStateException("Failed to setup activity monitoring")
        }
    }
}

/**
 * Helper interface for calling Swift Screen Time APIs from Kotlin
 * This is implemented on the Swift side and injected into Kotlin
 */
interface IOSScreenTimeHelper {
    fun requestAuthorization(completion: (success: Boolean, error: String?) -> Unit)
    fun isAuthorized(): Boolean
    fun shieldApp(bundleIdentifier: String): Boolean
    fun unshieldApp(bundleIdentifier: String): Boolean
    fun setupMonitoring(): Boolean
}

/**
 * Static helper to store and access the Swift bridge instance
 */
object IOSAppMonitorHelper {
    var instance: IOSScreenTimeHelper? = null
        private set

    fun initialize(helper: IOSScreenTimeHelper) {
        instance = helper
    }
}
