@file:OptIn(ExperimentalTime::class)

package id.compagnie.tawazn.data.repository

import id.compagnie.tawazn.database.TawaznDatabase
import id.compagnie.tawazn.domain.model.AppCategory
import id.compagnie.tawazn.domain.model.AppInfo
import id.compagnie.tawazn.domain.repository.AppRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.time.Instant
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlin.time.ExperimentalTime

class AppRepositoryImpl(
    private val database: TawaznDatabase
) : AppRepository {

    private val queries = database.appQueries

    override fun getAllApps(): Flow<List<AppInfo>> {
        return queries.selectAll()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { apps ->
                apps.map { it.toAppInfo() }
            }
    }

    override suspend fun getAppByPackageName(packageName: String): AppInfo? {
        return queries.selectByPackageName(packageName).executeAsOneOrNull()?.toAppInfo()
    }

    override fun getNonSystemApps(): Flow<List<AppInfo>> {
        return queries.selectNonSystemApps()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { apps ->
                apps.map { it.toAppInfo() }
            }
    }

    override fun getAppsByCategory(category: AppCategory): Flow<List<AppInfo>> {
        return queries.selectByCategory(category.name)
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { apps ->
                apps.map { it.toAppInfo() }
            }
    }

    override suspend fun refreshApps() {
        // This would call platform-specific app monitor to get fresh apps
        // For now, this is a placeholder
    }

    override suspend fun upsertApp(app: AppInfo) {
        queries.insert(
            packageName = app.packageName,
            appName = app.appName,
            iconPath = app.iconPath,
            category = app.category.name,
            isSystemApp = if (app.isSystemApp) 1L else 0L,
            installDate = app.installDate.toEpochMilliseconds(),
            lastUpdated = app.lastUpdated.toEpochMilliseconds()
        )
    }

    override suspend fun deleteApp(packageName: String) {
        queries.delete(packageName)
    }

    private fun id.compagnie.tawazn.database.App.toAppInfo() = AppInfo(
        packageName = packageName,
        appName = appName,
        iconPath = iconPath,
        category = AppCategory.valueOf(category ?: "OTHER"),
        isSystemApp = isSystemApp == 1L,
        installDate = Instant.fromEpochMilliseconds(installDate),
        lastUpdated = Instant.fromEpochMilliseconds(lastUpdated)
    )
}
