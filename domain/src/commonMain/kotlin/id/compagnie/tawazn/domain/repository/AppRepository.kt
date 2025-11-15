package id.compagnie.tawazn.domain.repository

import id.compagnie.tawazn.domain.model.AppCategory
import id.compagnie.tawazn.domain.model.AppInfo
import kotlinx.coroutines.flow.Flow

interface AppRepository {
    /**
     * Get all installed apps
     */
    fun getAllApps(): Flow<List<AppInfo>>

    /**
     * Get app by package name
     */
    suspend fun getAppByPackageName(packageName: String): AppInfo?

    /**
     * Get non-system apps only
     */
    fun getNonSystemApps(): Flow<List<AppInfo>>

    /**
     * Get apps by category
     */
    fun getAppsByCategory(category: AppCategory): Flow<List<AppInfo>>

    /**
     * Refresh apps from system
     */
    suspend fun refreshApps()

    /**
     * Insert or update app
     */
    suspend fun upsertApp(app: AppInfo)

    /**
     * Delete app
     */
    suspend fun deleteApp(packageName: String)
}
