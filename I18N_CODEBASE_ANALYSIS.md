# Tawazn Codebase Comprehensive Analysis

## Executive Summary

**Tawazn** is a sophisticated, cross-platform screen time management application built with **Kotlin Multiplatform Project (KMP)** using **Jetpack Compose Multiplatform**. The project targets Android, iOS, and Desktop platforms with a clean, modular architecture.

---

## 1. Framework & Technology Stack

### Primary Framework
- **Kotlin Multiplatform (KMP)** - Shared business logic across platforms
- **Jetpack Compose Multiplatform** - UI framework for all platforms
- **Compose Material 3** - Modern Material Design components
- **Compose Hot Reload** - Development experience enhancement

### Key Technologies

| Component | Library | Version | Purpose |
|-----------|---------|---------|---------|
| **UI Framework** | Compose Multiplatform | 1.9.0 | Cross-platform UI |
| **Language** | Kotlin | 2.2.20 | Primary programming language |
| **Navigation** | Voyager | 1.1.0-beta03 | Screen navigation with TabNavigator |
| **Dependency Injection** | Koin | 4.1.0 | DI container |
| **Database** | SQLDelight | 2.0.2 | Type-safe database |
| **Preferences** | DataStore | 1.1.1 | App preferences storage |
| **Async** | Coroutines | 1.10.2 | Async operations |
| **Serialization** | kotlinx.serialization | 1.7.3 | JSON serialization |
| **Date/Time** | kotlinx.datetime | 0.7.1 | Date/time operations |
| **Networking** | Ktor | 3.3.2 | HTTP client |
| **Logging** | Kermit | 2.0.4 | Logging utility |
| **Icons** | Phosphor Icons | 1.0.0 | Icon library (Bold style) |

### Target Platforms
- **Android** (minSdk: 24, targetSdk: 36)
- **iOS** (arm64, simulatorArm64)
- **Desktop/JVM** (Java 11+)

---

## 2. Project Structure

### High-Level Architecture

```
Tawazn/
├── composeApp/
│   ├── src/commonMain/kotlin/
│   │   ├── id/compagnie/tawazn/
│   │   │   ├── App.kt                    # Main app composable
│   │   │   ├── Greeting.kt
│   │   │   ├── Platform.kt
│   │   │   └── navigation/
│   │   │       ├── AppNavigation.kt      # Bottom tab navigation
│   │   │       └── NavigationDestination.kt
│   │   └── composeResources/
│   ├── src/androidMain/
│   ├── src/iosMain/
│   └── build.gradle.kts
│
├── core/                                  # Core modules (zero feature deps)
│   ├── common/                           # Shared utilities
│   ├── design-system/                    # UI components & theme
│   ├── database/                         # SQLDelight setup
│   ├── datastore/                        # Preferences storage
│   └── network/                          # Ktor configuration
│
├── domain/                                # Business logic (pure Kotlin)
│   └── model/                            # Domain models
│
├── data/                                  # Repository implementations
│   └── repository/                       # Concrete repository impls
│
├── feature/                               # Feature modules (independent)
│   ├── onboarding/
│   │   └── src/commonMain/kotlin/
│   │       ├── OnboardingScreen.kt
│   │       └── OnboardingScreenModel.kt
│   ├── dashboard/
│   │   └── DashboardScreen.kt
│   ├── app-blocking/
│   │   └── AppBlockingScreen.kt
│   ├── analytics/
│   │   └── AnalyticsScreen.kt
│   ├── usage-tracking/
│   │   └── UsageTrackingScreen.kt
│   └── settings/
│       ├── SettingsScreen.kt
│       ├── ProfileScreen.kt
│       ├── PrivacySecurityScreen.kt
│       ├── UsageGoalsScreen.kt
│       ├── AboutScreen.kt
│       ├── FocusSessionListScreen.kt
│       └── CreateEditFocusSessionScreen.kt
│
└── platform/                              # Platform-specific code
    ├── android/
    ├── ios/
    └── desktop/
```

### Key Architecture Principles
- **Zero Feature-to-Feature Dependencies** - Features never import each other
- **Composition Local Pattern** - Navigation callbacks passed via CompositionLocal
- **Clean Separation of Concerns** - Clear layer boundaries
- **Koin DI** - Centralized dependency injection per module

---

## 3. UI Screens & Components with Text Content

### Screen Inventory

#### 1. **OnboardingScreen** (`feature/onboarding`)
Location: `/home/user/Tawazn/feature/onboarding/src/commonMain/kotlin/id/compagnie/tawazn/feature/onboarding/OnboardingScreen.kt`

Pages:
- **WelcomePage**
  - "Welcome to Tawazn"
  - "توازن" (Arabic, meaning "balance")
  - "Find balance in your digital life. Track your screen time, block distracting apps, and achieve digital wellness."

- **FeaturePage**
  - "Powerful Features"
  - "Usage Tracking" - "Monitor your screen time with detailed statistics and insights"
  - "App Blocking" - "Block distracting apps and create focus sessions"
  - "Smart Scheduling" - "Set up automatic blocking schedules for better productivity"
  - "Insights & Analytics" - "Understand your digital habits with weekly reports"

- **PermissionPage**
  - "Required Permissions"
  - "All permissions granted!"
  - "Screen Time Access" - "Track app usage and screen time statistics. Your data stays private on this device."
  - "App Blocking" - "Allow Tawazn to block distracting apps and help you stay focused."
  - "Requesting permissions..."
  - "Check Again"
  - "Your data is private and secure"

- **ReadyPage**
  - "You're All Set!"
  - "Start Your Journey"
  - "Tawazn is ready to help you achieve digital wellness. Here's what you can do:"
  - "View your daily screen time"
  - "Block distracting apps instantly"
  - "Create focus sessions"
  - "Track your progress"
  - "Limited Functionality"
  - "Some features require permissions. You can grant them later in Settings."

- **Navigation Buttons**
  - "Continue" / "Get Started"
  - "Back"
  - "Skip"

---

#### 2. **DashboardScreen** (`feature/dashboard`)
Location: `/home/user/Tawazn/feature/dashboard/src/commonMain/kotlin/id/compagnie/tawazn/feature/dashboard/DashboardScreen.kt`

Content:
- "Dashboard"
- "Welcome back! 👋"
- "Today's Overview"
- "Screen Time" / "Apps Blocked" / "Most Used Today"
- "↓ 15% from yesterday" (subtitle)
- "Active now"
- "Instagram" / "45 minutes"
- "Quick Actions"
- "Block Apps"
- "View Usage"
- "Start Focus Mode"
- "Block distracting apps and stay productive"
- "Manage Sessions"
- "Weekly Insights"
- "Weekly Report"
- "Average: 3h 12m per day"
- "You're doing great! 📈"

---

#### 3. **AppBlockingScreen** (`feature/app-blocking`)
Location: `/home/user/Tawazn/feature/app-blocking/src/commonMain/kotlin/id/compagnie/tawazn/feature/appblocking/AppBlockingScreen.kt`

Content:
- "Block Apps"
- "Search apps..." (placeholder)
- "$blockedCount Apps Blocked"
- "$apps.size total apps"

---

#### 4. **AnalyticsScreen** (`feature/analytics`)
Location: `/home/user/Tawazn/feature/analytics/src/commonMain/kotlin/id/compagnie/tawazn/feature/analytics/AnalyticsScreen.kt`

Content:
- "Analytics & Insights"
- "This Week"
- "Avg Daily" / "Best Day"
- "This week"
- "Progress"
- "Today's Goal Progress"
- "% towards your daily goal of {goal} ({used} used)"
- "No Active Streak" / "{streak} Day Streak"
- "Keep it up! Best: {best} days"
- "Start by meeting your daily goal"
- "Insights"
- "Most Productive Day" - "{day} - Only {time} screen time"
- "Weekly Total" - "{time} total screen time this week"
- "Top App" - "{app} - {minutes}m average daily"
- "Recommendations"
- "Focus Session Suggestion"
- "Based on your usage patterns, we recommend creating a focus session from 8-10 PM to reduce evening screen time."
- "Manage Sessions"
- "Achievements"
- "Goal Getter" - "Met daily goal 5x"
- "Speed Demon" - "1 week streak"

---

#### 5. **UsageTrackingScreen** (`feature/usage-tracking`)
Location: `/home/user/Tawazn/feature/usage-tracking/src/commonMain/kotlin/id/compagnie/tawazn/feature/usagetracking/UsageTrackingScreen.kt`

Content:
- "Usage Tracking"
- Filter period chips: "TODAY", "WEEK", "MONTH"
- "Overview"
- "Screen Time" / "App Opens"
- "Total time" / "Times opened"
- "Most Used Apps"
- "No usage data yet"
- "Grant usage access permission to track your screen time"
- "{opens} opens"

---

#### 6. **SettingsScreen** (`feature/settings`)
Location: `/home/user/Tawazn/feature/settings/src/commonMain/kotlin/id/compagnie/tawazn/feature/settings/SettingsScreen.kt`

Content:
- "Settings"
- **Account Section**
  - "Profile" - "Manage your profile"
  - "Privacy & Security" - "Control your data"
- **Notifications Section**
  - "Notifications" - "Enable notifications"
  - "Daily Report" - "Get daily screen time summary"
  - "Weekly Report" - "Get weekly insights"
- **Focus & Productivity**
  - "Usage Goals" - "Set daily and weekly screen time goals"
  - "Focus Sessions" - "Manage scheduled blocking sessions"
- **Appearance**
  - "Use System Theme" - "Follow system dark mode setting"
  - "Dark Mode" - "Use dark theme" / "Disabled (using system theme)"
- **Data & Storage**
  - "Backup & Sync" - "Cloud backup (Coming soon)"
  - "Clear Data" - "Delete all usage data"
- **Platform Status**
  - "Permissions"
  - "Grant Permissions" / "Granted"
  - "Sync"
- **Permissions**
  - "Usage Access" - "Required for tracking"
  - "Accessibility Service" - "Required for blocking"
- **About**
  - "About Tawazn" - "Version 1.0.0"
  - "Privacy Policy" - "Read our privacy policy"
  - "Terms of Service" - "Read our terms"
  - "Report a Bug" - "Help us improve"
- **Footer**
  - "Made with ❤️ using Compose Multiplatform"
- **Clear Data Dialog**
  - "Clear All Data?"
  - "This will permanently delete all your usage history, blocked apps, and settings. This action cannot be undone."
  - "Clear All Data"
  - "Cancel"

---

#### 7. **ProfileScreen** (`feature/settings`)
Location: `/home/user/Tawazn/feature/settings/src/commonMain/kotlin/id/compagnie/tawazn/feature/settings/ProfileScreen.kt`

Content:
- "Profile"
- "Your Name" / "your.email@example.com" (placeholders in edit mode)
- "Set your name"
- "Add email"
- "Your Progress"
- "Day Streak"
- "Best Streak"
- "Account Information"
- "Member Since" - "January 2024"
- "Platform" - "{platformName}"
- "Your profile data is stored locally on your device and is never shared with third parties."
- "Save"
- "Cancel"

---

#### 8. **UsageGoalsScreen** (`feature/settings`)
Location: `/home/user/Tawazn/feature/settings/src/commonMain/kotlin/id/compagnie/tawazn/feature/settings/UsageGoalsScreen.kt`

Content:
- "Usage Goals"
- "Set Your Goals"
- "Set daily and weekly screen time goals to help maintain a healthy digital balance."
- "Daily Goal"
- "Target Screen Time" - "Maximum daily usage"
- "Quick Presets"
- "Weekly Goal"
- "Target Screen Time" - "Maximum weekly usage"
- "30 min" / "12 hours" (range labels)
- "3 hours" / "84 hours" (range labels)
- "Tips for Success"
- "• Start with realistic goals you can achieve
• Gradually reduce screen time over weeks
• Use focus sessions during peak usage times
• Review your analytics regularly for insights"
- "Save Goals"
- Preset buttons: "1h", "2h", "3h", "4h", "14h", "21h", "28h", "35h"

---

#### 9. **AboutScreen** (`feature/settings`)
Location: `/home/user/Tawazn/feature/settings/src/commonMain/kotlin/id/compagnie/tawazn/feature/settings/AboutScreen.kt`

Content:
- "About"
- "Tawazn"
- "Version 1.0.0"
- "Balance your digital life"
- "About Tawazn" (section)
- "Tawazn (تَوازُن) means 'balance' in Arabic. This app helps you achieve a healthier relationship with technology by tracking your screen time, blocking distracting apps, and providing insights into your digital habits."
- **Features**
  - "Usage Tracking" - "Monitor your app usage in real-time"
  - "App Blocking" - "Block distracting apps when you need focus"
  - "Focus Sessions" - "Schedule blocking sessions for maximum productivity"
  - "Analytics" - "Get insights and trends about your digital habits"
- **Links**
  - "Source Code" - "View on GitHub"
  - "Report a Bug" - "Help us improve"
  - "Licenses" - "Open source licenses"
- **Developer**
  - "Compagnie.id"
  - "Building tools for digital wellbeing"
- **Copyright**
  - "© 2024 Compagnie.id\nAll rights reserved"
  - "Made with ❤️ using Compose Multiplatform"
- **Licenses Dialog**
  - "Open Source Licenses"
  - "This app uses the following open source libraries:"
  - List of libraries with licenses (Compose Multiplatform, Kotlin, Kotlinx Coroutines, etc.)
  - "Close"

---

#### 10. **FocusSessionListScreen** (`feature/settings`)
Location: `/home/user/Tawazn/feature/settings/src/commonMain/kotlin/id/compagnie/tawazn/feature/settings/FocusSessionListScreen.kt`

Content:
- "Focus Sessions"
- "No Focus Sessions" (empty state)
- "Create a session to schedule app blocking"
- "Create Session"

---

#### 11. **PrivacySecurityScreen** (Brief check needed)
- Likely contains privacy/security settings text

---

#### 12. **CreateEditFocusSessionScreen** (Create/Edit focus sessions)
- Session creation/editing UI with time selectors

---

### Summary of Text Categories

**Static UI Labels:**
- Screen titles (Dashboard, Settings, Profile, etc.)
- Section headers (Account, Notifications, Appearance, etc.)
- Setting names and descriptions
- Button labels (Continue, Save, Cancel, Skip, etc.)
- Dialog titles and messages

**Dynamic Content:**
- Numerical values (screen time, app counts, streaks)
- Formatted time strings (2h 34m, 45 minutes)
- Percentage values
- User-entered data (username, email)
- Period names (TODAY, WEEK, MONTH)

**Informational Text:**
- Help messages
- Placeholder text
- Error messages
- Empty state messages
- Tips and guidance text

---

## 4. Current i18n Library Status

### Current State: NO i18n LIBRARY INSTALLED

Checked dependencies in `/home/user/Tawazn/gradle/libs.versions.toml`:
- No Compose i18n library found
- No Android internationalization setup
- All strings are hardcoded in Kotlin source files

### Potential i18n Solutions for Kotlin Multiplatform

**Recommended Option 1: Moko Resources**
- Library: `dev.icerock:moko-resources`
- Supports: Android, iOS, Desktop
- Composes with KMP naturally
- Resource-based approach with code generation

**Recommended Option 2: Composable Strings with Data Classes**
- Use Kotlin data classes for string resources
- Each language is a separate object
- Load via DI (Koin)
- Simple but effective

**Recommended Option 3: gettext/i18next style JSON**
- Manage translations in JSON files
- Load at runtime
- Flexible but requires runtime loading

**NOT Recommended: Android Resources Only**
- Android String resources (.xml) don't work on iOS/Desktop
- Would require platform-specific implementations
- Breaks KMP advantage

---

## 5. Package Dependencies & Architecture

### Gradle Module Structure

```
settings.gradle.kts
├── :composeApp              # Main app module (depends on all)
├── :core:common
├── :core:design-system      # UI components & theme
├── :core:database
├── :core:datastore
├── :core:network
├── :domain
├── :data
├── :feature:onboarding
├── :feature:dashboard
├── :feature:app-blocking
├── :feature:analytics
├── :feature:usage-tracking
├── :feature:settings
├── :platform:android
├── :platform:ios
└── :platform:desktop
```

### Dependency Hierarchy

```
composeApp
  ├── :core:common
  ├── :core:design-system
  ├── :core:database
  ├── :core:datastore
  ├── :core:network
  ├── :domain
  ├── :data
  ├── :feature:onboarding
  ├── :feature:dashboard
  ├── :feature:app-blocking
  ├── :feature:analytics
  ├── :feature:usage-tracking
  └── :feature:settings

:feature modules
  └── :core modules only (no cross-feature deps)

:core modules
  └── External dependencies only
```

### Key Gradle Dependencies (Relevant to i18n)

```toml
[versions]
composeMultiplatform = "1.9.0"
kotlin = "2.2.20"
koin = "4.1.0"
kotlinx-serialization = "1.7.3"
kotlinx-datetime = "0.7.1"
datastore = "1.1.1"

[libraries]
# Compose components
compose.runtime
compose.foundation
compose.material3
compose.ui
compose.components.resources

# DI (useful for i18n provider)
koin-core
koin-compose

# Serialization (for i18n JSON loading)
kotlinx-serialization-json

# Storage
datastore-preferences
datastore-preferences-android
```

---

## 6. Internationalization Recommendations

### Recommended Implementation Strategy

#### **Option A: Moko Resources (Best for Production)**

**Advantages:**
- Native KMP support
- Type-safe strings
- Code generation
- Works on all platforms
- IDE autocompletion
- No runtime serialization overhead

**Implementation Steps:**
1. Add dependency to `libs.versions.toml`:
   ```toml
   moko-resources = "0.24.0"  # or latest
   ```

2. Create resource structure:
   ```
   composeApp/src/commonMain/resources/
   └── values/
       ├── strings.xml
       └── strings_ar.xml
       └── strings_es.xml
   ```

3. Generated Kotlin code for compile-time strings
4. Inject via Koin for theme-based language switching

**Effort:** High (infrastructure setup), but production-ready

---

#### **Option B: Runtime JSON Strings (Flexible)**

**Advantages:**
- No compile-time generation needed
- Easy to add new languages
- Can be updated without recompile
- Simple to implement

**Implementation Steps:**
1. Create string resource files as JSON:
   ```
   composeApp/src/commonMain/resources/
   └── i18n/
       ├── en.json
       ├── ar.json
       └── es.json
   ```

2. Create StringProvider interface:
   ```kotlin
   interface StringProvider {
       fun getString(key: String): String
       fun setLanguage(lang: String)
   }
   ```

3. Inject via Koin in App.kt
4. Use CompositionLocal for access

**Effort:** Medium (moderate setup)

---

#### **Option C: Sealed Class Approach (Simple)**

**Advantages:**
- Type-safe
- Simple to understand
- No external dependencies
- Easy testing

**Implementation Steps:**
1. Create sealed class for strings:
   ```kotlin
   sealed class Strings {
       object English : StringProvider()
       object Arabic : StringProvider()
       
       // Properties for each string
       abstract val welcomeMessage: String
   }
   ```

2. Inject via Koin
3. Use CompositionLocal for access

**Effort:** Low (simple setup)

---

### Recommended Approach for Tawazn

**Strategy: Hybrid (JSON + Koin + CompositionLocal)**

1. **Phase 1: Enable English + Arabic**
   - Create `core/i18n` module
   - JSON files for translations
   - Runtime JSON parsing
   - Koin provider + CompositionLocal

2. **Phase 2: Platform-Specific Settings**
   - Store language preference in DataStore
   - System language detection
   - Per-platform overrides

3. **Phase 3: Runtime Language Switching**
   - Add language selection in Settings
   - Hot reload strings
   - No app restart needed

4. **Phase 4: Future Migration (Optional)**
   - Migrate to Moko Resources if scale demands

---

### Text Coverage Analysis

**Estimated String Keys Needed:**

| Screen | Static Strings | Dynamic Strings | Total |
|--------|---|---|---|
| Onboarding | 25 | 2 | 27 |
| Dashboard | 15 | 5 | 20 |
| App Blocking | 5 | 2 | 7 |
| Analytics | 20 | 8 | 28 |
| Usage Tracking | 10 | 4 | 14 |
| Settings | 30 | 5 | 35 |
| Profile | 15 | 2 | 17 |
| Usage Goals | 20 | 1 | 21 |
| About | 25 | 1 | 26 |
| Focus Sessions | 10 | 1 | 11 |
| Common Components | 15 | 0 | 15 |
| **TOTAL** | **190** | **31** | **221** |

---

## 7. Implementation Priority & Roadmap

### High Priority (Critical User-Facing)
1. Onboarding flow strings
2. Settings screen
3. Dashboard welcome message
4. Permission-related messages
5. Error/empty state messages

### Medium Priority (Important Features)
1. Analytics descriptions
2. Focus session management
3. Usage tracking UI
4. Goal-setting labels
5. Profile management

### Low Priority (Nice-to-Have)
1. About screen content
2. Tips and guidance text
3. Link descriptions
4. Footer text

---

## 8. Key Files Requiring i18n Refactoring

### Critical Files:
1. `/home/user/Tawazn/feature/onboarding/src/commonMain/kotlin/id/compagnie/tawazn/feature/onboarding/OnboardingScreen.kt`
2. `/home/user/Tawazn/feature/settings/src/commonMain/kotlin/id/compagnie/tawazn/feature/settings/SettingsScreen.kt`
3. `/home/user/Tawazn/feature/dashboard/src/commonMain/kotlin/id/compagnie/tawazn/feature/dashboard/DashboardScreen.kt`
4. `/home/user/Tawazn/feature/analytics/src/commonMain/kotlin/id/compagnie/tawazn/feature/analytics/AnalyticsScreen.kt`

### Important Files:
1. App.kt (theme message)
2. AnalyticsScreen, ProfileScreen, UsageGoalsScreen
3. AppBlockingScreen, UsageTrackingScreen

### All Feature Screens:
- Every Text() call with hardcoded strings

---

## 9. Conclusion

The Tawazn codebase is **well-structured for i18n implementation** with:

✅ **Strengths:**
- Clean Kotlin Multiplatform architecture
- Centralized dependency injection (Koin)
- Modular feature structure
- No hardcoded strings in complex logic
- DataStore already available for preferences

⚠️ **Current Gaps:**
- No i18n library installed
- All 221+ strings hardcoded in UI
- No language selection mechanism
- No translation files

✨ **Next Steps:**
1. Create `core/i18n` module
2. Implement runtime JSON-based translation provider
3. Add language selection to Settings
4. Start with English + Arabic (app already shows Arabic text)
5. Migrate remaining strings progressively

