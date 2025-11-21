package id.compagnie.tawazn.i18n.di

import id.compagnie.tawazn.i18n.SystemLocaleProvider
import org.koin.dsl.module
import java.util.Locale

actual val platformLocaleModule = module {
    single<SystemLocaleProvider> {
        object : SystemLocaleProvider {
            override fun getSystemLocale(): String = Locale.getDefault().language
        }
    }
}
