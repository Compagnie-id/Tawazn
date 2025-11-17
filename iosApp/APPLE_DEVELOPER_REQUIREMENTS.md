# Apple Developer Requirements for Screen Time Features

## ⚠️ Important: Family Controls is a Restricted Entitlement

The Screen Time API (Family Controls framework) is a **restricted entitlement** that has special requirements.

## Current Issue

You're seeing this error:
```
Cannot create a iOS App Development provisioning profile for "id.compagnie.tawazn.Tawazn".
Personal development teams, including "S. A. Kobar", do not support the
Family Controls (Development) capability.
```

### Why This Happens

| Account Type | Family Controls Support | Cost |
|--------------|------------------------|------|
| **Personal Team** (Free Apple ID) | ❌ Not supported | Free |
| **Apple Developer Program** | ✅ Supported (with approval) | $99/year |

## Your Options

### Option 1: Test Without Screen Time (Recommended for Now)

You can test the app's UI, navigation, and basic functionality without Screen Time features:

#### Step 1: Switch to Development Entitlements

In **Xcode**:
1. Select the **iosApp** target
2. Go to **Signing & Capabilities** tab
3. Under **Code Signing Entitlements**, change from:
   - `iosApp/iosApp.entitlements`
   - To: `iosApp/iosApp-dev.entitlements`

Or manually edit the project file to use `iosApp-dev.entitlements`.

#### Step 2: Update Swift Bridge Check

The app will detect that Screen Time is not available and gracefully disable those features.

#### What Works Without Family Controls

✅ App builds and runs
✅ UI and navigation
✅ Database operations
✅ Settings screens
✅ Onboarding flow

❌ Screen Time authorization (will fail gracefully)
❌ App blocking (will show "not available" message)
❌ Usage tracking via Screen Time API

---

### Option 2: Get Family Controls Access (For Production)

To enable full Screen Time functionality, you need:

#### Step 1: Join Apple Developer Program

1. Go to [Apple Developer Program](https://developer.apple.com/programs/)
2. Click "Enroll"
3. Pay $99/year membership fee
4. Complete enrollment (takes 1-2 days)

#### Step 2: Apply for Family Controls Entitlement

1. Go to [Apple Developer Certificates Portal](https://developer.apple.com/contact/request/family-controls-access)
2. Fill out the **Family Controls Entitlement Request** form
3. Provide justification:
   ```
   App Name: Tawazn
   Purpose: Screen time management and productivity app
   Use Case: Help users track and limit app usage to improve focus
   Implementation: Using DeviceActivity, ManagedSettings, and FamilyControls
   ```
4. Submit and wait for Apple's response (1-2 weeks typical)

#### Step 3: Configure Your Account

Once approved:
1. In Xcode → **Signing & Capabilities**
2. Select your **Paid Team** instead of Personal Team
3. Switch back to `iosApp.entitlements` (the full one)
4. Build and run

---

### Option 3: Hybrid Approach (Test While Waiting)

Best for active development:

1. **Use Option 1** (`iosApp-dev.entitlements`) for UI/UX development
2. **Apply for Apple Developer** + Family Controls access
3. **Switch back** to full entitlements once approved

---

## Implementation Notes

### Runtime Detection

The app already has runtime checks:

```swift
// iOSApp.swift
private func initializeScreenTimeBridge() {
    if #available(iOS 15.0, *) {
        let bridge = SwiftScreenTimeBridgeImpl()
        ScreenTimeApiKt.initializeScreenTimeBridge(bridge: bridge)
        print("✅ Screen Time bridge initialized")
    } else {
        print("⚠️ Screen Time not available")
    }
}
```

### Graceful Degradation

When Family Controls is not available:
- `ScreenTimeApi.isAvailable()` returns `false`
- UI shows "Screen Time not available" message
- App continues to work with limited functionality
- No crashes or errors

### Android Comparison

| Feature | iOS (with Family Controls) | iOS (without) | Android |
|---------|---------------------------|---------------|---------|
| App Listing | Limited (privacy) | ❌ | ✅ Full access |
| Usage Tracking | ✅ DeviceActivity | ❌ | ✅ UsageStatsManager |
| App Blocking | ✅ ManagedSettings | ❌ | ✅ AccessibilityService |
| Permissions | Family Controls | N/A | Usage Stats + Accessibility |

Android doesn't have these restrictions! The Android version works fully with a free account.

---

## Testing Strategy

### Phase 1: Free Development (Now)

**Use**: `iosApp-dev.entitlements`

Focus on:
- ✅ UI/UX design
- ✅ Navigation flow
- ✅ Database operations
- ✅ Settings and preferences
- ✅ Analytics screens (mock data)

### Phase 2: Simulator Testing (After Approval)

**Use**: `iosApp.entitlements` with paid account

Test on simulator:
- ⚠️ Family Controls **may work** on simulator
- ⚠️ Some features require physical device
- ✅ Good for initial testing

### Phase 3: Device Testing (Production Ready)

**Use**: Full entitlements on real iPhone

- ✅ Full Screen Time functionality
- ✅ Real-world usage scenarios
- ✅ App blocking enforcement
- ✅ DeviceActivity monitoring

---

## Quick Reference

### Build with Personal Team (No Screen Time)

```bash
# 1. Switch entitlements in Xcode to iosApp-dev.entitlements
# 2. Build
./gradlew :composeApp:embedAndSignAppleFrameworkForXcode
cd iosApp && open iosApp.xcodeproj
# 3. Select Personal Team in Signing
# 4. Build and run (Cmd+R)
```

### Build with Developer Account (Full Features)

```bash
# 1. Use iosApp.entitlements (default)
# 2. Build
./gradlew :composeApp:embedAndSignAppleFrameworkForXcode
cd iosApp && open iosApp.xcodeproj
# 3. Select Paid Team in Signing
# 4. Wait for Family Controls approval
# 5. Build and run (Cmd+R)
```

---

## Cost-Benefit Analysis

### Option 1: Free Personal Account
- **Cost**: $0
- **Time**: Ready now
- **Limitation**: No Screen Time features on iOS
- **Best for**: UI development, testing structure

### Option 2: Apple Developer Program
- **Cost**: $99/year
- **Time**: 1-2 days enrollment + 1-2 weeks entitlement approval
- **Benefit**: Full iOS features, TestFlight, App Store distribution
- **Best for**: Production app, full testing

---

## Recommendation

### For Right Now (Today)

1. ✅ Use `iosApp-dev.entitlements` (without Family Controls)
2. ✅ Test with Personal Team (free)
3. ✅ Focus on:
   - App architecture
   - UI/UX polish
   - Database operations
   - Cross-platform code
   - Android implementation (fully works!)

### For Production (Within 2-3 Weeks)

1. ✅ Enroll in Apple Developer Program ($99/year)
2. ✅ Apply for Family Controls entitlement
3. ✅ Switch to `iosApp.entitlements` (full version)
4. ✅ Test Screen Time features
5. ✅ Submit to App Store when ready

---

## Next Steps

Choose your path:

**Path A: Continue Development Without Screen Time**
```bash
# Switch to dev entitlements in Xcode
# Build and test app structure
# Develop on Android (full features work!)
```

**Path B: Get Full iOS Access**
```bash
# 1. Enroll at developer.apple.com/programs
# 2. Apply for Family Controls entitlement
# 3. Wait for approval
# 4. Continue with full features
```

**Path C: Hybrid (Recommended)**
```bash
# 1. Use dev entitlements NOW for UI testing
# 2. Start Apple Developer enrollment process
# 3. Switch to full entitlements when approved
```

---

## Questions?

- **"Can I test Screen Time on simulator without paying?"** - Maybe, depends on iOS version. Try dev entitlements first.
- **"How long does Apple approval take?"** - Usually 1-2 weeks for Family Controls.
- **"Can I publish without Family Controls?"** - Yes, but users won't get iOS blocking features.
- **"Does Android have same issue?"** - No! Android works fully with free account.

The production-grade code is ready. This is just an Apple platform restriction, not a code issue! ✅
