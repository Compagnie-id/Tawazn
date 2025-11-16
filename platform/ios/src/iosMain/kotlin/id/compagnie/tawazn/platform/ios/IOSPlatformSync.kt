package id.compagnie.tawazn.platform.ios

import co.touchlab.kermit.Logger
import id.compagnie.tawazn.domain.repository.AppRepository
import id.compagnie.tawazn.domain.repository.BlockedAppRepository
import id.compagnie.tawazn.domain.repository.UsageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

/**
 * Synchronizes iOS platform data with repositories
 *
 * This class bridges the gap between iOS Screen Time API and the app's data layer:
 * - Syncs usage data from DeviceActivity to UsageRepository
 * - Syncs shielded apps with BlockedAppRepository
 * - Manages Family Controls authorization
 *
 * Note: Requires Swift bridge implementation (ScreenTimeManager.swift)
 */
class IOSPlatformSync(
    private val appRepository: AppRepository,
    private val blockedAppRepository: BlockedAppRepository,
    private val usageRepository: UsageRepository
) {
    private val logger = Logger.withTag("IOSPlatformSync")
    private val appMonitor = IOSAppMonitorImpl()

    /**
     * Request Family Controls authorization
     * This must be called before any Screen Time features can be used
     */
    suspend fun requestAuthorization(): Result<Unit> = withContext(Dispatchers.Default) {
        try {
            logger.i { "Requesting Family Controls authorization..." }
            appMonitor.requestAuthorization()
        } catch (e: Exception) {
            logger.e(e) { "Failed to request authorization" }
            Result.failure(e)
        }
    }

    /**
     * Check if Family Controls is authorized
     */
    fun isAuthorized(): Boolean {
        return appMonitor.isAuthorized()
    }

    /**
     * Sync usage data from DeviceActivity to database
     * Note: iOS provides privacy-preserving aggregated data
     */
    suspend fun syncUsageData(daysBack: Int = 7) = withContext(Dispatchers.Default) {
        try {
            if (!isAuthorized()) {
                logger.w { "Family Controls not authorized" }
                return@withContext
            }

            logger.i { "Syncing usage data for last $daysBack days..." }

            val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
            val startDate = today.minus(daysBack, kotlinx.datetime.DateTimeUnit.DAY)

            val usageData = appMonitor.getAppUsageStats(startDate, today)
            logger.i { "Found ${usageData.size} usage records" }

            // Upsert usage data to database
            usageData.forEach { usage ->
                usageRepository.upsertUsage(usage)
            }

            logger.i { "Successfully synced ${usageData.size} usage records" }
        } catch (e: Exception) {
            logger.e(e) { "Failed to sync usage data" }
        }
    }

    /**
     * Sync blocked apps from database to ManagedSettings
     * This applies app shields based on blocked apps in the database
     */
    suspend fun syncBlockedApps() = withContext(Dispatchers.Default) {
        try {
            if (!isAuthorized()) {
                logger.w { "Family Controls not authorized, cannot shield apps" }
                return@withContext
            }

            logger.i { "Syncing blocked apps to ManagedSettings..." }

            val blockedApps = blockedAppRepository.getBlockedApps().first()
            val bundleIds = blockedApps.map { it.packageName }

            logger.i { "Found ${bundleIds.size} blocked apps" }

            // Shield each blocked app
            bundleIds.forEach { bundleId ->
                appMonitor.shieldApp(bundleId)
                    .onSuccess {
                        logger.i { "Shielded app: $bundleId" }
                    }
                    .onFailure { error ->
                        logger.e { "Failed to shield app $bundleId: ${error.message}" }
                    }
            }

            logger.i { "Successfully synced blocked apps" }
        } catch (e: Exception) {
            logger.e(e) { "Failed to sync blocked apps" }
        }
    }

    /**
     * Unblock an app by removing its shield
     */
    suspend fun unblockApp(bundleId: String) = withContext(Dispatchers.Default) {
        try {
            if (!isAuthorized()) {
                logger.w { "Family Controls not authorized" }
                return@withContext
            }

            appMonitor.unshieldApp(bundleId)
                .onSuccess {
                    logger.i { "Unshielded app: $bundleId" }
                }
                .onFailure { error ->
                    logger.e { "Failed to unshield app $bundleId: ${error.message}" }
                }
        } catch (e: Exception) {
            logger.e(e) { "Failed to unblock app" }
        }
    }

    /**
     * Set up device activity monitoring
     */
    suspend fun setupMonitoring() = withContext(Dispatchers.Default) {
        try {
            if (!isAuthorized()) {
                logger.w { "Family Controls not authorized, cannot setup monitoring" }
                return@withContext
            }

            logger.i { "Setting up device activity monitoring..." }

            appMonitor.setupActivityMonitoring()
                .onSuccess {
                    logger.i { "Successfully set up monitoring" }
                }
                .onFailure { error ->
                    logger.e { "Failed to setup monitoring: ${error.message}" }
                }
        } catch (e: Exception) {
            logger.e(e) { "Failed to setup monitoring" }
        }
    }

    /**
     * Perform full sync of all data
     */
    suspend fun performFullSync() = withContext(Dispatchers.Default) {
        logger.i { "Performing full platform sync..." }

        if (!isAuthorized()) {
            logger.w { "Cannot perform full sync - Family Controls not authorized" }
            logger.i { "Please request authorization first" }
            return@withContext
        }

        syncUsageData()
        syncBlockedApps()
        setupMonitoring()

        logger.i { "Full platform sync completed" }
    }

    /**
     * Check authorization and setup if needed
     */
    suspend fun ensureAuthorizedAndSetup(): Boolean = withContext(Dispatchers.Default) {
        if (!isAuthorized()) {
            logger.i { "Not authorized, requesting authorization..." }
            requestAuthorization().isSuccess
        } else {
            logger.i { "Already authorized" }
            true
        }
    }
}

/**
 * iOS Platform Sync Notes:
 *
 * Screen Time API Limitations:
 * - Requires special entitlement from Apple
 * - Must request authorization before use
 * - Provides privacy-preserving aggregated data
 * - Cannot access detailed real-time usage (by design)
 * - App shielding is immediate but monitoring data is delayed
 *
 * Best Practices:
 * 1. Always check authorization before operations
 * 2. Request authorization early in app lifecycle
 * 3. Handle authorization denial gracefully
 * 4. Sync blocked apps regularly
 * 5. Use DeviceActivity for scheduled blocking
 *
 * Swift Bridge:
 * - Implemented in ScreenTimeManager.swift
 * - Bridged via expect/actual pattern in IOSAppMonitorImpl
 * - Requires proper entitlement setup in Xcode
 */
