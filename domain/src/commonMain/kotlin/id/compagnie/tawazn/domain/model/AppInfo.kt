package id.compagnie.tawazn.domain.model

import kotlinx.datetime.Instant
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class AppInfo(
    val packageName: String,
    val appName: String,
    val iconPath: String? = null,
    val category: AppCategory = AppCategory.OTHER,
    val isSystemApp: Boolean = false,
    @Contextual val installDate: Instant,
    @Contextual val lastUpdated: Instant
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
