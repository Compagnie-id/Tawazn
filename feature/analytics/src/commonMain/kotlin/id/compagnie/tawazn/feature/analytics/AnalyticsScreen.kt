package id.compagnie.tawazn.feature.analytics

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import id.compagnie.tawazn.design.component.GlassCard
import id.compagnie.tawazn.design.component.StatsCard
import id.compagnie.tawazn.design.icons.TawaznIcons
import id.compagnie.tawazn.design.theme.TawaznTheme
import id.compagnie.tawazn.feature.settings.FocusSessionListScreen

class AnalyticsScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel = getScreenModel<AnalyticsScreenModel>()
        AnalyticsContent(screenModel)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsContent(screenModel: AnalyticsScreenModel) {
    val navigator = LocalNavigator.current
    val uiState by screenModel.uiState.collectAsState()

    // Remember navigation callback to prevent unnecessary recompositions
    val onManageSessionsClick = remember { { navigator?.push(FocusSessionListScreen()) } }

    // Compute derived values efficiently
    val progressPercent = remember(uiState.goalProgress) {
        derivedStateOf { (uiState.goalProgress * 100).toInt() }
    }.value

    val goalHours = remember(uiState.dailyGoal) {
        derivedStateOf { uiState.dailyGoal / 60f }
    }.value

    TawaznTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Analytics & Insights") },
                    navigationIcon = {
                        if (navigator?.canPop == true) {
                            IconButton(onClick = { navigator.pop() }) {
                                Icon(TawaznIcons.ArrowBack, "Back")
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.9f)
                    )
                )
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Weekly Summary
                item {
                    Text(
                        text = "This Week",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                }

                item {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            StatsCard(
                                title = "Avg Daily",
                                value = uiState.averageDailyTime.toHoursMinutesString(),
                                subtitle = "This week",
                                modifier = Modifier.weight(1f),
                                useGradient = true
                            )

                            StatsCard(
                                title = "Best Day",
                                value = uiState.bestDay?.second?.toHoursMinutesString() ?: "No data",
                                subtitle = uiState.bestDay?.first ?: "",
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                // Progress Section
                item {
                    Text(
                        text = "Progress",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                item {
                    GlassCard(useGradient = true) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Today's Goal Progress",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Icon(
                                    imageVector = if (uiState.goalProgress < 0.8f) TawaznIcons.TrendingDown else TawaznIcons.TrendingUp,
                                    contentDescription = "Trending",
                                    tint = if (uiState.goalProgress < 0.8f) TawaznTheme.colors.success else TawaznTheme.colors.warning
                                )
                            }

                            LinearProgressIndicator(
                                progress = { uiState.goalProgress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp),
                                color = if (uiState.goalProgress < 0.8f) TawaznTheme.colors.success else TawaznTheme.colors.warning,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                            )

                            Text(
                                text = "$progressPercent% towards your daily goal of ${formatGoalHours(goalHours)} (${uiState.todayUsage.toHoursMinutesString()} used)",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // Streak Section
                item {
                    GlassCard {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = TawaznIcons.LocalFireDepartment,
                                    contentDescription = "Streak",
                                    tint = TawaznTheme.colors.warning,
                                    modifier = Modifier.size(40.dp)
                                )

                                Column {
                                    Text(
                                        text = if (uiState.currentStreak > 0) "${uiState.currentStreak} Day Streak" else "No Active Streak",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = if (uiState.currentStreak > 0) "Keep it up! Best: ${uiState.longestStreak} days" else "Start by meeting your daily goal",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            if (uiState.currentStreak > 0) {
                                Text(
                                    text = "🔥",
                                    style = MaterialTheme.typography.displaySmall
                                )
                            }
                        }
                    }
                }

                // Insights Section
                item {
                    Text(
                        text = "Insights",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                item {
                    val bestDay = uiState.bestDay
                    if (bestDay != null) {
                        InsightCard(
                            icon = TawaznIcons.TrendingUp,
                            title = "Most Productive Day",
                            description = "${bestDay.first} - Only ${bestDay.second.toHoursMinutesString()} screen time",
                            color = TawaznTheme.colors.success
                        )
                    }
                }

                item {
                    val weeklyStats = uiState.weeklyStats
                    if (weeklyStats != null && weeklyStats.totalScreenTime.inWholeMinutes > 0) {
                        InsightCard(
                            icon = TawaznIcons.Schedule,
                            title = "Weekly Total",
                            description = "${weeklyStats.totalScreenTime.toHoursMinutesString()} total screen time this week",
                            color = TawaznTheme.colors.info
                        )
                    }
                }

                item {
                    val topDistraction = uiState.topDistraction
                    if (topDistraction != null) {
                        val avgDaily = topDistraction.totalTime.inWholeMinutes / 7
                        InsightCard(
                            icon = TawaznIcons.Apps,
                            title = "Top App",
                            description = "${topDistraction.appName} - ${avgDaily}m average daily",
                            color = TawaznTheme.colors.warning
                        )
                    }
                }

                // Recommendations
                item {
                    Text(
                        text = "Recommendations",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                item {
                    GlassCard(useGradient = true) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "Focus Session Suggestion",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )

                            Text(
                                text = "Based on your usage patterns, we recommend creating a focus session from 8-10 PM to reduce evening screen time.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Button(
                                onClick = onManageSessionsClick,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = TawaznTheme.colors.gradientMiddle
                                )
                            ) {
                                Text("Manage Sessions")
                            }
                        }
                    }
                }

                // Achievements
                item {
                    Text(
                        text = "Achievements",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        AchievementBadge(
                            emoji = "🎯",
                            title = "Goal Getter",
                            description = "Met daily goal 5x",
                            modifier = Modifier.weight(1f)
                        )

                        AchievementBadge(
                            emoji = "⚡",
                            title = "Speed Demon",
                            description = "1 week streak",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun InsightCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String,
    color: androidx.compose.ui.graphics.Color
) {
    GlassCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(32.dp)
            )

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun AchievementBadge(
    emoji: String,
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    GlassCard(modifier = modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = emoji,
                style = MaterialTheme.typography.displayMedium
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

fun formatGoalHours(hours: Float): String {
    val wholeHours = hours.toInt()
    val minutes = ((hours - wholeHours) * 60).toInt()

    return when {
        wholeHours > 0 && minutes > 0 -> "${wholeHours}h ${minutes}m"
        wholeHours > 0 -> "${wholeHours}h"
        minutes > 0 -> "${minutes}m"
        else -> "0m"
    }
}
