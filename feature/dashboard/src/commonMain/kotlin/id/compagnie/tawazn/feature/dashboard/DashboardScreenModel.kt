@file:OptIn(ExperimentalTime::class)

package id.compagnie.tawazn.feature.dashboard

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import co.touchlab.kermit.Logger
import id.compagnie.tawazn.domain.model.AppUsageSummary
import id.compagnie.tawazn.domain.repository.BlockedAppRepository
import id.compagnie.tawazn.domain.repository.UsageRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.ExperimentalTime

data class DashboardUiState(
    val isLoading: Boolean = true,
    val screenTimeToday: Duration = Duration.ZERO,
    val blockedAppsCount: Int = 0,
    val mostUsedApp: AppUsageSummary? = null,
    val screenTimeChange: String = "",
    val error: String? = null
)

class DashboardScreenModel : ScreenModel, KoinComponent {
    private val usageRepository: UsageRepository by inject()
    private val blockedAppRepository: BlockedAppRepository by inject()
    private val logger = Logger.withTag("DashboardScreenModel")

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadDashboardData()
    }

    fun loadDashboardData() {
        screenModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, error = null) }

                val now = kotlin.time.Clock.System.now()
                val today = now.toLocalDateTime(TimeZone.currentSystemDefault()).date

                // Get today's usage
                val todayUsageList = usageRepository.getTodayUsage(today).first()
                val todayTotalTime = todayUsageList.sumOf { it.totalTimeInForeground.inWholeMinutes }.minutes

                // Get blocked apps count
                val blockedApps = blockedAppRepository.getAllBlockedApps().first()
                val blockedAppsCount = blockedApps.size

                // Get most used app today
                val mostUsedApp = usageRepository.getTopUsedApps(today, today, 1).firstOrNull()

                // Calculate change from yesterday
                val yesterday = today.minus(1, DateTimeUnit.DAY)
                val yesterdayUsageList = usageRepository.getTodayUsage(yesterday).first()
                val yesterdayTotalTime = yesterdayUsageList.sumOf { it.totalTimeInForeground.inWholeMinutes }.minutes

                val screenTimeChange = calculateScreenTimeChange(todayTotalTime, yesterdayTotalTime)

                logger.i { "Dashboard loaded: ScreenTime=$todayTotalTime, BlockedApps=$blockedAppsCount, MostUsed=${mostUsedApp?.appName}" }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        screenTimeToday = todayTotalTime,
                        blockedAppsCount = blockedAppsCount,
                        mostUsedApp = mostUsedApp,
                        screenTimeChange = screenTimeChange
                    )
                }
            } catch (e: Exception) {
                logger.e(e) { "Failed to load dashboard data" }
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Failed to load dashboard: ${e.message}"
                    )
                }
            }
        }
    }

    fun refresh() {
        loadDashboardData()
    }

    private fun calculateScreenTimeChange(today: Duration, yesterday: Duration): String {
        if (yesterday.inWholeMinutes == 0L) {
            return if (today.inWholeMinutes > 0) {
                "First day of tracking"
            } else {
                "No data yet"
            }
        }

        val change = today.inWholeMinutes - yesterday.inWholeMinutes
        val percentChange = ((change.toFloat() / yesterday.inWholeMinutes) * 100).toInt()

        return when {
            percentChange > 0 -> "+$percentChange% from yesterday"
            percentChange < 0 -> "$percentChange% from yesterday"
            else -> "Same as yesterday"
        }
    }
}

/**
 * Format duration as "Xh Ym" or "Xm"
 */
fun Duration.toHoursMinutesString(): String {
    val hours = inWholeHours
    val minutes = (inWholeMinutes % 60)

    return when {
        hours > 0 && minutes > 0 -> "${hours}h ${minutes}m"
        hours > 0 -> "${hours}h"
        minutes > 0 -> "${minutes}m"
        else -> "0m"
    }
}
