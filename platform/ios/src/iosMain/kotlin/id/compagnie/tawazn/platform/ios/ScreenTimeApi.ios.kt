package id.compagnie.tawazn.platform.ios

import co.touchlab.kermit.Logger
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.datetime.LocalDate
import kotlin.coroutines.resume

/**
 * iOS implementation of ScreenTimeApi
 *
 * This is the actual implementation that will be used on iOS.
 * It delegates to a SwiftScreenTimeBridge which handles native iOS API calls.
 *
 * Architecture:
 * - ScreenTimeApi (interface) ← Platform-agnostic
 * - IOSScreenTimeApi (this class) ← iOS-specific Kotlin implementation
 * - SwiftScreenTimeBridge (interface) ← Contract for Swift
 * - SwiftScreenTimeBridgeImpl (Swift) ← Native iOS implementation
 *
 * This design allows for:
 * - Dependency injection (can inject mock bridge for testing)
 * - Clear separation between Kotlin and Swift
 * - Type-safe API with proper error handling
 */
class IOSScreenTimeApi(
    private val swiftBridge: SwiftScreenTimeBridge,
    private val logger: Logger = Logger.withTag("IOSScreenTimeApi")
) : ScreenTimeApi {

    override suspend fun requestAuthorization(): Result<Unit> = suspendCancellableCoroutine { continuation ->
        logger.i { "Requesting Family Controls authorization" }

        try {
            swiftBridge.requestAuthorization { success, errorMessage ->
                if (success) {
                    logger.i { "Family Controls authorization granted" }
                    continuation.resume(Result.success(Unit))
                } else {
                    val error = errorMessage ?: "Authorization denied"
                    logger.e { "Family Controls authorization failed: $error" }
                    continuation.resume(Result.failure(ScreenTimeException(error)))
                }
            }
        } catch (e: Exception) {
            logger.e(e) { "Exception while requesting authorization" }
            continuation.resume(Result.failure(e))
        }
    }

    override fun isAuthorized(): Boolean {
        return try {
            swiftBridge.isAuthorized()
        } catch (e: Exception) {
            logger.e(e) { "Failed to check authorization status" }
            false
        }
    }

    override fun getAuthorizationStatus(): AuthorizationStatus {
        return try {
            when (swiftBridge.getAuthorizationStatus()) {
                "not_determined" -> AuthorizationStatus.NOT_DETERMINED
                "denied" -> AuthorizationStatus.DENIED
                "approved" -> AuthorizationStatus.APPROVED
                else -> {
                    logger.w { "Unknown authorization status from Swift bridge" }
                    AuthorizationStatus.UNKNOWN
                }
            }
        } catch (e: Exception) {
            logger.e(e) { "Failed to get authorization status" }
            AuthorizationStatus.UNKNOWN
        }
    }

    override suspend fun shieldApp(bundleIdentifier: String): Result<Unit> = runCatching {
        logger.i { "Shielding app: $bundleIdentifier" }

        if (!isAuthorized()) {
            throw ScreenTimeException("Not authorized. Call requestAuthorization() first.")
        }

        val success = swiftBridge.shieldApp(bundleIdentifier)
        if (success) {
            logger.i { "Successfully shielded app: $bundleIdentifier" }
        } else {
            val error = "Failed to shield app: $bundleIdentifier"
            logger.e { error }
            throw ScreenTimeException(error)
        }
    }

    override suspend fun shieldApps(bundleIdentifiers: List<String>): Result<Unit> = runCatching {
        logger.i { "Shielding ${bundleIdentifiers.size} apps" }

        if (!isAuthorized()) {
            throw ScreenTimeException("Not authorized. Call requestAuthorization() first.")
        }

        if (bundleIdentifiers.isEmpty()) {
            logger.w { "Empty bundle identifiers list" }
            return@runCatching
        }

        val success = swiftBridge.shieldApps(bundleIdentifiers)
        if (success) {
            logger.i { "Successfully shielded ${bundleIdentifiers.size} apps" }
        } else {
            val error = "Failed to shield apps"
            logger.e { error }
            throw ScreenTimeException(error)
        }
    }

    override suspend fun unshieldApp(bundleIdentifier: String): Result<Unit> = runCatching {
        logger.i { "Unshielding app: $bundleIdentifier" }

        if (!isAuthorized()) {
            throw ScreenTimeException("Not authorized")
        }

        val success = swiftBridge.unshieldApp(bundleIdentifier)
        if (success) {
            logger.i { "Successfully unshielded app: $bundleIdentifier" }
        } else {
            val error = "Failed to unshield app: $bundleIdentifier"
            logger.e { error }
            throw ScreenTimeException(error)
        }
    }

    override suspend fun unshieldAllApps(): Result<Unit> = runCatching {
        logger.i { "Unshielding all apps" }

        if (!isAuthorized()) {
            throw ScreenTimeException("Not authorized")
        }

        val success = swiftBridge.unshieldAllApps()
        if (success) {
            logger.i { "Successfully unshielded all apps" }
        } else {
            val error = "Failed to unshield all apps"
            logger.e { error }
            throw ScreenTimeException(error)
        }
    }

    override suspend fun getShieldedApps(): Result<List<String>> = runCatching {
        logger.i { "Getting shielded apps" }

        if (!isAuthorized()) {
            logger.w { "Not authorized, returning empty list" }
            return@runCatching emptyList()
        }

        val shieldedApps = swiftBridge.getShieldedApps()
        logger.i { "Found ${shieldedApps.size} shielded apps" }
        shieldedApps
    }

    override suspend fun setupActivityMonitoring(activityName: String): Result<Unit> = runCatching {
        logger.i { "Setting up activity monitoring: $activityName" }

        if (!isAuthorized()) {
            throw ScreenTimeException("Not authorized")
        }

        val success = swiftBridge.startMonitoring(activityName)
        if (success) {
            logger.i { "Successfully started monitoring: $activityName" }
        } else {
            val error = "Failed to start monitoring: $activityName"
            logger.e { error }
            throw ScreenTimeException(error)
        }
    }

    override suspend fun stopActivityMonitoring(activityName: String): Result<Unit> = runCatching {
        logger.i { "Stopping activity monitoring: $activityName" }

        val success = swiftBridge.stopMonitoring(activityName)
        if (success) {
            logger.i { "Successfully stopped monitoring: $activityName" }
        } else {
            logger.w { "Failed to stop monitoring: $activityName" }
            // Don't throw error for stop, just log warning
        }
    }

    override suspend fun getAppUsageStats(
        startDate: LocalDate,
        endDate: LocalDate
    ): Result<List<AppUsageRecord>> {
        logger.w {
            "iOS Screen Time API does not provide programmatic access to usage history. " +
            "This is a privacy restriction by Apple. Returning empty list."
        }

        // Note: To get usage data on iOS, you would need to:
        // 1. Create a DeviceActivityMonitor app extension
        // 2. Implement callbacks in the extension
        // 3. Store data locally when callbacks are triggered
        // This is beyond the scope of the main app and requires additional setup

        return Result.success(emptyList())
    }

    override fun isAvailable(): Boolean {
        return swiftBridge.isAvailable()
    }
}

/**
 * Custom exception for Screen Time related errors
 */
class ScreenTimeException(message: String, cause: Throwable? = null) : Exception(message, cause)

/**
 * Factory function to create the iOS-specific ScreenTimeApi
 *
 * This function provides the actual implementation for iOS.
 * It retrieves the SwiftScreenTimeBridge from the DI container or initialization.
 */
actual fun createScreenTimeApi(): ScreenTimeApi {
    val bridge = getSwiftBridgeInstance()
        ?: throw IllegalStateException(
            "SwiftScreenTimeBridge not initialized. " +
            "Call initializeScreenTimeBridge() from Swift during app startup. " +
            "See docs/KMP_IOS_BEST_PRACTICES.md for setup instructions."
        )

    return IOSScreenTimeApi(bridge)
}

// Internal storage for the Swift bridge instance
// This will be set during app initialization from Swift
private var swiftBridgeInstance: SwiftScreenTimeBridge? = null

/**
 * Initialize the Swift bridge
 *
 * This function must be called from Swift during app initialization:
 * ```swift
 * ScreenTimeApiKt.initializeScreenTimeBridge(bridge: SwiftScreenTimeBridgeImpl())
 * ```
 *
 * @param bridge The Swift implementation of SwiftScreenTimeBridge
 */
fun initializeScreenTimeBridge(bridge: SwiftScreenTimeBridge) {
    swiftBridgeInstance = bridge
    Logger.withTag("ScreenTimeApi").i { "Swift bridge initialized successfully" }
}

/**
 * Get the current Swift bridge instance
 *
 * @return The initialized bridge, or null if not initialized
 */
internal fun getSwiftBridgeInstance(): SwiftScreenTimeBridge? = swiftBridgeInstance

/**
 * Clear the Swift bridge instance
 *
 * Useful for testing or cleanup
 */
internal fun clearSwiftBridgeInstance() {
    swiftBridgeInstance = null
}
