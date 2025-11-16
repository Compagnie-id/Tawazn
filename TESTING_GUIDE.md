# Tawazn Testing Guide

## 📋 Overview

This guide provides comprehensive testing procedures for the Tawazn screen time management app across all platforms (Android, iOS, Desktop).

## 📊 Using the Testing Checklist

### Import to Google Sheets / Excel

1. **Google Sheets:**
   ```
   File → Import → Upload → Select TESTING_CHECKLIST.csv
   ```

2. **Excel:**
   ```
   Data → From Text/CSV → Select TESTING_CHECKLIST.csv
   ```

3. **Add Conditional Formatting:**
   - Status column: Green for "Pass", Red for "Fail", Yellow for "Partial"
   - Platform columns: Checkmarks or color coding

### Column Definitions

| Column | Purpose |
|--------|---------|
| **Category** | Feature area being tested |
| **Test Case** | Short name of the test |
| **Description** | What to test and how |
| **Priority** | Critical / High / Medium / Low |
| **Android** | Test result for Android |
| **iOS** | Test result for iOS |
| **Desktop** | Test result for Desktop |
| **Status** | Overall status (Pass/Fail/Partial/Not Tested) |
| **Notes** | Any issues, observations, or context |

## 🎯 Testing Priorities

### Phase 1: Critical Tests (Must Pass Before Release)
- All tests marked **Critical**
- Build & Setup
- First Launch
- Core functionality (Settings, Goals, Sessions)
- Data Persistence
- Database Operations

### Phase 2: High Priority Tests
- All navigation flows
- UI/UX consistency
- Platform-specific features
- Error handling

### Phase 3: Medium/Low Priority Tests
- Visual polish
- Accessibility
- Performance optimization
- Nice-to-have features

## 🚀 Testing Workflow

### 1. Pre-Testing Setup

**Android:**
```bash
# Clean build
./gradlew clean

# Build debug APK
./gradlew :composeApp:assembleDebug

# Install on device/emulator
adb install -r composeApp/build/outputs/apk/debug/composeApp-debug.apk

# Or direct install
./gradlew :composeApp:installDebug
```

**iOS:**
```bash
# Open in Xcode
open iosApp/iosApp.xcworkspace

# Or build from CLI
./gradlew :composeApp:iosSimulatorArm64Test
```

**Desktop:**
```bash
./gradlew :composeApp:run
```

### 2. Test Execution

For each test case:

1. **Read the description** carefully
2. **Perform the action** described
3. **Verify the expected result**
4. **Mark the platform columns:**
   - ✅ = Pass
   - ❌ = Fail
   - ⚠️ = Partial/Issue
   - ⏭️ = Skipped
   - ➖ = Not Applicable

5. **Update Status column:**
   - Pass = All platforms pass
   - Fail = Any platform fails
   - Partial = Some platforms pass
   - Not Tested = Haven't tested yet

6. **Add Notes** if:
   - Test failed (describe the issue)
   - Found a bug (include steps to reproduce)
   - Observed unexpected behavior
   - Platform-specific differences

### 3. Bug Reporting

When you find a bug, document:

```markdown
## Bug #XXX: [Short Description]

**Platform:** Android / iOS / Desktop
**Priority:** Critical / High / Medium / Low
**Category:** Settings / Analytics / Focus Sessions / etc.

**Steps to Reproduce:**
1. Step one
2. Step two
3. Step three

**Expected Result:**
What should happen

**Actual Result:**
What actually happens

**Screenshots/Videos:**
[Attach if available]

**Environment:**
- OS Version: Android 14 / iOS 17 / Windows 11
- App Version: 1.0.0
- Device: Pixel 8 / iPhone 15 / Desktop
```

## 🔍 Detailed Test Procedures

### Settings - Dark Mode Test Example

**Test:** Toggle Dark Mode

**Steps:**
1. Open Tawazn app
2. Navigate to Settings
3. Scroll to "Appearance" section
4. Toggle "Dark Mode" switch ON
5. Observe theme change
6. Navigate to different screens
7. Return to Settings
8. Toggle "Dark Mode" switch OFF
9. Observe theme change back

**Expected Results:**
- ✅ Theme changes immediately when toggled
- ✅ All screens use dark theme when ON
- ✅ All screens use light theme when OFF
- ✅ No visual glitches during transition
- ✅ Text remains readable in both modes
- ✅ Icons and colors adapt appropriately

**Pass Criteria:**
- All expected results occur without issues

---

### Focus Sessions - Create Session Test Example

**Test:** Create Session

**Steps:**
1. Navigate to Settings → Focus Sessions
2. Click FAB "+" button
3. Enter name: "Work Focus"
4. Enter description: "Block social media during work"
5. Set start time: 09:00
6. Set end time: 17:00
7. Toggle "Repeat Daily" ON
8. Click "Create Session"
9. Verify session appears in list
10. Close app completely
11. Reopen app
12. Navigate to Focus Sessions
13. Verify session still exists

**Expected Results:**
- ✅ Form opens when FAB clicked
- ✅ All fields are editable
- ✅ Time pickers work correctly
- ✅ Save creates session
- ✅ Session appears in list immediately
- ✅ Session persists after restart
- ✅ All entered data is retained

**Pass Criteria:**
- Session is created and persists correctly

---

### Analytics - Real Data Test Example

**Test:** Real Data Display

**Steps:**
1. Ensure app has usage data (use apps or insert test data)
2. Navigate to Analytics screen
3. Observe stats cards
4. Note "Avg Daily" value
5. Note "Best Day" value
6. Check goal progress bar
7. View insights section
8. Verify top apps list

**Expected Results:**
- ✅ No "No data" messages if data exists
- ✅ Stats show real calculated values (not "3h 12m" hardcoded)
- ✅ Goal progress matches today's usage / goal
- ✅ Insights show actual app names and times
- ✅ Top apps list populated with real apps
- ✅ All times formatted correctly (Xh Ym)

**Pass Criteria:**
- All data is real and calculations are accurate

## 📊 Test Data Scenarios

### Scenario 1: Fresh Install
- No previous data
- Default preferences
- Empty states everywhere

**Test Focus:**
- First launch experience
- Empty state UI
- Default values

### Scenario 2: Moderate Usage
- 3-5 days of usage data
- 1-2 focus sessions created
- Goals set to 3h daily
- Profile filled out

**Test Focus:**
- Normal user experience
- Data aggregation
- Goal tracking

### Scenario 3: Heavy Usage
- 30+ days of usage data
- 10+ focus sessions
- Multiple apps with high usage
- Active streak of 7+ days

**Test Focus:**
- Performance with lots of data
- List scrolling
- Query optimization

### Scenario 4: Edge Cases
- Very long app names (>50 characters)
- Usage times > 10 hours/day
- 100+ apps tracked
- Session names with special characters

**Test Focus:**
- UI overflow handling
- Data validation
- Performance limits

## 🐛 Common Issues Checklist

### Build Issues
- [ ] Gradle sync fails
- [ ] SQLDelight generation errors
- [ ] DataStore compilation issues
- [ ] Platform-specific build errors

### Runtime Issues
- [ ] App crashes on launch
- [ ] Database creation fails
- [ ] DataStore file access denied
- [ ] Navigation crashes

### Data Issues
- [ ] Preferences don't persist
- [ ] Database queries return empty
- [ ] Data doesn't sync between screens
- [ ] Clear data doesn't work completely

### UI Issues
- [ ] Dark mode has visibility issues
- [ ] Text overflow/truncation
- [ ] Layout breaks on small screens
- [ ] Icons missing or wrong
- [ ] Touch targets too small

## 📈 Test Metrics

Track these metrics during testing:

| Metric | Target | Actual |
|--------|--------|--------|
| **Test Coverage** | 100% of critical tests | ___ % |
| **Pass Rate** | > 95% | ___ % |
| **Bugs Found** | Documented | ___ bugs |
| **Critical Bugs** | 0 | ___ bugs |
| **Android Pass Rate** | > 95% | ___ % |
| **iOS Pass Rate** | > 95% | ___ % |
| **Desktop Pass Rate** | > 95% | ___ % |
| **Performance Issues** | 0 | ___ issues |

## ✅ Test Completion Criteria

Before marking testing as complete:

- [ ] All **Critical** tests pass on all platforms
- [ ] All **High Priority** tests pass on all platforms
- [ ] > 90% of **Medium Priority** tests pass
- [ ] All found bugs are documented
- [ ] Critical bugs are fixed and re-tested
- [ ] Data persistence verified across restarts
- [ ] Database operations work correctly
- [ ] Navigation flows work on all platforms
- [ ] UI/UX is consistent and polished
- [ ] Performance is acceptable (no lag)

## 🔄 Regression Testing

After any code changes, re-run:

### Quick Regression Suite (15-20 minutes)
- App launches successfully
- All navigation works
- Dark mode toggle
- Create/edit/delete focus session
- Set and save goals
- View analytics
- Clear data

### Full Regression Suite (1-2 hours)
- All Critical and High priority tests
- Data persistence tests
- Cross-screen integration tests

## 📝 Test Report Template

After testing, create a summary report:

```markdown
# Tawazn Test Report - [Date]

## Summary
- **Total Tests:** XXX
- **Tests Passed:** XXX (XX%)
- **Tests Failed:** XXX (XX%)
- **Tests Skipped:** XXX (XX%)

## Platform Results
- **Android:** XX% pass rate
- **iOS:** XX% pass rate
- **Desktop:** XX% pass rate

## Critical Issues
1. [Issue description]
2. [Issue description]

## Known Limitations
1. [Limitation description]
2. [Limitation description]

## Recommendations
1. [Recommendation]
2. [Recommendation]

## Next Steps
- [ ] Fix critical bugs
- [ ] Re-test failed cases
- [ ] Proceed to release build
```

## 🚦 Go/No-Go Decision

**Criteria for Release:**

✅ **GO** if:
- All critical tests pass
- No critical bugs remain
- Core functionality works on all platforms
- Data persistence is reliable
- Performance is acceptable

❌ **NO-GO** if:
- Any critical test fails
- Critical bugs exist
- Data loss possible
- App crashes frequently
- Major UI/UX issues

## 📞 Support

If you encounter issues during testing:
1. Check TESTING_CHECKLIST.csv for similar cases
2. Review error logs (Android Logcat, iOS Console, Desktop terminal)
3. Document the issue in detail
4. Create a GitHub issue if it's a bug

---

**Happy Testing! 🧪**
