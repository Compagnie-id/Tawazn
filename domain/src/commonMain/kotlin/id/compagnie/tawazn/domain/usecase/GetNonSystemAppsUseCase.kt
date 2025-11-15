package id.compagnie.tawazn.domain.usecase

import id.compagnie.tawazn.domain.model.AppInfo
import id.compagnie.tawazn.domain.repository.AppRepository
import kotlinx.coroutines.flow.Flow

class GetNonSystemAppsUseCase(
    private val appRepository: AppRepository
) {
    operator fun invoke(): Flow<List<AppInfo>> {
        return appRepository.getNonSystemApps()
    }
}
