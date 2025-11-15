import Foundation
import FamilyControls
import DeviceActivity
import ManagedSettings

/// Bridge class to connect iOS Screen Time API with Kotlin Multiplatform
///
/// Required Setup:
/// 1. Add FamilyControls entitlement: com.apple.developer.family-controls
/// 2. Add NSFamilyControlsUsageDescription to Info.plist
/// 3. Request entitlement from Apple at: https://developer.apple.com/contact/request/family-controls-distribution/
@available(iOS 15.0, *)
@objc public class ScreenTimeManager: NSObject {

    // Singleton instance for Kotlin interop
    @objc public static let shared = ScreenTimeManager()

    private let authCenter = AuthorizationCenter.shared
    private let settingsStore = ManagedSettingsStore()
    private let activityCenter = DeviceActivityCenter()

    private override init() {
        super.init()
    }

    // MARK: - Authorization

    /// Request Family Controls authorization
    /// This must be called before using any Screen Time features
    @objc public func requestAuthorization(completion: @escaping (Bool, Error?) -> Void) {
        Task {
            do {
                try await authCenter.requestAuthorization(for: .individual)
                completion(true, nil)
            } catch {
                completion(false, error)
            }
        }
    }

    /// Check if authorization is granted
    @objc public func isAuthorized() -> Bool {
        return authCenter.authorizationStatus == .approved
    }

    /// Get current authorization status
    @objc public func getAuthorizationStatus() -> String {
        switch authCenter.authorizationStatus {
        case .notDetermined:
            return "notDetermined"
        case .denied:
            return "denied"
        case .approved:
            return "approved"
        @unknown default:
            return "unknown"
        }
    }

    // MARK: - App Blocking (Shielding)

    /// Shield (block) an app by bundle identifier
    @objc public func shieldApp(bundleIdentifier: String) -> Bool {
        guard isAuthorized() else { return false }

        do {
            let token = ApplicationToken(bundleIdentifier: bundleIdentifier)
            settingsStore.shield.applications = [token]
            return true
        } catch {
            print("Failed to shield app \(bundleIdentifier): \(error)")
            return false
        }
    }

    /// Shield multiple apps
    @objc public func shieldApps(bundleIdentifiers: [String]) -> Bool {
        guard isAuthorized() else { return false }

        do {
            let tokens = bundleIdentifiers.map { ApplicationToken(bundleIdentifier: $0) }
            settingsStore.shield.applications = Set(tokens)
            return true
        } catch {
            print("Failed to shield apps: \(error)")
            return false
        }
    }

    /// Unshield (unblock) an app
    @objc public func unshieldApp(bundleIdentifier: String) -> Bool {
        guard isAuthorized() else { return false }

        // Get current shielded apps
        var currentShielded = settingsStore.shield.applications ?? Set()

        // Remove the specific app
        let tokenToRemove = ApplicationToken(bundleIdentifier: bundleIdentifier)
        currentShielded.remove(tokenToRemove)

        // Update the store
        settingsStore.shield.applications = currentShielded
        return true
    }

    /// Unshield all apps
    @objc public func unshieldAllApps() -> Bool {
        guard isAuthorized() else { return false }
        settingsStore.shield.applications = nil
        return true
    }

    /// Get list of currently shielded app bundle identifiers
    @objc public func getShieldedApps() -> [String] {
        guard isAuthorized() else { return [] }

        let shielded = settingsStore.shield.applications ?? Set()
        return shielded.map { $0.bundleIdentifier ?? "" }.filter { !$0.isEmpty }
    }

    // MARK: - Device Activity Monitoring

    /// Set up 24/7 monitoring to track app usage
    @objc public func setupActivityMonitoring(activityName: String = "TawaznMonitoring") -> Bool {
        guard isAuthorized() else { return false }

        // Create a schedule that runs all day, every day
        let schedule = DeviceActivitySchedule(
            intervalStart: DateComponents(hour: 0, minute: 0),
            intervalEnd: DateComponents(hour: 23, minute: 59),
            repeats: true
        )

        do {
            let activityName = DeviceActivityName(activityName)
            try activityCenter.startMonitoring(activityName, during: schedule)
            return true
        } catch {
            print("Failed to start monitoring: \(error)")
            return false
        }
    }

    /// Stop monitoring for an activity
    @objc public func stopActivityMonitoring(activityName: String = "TawaznMonitoring") -> Bool {
        let activityName = DeviceActivityName(activityName)
        activityCenter.stopMonitoring([activityName])
        return true
    }

    /// Set up time limit for specific apps
    @objc public func setAppTimeLimit(
        bundleIdentifiers: [String],
        hours: Int,
        minutes: Int,
        activityName: String = "TawaznTimeLimit"
    ) -> Bool {
        guard isAuthorized() else { return false }

        // Create schedule for the time limit
        let schedule = DeviceActivitySchedule(
            intervalStart: DateComponents(hour: 0, minute: 0),
            intervalEnd: DateComponents(hour: hours, minute: minutes),
            repeats: true
        )

        do {
            let name = DeviceActivityName(activityName)
            try activityCenter.startMonitoring(name, during: schedule)

            // Shield apps when limit is reached
            let tokens = bundleIdentifiers.map { ApplicationToken(bundleIdentifier: $0) }
            settingsStore.shield.applications = Set(tokens)

            return true
        } catch {
            print("Failed to set time limit: \(error)")
            return false
        }
    }

    // MARK: - Helper Methods

    /// Check if Screen Time API is available on this device
    @objc public static func isAvailable() -> Bool {
        if #available(iOS 15.0, *) {
            return true
        } else {
            return false
        }
    }
}

/// Extension to make ApplicationToken hashable for Set operations
@available(iOS 15.0, *)
extension ApplicationToken: Hashable {
    public func hash(into hasher: inout Hasher) {
        hasher.combine(bundleIdentifier)
    }

    public static func == (lhs: ApplicationToken, rhs: ApplicationToken) -> Bool {
        return lhs.bundleIdentifier == rhs.bundleIdentifier
    }
}
