package id.compagnie.tawazn.i18n

/**
 * Interface for providing translations
 * Platform-specific implementations will load from resources
 */
interface TranslationsProvider {
    fun getTranslations(language: Language): Map<String, String>
}
