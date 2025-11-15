package id.compagnie.tawazn.domain.model

import kotlinx.datetime.Instant
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class BlockedApp(
    val id: Long = 0,
    val packageName: String,
    val appName: String,
    val iconPath: String? = null,
    val isBlocked: Boolean = true,
    @Contextual val blockedAt: Instant,
    @Contextual val blockedUntil: Instant? = null,
    @Contextual val blockDuration: Duration? = null,
    @Contextual val createdAt: Instant,
    @Contextual val updatedAt: Instant
)

@Serializable
data class BlockRequest(
    val packageName: String,
    val appName: String,
    val iconPath: String? = null,
    @Contextual val duration: Duration? = null
)
