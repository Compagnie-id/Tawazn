package id.compagnie.tawazn.domain.repository

import id.compagnie.tawazn.domain.model.BlockRequest
import id.compagnie.tawazn.domain.model.BlockedApp
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

interface BlockedAppRepository {
    /**
     * Get all blocked apps
     */
    fun getAllBlockedApps(): Flow<List<BlockedApp>>

    /**
     * Get currently active blocked apps
     */
    fun getActiveBlockedApps(currentTime: Instant): Flow<List<BlockedApp>>

    /**
     * Get blocked app by package name
     */
    suspend fun getBlockedAppByPackageName(packageName: String): BlockedApp?

    /**
     * Block an app
     */
    suspend fun blockApp(request: BlockRequest)

    /**
     * Unblock an app
     */
    suspend fun unblockApp(packageName: String)

    /**
     * Update block status
     */
    suspend fun updateBlockStatus(packageName: String, isBlocked: Boolean)

    /**
     * Check if app is blocked
     */
    suspend fun isAppBlocked(packageName: String): Boolean

    /**
     * Delete blocked app entry
     */
    suspend fun deleteBlockedApp(packageName: String)
}
