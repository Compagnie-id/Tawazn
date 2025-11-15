package id.compagnie.tawazn.domain.usecase

import id.compagnie.tawazn.domain.repository.UsageRepository

class SyncUsageUseCase(
    private val usageRepository: UsageRepository
) {
    suspend operator fun invoke(): Result<Unit> = runCatching {
        usageRepository.syncUsageFromSystem()
    }
}
