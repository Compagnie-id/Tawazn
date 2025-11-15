package id.compagnie.tawazn.domain.model

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class AppUsage(
    val id: Long = 0,
    val packageName: String,
    val appName: String,
    val usageDate: LocalDate,
    val totalTimeInForeground: Duration,
    val launchCount: Int = 0,
    val lastTimeUsed: Instant? = null,
    val createdAt: Instant,
    val updatedAt: Instant
)

@Serializable
data class UsageStats(
    val totalScreenTime: Duration,
    val totalLaunches: Int,
    val topApps: List<AppUsageSummary>,
    val dailyUsage: List<DailyUsage>
)

@Serializable
data class AppUsageSummary(
    val packageName: String,
    val appName: String,
    val totalTime: Duration,
    val totalLaunches: Int,
    val percentage: Float
)

@Serializable
data class DailyUsage(
    val date: LocalDate,
    val totalTime: Duration,
    val totalLaunches: Int
)
