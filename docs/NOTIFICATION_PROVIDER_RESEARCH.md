# Notification Provider Research for Tawazn

**Date:** 2025-11-23
**Platforms:** Android, iOS, Desktop (Windows, macOS, Linux)
**Framework:** Compose Multiplatform 1.9.0, Kotlin 2.2.20

## Executive Summary

For a Compose Multiplatform app targeting Android, iOS, and Desktop, **KMPNotifier** is the recommended notification solution. It provides comprehensive support for both local and push notifications across all target platforms.

---

## Recommended Solution: KMPNotifier

### Overview

**KMPNotifier** is a Kotlin Multiplatform Push Notification library that supports:
- **Local Notifications**: Android, iOS, Desktop, Web (JS/Wasm)
- **Push Notifications**: Android and iOS (via Firebase Cloud Messaging)

**Repository:** [mirzemehdi/KMPNotifier](https://github.com/mirzemehdi/KMPNotifier)
**Status:** Actively maintained, used in production (FindTravelNow KMP project)
**License:** Apache 2.0

### Key Features

✅ **Cross-Platform Support**
- Android (minSdk 21+)
- iOS (14.1+)
- Desktop (Windows, macOS, Linux)
- Web (JS and Wasm)

✅ **Local Notifications**
- Schedule notifications on all platforms
- Custom notification icons
- Notification actions and callbacks
- Deep linking support on Android

✅ **Push Notifications**
- Firebase Cloud Messaging integration
- Token management
- Payload handling
- Custom data support

✅ **Modern API**
- Clean and intuitive API
- Kotlin Coroutines support
- Type-safe configuration
- Platform-specific customization

### Installation

#### 1. Add Repository

In `settings.gradle.kts`:
```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}
```

#### 2. Add Dependency

In `gradle/libs.versions.toml`:
```toml
[versions]
kmpnotifier = "1.6.0"  # Latest stable version

[libraries]
kmpnotifier = { module = "io.github.mirzemehdi:kmpnotifier", version.ref = "kmpnotifier" }
```

In module `build.gradle.kts`:
```kotlin
kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kmpnotifier)
            }
        }
    }

    // iOS export
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            export(libs.kmpnotifier)  // Export to Swift
        }
    }
}
```

#### 3. Android Setup

Apply Google Services plugin in `composeApp/build.gradle.kts`:
```kotlin
plugins {
    id("com.google.gms.google-services")
}
```

Add `google-services.json` to `composeApp/` directory.

#### 4. iOS Setup

Add `GoogleService-Info.plist` to your iOS app target.

### Usage Examples

#### Initialize (Application Entry Point)

**Android (MainActivity.kt):**
```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        NotifierManager.initialize(
            configuration = NotificationPlatformConfiguration.Android(
                notificationIconResId = R.drawable.ic_notification,
            )
        )

        setContent {
            App()
        }
    }
}
```

**iOS (MainViewController.kt):**
```kotlin
fun MainViewController() = ComposeUIViewController {
    NotifierManager.initialize(
        configuration = NotificationPlatformConfiguration.Ios(
            showPushNotification = true,
            askNotificationPermissionOnStart = true
        )
    )
    App()
}
```

**Desktop (main.kt):**
```kotlin
fun main() = application {
    NotifierManager.initialize(
        NotificationPlatformConfiguration.Desktop(
            showPushNotification = true,
            notificationIconPath = "resources/ic_notification.png"
        )
    )

    Window(
        onCloseRequest = ::exitApplication,
        title = "Tawazn",
    ) {
        App()
    }
}
```

#### Send Local Notification

```kotlin
// In ViewModel or ScreenModel
class DashboardScreenModel : ScreenModel {
    private val notifier = NotifierManager.getLocalNotifier()

    fun sendUsageLimitNotification(appName: String, usageMinutes: Int) {
        notifier.notify {
            id = Random.nextInt(0, Int.MAX_VALUE)
            title = "Usage Limit Reached"
            body = "You've used $appName for $usageMinutes minutes today"
        }
    }

    fun scheduleBreakReminder() {
        notifier.notify {
            id = BREAK_REMINDER_ID
            title = "Take a Break 🌟"
            body = "Time to rest your eyes and stretch"
        }
    }
}
```

#### Push Notifications (Android/iOS only)

```kotlin
class NotificationService {
    private val pushNotifier = NotifierManager.getPushNotifier()

    init {
        pushNotifier?.onNewToken = { token ->
            println("FCM Token: $token")
            // Send to your backend
        }

        pushNotifier?.onPushNotification = { title, body ->
            println("Push notification: $title - $body")
        }

        pushNotifier?.onPayloadData = { data ->
            println("Payload data: $data")
            // Handle custom data
        }
    }
}
```

### Use Cases for Tawazn

1. **Usage Limit Alerts**
   - Notify users when app usage exceeds daily limits
   - Remind users of focus session start/end times

2. **Break Reminders**
   - Periodic notifications to take breaks
   - Eye strain prevention reminders

3. **Daily Reports**
   - End-of-day usage summaries
   - Weekly usage insights

4. **App Blocking Notifications**
   - Alert when attempting to open blocked apps
   - Session completion notifications

5. **Goal Achievements**
   - Celebrate screen time reduction milestones
   - Weekly goal completion alerts

### Platform-Specific Considerations

#### Android
- Requires `POST_NOTIFICATIONS` permission (Android 13+)
- Notification channels for categorization
- Custom notification sounds supported
- Deep linking available

#### iOS
- Requires user permission on first notification
- Limited customization compared to Android
- UNUserNotificationCenter integration
- App Group support for shared data

#### Desktop
- System tray notifications
- Custom notification icons
- Click actions supported
- No push notification support (local only)

---

## Alternative Solutions

### 1. Alarmee

**Repository:** [Tweener/alarmee](https://github.com/Tweener/alarmee)

**Pros:**
- Alarm scheduling + notifications
- Firebase integration for push
- Compose Multiplatform support

**Cons:**
- More focused on alarms than notifications
- Less documentation than KMPNotifier
- No desktop support mentioned

### 2. Manual expect/actual Implementation

**Approach:** Create platform-specific implementations

**Pros:**
- Full control over native APIs
- No third-party dependencies
- Custom features possible

**Cons:**
- Significant development effort
- Maintenance burden
- Requires expertise in all platforms

### 3. OneSignal

**Status:** ❌ No official KMP support

**Note:** OneSignal provides separate Android and iOS SDKs but no unified KMP solution. Would require expect/actual wrapper.

---

## Integration Plan for Tawazn

### Phase 1: Setup (Week 1)
1. Add KMPNotifier dependency
2. Configure Firebase for Android/iOS
3. Initialize in each platform entry point
4. Create notification module in `core/`

### Phase 2: Local Notifications (Week 2)
1. Implement usage limit notifications
2. Add break reminder system
3. Create notification preference settings
4. Test on all platforms

### Phase 3: Push Notifications (Week 3)
1. Set up Firebase Cloud Messaging
2. Implement token registration
3. Create backend endpoint for sending notifications
4. Test push on Android and iOS

### Phase 4: Advanced Features (Week 4)
1. Deep linking from notifications
2. Custom notification sounds
3. Notification actions (snooze, dismiss)
4. Analytics tracking

---

## Technical Architecture

### Module Structure

```
core/
  notification/
    src/
      commonMain/kotlin/
        - NotificationManager.kt        # Wrapper around KMPNotifier
        - NotificationPreferences.kt    # User preferences
        - NotificationScheduler.kt      # Schedule logic
      androidMain/kotlin/
        - AndroidNotificationConfig.kt
      iosMain/kotlin/
        - IosNotificationConfig.kt
      jvmMain/kotlin/
        - DesktopNotificationConfig.kt
```

### Dependency Injection (Koin)

```kotlin
// core/notification/di/NotificationModule.kt
val notificationModule = module {
    single { NotificationManager(get()) }
    single { NotificationScheduler(get(), get()) }
    single { NotificationPreferences(get()) }
}
```

---

## Testing Strategy

### Local Notifications
- ✅ Android: Test on API 24, 33+ (notification permissions)
- ✅ iOS: Test on iOS 14.1+ simulators
- ✅ Desktop: Test on Windows, macOS, Linux

### Push Notifications
- ✅ Android: Test FCM token registration and delivery
- ✅ iOS: Test APNs integration
- ❌ Desktop: Not supported (local only)

---

## Resources

### Official Documentation
- [KMPNotifier GitHub](https://github.com/mirzemehdi/KMPNotifier)
- [KMPNotifier Update Article](https://www.droidcon.com/2024/08/19/kmpnotifier-update-web-desktop-and-new-features-for-kotlin-multiplatform-notifications/)
- [How to Implement Push Notifications in KMP](https://proandroiddev.com/how-to-implement-push-notification-in-kotlin-multiplatform-5006ff20f76c)

### Alternative Resources
- [Native Notifications in Compose & KMP](https://khubaibkhan-4.medium.com/native-notifications-in-compose-kotlin-multiplatform-a-comprehensive-how-to-f1b01386a3b2)
- [Alarmee Library](https://github.com/Tweener/alarmee)
- [Firebase Cloud Messaging](https://firebase.google.com/docs/cloud-messaging)

---

## Conclusion

**KMPNotifier** is the optimal choice for Tawazn because:

1. ✅ **Complete Platform Coverage** - Works on Android, iOS, and Desktop
2. ✅ **Production-Ready** - Used in real-world apps
3. ✅ **Active Maintenance** - Regular updates and bug fixes
4. ✅ **Clean API** - Easy to integrate and use
5. ✅ **Firebase Integration** - Industry-standard push notifications
6. ✅ **Local Notification Support** - Essential for screen time reminders

The library aligns perfectly with Tawazn's requirements for cross-platform notification delivery while maintaining code sharing and reducing platform-specific complexity.

---

## Next Steps

1. **Review this research** with the team
2. **Add KMPNotifier** to project dependencies
3. **Set up Firebase** for Android and iOS apps
4. **Create notification module** in `core/`
5. **Implement first notification** (usage limit alert)
6. **Test across all platforms**

---

**Research compiled by:** Claude Code
**Last updated:** 2025-11-23
