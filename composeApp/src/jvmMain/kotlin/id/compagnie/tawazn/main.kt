package id.compagnie.tawazn

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import id.compagnie.tawazn.core.notification.DesktopNotificationConfig
import id.compagnie.tawazn.core.notification.NotificationManager

fun main() = application {
    // Initialize notification system
    NotificationManager.initialize(
        configuration = DesktopNotificationConfig.create(
            showPushNotification = false, // Desktop only supports local notifications
            notificationIconPath = null // TODO: Add notification icon path
        )
    )

    Window(
        onCloseRequest = ::exitApplication,
        title = "Tawazn",
    ) {
        App()
    }
}