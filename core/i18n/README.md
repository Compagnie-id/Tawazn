# i18n Module

Internationalization (i18n) support for the Tawazn app.

## Supported Languages

- **English** (en) - Default
- **Bahasa Indonesia** (id)
- **Arabic** (ar)

## Architecture

### Key Components

1. **StringProvider**: Main interface for accessing translations
2. **StringProviderImpl**: Implementation with system locale detection and DataStore persistence
3. **TranslationsProvider**: Provides translations for each language
4. **SystemLocaleProvider**: Platform-specific system locale detection
5. **Language**: Enum of supported languages

### Initialization Strategy

The i18n system uses a **two-phase initialization** approach to ensure zero race conditions:

#### Phase 1: Synchronous Initialization (in `init` block)
```kotlin
init {
    // Immediately detect system locale
    val systemLocale = systemLocaleProvider.getSystemLocale()
    val initialLanguage = Language.fromCode(systemLocale)

    // Load translations synchronously
    translations = translationsProvider.getTranslations(initialLanguage)
}
```

**Benefits:**
- UI has correct translations immediately (no flash of wrong language)
- No race conditions or empty strings
- Respects user's system language preference automatically

#### Phase 2: Async Preference Loading (in `initialize()`)
```kotlin
suspend fun initialize() {
    // Check if user has saved a language preference
    val savedLanguageCode = dataStore.data.first()[languageKey]

    if (savedLanguageCode != null) {
        // User explicitly chose a language, use it
        setLanguage(Language.fromCode(savedLanguageCode))
    }
}
```

**Benefits:**
- Respects explicit user choice over system locale
- Doesn't block app startup
- Gracefully handles DataStore not being ready

### Thread Safety

- `translations` map is marked `@Volatile` for visibility across threads
- `currentLanguage` uses `StateFlow` for thread-safe reactive updates
- Language switching is atomic and idempotent

## Usage

### In Compose UI

```kotlin
@Composable
fun MyScreen() {
    // Get localized string
    val title = stringResource("screen.title")

    // Get string with formatting
    val message = stringResource("greeting.message", userName)

    // Access StringProvider directly
    val stringProvider = LocalStringProvider.current
    val currentLang by stringProvider.currentLanguage.collectAsState()
}
```

### Changing Language

```kotlin
val stringProvider: StringProvider = koinInject()

// In a coroutine
scope.launch {
    stringProvider.setLanguage(Language.INDONESIAN)
}
```

### Adding New Translations

1. Open `Translations.kt`
2. Add your key to all three language functions:
   ```kotlin
   fun getEnglishTranslations() = mapOf(
       "my.new.key" to "My Translation"
   )

   fun getIndonesianTranslations() = mapOf(
       "my.new.key" to "Terjemahan Saya"
   )

   fun getArabicTranslations() = mapOf(
       "my.new.key" to "ترجمتي"
   )
   ```

### Translation Key Naming Convention

Use dot-notation with hierarchical structure:
- `screen.element` - Screen-specific text
- `common.action` - Common/shared text
- `error.type` - Error messages
- `notification.event` - Notifications

Examples:
- `settings.title` - "Settings" screen title
- `common.save` - Generic "Save" button
- `error.network` - Network error message

## Platform Support

The module supports:
- ✅ Android (via AndroidSystemLocaleProvider)
- ✅ JVM Desktop (via Locale.getDefault())
- ✅ iOS (via NSLocale.currentLocale)

## Testing

To test language switching:
1. Change your Android system language in Settings
2. Fresh install the app - it should use your system language
3. Switch language in app Settings - your choice is saved
4. Restart app - it should use your saved preference (not system locale)

## Performance

- **Initialization time**: ~1-2ms (synchronous locale detection + map creation)
- **Memory footprint**: ~330 strings × 3 languages = ~100KB
- **Language switch time**: ~1ms (single map assignment)

All translations are embedded in the binary (no file I/O or network requests).
