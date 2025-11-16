#!/bin/bash

# Tawazn Quick Test Script
# Run this to verify basic build and functionality

set -e  # Exit on error

echo "🧪 Tawazn Quick Test Suite"
echo "=========================="
echo ""

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Test counters
PASSED=0
FAILED=0

# Function to print test result
test_result() {
    if [ $1 -eq 0 ]; then
        echo -e "${GREEN}✅ PASS${NC}: $2"
        ((PASSED++))
    else
        echo -e "${RED}❌ FAIL${NC}: $2"
        ((FAILED++))
    fi
}

# Test 1: Gradle wrapper exists
echo "Test 1: Checking Gradle wrapper..."
if [ -f "./gradlew" ]; then
    test_result 0 "Gradle wrapper found"
else
    test_result 1 "Gradle wrapper not found"
fi

# Test 2: Build files exist
echo "Test 2: Checking build files..."
if [ -f "build.gradle.kts" ] && [ -f "settings.gradle.kts" ]; then
    test_result 0 "Build files exist"
else
    test_result 1 "Build files missing"
fi

# Test 3: Clean build
echo "Test 3: Running clean build..."
if ./gradlew clean > /dev/null 2>&1; then
    test_result 0 "Clean completed"
else
    test_result 1 "Clean failed"
fi

# Test 4: Compile check
echo "Test 4: Checking compilation..."
if ./gradlew compileKotlinAndroid compileKotlinIosSimulatorArm64 compileKotlinJvm --quiet > /dev/null 2>&1; then
    test_result 0 "Compilation successful"
else
    test_result 1 "Compilation failed"
fi

# Test 5: SQLDelight generation
echo "Test 5: Checking SQLDelight..."
if [ -d "core/database/build/generated" ] || ./gradlew :core:database:generateCommonMainTawaznDatabaseInterface --quiet > /dev/null 2>&1; then
    test_result 0 "SQLDelight generated"
else
    test_result 1 "SQLDelight generation failed"
fi

# Test 6: Check critical source files exist
echo "Test 6: Verifying critical files..."
CRITICAL_FILES=(
    "composeApp/src/commonMain/kotlin/id/compagnie/tawazn/App.kt"
    "core/datastore/src/commonMain/kotlin/id/compagnie/tawazn/core/datastore/AppPreferences.kt"
    "feature/analytics/src/commonMain/kotlin/id/compagnie/tawazn/feature/analytics/AnalyticsScreenModel.kt"
    "feature/settings/src/commonMain/kotlin/id/compagnie/tawazn/feature/settings/FocusSessionListScreen.kt"
    "feature/settings/src/commonMain/kotlin/id/compagnie/tawazn/feature/settings/UsageGoalsScreen.kt"
)

ALL_FILES_EXIST=true
for file in "${CRITICAL_FILES[@]}"; do
    if [ ! -f "$file" ]; then
        echo "  Missing: $file"
        ALL_FILES_EXIST=false
    fi
done

if $ALL_FILES_EXIST; then
    test_result 0 "All critical files exist"
else
    test_result 1 "Some critical files missing"
fi

# Test 7: Check DataStore platform files
echo "Test 7: Checking DataStore platform implementations..."
DATASTORE_FILES=(
    "core/datastore/src/androidMain/kotlin/id/compagnie/tawazn/core/datastore/DataStoreFactory.android.kt"
    "core/datastore/src/iosMain/kotlin/id/compagnie/tawazn/core/datastore/DataStoreFactory.ios.kt"
    "core/datastore/src/jvmMain/kotlin/id/compagnie/tawazn/core/datastore/DataStoreFactory.jvm.kt"
)

ALL_DATASTORE_EXIST=true
for file in "${DATASTORE_FILES[@]}"; do
    if [ ! -f "$file" ]; then
        echo "  Missing: $file"
        ALL_DATASTORE_EXIST=false
    fi
done

if $ALL_DATASTORE_EXIST; then
    test_result 0 "DataStore platform files exist"
else
    test_result 1 "DataStore platform files missing"
fi

# Test 8: Check database schema
echo "Test 8: Checking database schema..."
if [ -f "core/database/src/commonMain/sqldelight/id/compagnie/tawazn/database/BlockSession.sq" ]; then
    test_result 0 "Database schema files exist"
else
    test_result 1 "Database schema files missing"
fi

# Test 9: Try assembling Android debug APK
echo "Test 9: Assembling Android APK (this may take a while)..."
if ./gradlew :composeApp:assembleDebug --quiet > /dev/null 2>&1; then
    test_result 0 "Android APK assembled"
    if [ -f "composeApp/build/outputs/apk/debug/composeApp-debug.apk" ]; then
        APK_SIZE=$(du -h "composeApp/build/outputs/apk/debug/composeApp-debug.apk" | cut -f1)
        echo "  APK size: $APK_SIZE"
    fi
else
    test_result 1 "Android APK assembly failed"
fi

# Summary
echo ""
echo "=========================="
echo "📊 Test Summary"
echo "=========================="
echo -e "Passed: ${GREEN}$PASSED${NC}"
echo -e "Failed: ${RED}$FAILED${NC}"
TOTAL=$((PASSED + FAILED))
echo "Total:  $TOTAL"

if [ $FAILED -eq 0 ]; then
    echo ""
    echo -e "${GREEN}🎉 All tests passed! Ready for manual testing.${NC}"
    echo ""
    echo "Next steps:"
    echo "1. Install APK on Android device:"
    echo "   adb install -r composeApp/build/outputs/apk/debug/composeApp-debug.apk"
    echo ""
    echo "2. Run on Desktop:"
    echo "   ./gradlew :composeApp:run"
    echo ""
    echo "3. Open TESTING_CHECKLIST.csv in a spreadsheet app"
    echo ""
    exit 0
else
    echo ""
    echo -e "${RED}❌ Some tests failed. Please fix issues before testing.${NC}"
    echo ""
    echo "Check the errors above and:"
    echo "1. Ensure all dependencies are installed"
    echo "2. Run './gradlew clean build' to see detailed errors"
    echo "3. Check that all required files exist"
    echo ""
    exit 1
fi
