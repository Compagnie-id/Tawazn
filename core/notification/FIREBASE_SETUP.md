# Firebase Setup for Push Notifications

This guide explains how to set up Firebase Cloud Messaging for push notifications in Tawazn.

## Overview

Push notifications are supported on **Android** and **iOS** only. Desktop uses local notifications only.

## Prerequisites

1. Firebase account ([firebase.google.com](https://firebase.google.com))
2. Android and iOS apps registered in Firebase Console

## Setup Steps

### 1. Create Firebase Project

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Click "Add project" or select existing project
3. Follow the setup wizard

### 2. Add Android App

1. In Firebase Console, click "Add app" → Android
2. Enter package name: `id.compagnie.tawazn`
3. Download `google-services.json`
4. Place it in: `composeApp/google-services.json`

**Required:** Add Google Services plugin to `composeApp/build.gradle.kts`:

```kotlin
plugins {
    // ... other plugins
    id("com.google.gms.google-services") version "4.4.2"
}
```

And in root `build.gradle.kts`:

```kotlin
buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.2")
    }
}
```

### 3. Add iOS App

1. In Firebase Console, click "Add app" → iOS
2. Enter bundle ID: `id.compagnie.tawazn`
3. Download `GoogleService-Info.plist`
4. Add to Xcode project:
   - Open `iosApp/iosApp.xcodeproj` in Xcode
   - Drag `GoogleService-Info.plist` into the project
   - Ensure "Copy items if needed" is checked
   - Select the iosApp target

### 4. Enable Cloud Messaging

1. In Firebase Console, go to "Project Settings"
2. Navigate to "Cloud Messaging" tab
3. Under "Cloud Messaging API (Legacy)", enable it if not already enabled
4. Note your Server Key (for backend integration)

## Configuration Files

### Android: `google-services.json`

```json
{
  "project_info": {
    "project_number": "YOUR_PROJECT_NUMBER",
    "project_id": "YOUR_PROJECT_ID",
    "storage_bucket": "YOUR_STORAGE_BUCKET"
  },
  "client": [
    {
      "client_info": {
        "mobilesdk_app_id": "YOUR_APP_ID",
        "android_client_info": {
          "package_name": "id.compagnie.tawazn"
        }
      }
    }
  ]
}
```

**Location:** `composeApp/google-services.json`

### iOS: `GoogleService-Info.plist`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
<dict>
    <key>CLIENT_ID</key>
    <string>YOUR_CLIENT_ID</string>
    <key>REVERSED_CLIENT_ID</key>
    <string>YOUR_REVERSED_CLIENT_ID</string>
    <key>API_KEY</key>
    <string>YOUR_API_KEY</string>
    <key>GCM_SENDER_ID</key>
    <string>YOUR_SENDER_ID</string>
    <key>PLIST_VERSION</key>
    <string>1</string>
    <key>BUNDLE_ID</key>
    <string>id.compagnie.tawazn</string>
    <key>PROJECT_ID</key>
    <string>YOUR_PROJECT_ID</string>
    <key>STORAGE_BUCKET</key>
    <string>YOUR_STORAGE_BUCKET</string>
    <key>IS_ADS_ENABLED</key>
    <false/>
    <key>IS_ANALYTICS_ENABLED</key>
    <false/>
    <key>IS_APPINVITE_ENABLED</key>
    <true/>
    <key>IS_GCM_ENABLED</key>
    <true/>
    <key>IS_SIGNIN_ENABLED</key>
    <true/>
    <key>GOOGLE_APP_ID</key>
    <string>YOUR_GOOGLE_APP_ID</string>
</dict>
</plist>
```

**Location:** Add to Xcode project via Xcode IDE

## iOS Additional Setup

### APNs Authentication Key

1. Go to [Apple Developer Portal](https://developer.apple.com/account/resources/authkeys/list)
2. Create a new key with "Apple Push Notifications service (APNs)" enabled
3. Download the `.p8` file
4. In Firebase Console:
   - Go to Project Settings → Cloud Messaging
   - Under "APNs Authentication Key", upload the `.p8` file
   - Enter Key ID and Team ID

## Notification Icon (Android)

Create notification icon resources:

1. Create `ic_notification.xml` in `composeApp/src/androidMain/res/drawable/`
2. Or use PNG: `composeApp/src/androidMain/res/drawable-*/ic_notification.png`

**Recommended:** Use white icon on transparent background (Android guidelines)

## Notification Icon (Desktop)

Place notification icon:

```
composeApp/src/jvmMain/resources/ic_notification.png
```

## Testing

### Test Local Notifications

```kotlin
val notificationManager = get<NotificationManager>()
notificationManager.sendBreakReminder()
```

### Test Push Notifications

1. Get FCM token from app logs
2. Use Firebase Console "Cloud Messaging" → "Send test message"
3. Paste the token and send

### Send via cURL

```bash
curl -X POST https://fcm.googleapis.com/fcm/send \
  -H "Authorization: key=YOUR_SERVER_KEY" \
  -H "Content-Type: application/json" \
  -d '{
    "to": "DEVICE_FCM_TOKEN",
    "notification": {
      "title": "Test Notification",
      "body": "This is a test from cURL"
    }
  }'
```

## Troubleshooting

### Android

- **Issue:** "google-services.json not found"
  - **Solution:** Ensure file is in `composeApp/` directory

- **Issue:** "Default FirebaseApp is not initialized"
  - **Solution:** Verify google-services plugin is applied

### iOS

- **Issue:** "Firebase is not configured"
  - **Solution:** Check GoogleService-Info.plist is added to Xcode target

- **Issue:** "Notifications not received"
  - **Solution:** Verify APNs key is uploaded to Firebase Console

### Desktop

- **Note:** Desktop only supports local notifications, not push notifications

## Security Notes

⚠️ **Important:**

- Add `google-services.json` and `GoogleService-Info.plist` to `.gitignore`
- Never commit Firebase configuration files to public repositories
- Use environment-specific configurations for dev/staging/prod

## .gitignore Entries

Add to `.gitignore`:

```gitignore
# Firebase
google-services.json
GoogleService-Info.plist
```

## Resources

- [Firebase Cloud Messaging Documentation](https://firebase.google.com/docs/cloud-messaging)
- [KMPNotifier Documentation](https://github.com/mirzemehdi/KMPNotifier)
- [Android FCM Setup](https://firebase.google.com/docs/cloud-messaging/android/client)
- [iOS FCM Setup](https://firebase.google.com/docs/cloud-messaging/ios/client)

---

**Last Updated:** 2025-11-23
