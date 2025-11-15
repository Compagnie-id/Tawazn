@file:OptIn(ExperimentalTime::class)

package id.compagnie.tawazn.domain.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@Serializable
data class AppUsage(
    val id: Long = 0,
    val packageName: String,
    val appName: String,
    @Contextual val usageDate: LocalDate,
    @Contextual val totalTimeInForeground: Duration,
    val launchCount: Int = 0,
    @Contextual val lastTimeUsed: kotlin.time.Instant? = null,
    @Contextual val createdAt: kotlin.time.Instant,
    @Contextual val updatedAt: kotlin.time.Instant
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
