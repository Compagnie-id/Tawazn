package id.compagnie.tawazn.data.di

import id.compagnie.tawazn.data.repository.AppRepositoryImpl
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
    // Database
    single { DatabaseDriverFactory(get()).createDriver() }
    single { TawaznDatabaseFactory.createDatabase(get()) }

    // Repositories
    single<AppRepository> { AppRepositoryImpl(get()) }
    // single<BlockedAppRepository> { BlockedAppRepositoryImpl(get()) }
    // single<UsageRepository> { UsageRepositoryImpl(get(), get()) }
    // single<BlockSessionRepository> { BlockSessionRepositoryImpl(get()) }
}
