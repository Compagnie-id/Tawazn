package id.compagnie.tawazn.core.notification

import co.touchlab.kermit.Logger
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.random.Random

/**
 * Central notification manager for Tawazn app.
 * Provides a unified API for sending local and push notifications across all platforms.
 */
class NotificationManager {
    private val logger = Logger.withTag("NotificationManager")
    private val localNotifier = NotifierManager.getLocalNotifier()
    private val pushNotifier = NotifierManager.getPushNotifier()

    private val _fcmToken = MutableStateFlow<String?>(null)
    val fcmToken: StateFlow<String?> = _fcmToken.asStateFlow()

    init {
        setupPushNotificationListeners()
    }

    private fun setupPushNotificationListeners() {
        pushNotifier?.onNewToken = { token ->
            logger.d { "New FCM token received: $token" }
            _fcmToken.value = token
        }

        pushNotifier?.onPushNotification = { title, body ->
            logger.i { "Push notification received - Title: $title, Body: $body" }
            // Handle push notification received
        }

        pushNotifier?.onPayloadData = { data ->
            logger.d { "Push notification payload: $data" }
            // Handle custom payload data
        }
    }

    /**
     * Sends a local notification with the specified details.
     *
     * @param title Notification title
     * @param body Notification body/message
     * @param notificationId Optional notification ID (auto-generated if not provided)
     */
    fun sendLocalNotification(
        title: String,
        body: String,
        notificationId: Int = Random.nextInt(0, Int.MAX_VALUE)
    ) {
        try {
            localNotifier.notify(notificationId) {
                this.title = title
                this.body = body
            }
            logger.i { "Local notification sent - ID: $notificationId, Title: $title" }
        } catch (e: Exception) {
            logger.e(e) { "Failed to send local notification" }
        }
    }

    /**
     * Sends a usage limit notification when app usage exceeds the limit.
     *
     * @param appName Name of the app that exceeded the limit
     * @param usageMinutes Total usage time in minutes
     * @param limitMinutes Configured limit in minutes
     */
    fun sendUsageLimitNotification(
        appName: String,
        usageMinutes: Int,
        limitMinutes: Int
    ) {
        sendLocalNotification(
            title = "Usage Limit Reached ⚠️",
            body = "You've used $appName for $usageMinutes/$limitMinutes minutes today"
        )
    }

    /**
     * Sends a break reminder notification.
     *
     * @param message Optional custom message
     */
    fun sendBreakReminder(message: String = "Time to rest your eyes and stretch") {
        sendLocalNotification(
            title = "Take a Break 🌟",
            body = message,
            notificationId = BREAK_REMINDER_ID
        )
    }

    /**
     * Sends a notification when a blocking session starts.
     *
     * @param sessionName Name of the focus session
     * @param durationMinutes Session duration in minutes
     */
    fun sendBlockingSessionStarted(
        sessionName: String,
        durationMinutes: Int
    ) {
        sendLocalNotification(
            title = "Focus Session Started 🎯",
            body = "$sessionName is now active for $durationMinutes minutes"
        )
    }

    /**
     * Sends a notification when a blocking session ends.
     *
     * @param sessionName Name of the focus session
     */
    fun sendBlockingSessionEnded(sessionName: String) {
        sendLocalNotification(
            title = "Focus Session Complete ✅",
            body = "$sessionName has ended. Great job staying focused!"
        )
    }

    /**
     * Sends a daily usage summary notification.
     *
     * @param totalMinutes Total screen time in minutes
     * @param topApp Most used app name
     * @param topAppMinutes Most used app time in minutes
     */
    fun sendDailySummary(
        totalMinutes: Int,
        topApp: String,
        topAppMinutes: Int
    ) {
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60
        val timeString = if (hours > 0) "${hours}h ${minutes}m" else "${minutes}m"

        sendLocalNotification(
            title = "Daily Screen Time Summary 📊",
            body = "Total: $timeString | Most used: $topApp ($topAppMinutes min)",
            notificationId = DAILY_SUMMARY_ID
        )
    }

    /**
     * Sends a goal achievement notification.
     *
     * @param achievementDescription Description of the achievement
     */
    fun sendGoalAchievement(achievementDescription: String) {
        sendLocalNotification(
            title = "Goal Achieved! 🎉",
            body = achievementDescription
        )
    }

    /**
     * Sends a notification when attempting to open a blocked app.
     *
     * @param appName Name of the blocked app
     * @param reason Reason why the app is blocked
     */
    fun sendBlockedAppAttempt(
        appName: String,
        reason: String = "This app is currently blocked"
    ) {
        sendLocalNotification(
            title = "App Blocked 🚫",
            body = "$appName - $reason"
        )
    }

    companion object {
        private const val BREAK_REMINDER_ID = 1001
        private const val DAILY_SUMMARY_ID = 1002

        /**
         * Initializes the notification system with platform-specific configuration.
         * Must be called at app startup before using any notification features.
         *
         * @param configuration Platform-specific notification configuration
         */
        fun initialize(configuration: NotificationPlatformConfiguration) {
            NotifierManager.initialize(configuration)
        }
    }
}
