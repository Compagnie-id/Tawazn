package id.compagnie.tawazn.feature.usagetracking.di

import id.compagnie.tawazn.feature.usagetracking.UsageTrackingScreenModel
import org.koin.dsl.module

val usageTrackingModule = module {
    factory { UsageTrackingScreenModel() }
}
