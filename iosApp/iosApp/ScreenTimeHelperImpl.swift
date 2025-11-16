import Foundation
import FamilyControls

/// Swift implementation of the Screen Time helper
/// This bridges the Screen Time APIs to the Kotlin Multiplatform code
///
/// NOTE: This class will be connected to Kotlin at runtime via IOSAppMonitorHelper
/// No direct import of ComposeApp is needed here
@available(iOS 15.0, *)
public class ScreenTimeHelperImpl: NSObject {

    private let bridge = ScreenTimeBridge.shared

    public override init() {
        super.init()
    }

    // MARK: - Authorization

    @objc public func requestAuthorization(completion: @escaping (Bool, String?) -> Void) {
        bridge.requestAuthorization { success, error in
            completion(success, error)
        }
    }

    @objc public func isAuthorized() -> Bool {
        return bridge.isAuthorized()
    }

    // MARK: - App Blocking

    @objc public func shieldApp(bundleIdentifier: String) -> Bool {
        return bridge.shieldApp(bundleIdentifier: bundleIdentifier)
    }

    @objc public func unshieldApp(bundleIdentifier: String) -> Bool {
        return bridge.unshieldApp(bundleIdentifier: bundleIdentifier)
    }

    // MARK: - Monitoring

    @objc public func setupMonitoring() -> Bool {
        return bridge.setupActivityMonitoring()
    }
}
