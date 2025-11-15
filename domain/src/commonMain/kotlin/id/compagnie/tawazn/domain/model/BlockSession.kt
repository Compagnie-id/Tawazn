package id.compagnie.tawazn.domain.model

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class BlockSession(
    val id: Long = 0,
    val name: String,
    val description: String? = null,
    val isActive: Boolean = true,
    @Contextual val startTime: Instant,
    @Contextual val endTime: Instant,
    val repeatDaily: Boolean = false,
    val repeatWeekly: Boolean = false,
    val repeatDays: List<DayOfWeek> = emptyList(),
    val blockedApps: List<String> = emptyList(),
    @Contextual val createdAt: Instant,
    @Contextual val updatedAt: Instant
)

@Serializable
data class CreateBlockSessionRequest(
    val name: String,
    val description: String? = null,
    @Contextual val startTime: Instant,
    @Contextual val endTime: Instant,
    val repeatDaily: Boolean = false,
    val repeatWeekly: Boolean = false,
    val repeatDays: List<DayOfWeek> = emptyList(),
    val appPackageNames: List<String> = emptyList()
)
