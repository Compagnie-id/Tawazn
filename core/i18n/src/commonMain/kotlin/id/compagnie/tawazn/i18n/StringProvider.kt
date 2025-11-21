package id.compagnie.tawazn.i18n

import kotlinx.coroutines.flow.StateFlow

/**
 * Interface for providing localized strings
 */
interface StringProvider {
    /**
     * Current language as a flow
     */
    val currentLanguage: StateFlow<Language>

    /**
     * Get a localized string by key
     */
    fun getString(key: String): String

    /**
     * Get a localized string by key with format arguments
     */
    fun getString(key: String, vararg args: Any): String

    /**
     * Change the current language
     */
    suspend fun setLanguage(language: Language)

    /**
     * Check if a key exists in the current language
     */
    fun hasKey(key: String): Boolean
}
