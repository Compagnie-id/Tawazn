package id.compagnie.tawazn.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import id.compagnie.tawazn.database.TawaznDatabase
import id.compagnie.tawazn.domain.model.BlockRequest
import id.compagnie.tawazn.domain.model.BlockedApp
import id.compagnie.tawazn.domain.repository.BlockedAppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration

class BlockedAppRepositoryImpl(
    private val database: TawaznDatabase
) : BlockedAppRepository {

    private val queries = database.blockedAppQueries

    override fun getAllBlockedApps(): Flow<List<BlockedApp>> {
        return queries.selectAll()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { apps -> apps.map { it.toBlockedApp() } }
    }

    override fun getActiveBlockedApps(currentTime: Instant): Flow<List<BlockedApp>> {
        return queries.selectActiveBlocks(currentTime.toEpochMilliseconds())
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { apps -> apps.map { it.toBlockedApp() } }
    }

    override suspend fun getBlockedAppByPackageName(packageName: String): BlockedApp? {
        return queries.selectByPackageName(packageName).executeAsOneOrNull()?.toBlockedApp()
    }

    override suspend fun blockApp(request: BlockRequest) {
        val now = Clock.System.now()
        val blockedUntil = request.duration?.let { now + it }

        queries.insert(
            packageName = request.packageName,
            appName = request.appName,
            iconPath = request.iconPath,
            isBlocked = 1L,
            blockedAt = now.toEpochMilliseconds(),
            blockedUntil = blockedUntil?.toEpochMilliseconds(),
            blockDurationMinutes = request.duration?.inWholeMinutes,
            createdAt = now.toEpochMilliseconds(),
            updatedAt = now.toEpochMilliseconds()
        )
    }

    override suspend fun unblockApp(packageName: String) {
        queries.updateBlockStatus(
            isBlocked = 0L,
            updatedAt = Clock.System.now().toEpochMilliseconds(),
            packageName = packageName
        )
    }

    override suspend fun updateBlockStatus(packageName: String, isBlocked: Boolean) {
        queries.updateBlockStatus(
            isBlocked = if (isBlocked) 1L else 0L,
            updatedAt = Clock.System.now().toEpochMilliseconds(),
            packageName = packageName
        )
    }

    override suspend fun isAppBlocked(packageName: String): Boolean {
        return queries.selectByPackageName(packageName).executeAsOneOrNull()?.isBlocked == 1L
    }

    override suspend fun deleteBlockedApp(packageName: String) {
        queries.deleteByPackageName(packageName)
    }

    private fun id.compagnie.tawazn.database.BlockedApp.toBlockedApp() = BlockedApp(
        id = id,
        packageName = packageName,
        appName = appName,
        iconPath = iconPath,
        isBlocked = isBlocked == 1L,
        blockedAt = Instant.fromEpochMilliseconds(blockedAt),
        blockedUntil = blockedUntil?.let { Instant.fromEpochMilliseconds(it) },
        blockDuration = blockDurationMinutes?.let { Duration.parse("${it}m") },
        createdAt = Instant.fromEpochMilliseconds(createdAt),
        updatedAt = Instant.fromEpochMilliseconds(updatedAt)
    )
}
