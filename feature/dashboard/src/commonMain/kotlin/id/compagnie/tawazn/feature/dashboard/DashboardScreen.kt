package id.compagnie.tawazn.feature.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import id.compagnie.tawazn.design.component.GlassCard
import id.compagnie.tawazn.design.component.GradientButton
import id.compagnie.tawazn.design.component.StatsCard
import id.compagnie.tawazn.design.icons.TawaznIcons
import id.compagnie.tawazn.design.theme.TawaznTheme
import id.compagnie.tawazn.feature.appblocking.AppBlockingScreen
import id.compagnie.tawazn.feature.settings.FocusSessionListScreen
import id.compagnie.tawazn.feature.usagetracking.UsageTrackingScreen

class DashboardScreen : Screen {

    @Composable
    override fun Content() {
        DashboardContent()
    }
}

@Composable
fun DashboardContent() {
    val navigator = LocalNavigator.current

    // Remember navigation callbacks to prevent unnecessary recompositions
    val onBlockAppsClick = remember { { navigator?.push(AppBlockingScreen()); Unit } }
    val onViewUsageClick = remember { { navigator?.push(UsageTrackingScreen()); Unit } }
    val onManageSessionsClick = remember { { navigator?.push(FocusSessionListScreen()); Unit } }

    TawaznTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Dashboard",
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = "Welcome back! 👋",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Icon(
                            imageVector = TawaznIcons.Notifications,
                            contentDescription = "Notifications",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                item {
                    Text(
                        text = "Today's Overview",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatsCard(
                            title = "Screen Time",
                            value = "2h 34m",
                            subtitle = "↓ 15% from yesterday",
                            icon = TawaznIcons.AccessTime,
                            modifier = Modifier.weight(1f)
                        )

                        StatsCard(
                            title = "Apps Blocked",
                            value = "12",
                            subtitle = "Active now",
                            icon = TawaznIcons.Block,
                            modifier = Modifier.weight(1f),
                            useGradient = false
                        )
                    }
                }

                item {
                    StatsCard(
                        title = "Most Used Today",
                        value = "Instagram",
                        subtitle = "45 minutes",
                        icon = TawaznIcons.Apps,
                        useGradient = true
                    )
                }

                item {
                    Text(
                        text = "Quick Actions",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        QuickActionCard(
                            icon = TawaznIcons.Block,
                            title = "Block Apps",
                            onClick = onBlockAppsClick,
                            modifier = Modifier.weight(1f)
                        )

                        QuickActionCard(
                            icon = TawaznIcons.AccessTime,
                            title = "View Usage",
                            onClick = onViewUsageClick,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                item {
                    GlassCard(useGradient = true) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = TawaznIcons.LocalFireDepartment,
                                    contentDescription = "Focus",
                                    tint = TawaznTheme.colors.warning,
                                    modifier = Modifier.size(32.dp)
                                )

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Start Focus Mode",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.SemiBold
                                    )

                                    Text(
                                        text = "Block distracting apps and stay productive",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            GradientButton(
                                text = "Manage Sessions",
                                onClick = onManageSessionsClick,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                item {
                    Text(
                        text = "Weekly Insights",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                item {
                    GlassCard {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Weekly Report",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )

                                Icon(
                                    imageVector = TawaznIcons.TrendingDown,
                                    contentDescription = "Trending",
                                    tint = TawaznTheme.colors.success
                                )
                            }

                            Text(
                                text = "Average: 3h 12m per day",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Text(
                                text = "You're doing great! 📈",
                                style = MaterialTheme.typography.bodySmall,
                                color = TawaznTheme.colors.success,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
fun QuickActionCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    GlassCard(
        modifier = modifier,
        borderWidth = 1.5.dp
    ) {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = androidx.compose.ui.graphics.Color.Transparent
            ),
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = TawaznTheme.colors.gradientMiddle,
                    modifier = Modifier.size(32.dp)
                )

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
