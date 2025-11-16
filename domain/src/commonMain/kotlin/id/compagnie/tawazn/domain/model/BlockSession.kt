@file:OptIn(ExperimentalTime::class)

package id.compagnie.tawazn.domain.model

import kotlinx.datetime.DayOfWeek
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime

@Serializable
data class BlockSession(
    val id: Long = 0,
    val name: String,
    val description: String? = null,
    val isActive: Boolean = true,
    @Contextual val startTime: kotlin.time.Instant,
    @Contextual val endTime: kotlin.time.Instant,
    val repeatDaily: Boolean = false,
    val repeatWeekly: Boolean = false,
    val repeatDays: List<DayOfWeek> = emptyList(),
    val blockedApps: List<String> = emptyList(),
    @Contextual val createdAt: kotlin.time.Instant,
    @Contextual val updatedAt: kotlin.time.Instant
)

@Serializable
data class CreateBlockSessionRequest(
    val name: String,
    val description: String? = null,
    @Contextual val startTime: kotlin.time.Instant,
    @Contextual val endTime: kotlin.time.Instant,
    val repeatDaily: Boolean = false,
    val repeatWeekly: Boolean = false,
    val repeatDays: List<DayOfWeek> = emptyList(),
    val appPackageNames: List<String> = emptyList()
)
