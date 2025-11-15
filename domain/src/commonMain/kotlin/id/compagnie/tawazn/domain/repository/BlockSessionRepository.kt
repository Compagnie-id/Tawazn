@file:OptIn(ExperimentalTime::class)

package id.compagnie.tawazn.domain.repository

import id.compagnie.tawazn.domain.model.BlockSession
import id.compagnie.tawazn.domain.model.CreateBlockSessionRequest
import kotlinx.coroutines.flow.Flow
import kotlin.time.ExperimentalTime

interface BlockSessionRepository {
    /**
     * Get all block sessions
     */
    fun getAllSessions(): Flow<List<BlockSession>>

    /**
     * Get active sessions
     */
    fun getActiveSessions(): Flow<List<BlockSession>>

    /**
     * Get session by ID
     */
    suspend fun getSessionById(id: Long): BlockSession?

    /**
     * Get currently active sessions based on time
     */
    suspend fun getCurrentActiveSessions(currentTime: kotlin.time.Instant): List<BlockSession>

    /**
     * Create new block session
     */
    suspend fun createSession(request: CreateBlockSessionRequest): Long

    /**
     * Update block session
     */
    suspend fun updateSession(session: BlockSession)

    /**
     * Update session status
     */
    suspend fun updateSessionStatus(id: Long, isActive: Boolean)

    /**
     * Delete session
     */
    suspend fun deleteSession(id: Long)

    /**
     * Add app to session
     */
    suspend fun addAppToSession(sessionId: Long, packageName: String)

    /**
     * Remove app from session
     */
    suspend fun removeAppFromSession(sessionId: Long, packageName: String)

    /**
     * Get apps for session
     */
    suspend fun getAppsForSession(sessionId: Long): List<String>
}
