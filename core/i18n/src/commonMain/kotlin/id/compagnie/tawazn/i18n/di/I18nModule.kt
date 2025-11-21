package id.compagnie.tawazn.i18n.di

import id.compagnie.tawazn.i18n.StringProvider
import id.compagnie.tawazn.i18n.StringProviderImpl
import id.compagnie.tawazn.i18n.TranslationsProvider
import id.compagnie.tawazn.i18n.TranslationsProviderImpl
import org.koin.dsl.module

val i18nModule = module {
    single<TranslationsProvider> { TranslationsProviderImpl() }
    single<StringProvider> {
        StringProviderImpl(
            dataStore = get(),
            translationsProvider = get()
        )
    }
}
