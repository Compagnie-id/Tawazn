package id.compagnie.tawazn.i18n

/**
 * Supported languages in Tawazn
 */
enum class Language(val code: String, val displayName: String, val nativeName: String) {
    ENGLISH("en", "English", "English"),
    INDONESIAN("id", "Indonesian", "Bahasa Indonesia"),
    ARABIC("ar", "Arabic", "العربية");

    companion object {
        fun fromCode(code: String): Language {
            return entries.find { it.code == code } ?: ENGLISH
        }
    }
}
