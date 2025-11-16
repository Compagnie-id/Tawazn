package id.compagnie.tawazn.platform.ios

/**
 * Protocol/Interface that Swift must implement
 *
 * This interface defines the contract between Kotlin and Swift code.
 * The Swift implementation (SwiftScreenTimeBridgeImpl) will:
 * 1. Implement this interface
 * 2. Call native iOS Screen Time APIs
 * 3. Return results to Kotlin via callbacks
 *
 * Design principles:
 * - Simple data types (String, Boolean) for easy Swift interop
 * - Callback-based async operations (Swift completion handlers)
 * - Clear error messaging
 */
interface SwiftScreenTimeBridge {

    /**
     * Request Family Controls authorization
     *
     * This is called from Kotlin and implemented in Swift.
     * Swift should call AuthorizationCenter.shared.requestAuthorization()
     *
     * @param completion Callback with (success: Boolean, errorMessage: String?)
     */
    fun requestAuthorization(completion: (success: Boolean, errorMessage: String?) -> Unit)

    /**
     * Check if Family Controls is currently authorized
     *
     * @return true if AuthorizationCenter.shared.authorizationStatus == .approved
     */
    fun isAuthorized(): Boolean

    /**
     * Get authorization status as a string
     *
     * @return "not_determined", "denied", "approved", or "unknown"
     */
    fun getAuthorizationStatus(): String

    /**
     * Shield a single app
     *
     * Swift should call ManagedSettingsStore().shield.applications to add the app
     *
     * @param bundleIdentifier Bundle ID of the app (e.g., "com.apple.mobilesafari")
     * @return true if successful, false otherwise
     */
    fun shieldApp(bundleIdentifier: String): Boolean

    /**
     * Shield multiple apps at once
     *
     * @param bundleIdentifiers List of bundle IDs
     * @return true if successful, false otherwise
     */
    fun shieldApps(bundleIdentifiers: List<String>): Boolean

    /**
     * Unshield a single app
     *
     * @param bundleIdentifier Bundle ID of the app to unshield
     * @return true if successful, false otherwise
     */
    fun unshieldApp(bundleIdentifier: String): Boolean

    /**
     * Unshield all currently shielded apps
     *
     * @return true if successful, false otherwise
     */
    fun unshieldAllApps(): Boolean

    /**
     * Get list of currently shielded apps
     *
     * @return List of bundle IDs that are currently shielded
     */
    fun getShieldedApps(): List<String>

    /**
     * Start device activity monitoring
     *
     * Swift should call DeviceActivityCenter().startMonitoring()
     *
     * @param activityName Name for this monitoring session
     * @return true if successful, false otherwise
     */
    fun startMonitoring(activityName: String): Boolean

    /**
     * Stop device activity monitoring
     *
     * @param activityName Name of the monitoring session to stop
     * @return true if successful, false otherwise
     */
    fun stopMonitoring(activityName: String): Boolean

    /**
     * Check if Screen Time API is available
     *
     * @return true if iOS 15.0+, false otherwise
     */
    fun isAvailable(): Boolean
}
