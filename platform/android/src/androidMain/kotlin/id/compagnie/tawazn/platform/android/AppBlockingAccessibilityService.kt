package id.compagnie.tawazn.platform.android

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Intent
import android.view.accessibility.AccessibilityEvent
import co.touchlab.kermit.Logger

/**
 * Accessibility Service for blocking apps
 *
 * This service monitors when apps are launched and can block them by:
 * 1. Showing a blocking overlay
 * 2. Returning to home screen
 * 3. Launching a different app
 *
 * Required permissions in AndroidManifest.xml:
 * - BIND_ACCESSIBILITY_SERVICE
 *
 * Required configuration: res/xml/accessibility_service_config.xml
 */
class AppBlockingAccessibilityService : AccessibilityService() {

    private val logger = Logger.withTag("AppBlockingService")
    private val blockedApps = mutableSetOf<String>()

    override fun onServiceConnected() {
        super.onServiceConnected()
        logger.i { "Accessibility service connected" }

        val info = AccessibilityServiceInfo().apply {
            // Monitor window state changes (when apps open)
            eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED

            // Monitor all apps
            packageNames = null

            // Don't need to analyze content
            flags = AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS

            // Update notifications every 100ms
            notificationTimeout = 100

            feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
        }

        serviceInfo = info
        loadBlockedApps()
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val packageName = event.packageName?.toString() ?: return

            // Ignore system UI and our own app
            if (packageName.contains("systemui") ||
                packageName == this.packageName) {
                return
            }

            if (blockedApps.contains(packageName)) {
                logger.i { "Blocked app detected: $packageName" }
                blockApp(packageName)
            }
        }
    }

    override fun onInterrupt() {
        logger.w { "Accessibility service interrupted" }
    }

    /**
     * Block an app by returning to home screen
     */
    private fun blockApp(packageName: String) {
        try {
            // Method 1: Go to home screen
            val homeIntent = Intent(Intent.ACTION_MAIN).apply {
                addCategory(Intent.CATEGORY_HOME)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(homeIntent)

            // Method 2: Show blocking overlay (requires overlay permission)
            // showBlockingOverlay(packageName)

            logger.i { "Blocked app: $packageName" }
        } catch (e: Exception) {
            logger.e(e) { "Failed to block app: $packageName" }
        }
    }

    /**
     * Load blocked apps from database/preferences
     * In real implementation, this would query the repository
     */
    private fun loadBlockedApps() {
        // TODO: Load from repository
        // For now, using shared preferences as example
        val prefs = getSharedPreferences("tawazn_blocked_apps", MODE_PRIVATE)
        val blockedList = prefs.getStringSet("blocked_packages", emptySet()) ?: emptySet()
        blockedApps.clear()
        blockedApps.addAll(blockedList)

        logger.i { "Loaded ${blockedApps.size} blocked apps" }
    }

    /**
     * Update blocked apps list
     */
    fun updateBlockedApps(packages: Set<String>) {
        blockedApps.clear()
        blockedApps.addAll(packages)

        // Save to preferences
        val prefs = getSharedPreferences("tawazn_blocked_apps", MODE_PRIVATE)
        prefs.edit().putStringSet("blocked_packages", packages).apply()

        logger.i { "Updated blocked apps: ${blockedApps.size}" }
    }

    companion object {
        /**
         * Check if the accessibility service is enabled
         */
        fun isEnabled(context: android.content.Context): Boolean {
            val enabledServices = android.provider.Settings.Secure.getString(
                context.contentResolver,
                android.provider.Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            )
            return enabledServices?.contains(context.packageName) == true
        }
    }
}

/**
 * XML configuration for accessibility service
 * Should be placed in res/xml/accessibility_service_config.xml:
 *
 * <?xml version="1.0" encoding="utf-8"?>
 * <accessibility-service xmlns:android="http://schemas.android.com/apk/res/android"
 *     android:accessibilityEventTypes="typeWindowStateChanged"
 *     android:accessibilityFeedbackType="feedbackGeneric"
 *     android:accessibilityFlags="flagIncludeNotImportantViews"
 *     android:canRetrieveWindowContent="false"
 *     android:description="@string/accessibility_service_description"
 *     android:notificationTimeout="100"
 *     android:settingsActivity="id.compagnie.tawazn.MainActivity" />
 *
 *
 * And in AndroidManifest.xml:
 *
 * <service
 *     android:name=".platform.android.AppBlockingAccessibilityService"
 *     android:permission="android.permission.BIND_ACCESSIBILITY_SERVICE"
 *     android:exported="true">
 *     <intent-filter>
 *         <action android:name="android.accessibilityservice.AccessibilityService" />
 *     </intent-filter>
 *     <meta-data
 *         android:name="android.accessibilityservice"
 *         android:resource="@xml/accessibility_service_config" />
 * </service>
 */
