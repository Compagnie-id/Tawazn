package id.compagnie.tawazn.platform.ios

import id.compagnie.tawazn.domain.model.AppInfo
import id.compagnie.tawazn.domain.model.AppUsage
import kotlinx.datetime.LocalDate

/**
 * iOS-specific interface for app monitoring using Screen Time API
 *
 * Important: iOS Screen Time API requires special entitlement from Apple:
 * - com.apple.developer.family-controls
 *
 * Apply for this entitlement at: https://developer.apple.com/contact/request/family-controls-distribution/
 */
expect interface IOSAppMonitor {
    /**
     * Request Family Controls authorization
     * This must be called before using any Screen Time features
     */
    suspend fun requestAuthorization(): Result<Unit>

    /**
     * Check if Family Controls authorization is granted
     */
    fun isAuthorized(): Boolean

    /**
     * Get app usage statistics using DeviceActivity framework
     * Note: iOS provides privacy-preserving usage data
     */
    suspend fun getAppUsageStats(startDate: LocalDate, endDate: LocalDate): List<AppUsage>

    /**
     * Shield (block) an app using ManagedSettings
     */
    suspend fun shieldApp(bundleIdentifier: String): Result<Unit>

    /**
     * Unshield (unblock) an app
     */
    suspend fun unshieldApp(bundleIdentifier: String): Result<Unit>

    /**
     * Set up device activity monitoring schedule
     */
    suspend fun setupActivityMonitoring(): Result<Unit>
}
