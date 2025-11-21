# Tawazn i18n Quick Reference Guide

## Framework Summary

**Project Type:** Kotlin Multiplatform (KMP)  
**UI Framework:** Jetpack Compose Multiplatform  
**Targets:** Android, iOS, Desktop  
**Min Language Support:** English, Arabic  

**Key Technologies:**
- Kotlin 2.2.20
- Compose 1.9.0
- Koin 4.1.0 (DI)
- SQLDelight 2.0.2 (DB)
- DataStore 1.1.1 (Preferences)
- Voyager 1.1.0-beta03 (Navigation)

---

## File Locations Quick Map

### Important Feature Screens
| Screen | File Path |
|--------|-----------|
| Onboarding | `feature/onboarding/src/commonMain/kotlin/.../OnboardingScreen.kt` |
| Dashboard | `feature/dashboard/src/commonMain/kotlin/.../DashboardScreen.kt` |
| Settings | `feature/settings/src/commonMain/kotlin/.../SettingsScreen.kt` |
| App Blocking | `feature/app-blocking/src/commonMain/kotlin/.../AppBlockingScreen.kt` |
| Analytics | `feature/analytics/src/commonMain/kotlin/.../AnalyticsScreen.kt` |
| Usage Tracking | `feature/usage-tracking/src/commonMain/kotlin/.../UsageTrackingScreen.kt` |
| Profile | `feature/settings/src/commonMain/kotlin/.../ProfileScreen.kt` |
| Usage Goals | `feature/settings/src/commonMain/kotlin/.../UsageGoalsScreen.kt` |
| About | `feature/settings/src/commonMain/kotlin/.../AboutScreen.kt` |
| Focus Sessions | `feature/settings/src/commonMain/kotlin/.../FocusSessionListScreen.kt` |

### Core Modules
| Module | Purpose |
|--------|---------|
| `core/common` | Shared utilities |
| `core/design-system` | UI components & theme |
| `core/database` | Database setup |
| `core/datastore` | App preferences (USE FOR LANGUAGE PREFS!) |
| `core/network` | Ktor HTTP client |

---

## String Count by Screen

| Screen | Count | Priority | Complexity |
|--------|-------|----------|------------|
| Onboarding | 27 | High | Medium |
| Settings | 35 | High | High |
| Dashboard | 20 | High | Low |
| Analytics | 28 | Medium | High |
| Profile | 17 | Medium | Low |
| Usage Goals | 21 | Medium | Medium |
| About | 26 | Low | Low |
| Usage Tracking | 14 | Medium | Low |
| App Blocking | 7 | Low | Low |
| Focus Sessions | 11 | Low | Low |
| **Total** | **206** | | |

---

## Key Implementation Points

### 1. No i18n Library Currently Installed
- All 206+ strings are hardcoded in Kotlin files
- No translations exist
- Clean slate for implementation

### 2. Perfect for i18n Implementation
- Clean architecture with zero feature-to-feature dependencies
- Already using Koin for DI (perfect for StringProvider)
- DataStore available for language preference storage
- CompositionLocal pattern can be used for string access

### 3. Recommended Approach
Use **Runtime JSON-based provider** with:
- JSON files for translations (en.json, ar.json)
- Koin DI for StringProvider injection
- CompositionLocal for UI access
- DataStore for language preference persistence

### 4. High-Priority Screens to Start With
1. **OnboardingScreen** - First user experience is critical
2. **SettingsScreen** - Users expect localized settings
3. **DashboardScreen** - Main app interface
4. **AnalyticsScreen** - Important feature

---

## String Extraction Pattern

### Current Code Example
```kotlin
Text("Welcome to Tawazn")
Text("Dashboard")
Text("Screen Time")
```

### After i18n
```kotlin
val strings = LocalStringProvider.current
Text(strings.getString("onboarding.welcome_title"))
Text(strings.getString("dashboard.title"))
Text(strings.getString("dashboard.screen_time"))
```

### JSON Keys Pattern
```json
{
  "module.screen.element": "Text value"
}
```

Example:
```json
{
  "onboarding.welcome.title": "Welcome to Tawazn",
  "dashboard.header.title": "Dashboard",
  "dashboard.stats.screen_time": "Screen Time"
}
```

---

## String Provider Interface (Recommended)

```kotlin
interface StringProvider {
    /**
     * Get localized string by key
     * @param key Hierarchical key like "onboarding.welcome.title"
     * @param args Optional format arguments
     */
    fun getString(key: String, vararg args: Any = emptyArray()): String
    
    /**
     * Get localized string with fallback
     */
    fun getStringOrDefault(key: String, default: String): String
    
    /**
     * Change active language
     * @param languageCode ISO 639-1 language code (e.g., "en", "ar")
     */
    fun setLanguage(languageCode: String)
    
    /**
     * Get currently active language
     */
    fun getCurrentLanguage(): String
    
    /**
     * Get list of available languages
     */
    fun getAvailableLanguages(): List<Language>
}

enum class Language(val code: String, val displayName: String) {
    ENGLISH("en", "English"),
    ARABIC("ar", "العربية")
}
```

---

## Dependency Integration Points

### Koin Dependency Injection
```kotlin
// In core/i18n/I18nModule.kt
val i18nModule = module {
    single<StringProvider> { JsonStringProvider(get()) }
    single { LanguageRepository(get()) } // Uses DataStore
}
```

### DataStore Language Preference
```kotlin
// In core/datastore/AppPreferences.kt
val selectedLanguage: Flow<String> = 
    dataStore.data.map { prefs ->
        prefs[stringPreferencesKey("selected_language")] ?: "en"
    }

suspend fun setSelectedLanguage(language: String) {
    dataStore.edit { prefs ->
        prefs[stringPreferencesKey("selected_language")] = language
    }
}
```

### CompositionLocal Access in UI
```kotlin
// In core/i18n/LocalStringProvider.kt
val LocalStringProvider = compositionLocalOf<StringProvider> {
    error("StringProvider not provided")
}

// In App.kt
CompositionLocalProvider(
    LocalStringProvider provides stringProvider
) {
    // All composables can access stringProvider via LocalStringProvider.current
}
```

---

## Quick Implementation Checklist

### Week 1: Foundation
- [ ] Create `core/i18n` module
- [ ] Create StringProvider interface
- [ ] Implement JsonStringProvider
- [ ] Create translation JSON structure
- [ ] Add all 206+ strings to en.json
- [ ] Create ar.json with Arabic translations
- [ ] Set up Koin DI for StringProvider
- [ ] Create CompositionLocal wrapper

### Week 2: Integration & Testing
- [ ] Integrate i18nModule into composeApp
- [ ] Update App.kt to provide LocalStringProvider
- [ ] Add language preference to DataStore
- [ ] Test StringProvider with all keys
- [ ] Test language switching

### Week 3-4: Refactor Screens
- [ ] Refactor OnboardingScreen
- [ ] Refactor SettingsScreen
- [ ] Refactor DashboardScreen
- [ ] Refactor AnalyticsScreen
- [ ] Refactor all other screens (parallel)
- [ ] Test RTL rendering for Arabic
- [ ] Test dynamic string formatting

### Week 4: Polish & Deploy
- [ ] Add language selection to Settings UI
- [ ] Add system language detection
- [ ] Comprehensive testing on all screens
- [ ] Test on Android, iOS, Desktop
- [ ] Performance testing
- [ ] Deploy to production

---

## Testing Checklist

### Functional Testing
- [ ] All strings render correctly in English
- [ ] All strings render correctly in Arabic
- [ ] Language switch works without restart
- [ ] Language preference persists across app launches
- [ ] System language detection works
- [ ] Fallback to English works for missing keys

### UI/UX Testing
- [ ] RTL text alignment correct for Arabic
- [ ] Text doesn't overflow in any language
- [ ] Dialogs and alerts show translated text
- [ ] Empty states show translated text
- [ ] Button labels are translated
- [ ] Error messages are translated
- [ ] Placeholder text is translated

### Localization Testing
- [ ] Date formats correct per language
- [ ] Number formats correct per language
- [ ] Time formats correct per language
- [ ] Currency formats correct (if applicable)
- [ ] Plural forms handled correctly

### Platform Testing
- [ ] Android: All screens, languages, preferences
- [ ] iOS: All screens, languages, RTL support
- [ ] Desktop: All screens, languages, preferences

---

## Common Pitfalls to Avoid

1. **Don't** hardcode app version or company name - make it a key
2. **Don't** forget to translate button labels and placeholders
3. **Don't** assume English text lengths - Arabic is longer
4. **Don't** forget RTL considerations for Arabic
5. **Don't** put formatting logic in translation strings
6. **Don't** forget dialog and alert messages
7. **Don't** forget error messages
8. **Don't** translate brand name (Tawazn) - keep as-is

---

## Useful Commands

### Find all hardcoded Text() calls
```bash
grep -r "Text(" feature/*/src/commonMain/kotlin/ | grep -v "//.*Text(" | wc -l
```

### Find specific pattern
```bash
grep -r 'Text("' composeApp/src/ | grep -v "LocalStringProvider" | head -20
```

### Count strings per screen
```bash
find feature/ -name "*.kt" -exec grep -l "Text(" {} \; | xargs wc -l
```

---

## Resources

### Official Documentation
- [Kotlin Multiplatform Documentation](https://kotlinlang.org/docs/multiplatform.html)
- [Compose Multiplatform](https://www.jetbrains.com/help/compose/multiplatform/)
- [Koin DI Framework](https://insert-koin.io/)

### i18n Specific
- [Unicode CLDR (Date/Number Formats)](https://cldr.unicode.org/)
- [ISO 639-1 Language Codes](https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes)
- [RTL Considerations for Arabic](https://material.io/design/platform-guidance/android-bars.html#bottom-app-bar)

### KMP i18n Examples
- [Moko Resources](https://github.com/icerockdev/moko-resources)
- [KMP i18n Example Apps](https://github.com/JetBrains/compose-multiplatform-core)

---

## Contact Points

### Key Files to Modify
1. `settings.gradle.kts` - Add i18n module
2. `composeApp/build.gradle.kts` - Add dependency
3. `composeApp/src/commonMain/kotlin/App.kt` - Initialize i18n
4. `core/datastore/AppPreferences.kt` - Add language preference
5. All feature screen files - Replace hardcoded strings

### Key Classes to Create
1. `StringProvider.kt` - Main interface
2. `JsonStringProvider.kt` - Implementation
3. `Language.kt` - Language enum
4. `I18nModule.kt` - Koin configuration
5. `LocalStringProvider.kt` - CompositionLocal wrapper

---

## Success Metrics

- [ ] 100% of UI strings externalized
- [ ] English and Arabic fully supported
- [ ] Language switching works instantly
- [ ] No hardcoded strings in production code
- [ ] All screens tested in both languages
- [ ] No performance degradation
- [ ] Zero crashes due to missing keys

