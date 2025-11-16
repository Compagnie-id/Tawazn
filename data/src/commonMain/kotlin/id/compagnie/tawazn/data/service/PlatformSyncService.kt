package id.compagnie.tawazn.data.service

import id.compagnie.tawazn.domain.repository.AppRepository
import id.compagnie.tawazn.domain.repository.BlockedAppRepository
import id.compagnie.tawazn.domain.repository.UsageRepository

/**
 * Cross-platform service for syncing platform-specific data with repositories
 *
 * This service provides a unified API for:
 * - Syncing installed apps
 * - Syncing usage data
 * - Syncing and enforcing blocked apps
 * - Managing platform permissions
 *
 * Platform-specific implementations:
 * - Android: Uses UsageStatsManager and AccessibilityService
 * - iOS: Uses Screen Time API (FamilyControls, DeviceActivity)
 * - Desktop: Uses OS-specific commands (PowerShell, AppleScript, etc.)
 */
expect class PlatformSyncService {

    /**
     * Initialize the service with repositories
     */
    fun initialize(
        appRepository: AppRepository,
        blockedAppRepository: BlockedAppRepository,
        usageRepository: UsageRepository
    )

    /**
     * Sync all installed applications to the database
     */
    suspend fun syncInstalledApps()

    /**
     * Sync usage data from platform APIs to the database
     * @param daysBack Number of days of historical data to sync
     */
    suspend fun syncUsageData(daysBack: Int = 7)

    /**
     * Sync blocked apps from database to platform enforcement
     * - Android: Updates SharedPreferences for AccessibilityService
     * - iOS: Applies app shields via ManagedSettings
     * - Desktop: Starts/updates blocking enforcement
     */
    suspend fun syncBlockedApps()

    /**
     * Perform full synchronization of all platform data
     */
    suspend fun performFullSync()

    /**
     * Check if the platform has required permissions
     */
    fun hasRequiredPermissions(): Boolean

    /**
     * Request platform-specific permissions
     * @return true if permissions are granted or already available
     */
    suspend fun requestPermissions(): Boolean

    /**
     * Start background monitoring and enforcement services
     * - Android: Schedules WorkManager for periodic sync
     * - iOS: Sets up DeviceActivity monitoring
     * - Desktop: Starts active app monitoring and blocking enforcement
     */
    fun startBackgroundServices()

    /**
     * Stop all background services
     */
    fun stopBackgroundServices()

    /**
     * Get platform information and status
     */
    fun getPlatformInfo(): Map<String, String>
}

/**
 * Factory function to create PlatformSyncService
 */
expect fun createPlatformSyncService(): PlatformSyncService
