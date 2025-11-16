# How to Configure Family Controls Entitlement in Xcode

## The Problem
Family Controls doesn't appear as a capability option in Xcode's UI because it requires special approval from Apple.

## The Solution - Manual Configuration

I've already configured the entitlements file for you! Here's what was done:

### 1. Entitlements File Created
✅ File: `iosApp/iosApp/iosApp.entitlements`
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
<dict>
    <key>com.apple.developer.family-controls</key>
    <true/>
</dict>
</plist>
```

### 2. Xcode Project Updated
✅ The project file now references the entitlements file:
- Build Setting: `CODE_SIGN_ENTITLEMENTS = iosApp/iosApp.entitlements`
- Added to both Debug and Release configurations

## How to Verify in Xcode

1. **Open the project**:
   ```bash
   open iosApp/iosApp.xcodeproj
   ```

2. **Check Build Settings**:
   - Click on the **iosApp** project in the navigator (blue icon at the top)
   - Select the **iosApp** target (under TARGETS)
   - Click on **Build Settings** tab
   - Search for "entitlements" in the search bar
   - You should see: `Code Signing Entitlements` = `iosApp/iosApp.entitlements`

3. **View the Entitlements File**:
   - In Xcode's Project Navigator (left sidebar)
   - You should see `iosApp.entitlements` file
   - If not visible, add it: Right-click iosApp folder → Add Files → select `iosApp.entitlements`

4. **Check Signing & Capabilities**:
   - Select the **iosApp** target
   - Go to **Signing & Capabilities** tab
   - You won't see "Family Controls" listed in the UI (this is normal!)
   - The entitlement is configured via the .entitlements file instead

## Why Family Controls Doesn't Show in UI

Family Controls is a **restricted entitlement** that:
- ❌ Doesn't appear in Xcode's capability picker
- ✅ Must be added manually to the .entitlements file
- ⚠️ Requires approval from Apple to work on real devices
- ✅ Can be configured for development/testing without approval

## What You Need to Do

### For Simulator Testing (Works Now)
✅ Everything is configured! Just build and run:
```bash
# In Xcode
Cmd + B  (Build)
Cmd + R  (Run)
```

### For Real Device Testing (Requires Apple Approval)
1. **Apply for the entitlement**:
   - Visit: https://developer.apple.com/contact/request/family-controls-distribution/
   - Fill out the form explaining your screen time management app
   - Wait for approval (typically 2-4 weeks)

2. **After approval**:
   - The entitlement will work on real devices
   - Screen Time features will be fully functional
   - Users can authorize the app to manage Screen Time

## Troubleshooting

### "The executable was signed with invalid entitlements"
This error appears if:
- You haven't been approved for the Family Controls entitlement by Apple
- Testing on a real device (simulators have limited checking)

**Solution**:
- Test on simulator for now
- Apply for the entitlement from Apple
- Or temporarily remove the entitlement for testing other features

### Entitlements file not found
**Solution**:
1. Make sure `iosApp.entitlements` exists in `iosApp/iosApp/` directory
2. In Xcode, right-click iosApp folder → "Add Files to iosApp..."
3. Select `iosApp.entitlements`
4. Ensure "Add to targets" has iosApp checked

### Can't see the file in Xcode Navigator
**Solution**:
- The file is there, it might just not be visible in Xcode yet
- Close and reopen the project
- Or manually add it using "Add Files to iosApp..."

## Summary

✅ **DONE**: Entitlements file created and configured
✅ **DONE**: Xcode project updated to use entitlements
✅ **DONE**: Deployment target set to iOS 15.0
⏳ **PENDING**: Apply for Family Controls entitlement from Apple (for real device testing)
✅ **READY**: App can be built and tested on simulator now!

## Next Steps

1. Open project in Xcode: `open iosApp/iosApp.xcodeproj`
2. Build the project: `Cmd + B`
3. Run on simulator: `Cmd + R`
4. The app should launch successfully!

The Family Controls capability is configured correctly even though it doesn't show in Xcode's capability UI. This is completely normal for restricted entitlements.
