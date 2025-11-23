package id.compagnie.tawazn.feature.onboarding

import android.content.Context
import id.compagnie.tawazn.core.notification.NotificationPermissionHelper

/**
 * Android-specific notification permission checker
 */
class AndroidNotificationPermissionManager(private val context: Context) {
    fun hasNotificationPermission(): Boolean {
        return NotificationPermissionHelper.isNotificationPermissionGranted(context)
    }

    fun isPermissionRequired(): Boolean {
        return NotificationPermissionHelper.isPermissionRequired()
    }
}
