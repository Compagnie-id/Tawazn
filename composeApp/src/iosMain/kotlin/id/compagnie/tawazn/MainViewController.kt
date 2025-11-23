package id.compagnie.tawazn

import androidx.compose.ui.window.ComposeUIViewController
import id.compagnie.tawazn.core.notification.IosNotificationConfig
import id.compagnie.tawazn.core.notification.NotificationManager

fun MainViewController() = ComposeUIViewController {
    // Initialize notification system
    NotificationManager.initialize(
        configuration = IosNotificationConfig.create(
            showPushNotification = true,
            askNotificationPermissionOnStart = true
        )
    )

    App()
}