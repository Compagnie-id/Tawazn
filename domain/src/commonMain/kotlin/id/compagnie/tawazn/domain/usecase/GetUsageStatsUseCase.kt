package id.compagnie.tawazn.domain.usecase

import id.compagnie.tawazn.domain.model.UsageStats
import id.compagnie.tawazn.domain.repository.UsageRepository
import kotlinx.datetime.LocalDate

class GetUsageStatsUseCase(
    private val usageRepository: UsageRepository
) {
    suspend operator fun invoke(
        startDate: LocalDate,
        endDate: LocalDate
    ): Result<UsageStats> = runCatching {
        usageRepository.getUsageStats(startDate, endDate)
    }
}
