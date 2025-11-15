package id.compagnie.tawazn.feature.analytics

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import id.compagnie.tawazn.design.component.GlassCard
import id.compagnie.tawazn.design.component.StatsCard
import id.compagnie.tawazn.design.theme.TawaznTheme

class AnalyticsScreen : Screen {

    @Composable
    override fun Content() {
        AnalyticsContent()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsContent() {
    val navigator = LocalNavigator.currentOrThrow

    TawaznTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Analytics & Insights") },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(Icons.Default.ArrowBack, "Back")
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatsCard(
                            title = "Avg Daily",
                            value = "3h 12m",
                            subtitle = "↓ 15% vs last week",
                            modifier = Modifier.weight(1f),
                            useGradient = true
                        )

                        StatsCard(
                            title = "Best Day",
                            value = "2h 8m",
                            subtitle = "Wednesday",
                            modifier = Modifier.weight(1f)
                        )
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
                                    text = "Goal Progress",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Icon(
                                    imageVector = Icons.Default.TrendingDown,
                                    contentDescription = "Trending",
                                    tint = TawaznTheme.colors.success
                                )
                            }

                            LinearProgressIndicator(
                                progress = { 0.65f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp),
                                color = TawaznTheme.colors.success,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                            )

                            Text(
                                text = "65% towards your daily goal of 3 hours",
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
                                    imageVector = Icons.Default.LocalFireDepartment,
                                    contentDescription = "Streak",
                                    tint = TawaznTheme.colors.warning,
                                    modifier = Modifier.size(40.dp)
                                )

                                Column {
                                    Text(
                                        text = "7 Day Streak",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = "Keep it up!",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Text(
                                text = "🔥",
                                style = MaterialTheme.typography.displaySmall
                            )
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
                    InsightCard(
                        icon = Icons.Default.TrendingUp,
                        title = "Most Productive Day",
                        description = "Wednesday - Only 2h 8m screen time",
                        color = TawaznTheme.colors.success
                    )
                }

                item {
                    InsightCard(
                        icon = Icons.Default.Schedule,
                        title = "Peak Usage Time",
                        description = "You use your phone most between 8-10 PM",
                        color = TawaznTheme.colors.info
                    )
                }

                item {
                    InsightCard(
                        icon = Icons.Default.Apps,
                        title = "Top Distraction",
                        description = "Instagram - 1h 15m average daily",
                        color = TawaznTheme.colors.warning
                    )
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
                                onClick = { /* TODO: Create session */ },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = TawaznTheme.colors.gradientMiddle
                                )
                            ) {
                                Text("Create Session")
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
