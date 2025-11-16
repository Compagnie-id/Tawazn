@file:OptIn(ExperimentalTime::class)

package id.compagnie.tawazn.domain.usecase

import id.compagnie.tawazn.domain.model.BlockedApp
import id.compagnie.tawazn.domain.repository.BlockedAppRepository
import kotlinx.coroutines.flow.Flow
import kotlin.time.ExperimentalTime

class GetActiveBlockedAppsUseCase(
    private val blockedAppRepository: BlockedAppRepository
) {
    operator fun invoke(): Flow<List<BlockedApp>> {
        val currentTime = kotlin.time.Clock.System.now()
        return blockedAppRepository.getActiveBlockedApps(currentTime)
    }
}
