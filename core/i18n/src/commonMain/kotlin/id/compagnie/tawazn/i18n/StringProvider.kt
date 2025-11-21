package id.compagnie.tawazn.i18n

import kotlinx.coroutines.flow.StateFlow

/**
 * Interface for providing localized strings
 *
 * Implementation notes:
 * - The provider must be immediately ready to serve translations (no async initialization required)
 * - System locale is detected and loaded synchronously on creation
 * - User preferences are loaded asynchronously and may trigger a language change
 * - All methods are thread-safe
 */
interface StringProvider {
    /**
     * Current language as a StateFlow.
     * UI components should collect this to reactively update when language changes.
     */
    val currentLanguage: StateFlow<Language>

    /**
     * Get a localized string by key.
     *
     * @param key The translation key (e.g., "settings.title")
     * @return The translated string, or the key itself if translation is missing
     */
    fun getString(key: String): String

    /**
     * Get a localized string by key with format arguments.
     *
     * @param key The translation key
     * @param args Format arguments to be inserted into the translation string
     * @return The formatted translated string
     */
    fun getString(key: String, vararg args: Any): String

    /**
     * Change the current language.
     * This will:
     * 1. Update the current language flow
     * 2. Load new translations
     * 3. Save the preference to DataStore
     *
     * @param language The new language to use
     */
    suspend fun setLanguage(language: Language)

    /**
     * Check if a translation key exists in the current language.
     *
     * @param key The translation key to check
     * @return true if the key has a translation, false otherwise
     */
    fun hasKey(key: String): Boolean
}
