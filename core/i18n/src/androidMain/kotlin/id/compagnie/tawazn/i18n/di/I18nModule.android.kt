package id.compagnie.tawazn.i18n.di

import id.compagnie.tawazn.i18n.AndroidSystemLocaleProvider
import id.compagnie.tawazn.i18n.SystemLocaleProvider
import org.koin.dsl.module

actual val platformLocaleModule = module {
    single<SystemLocaleProvider> { AndroidSystemLocaleProvider(get()) }
}
