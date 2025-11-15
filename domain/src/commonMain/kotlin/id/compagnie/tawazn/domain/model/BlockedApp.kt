package id.compagnie.tawazn.domain.model

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class BlockedApp(
    val id: Long = 0,
    val packageName: String,
    val appName: String,
    val iconPath: String? = null,
    val isBlocked: Boolean = true,
    val blockedAt: Instant,
    val blockedUntil: Instant? = null,
    val blockDuration: Duration? = null,
    val createdAt: Instant,
    val updatedAt: Instant
)

@Serializable
data class BlockRequest(
    val packageName: String,
    val appName: String,
    val iconPath: String? = null,
    val duration: Duration? = null
)
