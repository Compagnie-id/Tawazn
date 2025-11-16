package id.compagnie.tawazn.platform.ios

import kotlinx.datetime.LocalDate

/**
 * Platform-agnostic interface for Screen Time functionality
 *
 * This interface abstracts the iOS Screen Time API to allow for:
 * - Platform-independent business logic
 * - Easy testing with mocks
 * - Separation of concerns
 *
 * Implementation is provided via expect/actual mechanism:
 * - iOS: Uses FamilyControls, DeviceActivity, and ManagedSettings frameworks
 * - Android: Returns stub implementation (not applicable)
 */
interface ScreenTimeApi {

    /**
     * Request authorization to use Screen Time features
     *
     * On iOS 15+, this prompts the user for Family Controls permission.
     * This must be called before any other Screen Time operations.
     *
     * @return Result.success if authorized, Result.failure with error details
     */
    suspend fun requestAuthorization(): Result<Unit>

    /**
     * Check if Screen Time authorization is currently granted
     *
     * @return true if authorized, false otherwise
     */
    fun isAuthorized(): Boolean

    /**
     * Get the current authorization status as a string
     *
     * @return One of: "not_determined", "denied", "approved", "unknown"
     */
    fun getAuthorizationStatus(): AuthorizationStatus

    /**
     * Shield (block) a specific app by its bundle identifier
     *
     * This will display the Screen Time shield UI when the user tries to open the app.
     * Requires authorization to be granted.
     *
     * @param bundleIdentifier The bundle ID of the app to shield (e.g., "com.apple.mobilesafari")
     * @return Result.success if shielded, Result.failure with error
     */
    suspend fun shieldApp(bundleIdentifier: String): Result<Unit>

    /**
     * Shield multiple apps at once
     *
     * More efficient than calling shieldApp multiple times.
     * Requires authorization to be granted.
     *
     * @param bundleIdentifiers List of bundle IDs to shield
     * @return Result.success if all shielded, Result.failure with error
     */
    suspend fun shieldApps(bundleIdentifiers: List<String>): Result<Unit>

    /**
     * Remove shield from a specific app
     *
     * @param bundleIdentifier The bundle ID of the app to unshield
     * @return Result.success if unshielded, Result.failure with error
     */
    suspend fun unshieldApp(bundleIdentifier: String): Result<Unit>

    /**
     * Remove shields from all apps
     *
     * @return Result.success if all unshielded, Result.failure with error
     */
    suspend fun unshieldAllApps(): Result<Unit>

    /**
     * Get list of currently shielded app bundle identifiers
     *
     * @return List of bundle IDs that are currently shielded
     */
    suspend fun getShieldedApps(): Result<List<String>>

    /**
     * Set up continuous device activity monitoring
     *
     * This enables the app to monitor app usage throughout the day.
     * On iOS, this uses DeviceActivity framework.
     *
     * @param activityName Unique name for this monitoring session
     * @return Result.success if monitoring started, Result.failure with error
     */
    suspend fun setupActivityMonitoring(activityName: String = "TawaznMonitoring"): Result<Unit>

    /**
     * Stop device activity monitoring
     *
     * @param activityName Name of the monitoring session to stop
     * @return Result.success if monitoring stopped, Result.failure with error
     */
    suspend fun stopActivityMonitoring(activityName: String = "TawaznMonitoring"): Result<Unit>

    /**
     * Get app usage statistics for a date range
     *
     * Note: iOS Screen Time API has privacy restrictions and may not provide
     * detailed historical data. This method may return limited or no data.
     *
     * @param startDate Start of the date range
     * @param endDate End of the date range
     * @return List of app usage records
     */
    suspend fun getAppUsageStats(
        startDate: LocalDate,
        endDate: LocalDate
    ): Result<List<AppUsageRecord>>

    /**
     * Check if Screen Time API is available on this device
     *
     * @return true if available (iOS 15.0+), false otherwise
     */
    fun isAvailable(): Boolean
}

/**
 * Authorization status for Screen Time features
 */
enum class AuthorizationStatus {
    /** User has not been asked for permission yet */
    NOT_DETERMINED,

    /** User denied permission */
    DENIED,

    /** User approved permission */
    APPROVED,

    /** Unknown state (shouldn't happen in normal operation) */
    UNKNOWN
}

/**
 * Represents app usage data for a specific time period
 *
 * Note: iOS provides limited usage data due to privacy restrictions
 */
data class AppUsageRecord(
    val bundleIdentifier: String,
    val appName: String?,
    val totalUsageSeconds: Long,
    val date: LocalDate
)

/**
 * Factory function to create the platform-specific ScreenTimeApi implementation
 *
 * This is an expect function that will be implemented differently on each platform:
 * - iOS: Returns IOSScreenTimeApi
 * - Android: Returns a stub/no-op implementation
 *
 * Use this function to obtain an instance, typically through dependency injection:
 * ```
 * val screenTimeApi = createScreenTimeApi()
 * ```
 */
expect fun createScreenTimeApi(): ScreenTimeApi
