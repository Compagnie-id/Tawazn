import SwiftUI

@main
struct iOSApp: App {
    var body: some Scene {
        WindowGroup {
            ContentView()
                .onAppear {
                    // Initialize Screen Time helper when the app appears
                    // This is called after ComposeApp framework is loaded
                    initializeScreenTimeHelper()
                }
        }
    }

    private func initializeScreenTimeHelper() {
        if #available(iOS 15.0, *) {
            // Create the helper implementation
            let helper = ScreenTimeHelperImpl()

            // Note: The actual connection to Kotlin happens through the framework
            // When ComposeApp is loaded, it will access this helper via the bridge
            // For now, we'll store it in a global variable that can be accessed
            ScreenTimeHelperBridge.shared.helper = helper
        }
    }
}

/// Bridge to make the helper accessible without importing ComposeApp at compile time
@available(iOS 15.0, *)
class ScreenTimeHelperBridge {
    static let shared = ScreenTimeHelperBridge()
    var helper: ScreenTimeHelperImpl?

    private init() {}
}