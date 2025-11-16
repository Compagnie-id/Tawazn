package id.compagnie.tawazn.platform.ios.di

import id.compagnie.tawazn.platform.ios.IOSPlatformSync
import id.compagnie.tawazn.platform.ios.ScreenTimeApi
import id.compagnie.tawazn.platform.ios.createScreenTimeApi
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

/**
 * Production-grade Koin DI module for iOS platform
 *
 * This module provides all iOS-specific dependencies using dependency injection.
 * It follows clean architecture and SOLID principles.
 *
 * Provided Services:
 * - ScreenTimeApi: Main interface for Screen Time functionality
 * - IOSPlatformSync: Service for synchronizing platform data with repositories
 *
 * Architecture Benefits:
 * - Testability: Easy to inject mocks for testing
 * - Maintainability: Clear dependency graph
 * - Flexibility: Easy to swap implementations
 * - Type Safety: Compile-time dependency resolution
 *
 * Usage in common code:
 * ```kotlin
 * class AppBlockingViewModel(
 *     private val screenTimeApi: ScreenTimeApi,  // Injected by Koin
 *     private val platformSync: IOSPlatformSync   // Injected by Koin
 * ) : ViewModel() {
 *     suspend fun blockApp(bundleId: String) {
 *         platformSync.blockApp(bundleId)
 *     }
 * }
 * ```
 */
val iosPlatformModule = module {

    /**
     * Provides ScreenTimeApi singleton
     *
     * This is the main entry point for Screen Time functionality.
     * The actual implementation is created via expect/actual mechanism,
     * which uses the SwiftScreenTimeBridge initialized from Swift.
     *
     * Dependencies: None (uses Swift bridge initialized at app startup)
     * Scope: Singleton (single instance throughout app lifecycle)
     */
    single<ScreenTimeApi> {
        createScreenTimeApi()
    }

    /**
     * Provides IOSPlatformSync singleton
     *
     * This service orchestrates synchronization between Screen Time and repositories.
     *
     * Dependencies:
     * - ScreenTimeApi: For Screen Time operations
     * - AppRepository: For app data
     * - BlockedAppRepository: For blocked app management
     * - UsageRepository: For usage statistics
     *
     * Scope: Singleton (maintains sync state throughout app lifecycle)
     */
    singleOf(::IOSPlatformSync)
}

/**
 * Get all iOS platform modules
 *
 * This function returns all Koin modules needed for iOS platform.
 * Call this when setting up Koin in the iOS app initialization.
 *
 * Example setup in common code:
 * ```kotlin
 * fun initializeKoin() {
 *     startKoin {
 *         modules(
 *             commonModules +
 *             getIOSPlatformModules()
 *         )
 *     }
 * }
 * ```
 *
 * @return List of Koin modules for iOS platform
 */
fun getIOSPlatformModules(): List<Module> {
    return listOf(
        iosPlatformModule
    )
}
