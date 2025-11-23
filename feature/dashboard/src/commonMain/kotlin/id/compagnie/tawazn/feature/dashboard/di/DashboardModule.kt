package id.compagnie.tawazn.feature.dashboard.di

import id.compagnie.tawazn.feature.dashboard.DashboardScreenModel
import org.koin.dsl.module

val dashboardModule = module {
    factory { DashboardScreenModel() }
}
