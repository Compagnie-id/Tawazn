package id.compagnie.tawazn.data.di

import id.compagnie.tawazn.core.datastore.AppPreferences
import id.compagnie.tawazn.core.datastore.DataStoreFactory
import id.compagnie.tawazn.data.repository.AppRepositoryImpl
import id.compagnie.tawazn.data.repository.BlockedAppRepositoryImpl
import id.compagnie.tawazn.data.repository.BlockSessionRepositoryImpl
import id.compagnie.tawazn.data.repository.UsageRepositoryImpl
import id.compagnie.tawazn.database.DatabaseDriverFactory
import id.compagnie.tawazn.database.TawaznDatabase
import id.compagnie.tawazn.database.TawaznDatabaseFactory
import id.compagnie.tawazn.domain.repository.AppRepository
import id.compagnie.tawazn.domain.repository.BlockedAppRepository
import id.compagnie.tawazn.domain.repository.BlockSessionRepository
import id.compagnie.tawazn.domain.repository.UsageRepository
import org.koin.core.module.Module
import org.koin.dsl.module

expect fun platformModule(): Module

val dataModule = module {
    // Database (DatabaseDriverFactory provided by platformModule)
    single { get<DatabaseDriverFactory>().createDriver() }
    single { TawaznDatabaseFactory.createDatabase(get()) }

    // DataStore & Preferences (DataStore created in platformModule)
    single { AppPreferences(get()) }

    // Repositories
    single<AppRepository> { AppRepositoryImpl(get()) }
    single<BlockedAppRepository> { BlockedAppRepositoryImpl(get()) }
    single<UsageRepository> { UsageRepositoryImpl(get()) }
    single<BlockSessionRepository> { BlockSessionRepositoryImpl(get()) }
}
