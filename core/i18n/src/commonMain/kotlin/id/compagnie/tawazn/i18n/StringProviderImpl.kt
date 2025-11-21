package id.compagnie.tawazn.i18n

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

/**
 * Implementation of StringProvider that loads translations from embedded JSON
 */
class StringProviderImpl(
    private val dataStore: DataStore<Preferences>,
    private val translationsProvider: TranslationsProvider
) : StringProvider {

    private val _currentLanguage = MutableStateFlow(Language.ENGLISH)
    override val currentLanguage: StateFlow<Language> = _currentLanguage.asStateFlow()

    private var translations: Map<String, String> = emptyMap()

    private val languageKey = stringPreferencesKey("app_language")

    suspend fun initialize() {
        // Load saved language preference
        val savedLanguageCode = dataStore.data.map { preferences ->
            preferences[languageKey] ?: Language.ENGLISH.code
        }.first()

        val language = Language.fromCode(savedLanguageCode)
        _currentLanguage.value = language
        loadTranslations(language)
    }

    override fun getString(key: String): String {
        return translations[key] ?: run {
            println("Missing translation for key: $key")
            key
        }
    }

    override fun getString(key: String, vararg args: Any): String {
        val template = getString(key)
        return try {
            String.format(template, *args)
        } catch (e: Exception) {
            println("Error formatting string for key: $key")
            template
        }
    }

    override suspend fun setLanguage(language: Language) {
        _currentLanguage.value = language
        loadTranslations(language)

        // Save preference
        dataStore.edit { preferences ->
            preferences[languageKey] = language.code
        }
    }

    override fun hasKey(key: String): Boolean {
        return translations.containsKey(key)
    }

    private fun loadTranslations(language: Language) {
        translations = translationsProvider.getTranslations(language)
    }
}
