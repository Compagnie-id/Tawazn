package id.compagnie.tawazn

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import id.compagnie.tawazn.core.notification.AndroidNotificationConfig
import id.compagnie.tawazn.core.notification.NotificationManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize notification system
        NotificationManager.initialize(
            configuration = AndroidNotificationConfig.create(
                context = applicationContext,
                notificationIconResId = null // TODO: Add notification icon resource
            )
        )

        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}