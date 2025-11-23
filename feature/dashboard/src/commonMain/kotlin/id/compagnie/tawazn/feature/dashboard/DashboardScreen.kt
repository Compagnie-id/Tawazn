package id.compagnie.tawazn.feature.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import id.compagnie.tawazn.design.component.GlassCard
import id.compagnie.tawazn.design.component.GradientButton
import id.compagnie.tawazn.design.component.StatsCard
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Bold
import com.adamglin.phosphoricons.bold.Bell
import com.adamglin.phosphoricons.bold.Clock
import com.adamglin.phosphoricons.bold.Prohibit
import com.adamglin.phosphoricons.bold.SquaresFour
import com.adamglin.phosphoricons.bold.Fire
import com.adamglin.phosphoricons.bold.TrendDown
import id.compagnie.tawazn.design.theme.TawaznTheme
import id.compagnie.tawazn.i18n.stringResource
import id.compagnie.tawazn.domain.repository.UserProfileRepository
import kotlinx.coroutines.flow.firstOrNull
import org.koin.compose.koinInject

/**
 * Navigation callbacks for Dashboard screen
 * This eliminates the need for feature-to-feature dependencies
 */
data class DashboardNavigation(
    val onBlockAppsClick: () -> Unit = {},
    val onViewUsageClick: () -> Unit = {},
    val onManageSessionsClick: () -> Unit = {}
)

val LocalDashboardNavigation = compositionLocalOf { DashboardNavigation() }

class DashboardScreen : Screen {

    @Composable
    override fun Content() {
        DashboardContent()
    }
}

@Composable
fun DashboardContent() {
    val navigation = LocalDashboardNavigation.current
    val userProfileRepository: UserProfileRepository = koinInject()
    val screenModel = koinScreenModel<DashboardScreenModel>()

    val uiState by screenModel.uiState.collectAsState()
    var userName by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        val profile = userProfileRepository.getUserProfile().firstOrNull()
        userName = profile?.name
    }

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
                                text = stringResource("dashboard.title"),
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = if (userName != null) "Welcome back, $userName! 👋" else stringResource("dashboard.welcome"),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Icon(
                            imageVector = PhosphorIcons.Bold.Bell,
                            contentDescription = stringResource("dashboard.title"),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                item {
                    Text(
                        text = stringResource("dashboard.today_overview"),
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
                            title = stringResource("dashboard.screen_time"),
                            value = if (uiState.isLoading) "..." else uiState.screenTimeToday.toHoursMinutesString(),
                            subtitle = if (uiState.isLoading) "" else uiState.screenTimeChange,
                            icon = PhosphorIcons.Bold.Clock,
                            modifier = Modifier.weight(1f)
                        )

                        StatsCard(
                            title = stringResource("dashboard.apps_blocked"),
                            value = if (uiState.isLoading) "..." else "${uiState.blockedAppsCount}",
                            subtitle = if (uiState.blockedAppsCount > 0) stringResource("dashboard.apps_blocked_status") else "No apps blocked",
                            icon = PhosphorIcons.Bold.Prohibit,
                            modifier = Modifier.weight(1f),
                            useGradient = false
                        )
                    }
                }

                item {
                    StatsCard(
                        title = stringResource("dashboard.most_used_today"),
                        value = if (uiState.isLoading) "..." else (uiState.mostUsedApp?.appName ?: "No data"),
                        subtitle = if (uiState.isLoading) "" else (uiState.mostUsedApp?.totalTime?.toHoursMinutesString() ?: ""),
                        icon = PhosphorIcons.Bold.SquaresFour,
                        useGradient = true
                    )
                }

                item {
                    Text(
                        text = stringResource("dashboard.quick_actions"),
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
                            icon = PhosphorIcons.Bold.Prohibit,
                            title = stringResource("dashboard.action.block_apps"),
                            onClick = navigation.onBlockAppsClick,
                            modifier = Modifier.weight(1f)
                        )

                        QuickActionCard(
                            icon = PhosphorIcons.Bold.Clock,
                            title = stringResource("dashboard.action.view_usage"),
                            onClick = navigation.onViewUsageClick,
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
                                    imageVector = PhosphorIcons.Bold.Fire,
                                    contentDescription = stringResource("dashboard.focus_mode.title"),
                                    tint = TawaznTheme.colors.warning,
                                    modifier = Modifier.size(32.dp)
                                )

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = stringResource("dashboard.focus_mode.title"),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.SemiBold
                                    )

                                    Text(
                                        text = stringResource("dashboard.focus_mode.description"),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            GradientButton(
                                text = stringResource("dashboard.action.manage_sessions"),
                                onClick = navigation.onManageSessionsClick,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                item {
                    Text(
                        text = stringResource("dashboard.weekly_insights"),
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
                                    text = stringResource("dashboard.weekly_report"),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )

                                Icon(
                                    imageVector = PhosphorIcons.Bold.TrendDown,
                                    contentDescription = stringResource("dashboard.weekly_report"),
                                    tint = TawaznTheme.colors.success
                                )
                            }

                            Text(
                                text = stringResource("dashboard.weekly_average"),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Text(
                                text = stringResource("dashboard.doing_great"),
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
