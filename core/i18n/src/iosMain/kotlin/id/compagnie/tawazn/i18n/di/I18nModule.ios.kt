package id.compagnie.tawazn.i18n.di

import id.compagnie.tawazn.i18n.SystemLocaleProvider
import org.koin.dsl.module
import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.languageCode

actual val platformLocaleModule = module {
    single<SystemLocaleProvider> {
        object : SystemLocaleProvider {
            override fun getSystemLocale(): String {
                return NSLocale.currentLocale.languageCode
            }
        }
    }
}
