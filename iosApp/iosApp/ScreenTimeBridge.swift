import Foundation
import FamilyControls
import DeviceActivity
import ManagedSettings

/// Bridge class to expose iOS Screen Time functionality to Kotlin Multiplatform
/// This class provides a simple interface that can be called from Kotlin via cinterop
@available(iOS 15.0, *)
@objc public class ScreenTimeBridge: NSObject {

    @objc public static let shared = ScreenTimeBridge()

    private let screenTimeManager = ScreenTimeManager.shared
    private let permissionHelper = IOSPermissionHelper.shared

    private override init() {
        super.init()
    }

    // MARK: - Authorization

    /// Request Family Controls authorization
    /// Returns true if authorization was granted, false otherwise
    @objc public func requestAuthorization(completion: @escaping (Bool, String?) -> Void) {
        permissionHelper.requestFamilyControlsAuthorization { success, error in
            completion(success, error)
        }
    }

    /// Check if Family Controls is currently authorized
    @objc public func isAuthorized() -> Bool {
        return permissionHelper.hasFamilyControlsPermission()
    }

    /// Get the current authorization status as a string
    /// Returns: "not_determined", "denied", "approved", or "unknown"
    @objc public func getAuthorizationStatus() -> String {
        return permissionHelper.getFamilyControlsStatus()
    }

    // MARK: - App Blocking/Shielding

    /// Shield (block) a single app by its bundle identifier
    @objc public func shieldApp(bundleIdentifier: String) -> Bool {
        guard isAuthorized() else {
            print("Cannot shield app - not authorized")
            return false
        }
        return screenTimeManager.shieldApp(bundleIdentifier: bundleIdentifier)
    }

    /// Shield multiple apps by their bundle identifiers
    @objc public func shieldApps(bundleIdentifiers: [String]) -> Bool {
        guard isAuthorized() else {
            print("Cannot shield apps - not authorized")
            return false
        }
        return screenTimeManager.shieldApps(bundleIdentifiers: bundleIdentifiers)
    }

    /// Unshield (unblock) a single app
    @objc public func unshieldApp(bundleIdentifier: String) -> Bool {
        guard isAuthorized() else {
            print("Cannot unshield app - not authorized")
            return false
        }
        return screenTimeManager.unshieldApp(bundleIdentifier: bundleIdentifier)
    }

    /// Unshield all currently shielded apps
    @objc public func unshieldAllApps() -> Bool {
        guard isAuthorized() else {
            print("Cannot unshield apps - not authorized")
            return false
        }
        return screenTimeManager.unshieldAllApps()
    }

    /// Get list of currently shielded app bundle identifiers
    @objc public func getShieldedApps() -> [String] {
        guard isAuthorized() else {
            return []
        }
        return screenTimeManager.getShieldedApps()
    }

    // MARK: - Device Activity Monitoring

    /// Set up 24/7 activity monitoring
    @objc public func setupActivityMonitoring() -> Bool {
        guard isAuthorized() else {
            print("Cannot setup monitoring - not authorized")
            return false
        }
        return screenTimeManager.setupActivityMonitoring()
    }

    /// Stop activity monitoring
    @objc public func stopActivityMonitoring() -> Bool {
        return screenTimeManager.stopActivityMonitoring()
    }

    /// Set time limit for specific apps
    /// - Parameters:
    ///   - bundleIdentifiers: Array of app bundle IDs
    ///   - hours: Number of hours for the limit
    ///   - minutes: Number of minutes for the limit
    @objc public func setAppTimeLimit(
        bundleIdentifiers: [String],
        hours: Int,
        minutes: Int
    ) -> Bool {
        guard isAuthorized() else {
            print("Cannot set time limit - not authorized")
            return false
        }
        return screenTimeManager.setAppTimeLimit(
            bundleIdentifiers: bundleIdentifiers,
            hours: hours,
            minutes: minutes
        )
    }

    // MARK: - Permissions & Settings

    /// Request notification permissions
    @objc public func requestNotificationPermission(completion: @escaping (Bool, String?) -> Void) {
        permissionHelper.requestNotificationPermission(completion: completion)
    }

    /// Check if notifications are enabled
    @objc public func areNotificationsEnabled() -> Bool {
        return permissionHelper.areNotificationsEnabled()
    }

    /// Open app settings
    @objc public func openAppSettings() {
        permissionHelper.openAppSettings()
    }

    /// Open Screen Time settings
    @objc public func openScreenTimeSettings() {
        permissionHelper.openScreenTimeSettings()
    }

    /// Get all permission statuses as a dictionary
    @objc public func getAllPermissionStatuses() -> [String: Any] {
        return permissionHelper.getAllPermissionStatuses()
    }

    // MARK: - Device Info

    /// Get iOS version string
    @objc public func getIOSVersion() -> String {
        return permissionHelper.getIOSVersion()
    }

    /// Get device model
    @objc public func getDeviceModel() -> String {
        return permissionHelper.getDeviceModel()
    }

    /// Check if Screen Time API is available on this iOS version
    @objc public static func isScreenTimeAvailable() -> Bool {
        return IOSPermissionHelper.isScreenTimeAvailable()
    }
}
