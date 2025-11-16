package id.compagnie.tawazn.data.di

import id.compagnie.tawazn.core.datastore.DataStoreFactory
import id.compagnie.tawazn.data.service.PlatformSyncService
import id.compagnie.tawazn.data.service.createPlatformSyncService
import id.compagnie.tawazn.platform.android.AndroidAppMonitor
import id.compagnie.tawazn.platform.android.PermissionHelper
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual fun platformModule() = module {
    // DataStore
    single { DataStoreFactory(androidContext()).createDataStore() }

    // Platform Monitors
    single { AndroidAppMonitor(androidContext()) }

    // Permission Helper
    single { PermissionHelper(androidContext()) }

    // Platform Sync Service
    single<PlatformSyncService> {
        createPlatformSyncService(androidContext()).apply {
            initialize(
                appRepository = get(),
                blockedAppRepository = get(),
                usageRepository = get()
            )
        }
    }
}
