import SwiftUI
import ComposeApp

@main
struct iOSApp: App {

    init() {
        // CRITICAL: Initialize Koin FIRST (before any dependency injection)
        initializeKoin()

        // Then initialize the Screen Time bridge (depends on Koin being ready)
        initializeScreenTimeBridge()

        // Additional app initialization
        configureLogging()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }

    // MARK: - Initialization

    /// Initialize Koin dependency injection
    ///
    /// This MUST be called first, before any other initialization.
    /// It sets up all dependency injection modules for the app.
    private func initializeKoin() {
        KoinInitializer_iosKt.initializeKoinIOS()
        print("✅ Koin initialized successfully")
    }

    /// Initialize the Screen Time API bridge
    ///
    /// This sets up the connection between Kotlin code and Swift Screen Time implementation.
    /// Must be called AFTER Koin initialization.
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
        #if DEBUG
        print("🐛 Debug mode: Verbose logging enabled")
        #else
        print("📱 Release mode: Production logging")
        #endif
    }
}
