package id.compagnie.tawazn.domain.repository

import id.compagnie.tawazn.domain.model.AppUsage
import id.compagnie.tawazn.domain.model.AppUsageSummary
import id.compagnie.tawazn.domain.model.DailyUsage
import id.compagnie.tawazn.domain.model.UsageStats
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface UsageRepository {
    /**
     * Get all usage records
     */
    fun getAllUsage(): Flow<List<AppUsage>>

    /**
     * Get usage for specific app
     */
    fun getUsageByPackageName(packageName: String): Flow<List<AppUsage>>

    /**
     * Get usage for specific date
     */
    fun getUsageByDate(date: LocalDate): Flow<List<AppUsage>>

    /**
     * Get usage for date range
     */
    fun getUsageByDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<AppUsage>>

    /**
     * Get today's usage
     */
    fun getTodayUsage(today: LocalDate): Flow<List<AppUsage>>

    /**
     * Get top used apps in date range
     */
    suspend fun getTopUsedApps(startDate: LocalDate, endDate: LocalDate, limit: Int = 10): List<AppUsageSummary>

    /**
     * Get daily usage summary for date range
     */
    suspend fun getDailyUsageSummary(startDate: LocalDate, endDate: LocalDate): List<DailyUsage>

    /**
     * Get usage statistics for date range
     */
    suspend fun getUsageStats(startDate: LocalDate, endDate: LocalDate): UsageStats

    /**
     * Sync usage from system
     */
    suspend fun syncUsageFromSystem()

    /**
     * Insert or update usage record
     */
    suspend fun upsertUsage(usage: AppUsage)

    /**
     * Delete old usage records
     */
    suspend fun deleteUsageOlderThan(date: LocalDate)
}
