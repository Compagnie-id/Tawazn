package id.compagnie.tawazn.domain.di

import id.compagnie.tawazn.domain.usecase.*
import org.koin.dsl.module

val domainModule = module {
    // Use Cases
    factory { BlockAppUseCase(get()) }
    factory { UnblockAppUseCase(get()) }
    factory { GetActiveBlockedAppsUseCase(get()) }
    factory { GetUsageStatsUseCase(get()) }
    factory { SyncUsageUseCase(get()) }
    factory { CreateBlockSessionUseCase(get()) }
    factory { GetNonSystemAppsUseCase(get()) }
}
