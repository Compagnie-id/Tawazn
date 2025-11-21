package id.compagnie.tawazn.i18n

/**
 * Simple implementation that returns embedded translations
 */
class TranslationsProviderImpl : TranslationsProvider {
    override fun getTranslations(language: Language): Map<String, String> {
        return when (language) {
            Language.ENGLISH -> getEnglishTranslations()
            Language.INDONESIAN -> getIndonesianTranslations()
            Language.ARABIC -> getArabicTranslations()
        }
    }
}
