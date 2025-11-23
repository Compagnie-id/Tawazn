package id.compagnie.tawazn

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import id.compagnie.tawazn.core.notification.AndroidNotificationConfig
import id.compagnie.tawazn.core.notification.NotificationManager
import id.compagnie.tawazn.core.notification.NotificationPermissionHelper

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize notification system
        NotificationManager.initialize(
            configuration = AndroidNotificationConfig.create(
                context = applicationContext,
                notificationIconResId = R.drawable.ic_notification
            )
        )

        setContent {
            // Request notification permission on first launch
            LaunchedEffect(Unit) {
                if (NotificationPermissionHelper.isPermissionRequired() &&
                    !NotificationPermissionHelper.isNotificationPermissionGranted(applicationContext)) {
                    NotificationPermissionHelper.requestNotificationPermission(this@MainActivity)
                }
            }

            App()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            NotificationPermissionHelper.NOTIFICATION_PERMISSION_REQUEST_CODE -> {
                val granted = NotificationPermissionHelper.handlePermissionResult(
                    requestCode,
                    grantResults
                )
                // Permission result handled - app can now send notifications if granted
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}