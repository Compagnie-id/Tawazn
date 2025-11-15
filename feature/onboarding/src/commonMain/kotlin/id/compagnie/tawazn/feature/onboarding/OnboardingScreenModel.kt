package id.compagnie.tawazn.feature.onboarding

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import co.touchlab.kermit.Logger
import id.compagnie.tawazn.data.service.PlatformSyncService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Screen model for onboarding flow
 * Manages platform permissions and initial setup
 */
class OnboardingScreenModel : ScreenModel, KoinComponent {

    private val platformSyncService: PlatformSyncService by inject()
    private val logger = Logger.withTag("OnboardingScreenModel")

    private val _permissionState = MutableStateFlow(PermissionState())
    val permissionState: StateFlow<PermissionState> = _permissionState.asStateFlow()

    private val _platformInfo = MutableStateFlow<Map<String, String>>(emptyMap())
    val platformInfo: StateFlow<Map<String, String>> = _platformInfo.asStateFlow()

    init {
        checkPermissions()
        loadPlatformInfo()
    }

    /**
     * Check current permission status
     */
    fun checkPermissions() {
        screenModelScope.launch {
            try {
                val hasPermissions = platformSyncService.hasRequiredPermissions()
                _permissionState.value = _permissionState.value.copy(
                    hasAllPermissions = hasPermissions,
                    isChecking = false
                )
                logger.i { "Permissions checked: $hasPermissions" }
            } catch (e: Exception) {
                logger.e(e) { "Failed to check permissions" }
                _permissionState.value = _permissionState.value.copy(
                    isChecking = false,
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
                _permissionState.value = _permissionState.value.copy(isRequesting = true)

                val granted = platformSyncService.requestPermissions()

                _permissionState.value = _permissionState.value.copy(
                    hasAllPermissions = granted,
                    isRequesting = false,
                    permissionRequested = true
                )

                logger.i { "Permissions request result: $granted" }

                // If permissions granted, perform initial sync
                if (granted) {
                    performInitialSync()
                }
            } catch (e: Exception) {
                logger.e(e) { "Failed to request permissions" }
                _permissionState.value = _permissionState.value.copy(
                    isRequesting = false,
                    error = e.message
                )
            }
        }
    }

    /**
     * Perform initial data sync
     */
    private fun performInitialSync() {
        screenModelScope.launch {
            try {
                logger.i { "Performing initial platform sync..." }
                platformSyncService.performFullSync()
                logger.i { "Initial sync completed" }
            } catch (e: Exception) {
                logger.e(e) { "Failed to perform initial sync" }
            }
        }
    }

    /**
     * Load platform information
     */
    private fun loadPlatformInfo() {
        screenModelScope.launch {
            try {
                val info = platformSyncService.getPlatformInfo()
                _platformInfo.value = info
                logger.i { "Platform info loaded: ${info.size} entries" }
            } catch (e: Exception) {
                logger.e(e) { "Failed to load platform info" }
            }
        }
    }

    /**
     * Start background services
     */
    fun startBackgroundServices() {
        try {
            platformSyncService.startBackgroundServices()
            logger.i { "Background services started" }
        } catch (e: Exception) {
            logger.e(e) { "Failed to start background services" }
        }
    }
}

/**
 * State for permission management
 */
data class PermissionState(
    val hasAllPermissions: Boolean = false,
    val isChecking: Boolean = true,
    val isRequesting: Boolean = false,
    val permissionRequested: Boolean = false,
    val error: String? = null
)
