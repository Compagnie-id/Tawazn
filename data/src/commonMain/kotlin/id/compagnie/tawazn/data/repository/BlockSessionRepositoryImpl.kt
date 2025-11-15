package id.compagnie.tawazn.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import id.compagnie.tawazn.database.TawaznDatabase
import id.compagnie.tawazn.domain.model.BlockSession
import id.compagnie.tawazn.domain.model.CreateBlockSessionRequest
import id.compagnie.tawazn.domain.repository.BlockSessionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class BlockSessionRepositoryImpl(
    private val database: TawaznDatabase
) : BlockSessionRepository {

    private val sessionQueries = database.blockSessionQueries

    override fun getAllSessions(): Flow<List<BlockSession>> {
        return sessionQueries.selectAllSessions()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { sessions -> sessions.map { it.toBlockSession() } }
    }

    override fun getActiveSessions(): Flow<List<BlockSession>> {
        return sessionQueries.selectActiveSessions()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { sessions -> sessions.map { it.toBlockSession() } }
    }

    override suspend fun getSessionById(id: Long): BlockSession? {
        return sessionQueries.selectSessionById(id).executeAsOneOrNull()?.toBlockSession()
    }

    override suspend fun getCurrentActiveSessions(currentTime: Instant): List<BlockSession> {
        return sessionQueries.selectCurrentActiveSessions(
            currentTime.toEpochMilliseconds(),
            currentTime.toEpochMilliseconds()
        ).executeAsList().map { it.toBlockSession() }
    }

    override suspend fun createSession(request: CreateBlockSessionRequest): Long {
        val now = Clock.System.now()

        sessionQueries.insertSession(
            name = request.name,
            description = request.description,
            isActive = 1L,
            startTime = request.startTime.toEpochMilliseconds(),
            endTime = request.endTime.toEpochMilliseconds(),
            repeatDaily = if (request.repeatDaily) 1L else 0L,
            repeatWeekly = if (request.repeatWeekly) 1L else 0L,
            repeatDays = Json.encodeToString(request.repeatDays.map { it.name }),
            createdAt = now.toEpochMilliseconds(),
            updatedAt = now.toEpochMilliseconds()
        )

        val sessionId = sessionQueries.selectAllSessions().executeAsList().lastOrNull()?.id ?: return -1

        // Add apps to session
        request.appPackageNames.forEach { packageName ->
            sessionQueries.insertSessionApp(sessionId, packageName)
        }

        return sessionId
    }

    override suspend fun updateSession(session: BlockSession) {
        sessionQueries.updateSession(
            name = session.name,
            description = session.description,
            isActive = if (session.isActive) 1L else 0L,
            startTime = session.startTime.toEpochMilliseconds(),
            endTime = session.endTime.toEpochMilliseconds(),
            repeatDaily = if (session.repeatDaily) 1L else 0L,
            repeatWeekly = if (session.repeatWeekly) 1L else 0L,
            repeatDays = Json.encodeToString(session.repeatDays.map { it.name }),
            updatedAt = Clock.System.now().toEpochMilliseconds(),
            id = session.id
        )

        // Update apps for session
        sessionQueries.deleteAllAppsForSession(session.id)
        session.blockedApps.forEach { packageName ->
            sessionQueries.insertSessionApp(session.id, packageName)
        }
    }

    override suspend fun updateSessionStatus(id: Long, isActive: Boolean) {
        sessionQueries.updateSessionStatus(
            isActive = if (isActive) 1L else 0L,
            updatedAt = Clock.System.now().toEpochMilliseconds(),
            id = id
        )
    }

    override suspend fun deleteSession(id: Long) {
        sessionQueries.deleteSession(id)
    }

    override suspend fun addAppToSession(sessionId: Long, packageName: String) {
        sessionQueries.insertSessionApp(sessionId, packageName)
    }

    override suspend fun removeAppFromSession(sessionId: Long, packageName: String) {
        sessionQueries.deleteSessionApp(sessionId, packageName)
    }

    override suspend fun getAppsForSession(sessionId: Long): List<String> {
        return sessionQueries.selectAppsForSession(sessionId).executeAsList()
    }

    private fun id.compagnie.tawazn.database.BlockSession.toBlockSession(): BlockSession {
        val apps = sessionQueries.selectAppsForSession(id).executeAsList()
        val days = try {
            Json.decodeFromString<List<String>>(repeatDays ?: "[]").map { DayOfWeek.valueOf(it) }
        } catch (e: Exception) {
            emptyList()
        }

        return BlockSession(
            id = id,
            name = name,
            description = description,
            isActive = isActive == 1L,
            startTime = Instant.fromEpochMilliseconds(startTime),
            endTime = Instant.fromEpochMilliseconds(endTime),
            repeatDaily = repeatDaily == 1L,
            repeatWeekly = repeatWeekly == 1L,
            repeatDays = days,
            blockedApps = apps,
            createdAt = Instant.fromEpochMilliseconds(createdAt),
            updatedAt = Instant.fromEpochMilliseconds(updatedAt)
        )
    }
}
