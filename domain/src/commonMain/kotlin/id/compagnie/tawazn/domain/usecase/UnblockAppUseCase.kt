package id.compagnie.tawazn.domain.usecase

import id.compagnie.tawazn.domain.repository.BlockedAppRepository

class UnblockAppUseCase(
    private val blockedAppRepository: BlockedAppRepository
) {
    suspend operator fun invoke(packageName: String): Result<Unit> = runCatching {
        blockedAppRepository.unblockApp(packageName)
    }
}
