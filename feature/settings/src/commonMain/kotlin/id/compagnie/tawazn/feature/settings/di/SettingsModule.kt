package id.compagnie.tawazn.feature.settings.di

import id.compagnie.tawazn.feature.settings.FocusSessionScreenModel
import id.compagnie.tawazn.feature.settings.SettingsScreenModel
import org.koin.dsl.module

val settingsModule = module {
    factory { SettingsScreenModel() }
    factory { FocusSessionScreenModel() }
}
