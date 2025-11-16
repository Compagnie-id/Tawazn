#!/bin/bash

# iOS Build and Run Script
# This script builds the Kotlin framework and opens the Xcode project

set -e  # Exit on error

echo "🚀 Building Tawazn iOS App"
echo ""

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Step 1: Build the Kotlin framework
echo -e "${BLUE}Step 1: Building Kotlin Multiplatform framework...${NC}"
cd "$(dirname "$0")/.."

if ./gradlew :composeApp:embedAndSignAppleFrameworkForXcode; then
    echo -e "${GREEN}✅ Kotlin framework built successfully${NC}"
else
    echo -e "${RED}❌ Failed to build Kotlin framework${NC}"
    echo ""
    echo "If you see network errors, make sure you have internet connection."
    echo "If you see other errors, check the Gradle output above."
    exit 1
fi

echo ""

# Step 2: Check if framework was created
FRAMEWORK_PATH="composeApp/build/compose/ios/framework"
if [ -d "$FRAMEWORK_PATH" ]; then
    echo -e "${GREEN}✅ Framework found at: $FRAMEWORK_PATH${NC}"
else
    echo -e "${RED}❌ Framework not found at expected location${NC}"
    echo "Expected: $FRAMEWORK_PATH"
    exit 1
fi

echo ""

# Step 3: Open Xcode project
echo -e "${BLUE}Step 2: Opening Xcode project...${NC}"
cd iosApp

if [ -f "iosApp.xcodeproj/project.pbxproj" ]; then
    open iosApp.xcodeproj
    echo -e "${GREEN}✅ Xcode project opened${NC}"
    echo ""
    echo "📱 Next steps in Xcode:"
    echo "   1. Select a simulator (iPhone 15, iOS 15.0+)"
    echo "   2. Press Cmd+B to build"
    echo "   3. Press Cmd+R to run"
    echo ""
    echo "   Note: Screen Time features require iOS 15.0 or higher"
else
    echo -e "${RED}❌ Xcode project not found${NC}"
    exit 1
fi
