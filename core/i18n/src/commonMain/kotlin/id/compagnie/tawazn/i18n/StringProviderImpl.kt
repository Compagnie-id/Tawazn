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
 * Implementation of StringProvider that loads translations
 *
 * Initialization strategy:
 * 1. Immediately loads system locale translations in init block (no race conditions)
 * 2. Asynchronously checks DataStore for saved user preference
 * 3. If user has saved preference, switches to that language
 */
class StringProviderImpl(
    private val dataStore: DataStore<Preferences>,
    private val translationsProvider: TranslationsProvider,
    private val systemLocaleProvider: SystemLocaleProvider
) : StringProvider {

    private val _currentLanguage: MutableStateFlow<Language>
    override val currentLanguage: StateFlow<Language>

    @Volatile
    private var translations: Map<String, String>

    @Volatile
    private var isInitialized = false

    private val languageKey = stringPreferencesKey("app_language")

    init {
        // Immediately detect and load system locale translations
        // This ensures the UI always has correct translations from the start
        val systemLocale = systemLocaleProvider.getSystemLocale()
        val initialLanguage = Language.fromCode(systemLocale)

        _currentLanguage = MutableStateFlow(initialLanguage)
        currentLanguage = _currentLanguage.asStateFlow()

        // Load translations for detected language synchronously
        translations = translationsProvider.getTranslations(initialLanguage)

        println("i18n: Initialized with system locale '$systemLocale' -> ${initialLanguage.displayName}")
    }

    suspend fun initialize() {
        // Check if user has explicitly saved a language preference
        val savedLanguageCode = dataStore.data
            .map { preferences -> preferences[languageKey] }
            .first()

        if (savedLanguageCode != null) {
            val savedLanguage = Language.fromCode(savedLanguageCode)
            println("i18n: Found saved preference '$savedLanguageCode' -> ${savedLanguage.displayName}")

            // Only switch if it's different from current
            if (savedLanguage != _currentLanguage.value) {
                _currentLanguage.value = savedLanguage
                loadTranslations(savedLanguage)
            }
        } else {
            println("i18n: No saved preference, using system locale ${_currentLanguage.value.displayName}")
        }

        isInitialized = true
    }

    override fun getString(key: String): String {
        return translations[key] ?: run {
            // Log missing translation (only happens if key doesn't exist in translations)
            println("i18n: Missing translation for key '$key' in language '${_currentLanguage.value.code}'")
            key
        }
    }

    override fun getString(key: String, vararg args: Any): String {
        val template = getString(key)
        return try {
            String.format(template, *args)
        } catch (e: Exception) {
            println("i18n: Error formatting string for key '$key': ${e.message}")
            template
        }
    }

    override suspend fun setLanguage(language: Language) {
        if (language == _currentLanguage.value) {
            // Already using this language, no need to reload
            return
        }

        _currentLanguage.value = language
        loadTranslations(language)

        // Save preference
        dataStore.edit { preferences ->
            preferences[languageKey] = language.code
        }

        println("i18n: Language changed to ${language.displayName} (${language.code})")
    }

    override fun hasKey(key: String): Boolean {
        return translations.containsKey(key)
    }

    private fun loadTranslations(language: Language) {
        // Load new translations and assign atomically
        // The @Volatile annotation ensures visibility across threads
        translations = translationsProvider.getTranslations(language)
        println("i18n: Loaded ${translations.size} translations for ${language.displayName}")
    }
}
