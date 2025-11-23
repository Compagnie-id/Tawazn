package id.compagnie.tawazn.feature.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import id.compagnie.tawazn.core.notification.NotificationManager
import org.koin.compose.koinInject

/**
 * Test/Demo screen for notification functionality.
 * Allows testing all notification types to verify integration.
 *
 * Usage: Add this screen to Settings menu for development/testing
 */
class NotificationTestScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val notificationManager: NotificationManager = koinInject()

        NotificationTestContent(
            notificationManager = notificationManager,
            onBackClick = { navigator.pop() }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NotificationTestContent(
    notificationManager: NotificationManager,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notification Test") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Text("←") // Replace with actual back icon
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Test Notification Types",
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = "Click any button below to test notifications. Make sure notification permissions are granted.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Basic Notification
            NotificationTestCard(
                title = "Basic Notification",
                description = "Simple notification with title and body"
            ) {
                notificationManager.sendLocalNotification(
                    title = "Test Notification",
                    body = "This is a basic test notification from Tawazn"
                )
            }

            // Usage Limit Notification
            NotificationTestCard(
                title = "Usage Limit Alert",
                description = "Notification when app usage exceeds limit"
            ) {
                notificationManager.sendUsageLimitNotification(
                    appName = "Instagram",
                    usageMinutes = 65,
                    limitMinutes = 60
                )
            }

            // Break Reminder
            NotificationTestCard(
                title = "Break Reminder",
                description = "Reminder to take a break from screen"
            ) {
                notificationManager.sendBreakReminder()
            }

            // Break Reminder (Custom Message)
            NotificationTestCard(
                title = "Custom Break Reminder",
                description = "Break reminder with custom message"
            ) {
                notificationManager.sendBreakReminder(
                    message = "You've been focused for 30 minutes. Time to rest your eyes!"
                )
            }

            // Focus Session Started
            NotificationTestCard(
                title = "Focus Session Started",
                description = "Notification when blocking session begins"
            ) {
                notificationManager.sendBlockingSessionStarted(
                    sessionName = "Deep Work",
                    durationMinutes = 90
                )
            }

            // Focus Session Ended
            NotificationTestCard(
                title = "Focus Session Ended",
                description = "Notification when blocking session completes"
            ) {
                notificationManager.sendBlockingSessionEnded(
                    sessionName = "Deep Work"
                )
            }

            // Daily Summary
            NotificationTestCard(
                title = "Daily Summary",
                description = "End of day usage summary"
            ) {
                notificationManager.sendDailySummary(
                    totalMinutes = 245,
                    topApp = "Twitter",
                    topAppMinutes = 85
                )
            }

            // Goal Achievement
            NotificationTestCard(
                title = "Goal Achievement",
                description = "Celebrate reaching a goal"
            ) {
                notificationManager.sendGoalAchievement(
                    "You reduced screen time by 30% this week! 🎉"
                )
            }

            // Blocked App Attempt
            NotificationTestCard(
                title = "Blocked App Attempt",
                description = "Notification when user tries to open blocked app"
            ) {
                notificationManager.sendBlockedAppAttempt(
                    appName = "TikTok",
                    reason = "This app is blocked during your Focus Session"
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // FCM Token Display (for debugging)
            val fcmToken by notificationManager.fcmToken.collectAsState()
            if (!fcmToken.isNullOrEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "FCM Token (for push notifications):",
                            style = MaterialTheme.typography.labelMedium
                        )
                        Text(
                            text = fcmToken ?: "Not available",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NotificationTestCard(
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
