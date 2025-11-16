package id.compagnie.tawazn.platform.android

import id.compagnie.tawazn.domain.model.AppInfo
import id.compagnie.tawazn.domain.model.AppUsage
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

/**
 * Platform-specific interface for app monitoring
 */
interface AppMonitor {
    /**
     * Get list of installed apps
     */
    suspend fun getInstalledApps(): List<AppInfo>

    /**
     * Get app usage statistics for a specific date range
     */
    suspend fun getAppUsageStats(startDate: LocalDate, endDate: LocalDate): List<AppUsage>

    /**
     * Check if usage access permission is granted
     */
    fun hasUsageStatsPermission(): Boolean

    /**
     * Request usage stats permission (opens settings)
     */
    fun requestUsageStatsPermission()

    /**
     * Check if app is currently running
     */
    suspend fun isAppRunning(packageName: String): Boolean

    /**
     * Block an app from launching
     */
    suspend fun blockApp(packageName: String): Result<Unit>

    /**
     * Unblock an app
     */
    suspend fun unblockApp(packageName: String): Result<Unit>
}
