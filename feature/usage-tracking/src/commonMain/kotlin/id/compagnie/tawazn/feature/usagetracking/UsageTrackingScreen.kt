@file:OptIn(ExperimentalTime::class)

package id.compagnie.tawazn.feature.usagetracking

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Bold
import com.adamglin.phosphoricons.bold.ArrowLeft
import com.adamglin.phosphoricons.bold.ArrowsClockwise
import com.adamglin.phosphoricons.bold.Clock
import com.adamglin.phosphoricons.bold.SquaresFour
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import id.compagnie.tawazn.design.component.GlassCard
import id.compagnie.tawazn.design.component.StatsCard
import id.compagnie.tawazn.design.theme.TawaznTheme
import id.compagnie.tawazn.i18n.stringResource
import id.compagnie.tawazn.domain.model.AppUsageSummary
import id.compagnie.tawazn.domain.model.UsageStats
import id.compagnie.tawazn.domain.repository.UsageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.todayIn
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.ExperimentalTime

class UsageTrackingScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel = koinScreenModel<UsageTrackingScreenModel>()
        UsageTrackingContent(screenModel)
    }
}

class UsageTrackingScreenModel : ScreenModel, KoinComponent {
    private val usageRepository: UsageRepository by inject()

    private val _usageStats = MutableStateFlow<UsageStats?>(null)
    val usageStats = _usageStats.asStateFlow()

    private val _selectedPeriod = MutableStateFlow(UsagePeriod.TODAY)
    val selectedPeriod = _selectedPeriod.asStateFlow()

    init {
        loadUsageStats()
    }

    fun selectPeriod(period: UsagePeriod) {
        _selectedPeriod.value = period
        loadUsageStats()
    }

    fun loadUsageStats() {
        screenModelScope.launch {
            val today = kotlin.time.Clock.System.todayIn(TimeZone.currentSystemDefault())

            val startDate: kotlinx.datetime.LocalDate
            val endDate: kotlinx.datetime.LocalDate

            when (_selectedPeriod.value) {
                UsagePeriod.TODAY -> {
                    startDate = today
                    endDate = today
                }
                UsagePeriod.WEEK -> {
                    startDate = today.minus(7, kotlinx.datetime.DateTimeUnit.DAY)
                    endDate = today
                }
                UsagePeriod.MONTH -> {
                    startDate = today.minus(30, kotlinx.datetime.DateTimeUnit.DAY)
                    endDate = today
                }
            }

            try {
                val stats = usageRepository.getUsageStats(startDate, endDate)
                _usageStats.value = stats
            } catch (e: Exception) {
                // Mock data for demo
                _usageStats.value = UsageStats(
                    totalScreenTime = 3.hours + 24.minutes,
                    totalLaunches = 127,
                    topApps = listOf(
                        AppUsageSummary("com.instagram", "Instagram", 1.hours + 15.minutes, 32, 0.36f),
                        AppUsageSummary("com.twitter", "Twitter", 54.minutes, 28, 0.26f),
                        AppUsageSummary("com.chrome", "Chrome", 42.minutes, 18, 0.20f),
                        AppUsageSummary("com.spotify", "Spotify", 33.minutes, 12, 0.16f),
                    ),
                    dailyUsage = emptyList()
                )
            }
        }
    }
}

enum class UsagePeriod {
    TODAY, WEEK, MONTH
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsageTrackingContent(screenModel: UsageTrackingScreenModel) {
    val usageStats by screenModel.usageStats.collectAsState()
    val selectedPeriod by screenModel.selectedPeriod.collectAsState()
    val navigator = LocalNavigator.current

    Scaffold(
        topBar = {
                TopAppBar(
                    title = { Text(stringResource("usage_tracking.title")) },
                    navigationIcon = {
                        IconButton(onClick = { navigator?.pop() }) {
                            Icon(PhosphorIcons.Bold.ArrowLeft, stringResource("common.back"))
                        }
                    },
                    actions = {
                        IconButton(onClick = { /* Refresh */ screenModel.loadUsageStats() }) {
                            Icon(PhosphorIcons.Bold.ArrowsClockwise, stringResource("usage_tracking.refresh"))
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
                // Period Selector
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        UsagePeriod.values().forEach { period ->
                            FilterChip(
                                selected = selectedPeriod == period,
                                onClick = { screenModel.selectPeriod(period) },
                                label = { Text(when(period) {
                                    UsagePeriod.TODAY -> stringResource("usage_tracking.today")
                                    UsagePeriod.WEEK -> stringResource("usage_tracking.week")
                                    UsagePeriod.MONTH -> stringResource("usage_tracking.month")
                                }) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                // Overview Stats
                item {
                    Text(
                        text = stringResource("usage_tracking.overview"),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                usageStats?.let { stats ->
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            StatsCard(
                                title = stringResource("dashboard.screen_time"),
                                value = formatDuration(stats.totalScreenTime),
                                subtitle = stringResource("usage_tracking.total_time"),
                                modifier = Modifier.weight(1f),
                                useGradient = true
                            )

                            StatsCard(
                                title = stringResource("usage_tracking.most_used"),
                                value = stats.totalLaunches.toString(),
                                subtitle = stringResource("usage_tracking.times_opened"),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    // Top Apps
                    item {
                        Text(
                            text = stringResource("usage_tracking.most_used"),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                    items(stats.topApps, key = { it.packageName }) { appUsage ->
                        UsageAppItem(appUsage)
                    }

                    if (stats.topApps.isEmpty()) {
                        item {
                            GlassCard(modifier = Modifier.fillMaxWidth()) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 32.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = PhosphorIcons.Bold.Clock,
                                        contentDescription = stringResource("usage_tracking.no_data"),
                                        modifier = Modifier.size(48.dp),
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = stringResource("usage_tracking.no_data"),
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = stringResource("usage_tracking.grant_permission"),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
}

@Composable
fun UsageAppItem(appUsage: AppUsageSummary) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // App Icon Placeholder
                Icon(
                    imageVector = PhosphorIcons.Bold.SquaresFour,
                    contentDescription = appUsage.appName,
                    tint = TawaznTheme.colors.chartColors.getOrElse(
                        appUsage.appName.hashCode() % TawaznTheme.colors.chartColors.size
                    ) { TawaznTheme.colors.gradientMiddle },
                    modifier = Modifier.size(40.dp)
                )

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = appUsage.appName,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = stringResource("usage_tracking.opens", appUsage.totalLaunches),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = formatDuration(appUsage.totalTime),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = TawaznTheme.colors.gradientMiddle
                )
                Text(
                    text = "${(appUsage.percentage * 100).toInt()}%",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

fun formatDuration(duration: kotlin.time.Duration): String {
    val hours = duration.inWholeHours
    val minutes = (duration.inWholeMinutes % 60)

    return when {
        hours > 0 -> "${hours}h ${minutes}m"
        minutes > 0 -> "${minutes}m"
        else -> "${duration.inWholeSeconds}s"
    }
}
