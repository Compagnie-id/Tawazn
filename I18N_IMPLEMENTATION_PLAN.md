# Tawazn i18n Implementation Plan

## Quick Start Checklist

### Phase 1: Foundation Setup (Week 1)

#### Create Core i18n Module
- [ ] Create new module: `core/i18n`
- [ ] Add `build.gradle.kts` with common dependencies
- [ ] Create package structure:
  - [ ] `id.compagnie.tawazn.core.i18n`
  - [ ] `id.compagnie.tawazn.core.i18n.provider`
  - [ ] `id.compagnie.tawazn.core.i18n.model`

#### Implement String Provider Infrastructure
```kotlin
// StringProvider.kt
interface StringProvider {
    fun getString(key: String, vararg args: Any = emptyArray()): String
    fun setLanguage(languageCode: String)
    fun getCurrentLanguage(): String
    fun getAvailableLanguages(): List<String>
}

// Language enum
enum class Language(val code: String, val displayName: String) {
    ENGLISH("en", "English"),
    ARABIC("ar", "العربية")
}
```

#### Create Translation JSON Files
- [ ] Create directories:
  ```
  composeApp/src/commonMain/resources/
  └── i18n/
      ├── en.json
      ├── ar.json
      ├── es.json (optional)
      └── strings-keys.json (reference file)
  ```

- [ ] Create `en.json` with all string keys
- [ ] Create `ar.json` with Arabic translations
- [ ] Create `strings-keys.json` documenting all keys and contexts

#### Example JSON Structure
```json
{
  "onboarding": {
    "welcome_title": "Welcome to Tawazn",
    "welcome_subtitle": "توازن",
    "welcome_description": "Find balance in your digital life. Track your screen time, block distracting apps, and achieve digital wellness.",
    "powerful_features": "Powerful Features",
    "usage_tracking_title": "Usage Tracking",
    "usage_tracking_desc": "Monitor your screen time with detailed statistics and insights"
  },
  "dashboard": {
    "title": "Dashboard",
    "greeting": "Welcome back! 👋",
    "todays_overview": "Today's Overview",
    "screen_time": "Screen Time",
    "apps_blocked": "Apps Blocked"
  }
}
```

---

### Phase 2: Koin Integration (Week 1-2)

#### Create Koin Module
- [ ] Create `I18nModule.kt` in `core/i18n`
- [ ] Set up Koin DSL:
  ```kotlin
  val i18nModule = module {
      single<StringProvider> { JsonStringProvider() }
      single { Language.ENGLISH } // or system language
  }
  ```

#### Add to composeApp Dependencies
- [ ] Update `composeApp/build.gradle.kts`:
  - [ ] Add dependency: `implementation(project(":core:i18n"))`
- [ ] Update `composeApp/src/commonMain/kotlin/App.kt`:
  - [ ] Load i18nModule in Koin initialization

#### Create CompositionLocal
- [ ] Create in `core/i18n`:
  ```kotlin
  val LocalStringProvider = compositionLocalOf<StringProvider> { 
      error("StringProvider not provided")
  }
  ```
- [ ] Provide in App.kt using CompositionLocalProvider

---

### Phase 3: Refactor Screens (Week 2-3)

#### High Priority Screens (Do First)
1. **OnboardingScreen**
   - [ ] Extract all hardcoded strings
   - [ ] Replace with `StringProvider.getString(key)`
   - [ ] Test on all screen pages
   - [ ] Verify button text, dialogs, empty states

2. **SettingsScreen**
   - [ ] Extract section headers
   - [ ] Extract all setting labels and descriptions
   - [ ] Extract dialog messages
   - [ ] Extract buttons and actions

3. **DashboardScreen**
   - [ ] Extract title, greeting, section headers
   - [ ] Extract card titles and stats labels
   - [ ] Extract button labels

#### Medium Priority Screens (Do Second)
4. **AnalyticsScreen**
   - [ ] Extract all section titles
   - [ ] Extract card labels and descriptions
   - [ ] Extract insight titles and messages

5. **UsageGoalsScreen**
   - [ ] Extract form labels
   - [ ] Extract help text and tips
   - [ ] Extract preset labels

#### Quick Refactor Pattern
```kotlin
// Before
Text("Welcome to Tawazn")

// After
val strings = LocalStringProvider.current
Text(strings.getString("onboarding.welcome_title"))
```

---

### Phase 4: Add Language Selection (Week 3)

#### Add to SettingsScreen
- [ ] Create new settings item for language selection
- [ ] Add RadioButton group or Dropdown for languages
- [ ] Implement language change logic:
  - [ ] Update StringProvider.setLanguage()
  - [ ] Save preference to DataStore
  - [ ] Recompose UI with new strings

#### Update ProfileScreen
- [ ] Add language preference to user profile
- [ ] Display current language

---

### Phase 5: DataStore Integration (Week 4)

#### Create LanguagePreference
- [ ] Add to AppPreferences:
  ```kotlin
  val selectedLanguage: Flow<String> = 
      dataStore.data.map { it[stringPreferencesKey("language")] ?: "en" }
  
  suspend fun setSelectedLanguage(languageCode: String) {
      dataStore.edit { it[stringPreferencesKey("language")] = languageCode }
  }
  ```

#### Auto-load on App Start
- [ ] In App.kt: Load saved language preference
- [ ] Pass to StringProvider
- [ ] Handle system language detection as fallback

---

### Phase 6: Testing & Validation (Week 4)

#### Unit Tests
- [ ] Test StringProvider.getString() for all keys
- [ ] Test language switching
- [ ] Test fallback to English when key missing
- [ ] Test with special characters and emojis

#### Manual Testing Checklist
- [ ] [ ] Switch to English - verify all text
- [ ] [ ] Switch to Arabic - verify all text and RTL layout
- [ ] [ ] Dynamic values (time, numbers) still format correctly
- [ ] [ ] All screens display correctly in both languages
- [ ] [ ] Navigation and buttons work in both languages
- [ ] [ ] Empty states show translated text
- [ ] [ ] Dialogs and alerts show translated text
- [ ] [ ] Permission messages are translated

---

## Implementation Files to Update

### New Files to Create
```
core/i18n/
├── build.gradle.kts
└── src/commonMain/kotlin/id/compagnie/tawazn/core/i18n/
    ├── I18nModule.kt
    ├── StringProvider.kt
    ├── JsonStringProvider.kt
    ├── Language.kt
    └── LocalStringProvider.kt

composeApp/src/commonMain/resources/i18n/
├── en.json
├── ar.json
└── strings-keys.json
```

### Files to Modify
1. `settings.gradle.kts` - Add i18n module
2. `composeApp/build.gradle.kts` - Add i18n dependency
3. `composeApp/src/commonMain/kotlin/App.kt` - Load i18nModule
4. `core/datastore/AppPreferences.kt` - Add language preference
5. All feature screens - Use LocalStringProvider

---

## String Keys Master List

### Onboarding Module (27 strings)
```
onboarding.welcome_title
onboarding.welcome_subtitle
onboarding.welcome_description
onboarding.powerful_features
onboarding.usage_tracking
onboarding.usage_tracking_desc
onboarding.app_blocking
onboarding.app_blocking_desc
onboarding.smart_scheduling
onboarding.smart_scheduling_desc
onboarding.analytics
onboarding.analytics_desc
onboarding.required_permissions
onboarding.all_permissions_granted
onboarding.screen_time_access
onboarding.screen_time_access_desc
onboarding.app_blocking_permission
onboarding.app_blocking_permission_desc
onboarding.requesting_permissions
onboarding.check_again
onboarding.data_private_secure
onboarding.ready_title
onboarding.start_journey
onboarding.ready_description
onboarding.view_screen_time
onboarding.block_apps_instant
onboarding.create_focus_sessions
onboarding.track_progress
onboarding.limited_functionality
onboarding.limited_functionality_desc
onboarding.continue
onboarding.get_started
onboarding.back
onboarding.skip
```

### Dashboard Module (20 strings)
```
dashboard.title
dashboard.greeting
dashboard.todays_overview
dashboard.screen_time
dashboard.apps_blocked
dashboard.most_used_today
dashboard.percent_decrease_yesterday
dashboard.active_now
dashboard.minutes_label
dashboard.quick_actions
dashboard.block_apps
dashboard.view_usage
dashboard.start_focus_mode
dashboard.stay_productive
dashboard.manage_sessions
dashboard.weekly_insights
dashboard.weekly_report
dashboard.average_daily
dashboard.youre_doing_great
```

### Continue for other modules...

---

## Testing Scenarios

### Scenario 1: Language Switch
1. Launch app in English
2. Go to Settings > Language
3. Select Arabic
4. Verify all text switches to Arabic
5. Go back to settings
6. Verify text persists in Arabic on app restart

### Scenario 2: Missing Translations
1. Launch with language set to unsupported language
2. Verify fallback to English
3. Check logs for missing key warnings

### Scenario 3: Dynamic Content
1. Set daily goal to "3h 45m"
2. Verify number formatting correct in target language
3. Check Arabic RTL support for numbers

---

## Estimated Effort

| Phase | Task | Effort | Duration |
|-------|------|--------|----------|
| 1 | Core module setup | 5-8 hours | 1-2 days |
| 2 | Koin integration | 3-4 hours | 1 day |
| 3 | Refactor screens | 20-30 hours | 1 week |
| 4 | Language selection | 4-6 hours | 1-2 days |
| 5 | DataStore integration | 4-6 hours | 1-2 days |
| 6 | Testing | 8-12 hours | 2-3 days |
| **Total** | | **44-66 hours** | **3-4 weeks** |

---

## Resources & References

### Kotlin Multiplatform i18n
- [Compose Multiplatform Documentation](https://www.jetbrains.com/help/compose/multiplatform/)
- [Moko Resources Library](https://github.com/icerockdev/moko-resources)
- [Koin Documentation](https://insert-koin.io/)

### JSON Parsing in KMP
- [kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization)
- [Compose Resources](https://www.jetbrains.com/help/compose/resources/)

### Best Practices
- Keep strings in single source of truth
- Use consistent key naming conventions
- Include context comments in JSON
- Test with actual translators for quality
- Plan for pluralization rules per language
- Consider date/number formatting per locale

