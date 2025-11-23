# Notification Module

Cross-platform notification system for Tawazn using KMPNotifier.

## Features

- ✅ **Local Notifications** - All platforms (Android, iOS, Desktop)
- ✅ **Push Notifications** - Android & iOS only (via Firebase)
- ✅ **Platform-specific Configuration** - Optimized for each platform
- ✅ **Koin Integration** - Dependency injection ready
- ✅ **Type-safe API** - Kotlin-first design

## Setup

### 1. Add Dependency

The notification module is automatically included in `composeApp`. No additional setup needed.

### 2. Initialize (Already Done)

Notification system is initialized in app entry points:
- Android: `MainActivity.kt`
- iOS: `MainViewController.kt`
- Desktop: `main.kt`

### 3. Firebase Setup (Optional - for Push Notifications)

See [FIREBASE_SETUP.md](./FIREBASE_SETUP.md) for detailed Firebase configuration instructions.

## Usage Examples

### Basic Local Notification

```kotlin
import id.compagnie.tawazn.core.notification.NotificationManager
import org.koin.compose.koinInject

@Composable
fun MyScreen() {
    val notificationManager: NotificationManager = koinInject()

    Button(onClick = {
        notificationManager.sendLocalNotification(
            title = "Hello!",
            body = "This is a test notification"
        )
    }) {
        Text("Send Notification")
    }
}
```

### Usage Limit Notification

```kotlin
@Composable
fun UsageTrackingScreen() {
    val notificationManager: NotificationManager = koinInject()

    LaunchedEffect(appUsage) {
        if (appUsage.minutes >= dailyLimit) {
            notificationManager.sendUsageLimitNotification(
                appName = "Instagram",
                usageMinutes = appUsage.minutes,
                limitMinutes = dailyLimit
            )
        }
    }
}
```

### Break Reminder

```kotlin
class BreakReminderScheduler(
    private val notificationManager: NotificationManager
) {
    fun scheduleBreakReminder() {
        // Schedule every 30 minutes
        notificationManager.sendBreakReminder(
            message = "You've been using your device for 30 minutes. Time for a break!"
        )
    }
}
```

### Focus Session Notifications

```kotlin
class FocusSessionManager(
    private val notificationManager: NotificationManager
) {
    fun startSession(sessionName: String, durationMinutes: Int) {
        notificationManager.sendBlockingSessionStarted(
            sessionName = sessionName,
            durationMinutes = durationMinutes
        )
    }

    fun endSession(sessionName: String) {
        notificationManager.sendBlockingSessionEnded(
            sessionName = sessionName
        )
    }
}
```

### Daily Summary

```kotlin
class DailySummaryService(
    private val notificationManager: NotificationManager,
    private val usageRepository: UsageRepository
) {
    suspend fun sendDailySummary() {
        val todayUsage = usageRepository.getTodayUsage()
        val totalMinutes = todayUsage.sumOf { it.usageMinutes }
        val topApp = todayUsage.maxByOrNull { it.usageMinutes }

        if (topApp != null) {
            notificationManager.sendDailySummary(
                totalMinutes = totalMinutes,
                topApp = topApp.appName,
                topAppMinutes = topApp.usageMinutes
            )
        }
    }
}
```

### In ViewModel/ScreenModel

```kotlin
class DashboardScreenModel(
    private val notificationManager: NotificationManager,
    private val usageRepository: UsageRepository
) : ScreenModel {

    fun checkUsageLimits() {
        viewModelScope.launch {
            val usage = usageRepository.getTodayUsage()

            usage.forEach { appUsage ->
                if (appUsage.usageMinutes >= appUsage.dailyLimit) {
                    notificationManager.sendUsageLimitNotification(
                        appName = appUsage.appName,
                        usageMinutes = appUsage.usageMinutes,
                        limitMinutes = appUsage.dailyLimit
                    )
                }
            }
        }
    }

    fun celebrateGoal() {
        notificationManager.sendGoalAchievement(
            "You reduced screen time by 2 hours this week! 🎉"
        )
    }
}
```

## API Reference

### NotificationManager

#### Initialization (Static)

```kotlin
NotificationManager.initialize(configuration: NotificationPlatformConfiguration)
```

#### Instance Methods

**Local Notifications:**

```kotlin
sendLocalNotification(title: String, body: String, notificationId: Int = Random.nextInt())

sendUsageLimitNotification(appName: String, usageMinutes: Int, limitMinutes: Int)

sendBreakReminder(message: String = "Time to rest your eyes and stretch")

sendBlockingSessionStarted(sessionName: String, durationMinutes: Int)

sendBlockingSessionEnded(sessionName: String)

sendDailySummary(totalMinutes: Int, topApp: String, topAppMinutes: Int)

sendGoalAchievement(achievementDescription: String)

sendBlockedAppAttempt(appName: String, reason: String = "This app is currently blocked")
```

**Push Notifications (Android/iOS only):**

```kotlin
val fcmToken: StateFlow<String?> // Observable FCM token
```

## Platform Configuration

### Android

```kotlin
AndroidNotificationConfig.create(
    context: Context,
    notificationIconResId: Int? = null
)
```

### iOS

```kotlin
IosNotificationConfig.create(
    showPushNotification: Boolean = true,
    askNotificationPermissionOnStart: Boolean = true
)
```

### Desktop

```kotlin
DesktopNotificationConfig.create(
    showPushNotification: Boolean = false, // Only local supported
    notificationIconPath: String? = null
)
```

## Notification Permissions

### Android (13+)

Add to `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.POST_NOTIFICATIONS"/>
```

Request permission at runtime:

```kotlin
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    ActivityCompat.requestPermissions(
        activity,
        arrayOf(Manifest.permission.POST_NOTIFICATIONS),
        REQUEST_CODE
    )
}
```

### iOS

Permissions requested automatically on first notification (if `askNotificationPermissionOnStart = true`).

### Desktop

No permission needed for local notifications.

## Testing

### Test Local Notification

```kotlin
@Test
fun testNotification() = runTest {
    val notificationManager = NotificationManager()

    notificationManager.sendBreakReminder()

    // Verify notification was sent
}
```

### Test in App

1. Run the app
2. Navigate to Settings or any screen
3. Trigger a notification action
4. Check system notification tray

## Troubleshooting

### Android

**Issue:** Notifications not showing
- Check `POST_NOTIFICATIONS` permission (Android 13+)
- Verify app has notification permission in system settings
- Check notification channels are enabled

**Issue:** Custom icon not showing
- Add `ic_notification` to drawable resources
- Pass `notificationIconResId` to config

### iOS

**Issue:** Permission not requested
- Set `askNotificationPermissionOnStart = true`
- Check Info.plist has notification usage description

**Issue:** Notifications not showing
- Verify notification permissions in iOS Settings
- Check Firebase configuration (for push)

### Desktop

**Issue:** Notifications not appearing
- Check OS notification settings
- Verify notification icon path is correct

## Dependencies

- **KMPNotifier** `1.2.0` - Core notification library
- **Koin** - Dependency injection
- **Kermit** - Logging

## Resources

- [KMPNotifier Documentation](https://github.com/mirzemehdi/KMPNotifier)
- [Firebase Setup Guide](./FIREBASE_SETUP.md)
- [Notification Research](../../docs/NOTIFICATION_PROVIDER_RESEARCH.md)

---

**Module:** `core:notification`
**Package:** `id.compagnie.tawazn.core.notification`
**Last Updated:** 2025-11-23
