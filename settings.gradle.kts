rootProject.name = "Tawazn"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

include(":composeApp")

// Core Modules
include(":core:common")
include(":core:design-system")
include(":core:database")
include(":core:datastore")
include(":core:network")

// Domain Layer
include(":domain")

// Data Layer
include(":data")

// Feature Modules
include(":feature:dashboard")
include(":feature:app-blocking")
include(":feature:usage-tracking")
include(":feature:analytics")
include(":feature:settings")
include(":feature:onboarding")

// Platform-Specific
include(":platform:android")
include(":platform:ios")
include(":platform:desktop")