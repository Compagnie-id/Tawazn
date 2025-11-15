@file:OptIn(ExperimentalTime::class)

package id.compagnie.tawazn.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import id.compagnie.tawazn.database.TawaznDatabase
import id.compagnie.tawazn.domain.model.AppUsage
import id.compagnie.tawazn.domain.model.AppUsageSummary
import id.compagnie.tawazn.domain.model.DailyUsage
import id.compagnie.tawazn.domain.model.UsageStats
import id.compagnie.tawazn.domain.repository.UsageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class UsageRepositoryImpl(
    private val database: TawaznDatabase
) : UsageRepository {

    private val queries = database.appUsageQueries

    override fun getAllUsage(): Flow<List<AppUsage>> {
        return queries.selectAll()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { usages -> usages.map { it.toAppUsage() } }
    }

    override fun getUsageByPackageName(packageName: String): Flow<List<AppUsage>> {
        return queries.selectByPackageName(packageName)
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { usages -> usages.map { it.toAppUsage() } }
    }

    override fun getUsageByDate(date: LocalDate): Flow<List<AppUsage>> {
        return queries.selectByDate(date.toEpochDays().toLong())
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { usages -> usages.map { it.toAppUsage() } }
    }

    override fun getUsageByDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<AppUsage>> {
        return queries.selectByDateRange(
            startDate.toEpochDays().toLong(),
            endDate.toEpochDays().toLong()
        )
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { usages -> usages.map { it.toAppUsage() } }
    }

    override fun getTodayUsage(today: LocalDate): Flow<List<AppUsage>> {
        return queries.selectTodayUsage(today.toEpochDays().toLong())
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { usages -> usages.map { it.toAppUsage() } }
    }

    override suspend fun getTopUsedApps(startDate: LocalDate, endDate: LocalDate, limit: Int): List<AppUsageSummary> {
        val results = queries.selectTopUsedApps(
            startDate.toEpochDays().toLong(),
            endDate.toEpochDays().toLong(),
            limit.toLong()
        ).executeAsList()

        val totalTime = results.sumOf { it.totalTime ?: 0 }

        return results.map {
            AppUsageSummary(
                packageName = it.packageName,
                appName = it.appName,
                totalTime = (it.totalTime ?: 0).milliseconds,
                totalLaunches = (it.totalLaunches ?: 0).toInt(),
                percentage = if (totalTime > 0) (it.totalTime ?: 0).toFloat() / totalTime else 0f
            )
        }
    }

    override suspend fun getDailyUsageSummary(startDate: LocalDate, endDate: LocalDate): List<DailyUsage> {
        return queries.selectTotalUsageByDate(
            startDate.toEpochDays().toLong(),
            endDate.toEpochDays().toLong()
        ).executeAsList().map {
            DailyUsage(
                date = LocalDate.fromEpochDays(it.usageDate.toInt()),
                totalTime = (it.totalTime ?: 0).milliseconds,
                totalLaunches = (it.totalLaunches ?: 0).toInt()
            )
        }
    }

    override suspend fun getUsageStats(startDate: LocalDate, endDate: LocalDate): UsageStats {
        val topApps = getTopUsedApps(startDate, endDate, 10)
        val dailyUsage = getDailyUsageSummary(startDate, endDate)

        return UsageStats(
            totalScreenTime = topApps.fold(0.milliseconds) { acc, app -> acc + app.totalTime },
            totalLaunches = topApps.sumOf { it.totalLaunches },
            topApps = topApps,
            dailyUsage = dailyUsage
        )
    }

    override suspend fun syncUsageFromSystem() {
        // This would call platform-specific app monitor to get fresh usage data
        // For now, this is a placeholder
    }

    override suspend fun upsertUsage(usage: AppUsage) {
        queries.insert(
            packageName = usage.packageName,
            appName = usage.appName,
            usageDate = usage.usageDate.toEpochDays().toLong(),
            totalTimeInForeground = usage.totalTimeInForeground.inWholeMilliseconds,
            launchCount = usage.launchCount.toLong(),
            lastTimeUsed = usage.lastTimeUsed?.toEpochMilliseconds(),
            createdAt = usage.createdAt.toEpochMilliseconds(),
            updatedAt = usage.updatedAt.toEpochMilliseconds()
        )
    }

    override suspend fun deleteUsageOlderThan(date: LocalDate) {
        queries.deleteOlderThan(date.toEpochDays().toLong())
    }

    private fun id.compagnie.tawazn.database.AppUsage.toAppUsage() = AppUsage(
        id = id,
        packageName = packageName,
        appName = appName,
        usageDate = LocalDate.fromEpochDays(usageDate.toInt()),
        totalTimeInForeground = totalTimeInForeground.milliseconds,
        launchCount = launchCount.toInt(),
        lastTimeUsed = lastTimeUsed?.let { Instant.fromEpochMilliseconds(it) },
        createdAt = Instant.fromEpochMilliseconds(createdAt),
        updatedAt = Instant.fromEpochMilliseconds(updatedAt)
    )
}
