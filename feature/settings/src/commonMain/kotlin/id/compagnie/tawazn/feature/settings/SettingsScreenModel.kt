package id.compagnie.tawazn.feature.settings

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import co.touchlab.kermit.Logger
import id.compagnie.tawazn.core.datastore.AppPreferences
import id.compagnie.tawazn.data.service.PlatformSyncService
import id.compagnie.tawazn.database.TawaznDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Screen model for settings screen
 * Manages platform sync service, permissions, and user preferences
 */
class SettingsScreenModel : ScreenModel, KoinComponent {
    private val platformSyncService: PlatformSyncService by inject()
    private val appPreferences: AppPreferences by inject()
    private val database: TawaznDatabase by inject()
    private val logger = Logger.withTag("SettingsScreenModel")

    private val _platformState = MutableStateFlow(PlatformState())
    val platformState: StateFlow<PlatformState> = _platformState.asStateFlow()

    // Preferences flows
    val darkMode = appPreferences.darkMode
    val useSystemTheme = appPreferences.useSystemTheme
    val notificationsEnabled = appPreferences.notificationsEnabled
    val dailyReportEnabled = appPreferences.dailyReportEnabled
    val weeklyReportEnabled = appPreferences.weeklyReportEnabled

    init {
        loadPlatformStatus()
    }

    /**
     * Load platform status and information
     */
    fun loadPlatformStatus() {
        screenModelScope.launch {
            try {
                _platformState.value = _platformState.value.copy(isLoading = true)
                val hasPermissions = platformSyncService.hasRequiredPermissions()
                val platformInfo = platformSyncService.getPlatformInfo()

                _platformState.value = _platformState.value.copy(
                    hasPermissions = hasPermissions,
                    platformInfo = platformInfo,
                    isLoading = false,
                    error = null
                )
                logger.i { "Platform status loaded: hasPermissions=$hasPermissions" }
            } catch (e: Exception) {
                logger.e(e) { "Failed to load platform status" }
                _platformState.value = _platformState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    /**
     * Request platform permissions
     */
    fun requestPermissions() {
        screenModelScope.launch {
            try {
                _platformState.value = _platformState.value.copy(isRequestingPermissions = true)
                val granted = platformSyncService.requestPermissions()
                _platformState.value = _platformState.value.copy(
                    hasPermissions = granted,
                    isRequestingPermissions = false
                )
                logger.i { "Permission request result: $granted" }

                // Reload platform status after permission change
                if (granted) {
                    loadPlatformStatus()
                }
            } catch (e: Exception) {
                logger.e(e) { "Failed to request permissions" }
                _platformState.value = _platformState.value.copy(
                    isRequestingPermissions = false,
                    error = e.message
                )
            }
        }
    }

    /**
     * Perform platform sync
     */
    fun performSync() {
        screenModelScope.launch {
            try {
                _platformState.value = _platformState.value.copy(isSyncing = true)
                platformSyncService.performFullSync()
                _platformState.value = _platformState.value.copy(
                    isSyncing = false,
                    lastSyncTime = System.currentTimeMillis()
                )
                logger.i { "Platform sync completed" }
            } catch (e: Exception) {
                logger.e(e) { "Failed to perform sync" }
                _platformState.value = _platformState.value.copy(
                    isSyncing = false,
                    error = e.message
                )
            }
        }
    }

    /**
     * Start background services
     */
    fun startBackgroundServices() {
        try {
            platformSyncService.startBackgroundServices()
            _platformState.value = _platformState.value.copy(servicesRunning = true)
            logger.i { "Background services started" }
        } catch (e: Exception) {
            logger.e(e) { "Failed to start background services" }
        }
    }

    /**
     * Stop background services
     */
    fun stopBackgroundServices() {
        try {
            platformSyncService.stopBackgroundServices()
            _platformState.value = _platformState.value.copy(servicesRunning = false)
            logger.i { "Background services stopped" }
        } catch (e: Exception) {
            logger.e(e) { "Failed to stop background services" }
        }
    }

    // Preference management methods

    /**
     * Toggle dark mode
     */
    fun setDarkMode(enabled: Boolean) {
        screenModelScope.launch {
            try {
                appPreferences.setDarkMode(enabled)
                logger.i { "Dark mode ${if (enabled) "enabled" else "disabled"}" }
            } catch (e: Exception) {
                logger.e(e) { "Failed to set dark mode" }
            }
        }
    }

    /**
     * Set use system theme
     */
    fun setUseSystemTheme(enabled: Boolean) {
        screenModelScope.launch {
            try {
                appPreferences.setUseSystemTheme(enabled)
                logger.i { "Use system theme ${if (enabled) "enabled" else "disabled"}" }
            } catch (e: Exception) {
                logger.e(e) { "Failed to set use system theme" }
            }
        }
    }

    /**
     * Toggle notifications
     */
    fun setNotificationsEnabled(enabled: Boolean) {
        screenModelScope.launch {
            try {
                appPreferences.setNotificationsEnabled(enabled)
                logger.i { "Notifications ${if (enabled) "enabled" else "disabled"}" }
            } catch (e: Exception) {
                logger.e(e) { "Failed to set notifications" }
            }
        }
    }

    /**
     * Toggle daily report
     */
    fun setDailyReportEnabled(enabled: Boolean) {
        screenModelScope.launch {
            try {
                appPreferences.setDailyReportEnabled(enabled)
                logger.i { "Daily report ${if (enabled) "enabled" else "disabled"}" }
            } catch (e: Exception) {
                logger.e(e) { "Failed to set daily report" }
            }
        }
    }

    /**
     * Toggle weekly report
     */
    fun setWeeklyReportEnabled(enabled: Boolean) {
        screenModelScope.launch {
            try {
                appPreferences.setWeeklyReportEnabled(enabled)
                logger.i { "Weekly report ${if (enabled) "enabled" else "disabled"}" }
            } catch (e: Exception) {
                logger.e(e) { "Failed to set weekly report" }
            }
        }
    }

    /**
     * Clear all app data (database + preferences)
     */
    fun clearAllData() {
        screenModelScope.launch {
            try {
                logger.i { "Clearing all data..." }
                // Clear database
                database.apply {
                    appUsageQueries.deleteAll()
                    blockedAppQueries.deleteAll()
                    blockSessionQueries.deleteAll()
                    appQueries.deleteAll()
                }
                // Clear preferences
                appPreferences.clearAll()
                logger.i { "All data cleared successfully" }
            } catch (e: Exception) {
                logger.e(e) { "Failed to clear data" }
            }
        }
    }
}

/**
 * State for platform management
 */
data class PlatformState(
    val hasPermissions: Boolean = false,
    val platformInfo: Map<String, String> = emptyMap(),
    val isLoading: Boolean = true,
    val isRequestingPermissions: Boolean = false,
    val isSyncing: Boolean = false,
    val servicesRunning: Boolean = false,
    val lastSyncTime: Long? = null,
    val error: String? = null
)
