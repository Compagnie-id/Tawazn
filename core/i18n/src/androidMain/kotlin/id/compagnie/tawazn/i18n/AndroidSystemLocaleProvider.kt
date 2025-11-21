package id.compagnie.tawazn.i18n

import android.content.Context
import java.util.Locale

/**
 * Android implementation that reads the system locale
 */
class AndroidSystemLocaleProvider(private val context: Context) : SystemLocaleProvider {
    override fun getSystemLocale(): String {
        val locale = context.resources.configuration.locales[0]
        return locale.language
    }
}
