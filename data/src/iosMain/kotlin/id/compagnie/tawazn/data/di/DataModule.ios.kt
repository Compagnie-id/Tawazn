package id.compagnie.tawazn.data.di

import id.compagnie.tawazn.core.datastore.DataStoreFactory
import id.compagnie.tawazn.data.service.PlatformSyncService
import id.compagnie.tawazn.data.service.createPlatformSyncService
import id.compagnie.tawazn.platform.ios.IOSAppMonitorImpl
import org.koin.dsl.module

actual fun platformModule() = module {
    // DataStore
    single { DataStoreFactory().createDataStore() }

    // Platform Monitors
    single { IOSAppMonitorImpl() }

    // Platform Sync Service
    single<PlatformSyncService> {
        createPlatformSyncService().apply {
            initialize(
                appRepository = get(),
                blockedAppRepository = get(),
                usageRepository = get()
            )
        }
    }
}
