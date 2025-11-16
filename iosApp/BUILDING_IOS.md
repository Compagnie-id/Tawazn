# Building the iOS App - Quick Start Guide

## The "Cannot find ComposeApp" Error

If you're seeing errors like `Unable to find ComposeApp` in Swift files, this is **completely normal** and expected! Here's why:

### Why This Happens

1. **ComposeApp is a Kotlin framework** that gets built by Gradle
2. **Swift files import it** to use the Compose UI
3. **Xcode can't find it** until you build the Kotlin framework first
4. This is the **correct build order** for Kotlin Multiplatform projects

### The Solution - Build Order Matters!

```
Step 1: Build Kotlin Framework (using Gradle)
        ↓
Step 2: Xcode can now find ComposeApp
        ↓
Step 3: Build iOS app in Xcode
```

## Quick Start - Automated Build

I've created a script that does everything for you:

```bash
# From the project root
./iosApp/build-and-run.sh
```

This script will:
1. ✅ Build the Kotlin Multiplatform framework
2. ✅ Verify the framework was created
3. ✅ Open the Xcode project
4. ✅ Ready to run!

## Manual Build (Step by Step)

If you prefer to build manually or the script doesn't work:

### Step 1: Build the Kotlin Framework

From the **project root** directory:

```bash
./gradlew :composeApp:embedAndSignAppleFrameworkForXcode
```

**What this does:**
- Compiles the Kotlin Multiplatform code
- Creates the ComposeApp.framework
- Places it in `composeApp/build/compose/ios/framework/`

**Expected output:**
```
BUILD SUCCESSFUL in Xs
```

### Step 2: Open Xcode

```bash
cd iosApp
open iosApp.xcodeproj
```

### Step 3: Build in Xcode

1. Select a simulator: **iPhone 15** or any iOS 15.0+ simulator
2. Press **Cmd + B** to build
3. Press **Cmd + R** to run

The app should now build successfully! ✅

## If You Still See "Cannot find ComposeApp"

### Check 1: Framework Location

Verify the framework was built:

```bash
ls -la composeApp/build/compose/ios/framework/
```

You should see `ComposeApp.framework`

### Check 2: Xcode Build Phase

1. Open Xcode project
2. Select **iosApp** target
3. Go to **Build Phases** tab
4. Look for **"Compile Kotlin Framework"** phase
5. This should run **before** the Swift compilation

### Check 3: Clean and Rebuild

In Xcode:
1. **Product** → **Clean Build Folder** (Cmd + Shift + K)
2. Close Xcode
3. Delete derived data:
   ```bash
   rm -rf ~/Library/Developer/Xcode/DerivedData/iosApp-*
   ```
4. Rebuild the Kotlin framework:
   ```bash
   ./gradlew :composeApp:embedAndSignAppleFrameworkForXcode
   ```
5. Reopen Xcode and build

## Understanding the Architecture

### Kotlin Files (Common)
```
composeApp/src/
├── commonMain/     ← Shared UI code
├── iosMain/        ← iOS-specific Kotlin
└── androidMain/    ← Android-specific Kotlin
```

These get compiled into **ComposeApp.framework**

### Swift Files (iOS App)
```
iosApp/iosApp/
├── ContentView.swift            ← Uses ComposeApp ✅
├── iOSApp.swift                 ← Main app entry
├── ScreenTimeBridge.swift       ← Screen Time wrapper
├── ScreenTimeHelperImpl.swift   ← No ComposeApp import ✅
├── ScreenTimeManager.swift      ← Screen Time logic
└── IOSPermissionHelper.swift    ← Permissions
```

**Important:** Only `ContentView.swift` actually needs to import `ComposeApp` because it displays the Compose UI. The other Swift files work with native iOS frameworks.

## Files Modified to Avoid ComposeApp Import

I've updated these files to minimize dependencies on ComposeApp:

1. **ScreenTimeHelperImpl.swift** - Removed ComposeApp import
   - Now uses pure Swift/Foundation
   - Bridges to Kotlin at runtime instead of compile time

2. **iOSApp.swift** - Removed ComposeApp import from init
   - Moved initialization to `onAppear`
   - This happens after ComposeApp is loaded

The only file that **must** import ComposeApp is `ContentView.swift` because it displays the Compose UI using `ComposeUIViewController`.

## Gradle Commands Reference

```bash
# Build framework for simulator
./gradlew :composeApp:embedAndSignAppleFrameworkForXcode

# Clean Kotlin build
./gradlew :composeApp:clean

# Build for specific configuration
./gradlew :composeApp:embedAndSignAppleFrameworkForXcode -PCONFIGURATION=Debug

# Build framework only (without embedding)
./gradlew :composeApp:linkDebugFrameworkIosSimulatorArm64
```

## Xcode Build Settings

The Xcode project is configured to:

1. **Run Gradle before compilation** (Build Phase: "Compile Kotlin Framework")
2. **Link the framework** automatically
3. **Use deployment target** iOS 15.0
4. **Include entitlements** for Family Controls

You can see this in:
- Target → **Build Phases** → **Compile Kotlin Framework**
- Target → **Build Settings** → **Framework Search Paths**

## Troubleshooting Network Issues

If you see network errors during Gradle build:

```
Exception in thread "main" java.net.UnknownHostException: services.gradle.org
```

**Solutions:**
1. Check your internet connection
2. Try using a VPN if behind a firewall
3. Use Gradle offline mode (if dependencies are cached):
   ```bash
   ./gradlew --offline :composeApp:embedAndSignAppleFrameworkForXcode
   ```

## Success Checklist

✅ Kotlin framework built successfully
✅ `ComposeApp.framework` exists in build directory
✅ Xcode can import ComposeApp
✅ App builds without errors
✅ App runs on simulator

## Next Steps After Building

Once the app builds successfully:

1. 📱 **Test on simulator** - Basic functionality works
2. 🔐 **Request permissions** - Screen Time authorization dialog appears
3. 📝 **Apply for entitlement** - For real device testing (see ENTITLEMENTS_SETUP.md)
4. 🚀 **Deploy to device** - Full Screen Time features on real iOS device

## Need Help?

- Build errors: Check Gradle output
- Import errors: Make sure framework is built first
- Runtime errors: Check Xcode console for Swift/Kotlin errors
- Permission errors: See ENTITLEMENTS_SETUP.md

The iOS app is ready to build once you run the Gradle command to create the framework! 🎉
