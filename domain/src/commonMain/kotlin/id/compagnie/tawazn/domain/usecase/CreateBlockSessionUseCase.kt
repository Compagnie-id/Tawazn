package id.compagnie.tawazn.domain.usecase

import id.compagnie.tawazn.domain.model.CreateBlockSessionRequest
import id.compagnie.tawazn.domain.repository.BlockSessionRepository

class CreateBlockSessionUseCase(
    private val blockSessionRepository: BlockSessionRepository
) {
    suspend operator fun invoke(request: CreateBlockSessionRequest): Result<Long> = runCatching {
        blockSessionRepository.createSession(request)
    }
}
