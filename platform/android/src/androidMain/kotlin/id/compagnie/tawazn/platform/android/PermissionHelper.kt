package id.compagnie.tawazn.platform.android

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.os.Process
import android.provider.Settings

/**
 * Helper class for managing app permissions on Android
 */
class PermissionHelper(private val context: Context) {

    /**
     * Check if usage stats permission is granted
     */
    fun hasUsageStatsPermission(): Boolean {
        return try {
            val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as? AppOpsManager
            val mode = appOps?.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                Process.myUid(),
                context.packageName
            )
            mode == AppOpsManager.MODE_ALLOWED
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Open usage stats settings to request permission
     */
    fun requestUsageStatsPermission() {
        try {
            val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: Exception) {
            // Fallback to general settings
            openSettings()
        }
    }

    /**
     * Check if accessibility service is enabled
     * Note: This requires an AccessibilityService to be implemented
     */
    fun hasAccessibilityPermission(): Boolean {
        // Check if our accessibility service is enabled
        val enabledServices = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        )
        return enabledServices?.contains(context.packageName) == true
    }

    /**
     * Open accessibility settings
     */
    fun requestAccessibilityPermission() {
        try {
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: Exception) {
            openSettings()
        }
    }

    /**
     * Check if overlay permission is granted (for blocking overlays)
     */
    fun hasOverlayPermission(): Boolean {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(context)
        } else {
            true
        }
    }

    /**
     * Request overlay permission
     */
    fun requestOverlayPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            try {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    android.net.Uri.parse("package:${context.packageName}")
                )
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            } catch (e: Exception) {
                openSettings()
            }
        }
    }

    /**
     * Open app settings
     */
    fun openSettings() {
        try {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.data = android.net.Uri.parse("package:${context.packageName}")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: Exception) {
            // Ignore if settings can't be opened
        }
    }

    /**
     * Get all required permissions status
     */
    fun getPermissionStatus(): PermissionStatus {
        return PermissionStatus(
            hasUsageStats = hasUsageStatsPermission(),
            hasAccessibility = hasAccessibilityPermission(),
            hasOverlay = hasOverlayPermission()
        )
    }
}

data class PermissionStatus(
    val hasUsageStats: Boolean,
    val hasAccessibility: Boolean,
    val hasOverlay: Boolean
) {
    val allGranted: Boolean
        get() = hasUsageStats && hasAccessibility && hasOverlay

    val usageTrackingReady: Boolean
        get() = hasUsageStats

    val blockingReady: Boolean
        get() = hasAccessibility || hasOverlay
}
