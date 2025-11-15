package id.compagnie.tawazn.feature.analytics

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import co.touchlab.kermit.Logger
import id.compagnie.tawazn.core.datastore.AppPreferences
import id.compagnie.tawazn.domain.model.AppUsageSummary
import id.compagnie.tawazn.domain.model.DailyUsage
import id.compagnie.tawazn.domain.model.UsageStats
import id.compagnie.tawazn.domain.repository.UsageRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

data class AnalyticsUiState(
    val isLoading: Boolean = true,
    val weeklyStats: UsageStats? = null,
    val todayUsage: Duration = Duration.ZERO,
    val dailyGoal: Int = 180, // minutes
    val weeklyUsage: List<DailyUsage> = emptyList(),
    val topApps: List<AppUsageSummary> = emptyList(),
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val averageDailyTime: Duration = Duration.ZERO,
    val bestDay: Pair<String, Duration>? = null,
    val peakUsageHour: Int? = null,
    val topDistraction: AppUsageSummary? = null,
    val goalProgress: Float = 0f,
    val error: String? = null
)

class AnalyticsScreenModel : ScreenModel, KoinComponent {
    private val repository: UsageRepository by inject()
    private val appPreferences: AppPreferences by inject()
    private val logger = Logger.withTag("AnalyticsScreenModel")

    private val _uiState = MutableStateFlow(AnalyticsUiState())
    val uiState: StateFlow<AnalyticsUiState> = _uiState.asStateFlow()

    init {
        loadAnalytics()
    }

    fun loadAnalytics() {
        screenModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, error = null) }

                val now = Clock.System.now()
                val today = now.toLocalDateTime(TimeZone.currentSystemDefault()).date
                val weekAgo = today.minus(7, DateTimeUnit.DAY)

                // Get daily goal from preferences
                val dailyGoalMinutes = appPreferences.dailyUsageGoalMinutes.first()

                // Get streak info
                val currentStreak = appPreferences.currentStreak.first()
                val longestStreak = appPreferences.longestStreak.first()

                // Get weekly stats
                val weeklyStats = repository.getUsageStats(weekAgo, today)
                val dailyUsageSummary = repository.getDailyUsageSummary(weekAgo, today)
                val topApps = repository.getTopUsedApps(weekAgo, today, 10)

                // Get today's usage
                val todayUsageList = repository.getTodayUsage(today).first()
                val todayTotalTime = todayUsageList.sumOf { it.totalTimeInForeground.inWholeMinutes }.minutes

                // Calculate average daily time
                val avgDailyTime = if (dailyUsageSummary.isNotEmpty()) {
                    val totalMinutes = dailyUsageSummary.sumOf { it.totalTime.inWholeMinutes }
                    (totalMinutes / dailyUsageSummary.size).minutes
                } else {
                    Duration.ZERO
                }

                // Find best day (lowest usage)
                val bestDay = dailyUsageSummary.minByOrNull { it.totalTime }?.let { day ->
                    val dayName = getDayName(day.date.dayOfWeek.value)
                    dayName to day.totalTime
                }

                // Calculate goal progress (today vs goal)
                val goalProgress = if (dailyGoalMinutes > 0) {
                    (todayTotalTime.inWholeMinutes.toFloat() / dailyGoalMinutes).coerceIn(0f, 1f)
                } else {
                    0f
                }

                // Get top distraction (most used app)
                val topDistraction = topApps.firstOrNull()

                logger.i { "Analytics loaded: Weekly=${weeklyStats.totalScreenTime}, Today=$todayTotalTime, Streak=$currentStreak" }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        weeklyStats = weeklyStats,
                        todayUsage = todayTotalTime,
                        dailyGoal = dailyGoalMinutes,
                        weeklyUsage = dailyUsageSummary,
                        topApps = topApps,
                        currentStreak = currentStreak,
                        longestStreak = longestStreak,
                        averageDailyTime = avgDailyTime,
                        bestDay = bestDay,
                        topDistraction = topDistraction,
                        goalProgress = goalProgress
                    )
                }
            } catch (e: Exception) {
                logger.e(e) { "Failed to load analytics" }
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Failed to load analytics: ${e.message}"
                    )
                }
            }
        }
    }

    fun refresh() {
        loadAnalytics()
    }

    private fun getDayName(dayOfWeek: Int): String {
        return when (dayOfWeek) {
            1 -> "Monday"
            2 -> "Tuesday"
            3 -> "Wednesday"
            4 -> "Thursday"
            5 -> "Friday"
            6 -> "Saturday"
            7 -> "Sunday"
            else -> "Unknown"
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
