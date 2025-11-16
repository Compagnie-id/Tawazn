package id.compagnie.tawazn.feature.analytics.di

import id.compagnie.tawazn.feature.analytics.AnalyticsScreenModel
import org.koin.dsl.module

val analyticsModule = module {
    factory { AnalyticsScreenModel() }
}
