package id.compagnie.tawazn.data.di

import id.compagnie.tawazn.core.datastore.DataStoreFactory
import id.compagnie.tawazn.database.DatabaseDriverFactory
import org.koin.dsl.module

/**
 * iOS platform-specific data module
 *
 * This module provides iOS-specific implementations for:
 * - DataStore (for preferences storage)
 * - DatabaseDriverFactory (for SQLDelight)
 *
 * Note: Platform sync and monitoring are now handled by the
 * ios platform module (platform/ios), not here.
 *
 * Old dependencies (REMOVED for production architecture):
 * - IOSAppMonitorImpl ❌ (deprecated, use ScreenTimeApi from platform/ios module)
 * - PlatformSyncService ❌ (deprecated, use IOSPlatformSync from platform/ios module)
 *
 * The new production-grade services are provided by:
 * - iosPlatformModule (from platform/ios/di)
 *   - Provides: ScreenTimeApi
 *   - Provides: IOSPlatformSync
 */
actual fun platformModule() = module {
    // DataStore factory for preferences
    single { DataStoreFactory().createDataStore() }

    // Database driver factory for SQLDelight
    single { DatabaseDriverFactory() }
}
