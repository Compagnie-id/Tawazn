import Foundation
import FamilyControls
import ComposeApp

/// Production-grade Swift implementation of the ScreenTimeApi bridge
///
/// This class implements the `SwiftScreenTimeBridge` protocol defined in Kotlin,
/// providing the actual iOS Screen Time functionality using native frameworks.
///
/// Architecture:
/// - Implements `SwiftScreenTimeBridge` protocol from Kotlin
/// - Delegates to `ScreenTimeBridge` for actual Screen Time operations
/// - Provides clean error handling and logging
/// - Thread-safe operations
///
/// Usage:
/// ```swift
/// let bridge = SwiftScreenTimeBridgeImpl()
/// ScreenTimeApiKt.initializeScreenTimeBridge(bridge: bridge)
/// ```
@available(iOS 15.0, *)
public class SwiftScreenTimeBridgeImpl: NSObject, SwiftScreenTimeBridge {

    // MARK: - Properties

    /// Underlying Screen Time manager
    private let screenTimeManager: ScreenTimeBridge

    /// Permission helper for authorization
    private let permissionHelper: IOSPermissionHelper

    // MARK: - Initialization

    public override init() {
        self.screenTimeManager = ScreenTimeBridge.shared
        self.permissionHelper = IOSPermissionHelper.shared
        super.init()
    }

    // MARK: - SwiftScreenTimeBridge Protocol Implementation

    // MARK: Authorization

    public func requestAuthorization(completion: @escaping (Bool, String?) -> Void) {
        // Ensure we're on the main thread for UI-related operations
        DispatchQueue.main.async { [weak self] in
            guard let self = self else {
                completion(false, "Bridge deallocated")
                return
            }

            self.permissionHelper.requestFamilyControlsAuthorization { success, error in
                // Callback is already on main thread from permission helper
                completion(success, error)
            }
        }
    }

    public func isAuthorized() -> Bool {
        return permissionHelper.hasFamilyControlsPermission()
    }

    public func getAuthorizationStatus() -> String {
        return permissionHelper.getFamilyControlsStatus()
    }

    // MARK: App Shielding

    public func shieldApp(bundleIdentifier: String) -> Bool {
        guard isAuthorized() else {
            print("❌ Cannot shield app: Not authorized")
            return false
        }

        guard !bundleIdentifier.isEmpty else {
            print("❌ Cannot shield app: Empty bundle identifier")
            return false
        }

        let success = screenTimeManager.shieldApp(bundleIdentifier: bundleIdentifier)

        if success {
            print("✅ Successfully shielded app: \(bundleIdentifier)")
        } else {
            print("❌ Failed to shield app: \(bundleIdentifier)")
        }

        return success
    }

    public func shieldApps(bundleIdentifiers: [String]) -> Bool {
        guard isAuthorized() else {
            print("❌ Cannot shield apps: Not authorized")
            return false
        }

        guard !bundleIdentifiers.isEmpty else {
            print("⚠️ Shield apps called with empty list")
            return true // Not an error, just a no-op
        }

        let success = screenTimeManager.shieldApps(bundleIdentifiers: bundleIdentifiers)

        if success {
            print("✅ Successfully shielded \(bundleIdentifiers.count) apps")
        } else {
            print("❌ Failed to shield apps")
        }

        return success
    }

    public func unshieldApp(bundleIdentifier: String) -> Bool {
        guard isAuthorized() else {
            print("❌ Cannot unshield app: Not authorized")
            return false
        }

        guard !bundleIdentifier.isEmpty else {
            print("❌ Cannot unshield app: Empty bundle identifier")
            return false
        }

        let success = screenTimeManager.unshieldApp(bundleIdentifier: bundleIdentifier)

        if success {
            print("✅ Successfully unshielded app: \(bundleIdentifier)")
        } else {
            print("❌ Failed to unshield app: \(bundleIdentifier)")
        }

        return success
    }

    public func unshieldAllApps() -> Bool {
        guard isAuthorized() else {
            print("❌ Cannot unshield apps: Not authorized")
            return false
        }

        let success = screenTimeManager.unshieldAllApps()

        if success {
            print("✅ Successfully unshielded all apps")
        } else {
            print("❌ Failed to unshield all apps")
        }

        return success
    }

    public func getShieldedApps() -> [String] {
        guard isAuthorized() else {
            print("⚠️ Cannot get shielded apps: Not authorized")
            return []
        }

        let shieldedApps = screenTimeManager.getShieldedApps()
        print("📊 Found \(shieldedApps.count) shielded apps")

        return shieldedApps
    }

    // MARK: Device Activity Monitoring

    public func startMonitoring(activityName: String) -> Bool {
        guard isAuthorized() else {
            print("❌ Cannot start monitoring: Not authorized")
            return false
        }

        guard !activityName.isEmpty else {
            print("❌ Cannot start monitoring: Empty activity name")
            return false
        }

        let success = screenTimeManager.setupActivityMonitoring(activityName: activityName)

        if success {
            print("✅ Successfully started monitoring: \(activityName)")
        } else {
            print("❌ Failed to start monitoring: \(activityName)")
        }

        return success
    }

    public func stopMonitoring(activityName: String) -> Bool {
        guard !activityName.isEmpty else {
            print("❌ Cannot stop monitoring: Empty activity name")
            return false
        }

        let success = screenTimeManager.stopActivityMonitoring(activityName: activityName)

        if success {
            print("✅ Successfully stopped monitoring: \(activityName)")
        } else {
            print("⚠️ Failed to stop monitoring: \(activityName)")
        }

        return success
    }

    // MARK: Availability

    public func isAvailable() -> Bool {
        return IOSPermissionHelper.isScreenTimeAvailable()
    }
}

// MARK: - Additional Helper Extension

@available(iOS 15.0, *)
extension SwiftScreenTimeBridgeImpl {

    /// Get detailed permission statuses for debugging
    ///
    /// Returns a dictionary with all permission states
    public func getDetailedPermissionStatus() -> [String: Any] {
        return permissionHelper.getAllPermissionStatuses()
    }

    /// Open Screen Time settings
    ///
    /// Navigates the user to the app's settings page
    public func openSettings() {
        permissionHelper.openAppSettings()
    }
}
