import Foundation
import FamilyControls
import ComposeApp

/// Swift implementation of the IOSScreenTimeHelper protocol defined in Kotlin
/// This bridges the Screen Time APIs to the Kotlin Multiplatform code
@available(iOS 15.0, *)
class ScreenTimeHelperImpl: IOSScreenTimeHelper {

    private let bridge = ScreenTimeBridge.shared

    // MARK: - Authorization

    func requestAuthorization(completion: @escaping (Bool, String?) -> Void) {
        bridge.requestAuthorization { success, error in
            completion(success, error)
        }
    }

    func isAuthorized() -> Bool {
        return bridge.isAuthorized()
    }

    // MARK: - App Blocking

    func shieldApp(bundleIdentifier: String) -> Bool {
        return bridge.shieldApp(bundleIdentifier: bundleIdentifier)
    }

    func unshieldApp(bundleIdentifier: String) -> Bool {
        return bridge.unshieldApp(bundleIdentifier: bundleIdentifier)
    }

    // MARK: - Monitoring

    func setupMonitoring() -> Bool {
        return bridge.setupActivityMonitoring()
    }
}
