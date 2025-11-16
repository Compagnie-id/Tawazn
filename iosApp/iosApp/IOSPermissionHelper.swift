import Foundation
import FamilyControls
import UIKit

/// Permission helper for iOS to manage Screen Time and other permissions
@available(iOS 15.0, *)
@objc public class IOSPermissionHelper: NSObject {

    @objc public static let shared = IOSPermissionHelper()

    private let authCenter = AuthorizationCenter.shared

    private override init() {
        super.init()
    }

    // MARK: - Family Controls (Screen Time)

    /// Request Family Controls authorization with async/await
    @objc public func requestFamilyControlsAuthorization(completion: @escaping (Bool, String?) -> Void) {
        Task {
            do {
                try await authCenter.requestAuthorization(for: .individual)
                DispatchQueue.main.async {
                    completion(true, nil)
                }
            } catch {
                DispatchQueue.main.async {
                    completion(false, error.localizedDescription)
                }
            }
        }
    }

    /// Check if Family Controls is authorized
    @objc public func hasFamilyControlsPermission() -> Bool {
        return authCenter.authorizationStatus == .approved
    }

    /// Get the current Family Controls authorization status
    @objc public func getFamilyControlsStatus() -> String {
        switch authCenter.authorizationStatus {
        case .notDetermined:
            return "not_determined"
        case .denied:
            return "denied"
        case .approved:
            return "approved"
        @unknown default:
            return "unknown"
        }
    }

    // MARK: - Permission Status

    /// Get all permission statuses as a dictionary
    @objc public func getAllPermissionStatuses() -> [String: Any] {
        return [
            "familyControls": hasFamilyControlsPermission(),
            "familyControlsStatus": getFamilyControlsStatus(),
            "screenTimeAvailable": IOSPermissionHelper.isScreenTimeAvailable(),
            "notificationsEnabled": areNotificationsEnabled()
        ]
    }

    // MARK: - Notifications

    /// Check if notifications are enabled
    @objc public func areNotificationsEnabled() -> Bool {
        // This is a simplified check - actual implementation would need UNUserNotificationCenter
        return true
    }

    /// Request notification permissions
    @objc public func requestNotificationPermission(completion: @escaping (Bool, String?) -> Void) {
        UNUserNotificationCenter.current().requestAuthorization(options: [.alert, .badge, .sound]) { granted, error in
            DispatchQueue.main.async {
                if let error = error {
                    completion(false, error.localizedDescription)
                } else {
                    completion(granted, nil)
                }
            }
        }
    }

    // MARK: - Settings Navigation

    /// Open app settings
    @objc public func openAppSettings() {
        guard let settingsUrl = URL(string: UIApplication.openSettingsURLString) else {
            return
        }

        if UIApplication.shared.canOpenURL(settingsUrl) {
            UIApplication.shared.open(settingsUrl, options: [:], completionHandler: nil)
        }
    }

    /// Open Screen Time settings
    @objc public func openScreenTimeSettings() {
        // Note: There's no direct URL scheme to Screen Time settings
        // We can only open the app settings
        openAppSettings()
    }

    // MARK: - Helper Methods

    /// Check if Screen Time API is available on this iOS version
    @objc public static func isScreenTimeAvailable() -> Bool {
        if #available(iOS 15.0, *) {
            return true
        } else {
            return false
        }
    }

    /// Get iOS version string
    @objc public func getIOSVersion() -> String {
        return UIDevice.current.systemVersion
    }

    /// Get device model
    @objc public func getDeviceModel() -> String {
        return UIDevice.current.model
    }
}

import UserNotifications
