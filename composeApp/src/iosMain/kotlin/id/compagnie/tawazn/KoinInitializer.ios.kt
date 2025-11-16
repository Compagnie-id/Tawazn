package id.compagnie.tawazn

import id.compagnie.tawazn.data.di.dataModule
import id.compagnie.tawazn.data.di.platformModule
import id.compagnie.tawazn.domain.di.domainModule
import id.compagnie.tawazn.feature.analytics.di.analyticsModule
import id.compagnie.tawazn.feature.appblocking.di.appBlockingModule
import id.compagnie.tawazn.feature.onboarding.di.onboardingModule
import id.compagnie.tawazn.feature.settings.di.settingsModule
import id.compagnie.tawazn.feature.usagetracking.di.usageTrackingModule
import id.compagnie.tawazn.platform.ios.di.iosPlatformModule
import org.koin.core.context.startKoin

/**
 * Initialize Koin for iOS
 *
 * This function sets up all dependency injection modules for the iOS app.
 * It must be called BEFORE any Compose UI is displayed.
 *
 * Call this from Swift during app initialization:
 * ```swift
 * @main
 * struct iOSApp: App {
 *     init() {
 *         // Initialize Koin FIRST
 *         MainViewControllerKt.initializeKoinIOS()
 *
 *         // Then initialize Swift bridge
 *         let bridge = SwiftScreenTimeBridgeImpl()
 *         ScreenTimeApiKt.initializeScreenTimeBridge(bridge: bridge)
 *     }
 * }
 * ```
 */
fun initializeKoinIOS() {
    startKoin {
        modules(
            // Platform module - provides DataStore, DatabaseDriver, etc.
            platformModule(),

            // Data module - provides repositories
            dataModule,

            // Domain module - provides use cases
            domainModule,

            // iOS Platform module - provides ScreenTimeApi, IOSPlatformSync
            iosPlatformModule,

            // Feature modules
            onboardingModule,
            appBlockingModule,
            analyticsModule,
            settingsModule,
            usageTrackingModule
        )
    }
}
