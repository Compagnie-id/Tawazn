package id.compagnie.tawazn.i18n

/**
 * Provides system locale information
 */
interface SystemLocaleProvider {
    /**
     * Returns the system's current language code (e.g., "en", "id", "ar")
     */
    fun getSystemLocale(): String
}
