@file:OptIn(ExperimentalTime::class)

package id.compagnie.tawazn.domain.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime

@Serializable
data class AppInfo(
    val packageName: String,
    val appName: String,
    val iconPath: String? = null,
    val category: AppCategory = AppCategory.OTHER,
    val isSystemApp: Boolean = false,
    @Contextual val installDate: kotlin.time.Instant,
    @Contextual val lastUpdated: kotlin.time.Instant
)

@Serializable
enum class AppCategory {
    SOCIAL_MEDIA,
    ENTERTAINMENT,
    PRODUCTIVITY,
    COMMUNICATION,
    GAMES,
    NEWS,
    SHOPPING,
    EDUCATION,
    HEALTH_FITNESS,
    FINANCE,
    TRAVEL,
    TOOLS,
    OTHER
}
