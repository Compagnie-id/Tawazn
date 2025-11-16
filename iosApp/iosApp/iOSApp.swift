import SwiftUI
import ComposeApp

@main
struct iOSApp: App {

    init() {
        // Initialize the production-grade Screen Time bridge
        initializeScreenTimeBridge()

        // Additional app initialization can go here
        configureLogging()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }

    // MARK: - Initialization

    /// Initialize the Screen Time API bridge
    ///
    /// This sets up the connection between Kotlin code and Swift Screen Time implementation.
    /// Called once during app initialization.
    private func initializeScreenTimeBridge() {
        if #available(iOS 15.0, *) {
            let bridge = SwiftScreenTimeBridgeImpl()
            ScreenTimeApiKt.initializeScreenTimeBridge(bridge: bridge)
            print("✅ Screen Time bridge initialized successfully")
        } else {
            print("⚠️ Screen Time API not available on iOS < 15.0")
        }
    }

    /// Configure app-wide logging settings
    private func configureLogging() {
        // Configure logging level based on build configuration
        #if DEBUG
        print("🐛 Debug mode: Verbose logging enabled")
        #else
        print("📱 Release mode: Production logging")
        #endif
    }
}
