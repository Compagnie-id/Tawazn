# Testing Documentation

This directory contains comprehensive testing documentation for the Tawazn app.

## 📁 Files

### 1. TESTING_CHECKLIST.csv
**Purpose:** Comprehensive test case spreadsheet
**How to Use:** Import into Google Sheets or Excel

**Contains:**
- 180+ test cases across all features
- Platform columns (Android, iOS, Desktop)
- Priority levels (Critical, High, Medium, Low)
- Status tracking
- Notes column for issues

**Import Instructions:**
```
Google Sheets: File → Import → Upload → TESTING_CHECKLIST.csv
Excel: Data → From Text/CSV → TESTING_CHECKLIST.csv
```

### 2. TESTING_GUIDE.md
**Purpose:** Detailed testing procedures and guidelines
**How to Use:** Read before starting testing

**Contains:**
- Testing workflow
- Detailed test procedures with examples
- Bug reporting template
- Test data scenarios
- Common issues checklist
- Go/No-Go decision criteria
- Test report template

### 3. quick-test.sh
**Purpose:** Automated build verification script
**How to Use:** Run before manual testing

**Usage:**
```bash
./quick-test.sh
```

**What it checks:**
- ✅ Gradle wrapper exists
- ✅ Build files present
- ✅ Clean build succeeds
- ✅ Compilation passes
- ✅ SQLDelight generates correctly
- ✅ Critical source files exist
- ✅ DataStore platform files exist
- ✅ Database schema present
- ✅ Android APK assembles

## 🚀 Quick Start

### Option 1: Full Manual Testing (Recommended)

1. **Import the checklist:**
   ```bash
   # Open TESTING_CHECKLIST.csv in Google Sheets or Excel
   ```

2. **Read the guide:**
   ```bash
   # Open TESTING_GUIDE.md
   ```

3. **Start testing:**
   - Follow the guide's procedures
   - Mark results in the spreadsheet
   - Document any bugs

### Option 2: Quick Verification

1. **Run the automated test:**
   ```bash
   ./quick-test.sh
   ```

2. **If all pass, proceed to:**
   - Install on Android device
   - Test critical features manually
   - Check TESTING_CHECKLIST.csv for specific cases

## 📊 Test Categories

The checklist covers:

1. **Build & Setup** (6 tests)
2. **First Launch** (5 tests)
3. **Settings** (30+ tests)
4. **Profile Screen** (11 tests)
5. **Privacy & Security** (8 tests)
6. **About Screen** (7 tests)
7. **Focus Sessions** (30+ tests)
8. **Usage Goals** (20+ tests)
9. **Analytics** (24+ tests)
10. **Dashboard** (6 tests)
11. **Database** (8 tests)
12. **DataStore** (9 tests)
13. **Performance** (6 tests)
14. **UI/UX** (8 tests)
15. **Error Handling** (5 tests)
16. **Integration** (10+ tests)
17. **Platform Specific** (13 tests)
18. **Accessibility** (4 tests)
19. **Regression** (2 tests)
20. **End-to-End** (5 scenarios)

**Total: 180+ test cases**

## 🎯 Testing Priorities

### Phase 1: Critical (Must Pass)
- Build verification
- First launch
- Core CRUD operations
- Data persistence
- Critical navigation flows

**Time Estimate:** 2-3 hours

### Phase 2: High Priority
- All settings features
- Analytics with real data
- Platform-specific features
- Error handling

**Time Estimate:** 3-4 hours

### Phase 3: Medium/Low Priority
- UI polish
- Performance
- Accessibility
- Edge cases

**Time Estimate:** 2-3 hours

**Total Estimate:** 7-10 hours for comprehensive testing

## 📝 Test Execution Workflow

```
┌─────────────────────────┐
│  Run quick-test.sh      │
│  (Build Verification)   │
└───────────┬─────────────┘
            │
            ▼
┌─────────────────────────┐
│  Import Checklist to    │
│  Spreadsheet            │
└───────────┬─────────────┘
            │
            ▼
┌─────────────────────────┐
│  Test Critical Cases    │
│  (Phase 1)              │
└───────────┬─────────────┘
            │
            ▼
┌─────────────────────────┐
│  Document Any Issues    │
└───────────┬─────────────┘
            │
            ▼
┌─────────────────────────┐
│  Test High Priority     │
│  (Phase 2)              │
└───────────┬─────────────┘
            │
            ▼
┌─────────────────────────┐
│  Fix Critical Bugs      │
└───────────┬─────────────┘
            │
            ▼
┌─────────────────────────┐
│  Regression Testing     │
└───────────┬─────────────┘
            │
            ▼
┌─────────────────────────┐
│  Test Medium/Low        │
│  (Phase 3)              │
└───────────┬─────────────┘
            │
            ▼
┌─────────────────────────┐
│  Generate Test Report   │
└───────────┬─────────────┘
            │
            ▼
┌─────────────────────────┐
│  Go/No-Go Decision      │
└─────────────────────────┘
```

## 🐛 Bug Reporting

When you find a bug:

1. **Document in spreadsheet Notes column**
2. **Create GitHub issue** (if using GitHub)
3. **Use bug template** from TESTING_GUIDE.md
4. **Include:**
   - Steps to reproduce
   - Expected vs actual result
   - Screenshots/videos
   - Platform and environment details

## ✅ Completion Criteria

Testing is complete when:

- [ ] All critical tests pass
- [ ] All high priority tests pass
- [ ] >90% of medium priority tests pass
- [ ] All bugs are documented
- [ ] Critical bugs are fixed
- [ ] Test report is generated

## 📊 Sample Test Results

After testing, your spreadsheet should look like:

| Test Case | Android | iOS | Desktop | Status | Notes |
|-----------|---------|-----|---------|--------|-------|
| App Starts | ✅ | ✅ | ✅ | Pass | - |
| Dark Mode Toggle | ✅ | ✅ | ✅ | Pass | - |
| Create Session | ✅ | ⚠️ | ✅ | Partial | iOS: Minor UI issue |
| Save Goals | ✅ | ✅ | ✅ | Pass | - |

## 🔧 Troubleshooting

### quick-test.sh fails
```bash
# Check Gradle wrapper
./gradlew --version

# Try clean build manually
./gradlew clean build --stacktrace
```

### Can't import CSV
```bash
# Verify file exists
ls -la TESTING_CHECKLIST.csv

# Check file format
file TESTING_CHECKLIST.csv
```

### Build errors
```bash
# Clean and rebuild
./gradlew clean
./gradlew build --info
```

## 📞 Getting Help

1. **Check TESTING_GUIDE.md** for detailed procedures
2. **Review error logs** (Logcat for Android, Console for iOS)
3. **Check project README** for setup instructions
4. **Create an issue** with test results attached

## 📈 Metrics to Track

During testing, track:

- **Test Coverage:** % of tests executed
- **Pass Rate:** % of tests passed
- **Bug Count:** Total bugs found
- **Critical Bugs:** High-severity issues
- **Platform Issues:** Platform-specific problems
- **Performance Issues:** Lag, crashes, slowness

## 🎓 Best Practices

1. **Test one platform at a time** for consistency
2. **Start with critical tests** to catch blockers early
3. **Document as you go** don't wait until the end
4. **Take screenshots** of bugs and UI issues
5. **Retest after fixes** to prevent regressions
6. **Use real devices** when possible, not just emulators
7. **Test with different data** (empty, moderate, heavy)
8. **Test edge cases** (long names, special characters, etc.)

## 📅 Recommended Testing Schedule

### Day 1: Build & Critical Features (3-4 hours)
- Run quick-test.sh
- Test builds on all platforms
- Test critical features (Settings, Sessions, Goals)
- Document any blockers

### Day 2: High Priority Features (3-4 hours)
- Test all navigation
- Test data persistence
- Test analytics integration
- Fix any critical bugs

### Day 3: Medium Priority & Polish (2-3 hours)
- Test UI/UX
- Test performance
- Test edge cases
- Fix remaining issues

### Day 4: Regression & Sign-off (1-2 hours)
- Re-test critical features
- Verify all bugs fixed
- Generate test report
- Go/No-Go decision

**Total: 9-13 hours spread over 4 days**

---

**Ready to start testing?**

1. Run `./quick-test.sh`
2. Import `TESTING_CHECKLIST.csv`
3. Follow `TESTING_GUIDE.md`

**Good luck! 🧪**
