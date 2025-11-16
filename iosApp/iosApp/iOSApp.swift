import SwiftUI
import ComposeApp

@main
struct iOSApp: App {

    init() {
        // Initialize the Screen Time helper for Kotlin
        if #available(iOS 15.0, *) {
            let helper = ScreenTimeHelperImpl()
            IOSAppMonitorHelper.shared.initialize(helper: helper)
        }
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}