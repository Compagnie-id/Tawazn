package id.compagnie.tawazn.feature.settings

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import co.touchlab.kermit.Logger
import id.compagnie.tawazn.domain.model.BlockSession
import id.compagnie.tawazn.domain.model.CreateBlockSessionRequest
import id.compagnie.tawazn.domain.repository.BlockSessionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FocusSessionScreenModel : ScreenModel, KoinComponent {
    private val repository: BlockSessionRepository by inject()
    private val logger = Logger.withTag("FocusSessionScreenModel")

    val allSessions = repository.getAllSessions()
        .stateIn(screenModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeSessions = repository.getActiveSessions()
        .stateIn(screenModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun createSession(request: CreateBlockSessionRequest, onComplete: (Long) -> Unit) {
        screenModelScope.launch {
            try {
                logger.i { "Creating session: ${request.name}" }
                val sessionId = repository.createSession(request)
                logger.i { "Session created with ID: $sessionId" }
                onComplete(sessionId)
            } catch (e: Exception) {
                logger.e(e) { "Failed to create session" }
            }
        }
    }

    fun updateSession(session: BlockSession, onComplete: () -> Unit) {
        screenModelScope.launch {
            try {
                logger.i { "Updating session: ${session.name}" }
                repository.updateSession(session)
                logger.i { "Session updated successfully" }
                onComplete()
            } catch (e: Exception) {
                logger.e(e) { "Failed to update session" }
            }
        }
    }

    fun toggleSession(id: Long, isActive: Boolean) {
        screenModelScope.launch {
            try {
                logger.i { "Toggling session $id to ${if (isActive) "active" else "inactive"}" }
                repository.updateSessionStatus(id, isActive)
            } catch (e: Exception) {
                logger.e(e) { "Failed to toggle session status" }
            }
        }
    }

    fun deleteSession(id: Long) {
        screenModelScope.launch {
            try {
                logger.i { "Deleting session $id" }
                repository.deleteSession(id)
                logger.i { "Session deleted successfully" }
            } catch (e: Exception) {
                logger.e(e) { "Failed to delete session" }
            }
        }
    }

    fun addAppToSession(sessionId: Long, packageName: String) {
        screenModelScope.launch {
            try {
                logger.i { "Adding app $packageName to session $sessionId" }
                repository.addAppToSession(sessionId, packageName)
            } catch (e: Exception) {
                logger.e(e) { "Failed to add app to session" }
            }
        }
    }

    fun removeAppFromSession(sessionId: Long, packageName: String) {
        screenModelScope.launch {
            try {
                logger.i { "Removing app $packageName from session $sessionId" }
                repository.removeAppFromSession(sessionId, packageName)
            } catch (e: Exception) {
                logger.e(e) { "Failed to remove app from session" }
            }
        }
    }

    suspend fun getAppsForSession(sessionId: Long): List<String> {
        return try {
            repository.getAppsForSession(sessionId)
        } catch (e: Exception) {
            logger.e(e) { "Failed to get apps for session" }
            emptyList()
        }
    }
}
