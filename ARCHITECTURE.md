# Tawazn - Architecture Documentation

## 📐 Architecture Overview

Tawazn follows **Multi-Module Clean Architecture** with clear separation of concerns:

```
┌─────────────────────────────────────────────────────┐
│                 Presentation Layer                   │
│         (Compose UI + ViewModels + Voyager)         │
└─────────────────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────┐
│                   Domain Layer                       │
│    (Use Cases + Models + Repository Interfaces)     │
│              [Pure Kotlin - No Dependencies]         │
└─────────────────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────┐
│                    Data Layer                        │
│    (Repository Implementations + Data Sources)       │
│         (SQLDelight + DataStore + Ktor)             │
└─────────────────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────┐
│                 Platform Layer                       │
│   (Android UsageStats + iOS ScreenTime + Desktop)   │
└─────────────────────────────────────────────────────┘
```

## 📁 Module Structure

### Core Modules

#### `:core:common`
- Shared utilities, extensions, and constants
- Platform-agnostic code
- **Tech**: Kotlin, kotlinx.coroutines, kotlinx.datetime, kotlinx.serialization

#### `:core:design-system`
- Liquid glass UI components (Glassmorphism)
- Theme configuration (colors, typography, shapes)
- Reusable composables
- **Components**: GlassCard, GradientButton, StatsCard, etc.
- **Tech**: Compose Multiplatform, Material 3

#### `:core:database`
- SQLDelight database configuration
- SQL schemas and queries
- Database drivers for each platform
- **Tables**: App, BlockedApp, AppUsage, BlockSession
- **Tech**: SQLDelight 2.1.0

#### `:core:datastore`
- Key-value storage for preferences
- Settings and configuration
- **Tech**: DataStore Preferences

#### `:core:network`
- Ktor HTTP client setup
- API clients (for future sync features)
- **Tech**: Ktor 3.3.2, kotlinx.serialization

### Domain Layer (`:domain`)

Pure Kotlin module with no external dependencies.

**Models**:
- `AppInfo` - Installed app information
- `BlockedApp` - Blocked app data
- `AppUsage` - Usage statistics
- `BlockSession` - Scheduled blocking sessions
- `UsageStats` - Aggregated usage data

**Repositories** (Interfaces):
- `AppRepository` - App management
- `BlockedAppRepository` - App blocking
- `UsageRepository` - Usage tracking
- `BlockSessionRepository` - Session management

**Use Cases**:
- `BlockAppUseCase`
- `UnblockAppUseCase`
- `GetActiveBlockedAppsUseCase`
- `GetUsageStatsUseCase`
- `SyncUsageUseCase`
- `CreateBlockSessionUseCase`
- `GetNonSystemAppsUseCase`

### Data Layer (`:data`)

**Repository Implementations**:
- `AppRepositoryImpl` - Uses SQLDelight + Platform monitors
- `BlockedAppRepositoryImpl` - Database + platform blocking
- `UsageRepositoryImpl` - Database + platform usage stats
- `BlockSessionRepositoryImpl` - Database operations

**Data Sources**:
- Local: SQLDelight database
- Remote: Ktor client (future)
- Platform: Android UsageStatsManager, iOS Screen Time API

### Platform Modules

#### `:platform:android`
- `AndroidAppMonitor` - UsageStatsManager implementation
- App usage tracking via UsageEvents API
- Package manager integration
- App blocking infrastructure (AccessibilityService ready)

**Required Permissions**:
- `PACKAGE_USAGE_STATS`
- `QUERY_ALL_PACKAGES`

#### `:platform:ios`
- `IOSAppMonitorImpl` - Screen Time API wrapper
- FamilyControls integration (requires entitlement)
- DeviceActivity monitoring
- ManagedSettings for app shielding

**Required Entitlements**:
- `com.apple.developer.family-controls`

**Required Frameworks**:
- FamilyControls.framework
- DeviceActivity.framework
- ManagedSettings.framework

#### `:platform:desktop`
- `DesktopAppMonitor` - Cross-platform desktop monitoring
- Windows: WMI, PowerShell
- macOS: system_profiler, Screen Time API
- Linux: Package managers, window managers

### Feature Modules

All feature modules follow the same structure:
- UI (Compose screens)
- ViewModels/ScreenModels
- Navigation (Voyager screens)

#### `:feature:dashboard`
- Main dashboard with stats overview
- Today's screen time
- Quick actions
- Weekly insights

#### `:feature:app-blocking`
- Block/unblock apps
- Scheduled blocking
- Block sessions management

#### `:feature:usage-tracking`
- Detailed usage statistics
- Charts and graphs
- App breakdown
- Time analysis

#### `:feature:analytics`
- Weekly/monthly reports
- Trends and insights
- Productivity metrics

#### `:feature:settings`
- App preferences
- Theme selection
- Notifications
- Data management

#### `:feature:onboarding`
- Welcome screens
- Permission requests
- Initial setup

### Main App (`:composeApp`)
- App entry point
- Koin DI initialization
- Navigation setup
- Platform-specific configuration

## 🔧 Dependency Injection (Koin)

### Module Organization

```kotlin
// Domain Module
val domainModule = module {
    factory { BlockAppUseCase(get()) }
    factory { GetUsageStatsUseCase(get()) }
    // ... other use cases
}

// Data Module
val dataModule = module {
    single { DatabaseDriverFactory(get()).createDriver() }
    single { TawaznDatabaseFactory.createDatabase(get()) }
    single<AppRepository> { AppRepositoryImpl(get()) }
    // ... other repositories
}

// Platform Module (Android example)
actual fun platformModule() = module {
    single { AndroidAppMonitor(androidContext()) }
}
```

### Initialization

**Android**:
```kotlin
class TawaznApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@TawaznApplication)
            modules(platformModule(), dataModule, domainModule)
        }
    }
}
```

**iOS** (in Swift):
```swift
func initKoin() {
    KoinKt.doInitKoin(modules: [
        platformModule(),
        dataModule,
        domainModule
    ])
}
```

**Desktop**:
```kotlin
fun main() = application {
    initKoin()
    // ...
}
```

## 🎨 Design System

### Liquid Glass UI

**Glassmorphism Effect**:
- Semi-transparent backgrounds
- Blur effects
- Subtle borders
- Gradient overlays

**Usage**:
```kotlin
GlassCard(
    cornerRadius = 16.dp,
    useGradient = true
) {
    // Content
}
```

### Theme

**Colors**:
- Primary: Indigo (#6366F1)
- Secondary: Purple (#8B5CF6)
- Accent: Pink (#EC4899)
- Success/Warning/Error/Info

**Typography**:
- Material 3 type scale
- Display, Headline, Title, Body, Label variants

**Shapes**:
- Rounded corners (4dp to 24dp)
- Smooth transitions

## 📊 Database Schema

### Tables

**App**:
- packageName (PK)
- appName
- iconPath
- category
- isSystemApp
- installDate, lastUpdated

**BlockedApp**:
- id (PK)
- packageName (FK)
- isBlocked
- blockedAt, blockedUntil
- blockDurationMinutes

**AppUsage**:
- id (PK)
- packageName (FK)
- usageDate
- totalTimeInForeground
- launchCount
- lastTimeUsed

**BlockSession**:
- id (PK)
- name, description
- isActive
- startTime, endTime
- repeatDaily, repeatWeekly, repeatDays

**BlockSessionApp** (Junction):
- sessionId (FK)
- packageName (FK)

## 🔄 Data Flow Example

### Blocking an App

1. **UI Layer**: User taps "Block App" button
2. **ViewModel**: Calls `blockAppUseCase(packageName, duration)`
3. **Use Case**: Validates input, calls repository
4. **Repository**:
   - Saves to database (SQLDelight)
   - Calls platform monitor to enforce block
5. **Platform Layer**:
   - Android: Updates AccessibilityService
   - iOS: Calls ManagedSettings to shield app
   - Desktop: Updates process monitor
6. **Result**: Flows back up through layers
7. **UI**: Updates to show blocked state

### Getting Usage Stats

1. **UI Layer**: Dashboard screen loads
2. **ViewModel**: Collects flow from `getUsageStatsUseCase(startDate, endDate)`
3. **Use Case**: Calls repository
4. **Repository**:
   - Queries database for cached data
   - If outdated, syncs from platform
5. **Platform Layer**:
   - Android: UsageStatsManager.queryEvents()
   - iOS: DeviceActivity data
   - Desktop: Process monitoring logs
6. **Repository**: Saves fresh data to database
7. **Use Case**: Aggregates and formats data
8. **ViewModel**: Transforms to UI state
9. **UI**: Displays stats with charts

## 🧪 Testing Strategy

### Unit Tests
- Domain layer (use cases, models)
- Pure business logic
- No dependencies

### Integration Tests
- Repository implementations
- Database operations
- Platform monitor integration

### UI Tests
- Compose UI tests
- Navigation flows
- User interactions

## 🚀 Build & Performance

### Build Optimization
- Modularization for parallel builds
- Incremental compilation
- Gradle configuration cache
- Build cache enabled

### Runtime Performance
- Flow-based reactive programming
- Efficient database queries with indexes
- Coroutines for async operations
- Lazy loading and pagination

### Offline-First Strategy
- Local database as source of truth
- Background sync when online
- Conflict resolution
- Cache management

## 📱 Platform-Specific Notes

### Android
- Minimum SDK: 24 (Android 7.0)
- Target SDK: 36
- Requires special permissions (usage stats)
- AccessibilityService for blocking (not yet implemented)

### iOS
- Minimum iOS: 15.0
- Requires Family Controls entitlement from Apple
- Privacy-preserving usage data
- User authorization required

### Desktop
- JVM 11+
- Limited monitoring capabilities
- Platform-specific implementations needed
- Works on Windows, macOS, Linux

## 🔐 Security & Privacy

- Local-first: All data stored on device
- No cloud sync (yet)
- User consent for permissions
- Encrypted database (optional)
- Privacy-preserving iOS implementation

## 📈 Future Enhancements

1. **Cloud Sync**: Multi-device synchronization
2. **ML Insights**: Smart usage predictions
3. **Focus Modes**: Customizable blocking profiles
4. **Gamification**: Achievements and streaks
5. **Social Features**: Compare with friends
6. **Advanced Analytics**: AI-powered insights
7. **Widget Support**: Home screen widgets
8. **Wear OS/watchOS**: Companion apps
