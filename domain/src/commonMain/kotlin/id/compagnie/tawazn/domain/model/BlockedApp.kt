package id.compagnie.tawazn.domain.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@Serializable
data class BlockedApp @OptIn(ExperimentalTime::class) constructor(
    val id: Long = 0,
    val packageName: String,
    val appName: String,
    val iconPath: String? = null,
    val isBlocked: Boolean = true,
    @Contextual val blockedAt: kotlin.time.Instant,
    @Contextual val blockedUntil: kotlin.time.Instant? = null,
    @Contextual val blockDuration: Duration? = null,
    @Contextual val createdAt: kotlin.time.Instant,
    @Contextual val updatedAt: kotlin.time.Instant
)

@Serializable
data class BlockRequest(
    val packageName: String,
    val appName: String,
    val iconPath: String? = null,
    @Contextual val duration: Duration? = null
)
