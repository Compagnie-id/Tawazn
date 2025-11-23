package id.compagnie.tawazn.i18n

/**
 * Supported languages in Tawazn
 */
enum class Language(val code: String, val displayName: String, val nativeName: String) {
    ARABIC("ar", "Arabic", "العربية"),

    INDONESIAN("id", "Indonesian", "Bahasa Indonesia"),

    ENGLISH("en", "English", "English");

    companion object {
        fun fromCode(code: String): Language {
            return entries.find { it.code == code } ?: ENGLISH
        }
    }
}
