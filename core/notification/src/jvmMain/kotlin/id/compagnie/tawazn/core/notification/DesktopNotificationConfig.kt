package id.compagnie.tawazn.core.notification

import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import java.io.File

/**
 * Desktop-specific notification configuration.
 */
object DesktopNotificationConfig {
    /**
     * Creates Desktop notification configuration.
     *
     * @param showPushNotification Whether to show push notifications (only local notifications supported)
     * @param notificationIconPath Path to the notification icon resource
     * @return Desktop notification platform configuration
     */
    fun create(
        showPushNotification: Boolean = false,
        notificationIconPath: String? = null
    ): NotificationPlatformConfiguration {
        return NotificationPlatformConfiguration.Desktop(
            showPushNotification = showPushNotification,
            notificationIconPath = notificationIconPath
        )
    }

    /**
     * Gets the default notification icon path for desktop platforms.
     *
     * @return Path to the notification icon in resources
     */
    fun getDefaultIconPath(): String {
        // This assumes notification icon is placed in resources folder
        return System.getProperty("compose.application.resources.dir")?.let { resourcesDir ->
            resourcesDir + File.separator + "ic_notification.png"
        } ?: "ic_notification.png"
    }
}
