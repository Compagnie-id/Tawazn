package id.compagnie.tawazn.core.notification

import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration

/**
 * iOS-specific notification configuration.
 */
object IosNotificationConfig {
    /**
     * Creates iOS notification configuration.
     *
     * @param showPushNotification Whether to show push notifications
     * @param askNotificationPermissionOnStart Whether to request permission on app start
     * @return iOS notification platform configuration
     */
    fun create(
        showPushNotification: Boolean = true,
        askNotificationPermissionOnStart: Boolean = true
    ): NotificationPlatformConfiguration {
        return NotificationPlatformConfiguration.Ios(
            showPushNotification = showPushNotification,
            askNotificationPermissionOnStart = askNotificationPermissionOnStart
        )
    }
}
