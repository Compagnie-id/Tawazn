package id.compagnie.tawazn.domain.model

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class BlockSession(
    val id: Long = 0,
    val name: String,
    val description: String? = null,
    val isActive: Boolean = true,
    val startTime: Instant,
    val endTime: Instant,
    val repeatDaily: Boolean = false,
    val repeatWeekly: Boolean = false,
    val repeatDays: List<DayOfWeek> = emptyList(),
    val blockedApps: List<String> = emptyList(),
    val createdAt: Instant,
    val updatedAt: Instant
)

@Serializable
data class CreateBlockSessionRequest(
    val name: String,
    val description: String? = null,
    val startTime: Instant,
    val endTime: Instant,
    val repeatDaily: Boolean = false,
    val repeatWeekly: Boolean = false,
    val repeatDays: List<DayOfWeek> = emptyList(),
    val appPackageNames: List<String> = emptyList()
)
