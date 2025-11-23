package id.compagnie.tawazn.core.notification

import android.content.Context
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration

/**
 * Android-specific notification configuration.
 */
object AndroidNotificationConfig {
    /**
     * Creates Android notification configuration.
     *
     * @param context Application context
     * @param notificationIconResId Resource ID for the notification icon
     * @return Android notification platform configuration
     */
    fun create(
        context: Context,
        notificationIconResId: Int
    ): NotificationPlatformConfiguration {
        return NotificationPlatformConfiguration.Android(
            notificationIconResId = notificationIconResId
        )
    }
}
