package id.compagnie.tawazn.core.notification

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/**
 * Helper class for managing notification permissions on Android.
 * Handles runtime permission requests for Android 13+ (API 33+).
 */
object NotificationPermissionHelper {

    const val NOTIFICATION_PERMISSION_REQUEST_CODE = 1001

    /**
     * Checks if notification permission is granted.
     *
     * @param context Application context
     * @return true if permission is granted or not required (Android 12-), false otherwise
     */
    fun isNotificationPermissionGranted(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            // Permission not required for Android 12 and below
            true
        }
    }

    /**
     * Checks if we should show permission rationale to the user.
     *
     * @param activity Activity context
     * @return true if rationale should be shown
     */
    fun shouldShowPermissionRationale(activity: Activity): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.POST_NOTIFICATIONS
            )
        } else {
            false
        }
    }

    /**
     * Requests notification permission from the user.
     * Only requests on Android 13+ (API 33+).
     *
     * @param activity Activity context
     */
    fun requestNotificationPermission(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                NOTIFICATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    /**
     * Checks if notification permission is required for the current Android version.
     *
     * @return true if permission is required (Android 13+), false otherwise
     */
    fun isPermissionRequired(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
    }

    /**
     * Handles the result of permission request.
     *
     * @param requestCode Request code from onRequestPermissionsResult
     * @param grantResults Grant results array
     * @return true if notification permission was granted, false otherwise
     */
    fun handlePermissionResult(
        requestCode: Int,
        grantResults: IntArray
    ): Boolean {
        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            return grantResults.isNotEmpty() &&
                   grantResults[0] == PackageManager.PERMISSION_GRANTED
        }
        return false
    }
}
