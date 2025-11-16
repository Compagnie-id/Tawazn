package id.compagnie.tawazn.domain.usecase

import id.compagnie.tawazn.domain.model.BlockRequest
import id.compagnie.tawazn.domain.repository.BlockedAppRepository
import kotlin.time.Duration

class BlockAppUseCase(
    private val blockedAppRepository: BlockedAppRepository
) {
    suspend operator fun invoke(
        packageName: String,
        appName: String,
        iconPath: String? = null,
        duration: Duration? = null
    ): Result<Unit> = runCatching {
        val request = BlockRequest(
            packageName = packageName,
            appName = appName,
            iconPath = iconPath,
            duration = duration
        )
        blockedAppRepository.blockApp(request)
    }
}
