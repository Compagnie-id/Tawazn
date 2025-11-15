# Tawazn - Screen Time Management App

<div align="center">

🚀 **Cross-Platform Screen Time Manager** | Built with Compose Multiplatform & Clean Architecture

[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.20-blue.svg)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/Compose-1.9.0-green.svg)](https://www.jetbrains.com/lp/compose-multiplatform)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

</div>

---

## 📱 About Tawazn

**Tawazn** (Arabic: توازن, meaning "Balance") is a sophisticated screen time management application that helps users achieve digital wellness by tracking app usage, blocking distracting apps, and providing insightful analytics across iOS, Android, and Desktop platforms.

### ✨ Key Features

- 📊 **Real-time Usage Tracking** - Monitor app usage with accurate statistics
- 🚫 **Smart App Blocking** - Block distracting apps on demand or schedule
- 📅 **Scheduled Sessions** - Create custom blocking schedules
- 📈 **Insights & Analytics** - Detailed reports with charts and trends
- 🎨 **Liquid Glass UI** - Beautiful glassmorphism design
- 🌙 **Dark Mode Support** - Stunning light and dark themes
- 💾 **Offline-First** - All data stored locally for privacy
- 🔄 **Multi-Platform** - Works on Android, iOS, and Desktop

---

## 🏗️ Architecture

### Multi-Module Clean Architecture

```
Tawazn/
├── composeApp/              # Main application
├── core/
│   ├── common/              # Shared utilities
│   ├── design-system/       # Liquid glass UI components
│   ├── database/            # SQLDelight setup
│   ├── datastore/           # Preferences storage
│   └── network/             # Ktor client
├── domain/                  # Business logic (Pure Kotlin)
├── data/                    # Repository implementations
├── feature/
│   ├── dashboard/           # Main dashboard
│   ├── app-blocking/        # App blocking feature
│   ├── usage-tracking/      # Usage statistics
│   ├── analytics/           # Insights & reports
│   ├── settings/            # App settings
│   └── onboarding/          # User onboarding
└── platform/
    ├── android/             # Android-specific (UsageStatsManager)
    ├── ios/                 # iOS-specific (Screen Time API)
    └── desktop/             # Desktop-specific monitoring
```

For detailed architecture documentation, see [ARCHITECTURE.md](ARCHITECTURE.md).

---

## 🛠️ Tech Stack

### Latest Dependencies (2025)

| Technology | Version | Purpose |
|-----------|---------|---------|
| **Kotlin** | 2.2.20 | Programming language |
| **Compose Multiplatform** | 1.9.0 | UI framework |
| **SQLDelight** | 2.1.0 | Type-safe database |
| **Koin** | 4.1.0 | Dependency injection |
| **Ktor** | 3.3.2 | HTTP client |
| **Voyager** | 1.1.0 | Navigation |
| **Coroutines** | 1.10.2 | Async programming |
| **kotlinx.datetime** | 0.7.1 | Date/time handling |
| **kotlinx.serialization** | 1.7.3 | JSON serialization |
| **DataStore** | 1.1.1 | Preferences storage |
| **Kermit** | 2.0.4 | Logging |

### Design System

- **Material 3** - Modern design guidelines
- **Glassmorphism** - Liquid glass UI effects
- **Custom Theme** - Indigo/Purple/Pink gradient palette
- **Responsive Layout** - Adapts to all screen sizes

---

## 📋 Platform-Specific Requirements

### Android (API 24+)

**Required Permissions**:
```xml
<uses-permission android:name="android.permission.PACKAGE_USAGE_STATS" />
<uses-permission android:name="android.permission.QUERY_ALL_PACKAGES" />
```

**Key APIs**:
- `UsageStatsManager` - Track app usage
- `PackageManager` - Get installed apps
- `UsageEvents` - Accurate foreground/background tracking

**Optional** (for app blocking):
- `AccessibilityService` - Detect app launches
- `SYSTEM_ALERT_WINDOW` - Draw blocking overlays
- `DeviceAdmin` API - Enhanced enforcement

**Documentation**: Android's `UsageStatsManager` provides detailed app usage data through the `UsageEvents` API, which tracks foreground and background app activities.

### iOS (15.0+)

**Required Entitlements**:
```xml
<key>com.apple.developer.family-controls</key>
<true/>
```

**Required Frameworks**:
- `FamilyControls.framework` - Authorization
- `DeviceActivity.framework` - Usage monitoring
- `ManagedSettings.framework` - App shielding
- `ManagedSettingsUI.framework` - Custom UI

**Info.plist**:
```xml
<key>NSFamilyControlsUsageDescription</key>
<string>We need this permission to help you manage screen time</string>
```

**Important Notes**:
1. Requires special entitlement from Apple (apply at [Family Controls Distribution](https://developer.apple.com/contact/request/family-controls-distribution/))
2. Approval process can take several weeks
3. Privacy-preserving API with user consent
4. Requires Swift interop for native APIs

**Documentation**:
- [WWDC21: Meet the Screen Time API](https://developer.apple.com/videos/play/wwdc2021/10123/)
- [WWDC22: What's new in Screen Time API](https://developer.apple.com/videos/play/wwdc2022/110336/)
- [Screen Time API Documentation](https://developer.apple.com/documentation/familycontrols)

### Desktop (JVM 11+)

**Supported Platforms**:
- Windows 10/11
- macOS 10.15+
- Linux (Ubuntu, Fedora, etc.)

**Platform-Specific Methods**:

**Windows**:
- WMI (Windows Management Instrumentation)
- PowerShell commands
- Registry queries
- Event Viewer for usage tracking

**macOS**:
- `system_profiler` for app information
- Screen Time API (requires permission)
- Process monitoring with `ps`
- /Applications directory

**Linux**:
- Package managers (dpkg, rpm, flatpak, snap)
- .desktop files in /usr/share/applications
- Process monitoring
- Window manager integration

---

## 🚀 Getting Started

### Prerequisites

- JDK 11 or higher
- Android Studio (for Android development)
- Xcode 15+ (for iOS development)
- Kotlin 2.2.20

### Clone & Build

```bash
# Clone the repository
git clone <repository-url>
cd Tawazn

# Build the project
./gradlew build

# Run tests
./gradlew test
```

### Running on Different Platforms

#### Android

```bash
# Debug build
./gradlew :composeApp:assembleDebug

# Install on connected device
./gradlew :composeApp:installDebug

# Run directly
./gradlew :composeApp:run
```

Or use Android Studio's run configuration.

**Granting Usage Stats Permission**:
1. Install the app
2. Open the app
3. Navigate to Settings → Usage Stats Permission
4. Grant permission in system settings

#### iOS

```bash
# Open in Xcode
open iosApp/iosApp.xcodeproj

# Build from command line (requires Xcode)
xcodebuild -project iosApp/iosApp.xcodeproj -scheme iosApp -configuration Debug
```

**Setup Steps**:
1. Apply for Family Controls entitlement from Apple
2. Add entitlement to your project
3. Configure Info.plist with usage description
4. Build and run from Xcode

#### Desktop

```bash
# Run JVM application
./gradlew :composeApp:run

# Create distributable package
./gradlew :composeApp:packageDistributionForCurrentOS

# Packages will be in:
# - composeApp/build/compose/binaries/main/dmg (macOS)
# - composeApp/build/compose/binaries/main/msi (Windows)
# - composeApp/build/compose/binaries/main/deb (Linux)
```

---

## 📁 Project Structure

```
/
├── composeApp/                         # Main app module
│   ├── src/commonMain/                 # Shared app code
│   ├── src/androidMain/                # Android app code
│   ├── src/iosMain/                    # iOS app code
│   └── src/jvmMain/                    # Desktop app code
│
├── core/                               # Core modules
│   ├── common/                         # Utilities, extensions
│   ├── design-system/                  # Liquid glass UI
│   ├── database/                       # SQLDelight
│   ├── datastore/                      # Preferences
│   └── network/                        # Ktor client
│
├── domain/                             # Business logic
│   ├── model/                          # Domain models
│   ├── repository/                     # Repository interfaces
│   └── usecase/                        # Use cases
│
├── data/                               # Data layer
│   ├── repository/                     # Repository implementations
│   └── di/                             # Koin modules
│
├── feature/                            # Feature modules
│   ├── dashboard/                      # Main dashboard
│   ├── app-blocking/                   # App blocking
│   ├── usage-tracking/                 # Usage stats
│   ├── analytics/                      # Analytics
│   ├── settings/                       # Settings
│   └── onboarding/                     # Onboarding
│
├── platform/                           # Platform-specific
│   ├── android/                        # Android APIs
│   ├── ios/                            # iOS APIs
│   └── desktop/                        # Desktop APIs
│
├── iosApp/                             # iOS native wrapper
├── gradle/                             # Gradle configuration
│   └── libs.versions.toml              # Version catalog
│
├── ARCHITECTURE.md                     # Architecture docs
└── README.md                           # This file
```

---

## 🎨 Design System

### Liquid Glass UI

The app features a modern **glassmorphism** design with:

- Semi-transparent backgrounds with blur effects
- Subtle borders and gradients
- Smooth animations and transitions
- Beautiful color palette

### Theme Colors

```kotlin
Primary: #6366F1 (Indigo)
Secondary: #8B5CF6 (Purple)
Accent: #EC4899 (Pink)
Success: #10B981
Warning: #F59E0B
Error: #EF4444
```

### UI Components

- **GlassCard** - Card with glassmorphism effect
- **GradientButton** - Button with gradient background
- **StatsCard** - Statistics display card
- **OutlinedGlassButton** - Outlined button variant

---

## 💾 Database Schema

### Tables

1. **App** - Installed applications
2. **BlockedApp** - Blocked applications with duration
3. **AppUsage** - Daily usage statistics
4. **BlockSession** - Scheduled blocking sessions
5. **BlockSessionApp** - Many-to-many junction table

See [ARCHITECTURE.md](ARCHITECTURE.md) for detailed schema.

---

## 🧪 Testing

```bash
# Run all tests
./gradlew test

# Android instrumented tests
./gradlew :composeApp:connectedAndroidTest

# Desktop tests
./gradlew :composeApp:jvmTest
```

---

## 📚 Documentation

- [ARCHITECTURE.md](ARCHITECTURE.md) - Detailed architecture guide
- [Kotlin Multiplatform Docs](https://www.jetbrains.com/help/kotlin-multiplatform-dev/)
- [Compose Multiplatform Docs](https://www.jetbrains.com/lp/compose-multiplatform/)
- [SQLDelight Documentation](https://cashapp.github.io/sqldelight/)
- [Koin Documentation](https://insert-koin.io/)

---

## 🛣️ Roadmap

### Phase 1: Foundation ✅
- [x] Multi-module architecture setup
- [x] Database schema with SQLDelight
- [x] Domain layer with use cases
- [x] Liquid glass design system
- [x] Platform-specific implementations
- [x] Basic dashboard UI

### Phase 2: Core Features (In Progress)
- [ ] Complete repository implementations
- [ ] App usage tracking integration
- [ ] App blocking functionality
- [ ] Scheduled sessions
- [ ] Settings screen
- [ ] Onboarding flow

### Phase 3: Analytics
- [ ] Usage charts and graphs
- [ ] Weekly/monthly reports
- [ ] Trends and insights
- [ ] Export functionality

### Phase 4: Enhancement
- [ ] Focus modes
- [ ] Widgets (iOS, Android)
- [ ] Notifications
- [ ] Accessibility improvements

### Phase 5: Advanced
- [ ] Cloud sync
- [ ] Multi-device support
- [ ] ML-powered insights
- [ ] Social features

---

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👨‍💻 Author

Built with ❤️ using Compose Multiplatform and Kotlin

---

## 🙏 Acknowledgments

- [Opal](https://www.opal.so/) for inspiration
- [JetBrains](https://www.jetbrains.com/) for Kotlin and Compose Multiplatform
- [Cash App](https://cashapp.github.io/sqldelight/) for SQLDelight
- [Insert Koin](https://insert-koin.io/) for dependency injection
- The Kotlin Multiplatform community

---

<div align="center">

**[Documentation](ARCHITECTURE.md)** • **[Issues](../../issues)** • **[Discussions](../../discussions)**

Made with Compose Multiplatform 🚀

</div>
