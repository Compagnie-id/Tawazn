package id.compagnie.tawazn.domain.model

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class AppUsage(
    val id: Long = 0,
    val packageName: String,
    val appName: String,
    @Contextual val usageDate: LocalDate,
    @Contextual val totalTimeInForeground: Duration,
    val launchCount: Int = 0,
    @Contextual val lastTimeUsed: Instant? = null,
    @Contextual val createdAt: Instant,
    @Contextual val updatedAt: Instant
)

@Serializable
data class UsageStats(
    @Contextual val totalScreenTime: Duration,
    val totalLaunches: Int,
    val topApps: List<AppUsageSummary>,
    val dailyUsage: List<DailyUsage>
)

@Serializable
data class AppUsageSummary(
    val packageName: String,
    val appName: String,
    @Contextual val totalTime: Duration,
    val totalLaunches: Int,
    val percentage: Float
)

@Serializable
data class DailyUsage(
    @Contextual val date: LocalDate,
    @Contextual val totalTime: Duration,
    val totalLaunches: Int
)
