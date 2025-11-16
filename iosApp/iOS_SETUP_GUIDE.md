# iOS Setup Guide for Tawazn

This guide will help you set up the iOS app with Screen Time functionality.

## Overview

The iOS implementation has been updated to properly integrate with iOS Screen Time APIs. The following files have been created/updated:

### New Files Created
1. `iosApp/iosApp/iosApp.entitlements` - Family Controls entitlement
2. `iosApp/iosApp/ScreenTimeBridge.swift` - Bridge to Screen Time APIs
3. `iosApp/iosApp/ScreenTimeHelperImpl.swift` - Swift implementation of Kotlin interface
4. Updated `iosApp/iosApp/iOSApp.swift` - Initialize the bridge on app startup

### Updated Kotlin Files
1. `platform/ios/src/iosMain/kotlin/.../IOSAppMonitorImpl.kt` - Now uses Swift bridge instead of NotImplementedError

## Setup Steps in Xcode

### 1. Add New Swift Files to Xcode Project

Open `iosApp/iosApp.xcodeproj` in Xcode, then:

1. In the Project Navigator, right-click on the `iosApp` folder
2. Select "Add Files to iosApp..."
3. Add the following files (if not already added):
   - `ScreenTimeBridge.swift`
   - `ScreenTimeHelperImpl.swift`
4. Make sure "Copy items if needed" is **unchecked** (files are already in the correct location)
5. Make sure "Add to targets" has **iosApp** checked

### 2. Add Entitlements File

1. In Xcode, select the `iosApp` target
2. Go to "Signing & Capabilities" tab
3. Click "+ Capability"
4. Add "Family Controls" capability (this will automatically create/update the entitlements file)
5. Alternatively, manually add the entitlements file:
   - In the target settings, under "Build Settings"
   - Search for "Code Signing Entitlements"
   - Set the value to: `iosApp/iosApp.entitlements`

### 3. Add Required Frameworks

The app already imports the necessary frameworks in Swift code:
- `FamilyControls.framework` - For authorization and app tokens
- `DeviceActivity.framework` - For monitoring
- `ManagedSettings.framework` - For app shielding

These are automatically linked when you import them in Swift files.

### 4. Verify Info.plist

The `Info.plist` should already contain (verify this):

```xml
<key>NSFamilyControlsUsageDescription</key>
<string>Tawazn needs access to Screen Time to help you manage your app usage, block distracting apps, and achieve digital wellness. Your data stays private on your device.</string>
```

### 5. Request Family Controls Entitlement from Apple

**IMPORTANT**: The Family Controls entitlement requires approval from Apple.

1. Go to: https://developer.apple.com/contact/request/family-controls-distribution/
2. Fill out the form explaining your use case (screen time management app)
3. Wait for approval (can take several weeks)
4. Once approved, the entitlement will work on real devices

**Note**: The app will still run without approval, but Screen Time features won't work. For simulator testing during development, you can still test the UI and basic functionality.

### 6. Update Deployment Target

The deployment target has been updated to iOS 15.0 (required for Screen Time API).

Verify in Xcode:
1. Select the iosApp target
2. Go to "General" tab
3. Under "Minimum Deployments", ensure it's set to iOS 15.0 or higher

## Testing on Simulator

### Current Limitations

1. **Simulator Compatibility**: iOS 26.0 doesn't exist - the latest iOS is 18.x. If you're seeing "iOS 26.0 simulator", this might be:
   - A typo (perhaps iOS 16.0 or 18.0?)
   - An Xcode version mismatch
   - A simulator configuration issue

2. **Screen Time on Simulator**: Screen Time APIs have limited functionality on simulator:
   - Authorization requests work
   - App shielding may not work fully
   - Usage monitoring requires a real device

### Steps to Test

1. Build the app in Xcode: `Cmd + B`
2. Run on simulator: `Cmd + R`
3. The app should launch without crashing
4. Test the app blocking feature - it will request authorization
5. For full testing, use a real iOS device with iOS 15.0+

## Troubleshooting

### Error: "ScreenTimeBridge not found" or "Cannot find ScreenTimeBridge in scope"

**Solution**: Make sure `ScreenTimeBridge.swift` is added to the iosApp target in Xcode.

1. Select `ScreenTimeBridge.swift` in Project Navigator
2. In the File Inspector (right panel), check "Target Membership"
3. Ensure `iosApp` is checked

### Error: "Module 'ComposeApp' not found"

**Solution**: Build the Kotlin framework first before running the iOS app.

```bash
# From project root
./gradlew :composeApp:embedAndSignAppleFrameworkForXcode
```

### Error: Authorization Always Fails

**Possible causes**:
1. Running on simulator (limited support)
2. Family Controls entitlement not approved by Apple
3. Entitlements file not properly configured

**Solution**:
- Test on a real device
- Check that the entitlements file is correctly set in build settings
- Wait for Apple's approval of the entitlement

### App Crashes on Launch

**Check**:
1. All Swift files are added to the iosApp target
2. The ComposeApp framework is properly built
3. Check Xcode console for error messages

## Building from Command Line

```bash
# Open project in Xcode
open iosApp/iosApp.xcodeproj

# Or build from command line (requires Xcode installed)
xcodebuild -project iosApp/iosApp.xcodeproj \
  -scheme iosApp \
  -configuration Debug \
  -destination 'platform=iOS Simulator,name=iPhone 15'
```

## Architecture

```
┌─────────────────────────────────────────────┐
│           Compose Multiplatform UI           │
│              (Kotlin Common)                 │
└─────────────────┬───────────────────────────┘
                  │
┌─────────────────▼───────────────────────────┐
│        IOSAppMonitorImpl (Kotlin/Native)     │
│         Uses IOSScreenTimeHelper             │
└─────────────────┬───────────────────────────┘
                  │
┌─────────────────▼───────────────────────────┐
│    ScreenTimeHelperImpl (Swift)              │
│         Implements Kotlin Interface          │
└─────────────────┬───────────────────────────┘
                  │
┌─────────────────▼───────────────────────────┐
│       ScreenTimeBridge (Swift)               │
│    Wraps ScreenTimeManager & PermissionHelper│
└─────────────────┬───────────────────────────┘
                  │
┌─────────────────▼───────────────────────────┐
│      iOS Screen Time API (Native)            │
│  FamilyControls, DeviceActivity, etc.        │
└─────────────────────────────────────────────┘
```

## Next Steps

1. Open the project in Xcode
2. Add the new Swift files to the target (if needed)
3. Build and run on simulator/device
4. Apply for Family Controls entitlement from Apple
5. Test on a real device once entitlement is approved

## Questions?

If you encounter issues:
1. Check the Xcode build output for specific errors
2. Verify all files are properly added to the target
3. Ensure the deployment target is iOS 15.0+
4. Make sure you've built the Kotlin framework

For Screen Time API documentation:
- [Family Controls Documentation](https://developer.apple.com/documentation/familycontrols)
- [Device Activity Documentation](https://developer.apple.com/documentation/deviceactivity)
- [WWDC21: Meet the Screen Time API](https://developer.apple.com/videos/play/wwdc2021/10123/)
