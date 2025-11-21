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
    private val translationsProvider: TranslationsProvider,
    private val systemLocaleProvider: SystemLocaleProvider
) : StringProvider {

    private val _currentLanguage = MutableStateFlow(Language.ENGLISH)
    override val currentLanguage: StateFlow<Language> = _currentLanguage.asStateFlow()

    private var translations: Map<String, String> = emptyMap()

    private val languageKey = stringPreferencesKey("app_language")

    init {
        // Load English translations immediately to prevent empty strings
        // This ensures the app has translations available even before async init completes
        translations = translationsProvider.getTranslations(Language.ENGLISH)
    }

    suspend fun initialize() {
        // Load saved language preference, or use system locale as fallback
        val savedLanguageCode = dataStore.data.map { preferences ->
            preferences[languageKey]
        }.first()

        val language = if (savedLanguageCode != null) {
            // User has explicitly selected a language
            Language.fromCode(savedLanguageCode)
        } else {
            // No saved preference, use system locale
            val systemLocale = systemLocaleProvider.getSystemLocale()
            Language.fromCode(systemLocale)
        }

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
