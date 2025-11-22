package id.compagnie.tawazn.feature.onboarding

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import co.touchlab.kermit.Logger
import id.compagnie.tawazn.core.datastore.AppPreferences
import id.compagnie.tawazn.data.service.PlatformSyncService
import id.compagnie.tawazn.domain.model.AppInfo
import id.compagnie.tawazn.domain.model.DistractingApp
import id.compagnie.tawazn.domain.model.PhoneHabit
import id.compagnie.tawazn.domain.repository.UserProfileRepository
import id.compagnie.tawazn.domain.usecase.GetNonSystemAppsUseCase
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
    private val appPreferences: AppPreferences by inject()
    private val userProfileRepository: UserProfileRepository by inject()
    private val getNonSystemAppsUseCase: GetNonSystemAppsUseCase by inject()
    private val logger = Logger.withTag("OnboardingScreenModel")

    private val _permissionState = MutableStateFlow(PermissionState())
    val permissionState: StateFlow<PermissionState> = _permissionState.asStateFlow()

    private val _platformInfo = MutableStateFlow<Map<String, String>>(emptyMap())
    val platformInfo: StateFlow<Map<String, String>> = _platformInfo.asStateFlow()

    private val _onboardingCompleted = MutableStateFlow(false)
    val onboardingCompleted: StateFlow<Boolean> = _onboardingCompleted.asStateFlow()

    // User profile states
    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _userAge = MutableStateFlow<Int?>(null)
    val userAge: StateFlow<Int?> = _userAge.asStateFlow()

    private val _dailyScreenTimeHours = MutableStateFlow<Int?>(null)
    val dailyScreenTimeHours: StateFlow<Int?> = _dailyScreenTimeHours.asStateFlow()

    private val _selectedHabits = MutableStateFlow<List<PhoneHabit>>(emptyList())
    val selectedHabits: StateFlow<List<PhoneHabit>> = _selectedHabits.asStateFlow()

    private val _guessedYearlyHours = MutableStateFlow<Int?>(null)
    val guessedYearlyHours: StateFlow<Int?> = _guessedYearlyHours.asStateFlow()

    private val _distractingApps = MutableStateFlow<List<DistractingApp>>(emptyList())
    val distractingApps: StateFlow<List<DistractingApp>> = _distractingApps.asStateFlow()

    private val _installedApps = MutableStateFlow<List<AppInfo>>(emptyList())
    val installedApps: StateFlow<List<AppInfo>> = _installedApps.asStateFlow()

    private val _isLoadingApps = MutableStateFlow(false)
    val isLoadingApps: StateFlow<Boolean> = _isLoadingApps.asStateFlow()

    init {
        checkPermissions()
        loadPlatformInfo()
        loadInstalledApps()
    }

    /**
     * Check current permission status
     */
    fun checkPermissions() {
        screenModelScope.launch {
            try {
                _permissionState.value = _permissionState.value.copy(isChecking = true)
                val hasPermissions = platformSyncService.hasRequiredPermissions()
                _permissionState.value = _permissionState.value.copy(
                    hasAllPermissions = hasPermissions,
                    hasUsageStatsPermission = hasPermissions,
                    hasAccessibilityPermission = hasPermissions,
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
                    hasUsageStatsPermission = granted,
                    hasAccessibilityPermission = granted,
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

    /**
     * Save user name and age
     */
    fun saveUserInfo(name: String, age: Int) {
        screenModelScope.launch {
            try {
                _userName.value = name
                _userAge.value = age
                userProfileRepository.saveUserInfo(name, age)
                logger.i { "User info saved: $name, age $age" }
            } catch (e: Exception) {
                logger.e(e) { "Failed to save user info" }
            }
        }
    }

    /**
     * Save daily screen time hours
     */
    fun saveDailyScreenTime(hours: Int) {
        screenModelScope.launch {
            try {
                _dailyScreenTimeHours.value = hours
                userProfileRepository.saveDailyScreenTime(hours)
                logger.i { "Daily screen time saved: $hours hours" }
            } catch (e: Exception) {
                logger.e(e) { "Failed to save daily screen time" }
            }
        }
    }

    /**
     * Toggle habit selection
     */
    fun toggleHabit(habit: PhoneHabit) {
        val currentHabits = _selectedHabits.value.toMutableList()
        if (currentHabits.contains(habit)) {
            currentHabits.remove(habit)
        } else {
            currentHabits.add(habit)
        }
        _selectedHabits.value = currentHabits
    }

    /**
     * Save selected habits
     */
    fun saveSelectedHabits() {
        screenModelScope.launch {
            try {
                userProfileRepository.saveSelectedHabits(_selectedHabits.value)
                logger.i { "Selected habits saved: ${_selectedHabits.value.size} habits" }
            } catch (e: Exception) {
                logger.e(e) { "Failed to save selected habits" }
            }
        }
    }

    /**
     * Save guessed yearly hours
     */
    fun saveGuessedYearlyHours(hours: Int) {
        screenModelScope.launch {
            try {
                _guessedYearlyHours.value = hours
                userProfileRepository.saveGuessedYearlyHours(hours)
                logger.i { "Guessed yearly hours saved: $hours" }
            } catch (e: Exception) {
                logger.e(e) { "Failed to save guessed yearly hours" }
            }
        }
    }

    /**
     * Add or update a distracting app
     */
    fun updateDistractingApp(app: DistractingApp) {
        val currentApps = _distractingApps.value.toMutableList()
        val existingIndex = currentApps.indexOfFirst { it.packageName == app.packageName }
        if (existingIndex >= 0) {
            currentApps[existingIndex] = app
        } else {
            currentApps.add(app)
        }
        _distractingApps.value = currentApps
    }

    /**
     * Remove a distracting app
     */
    fun removeDistractingApp(packageName: String) {
        _distractingApps.value = _distractingApps.value.filter { it.packageName != packageName }
    }

    /**
     * Save distracting apps
     */
    fun saveDistractingApps() {
        screenModelScope.launch {
            try {
                userProfileRepository.saveDistractingApps(_distractingApps.value)
                logger.i { "Distracting apps saved: ${_distractingApps.value.size} apps" }
            } catch (e: Exception) {
                logger.e(e) { "Failed to save distracting apps" }
            }
        }
    }

    /**
     * Observe installed apps from database
     * This continuously syncs the state with the database
     */
    private fun loadInstalledApps() {
        screenModelScope.launch {
            try {
                getNonSystemAppsUseCase().collect { apps ->
                    _installedApps.value = apps
                    logger.i { "Installed apps updated: ${apps.size} apps" }
                }
            } catch (e: Exception) {
                logger.e(e) { "Failed to observe installed apps" }
            }
        }
    }

    /**
     * Manually refresh installed apps from system
     * Useful when apps haven't been synced yet
     */
    fun refreshInstalledApps() {
        // Prevent multiple simultaneous refreshes
        if (_isLoadingApps.value) {
            logger.w { "App refresh already in progress, skipping" }
            return
        }

        screenModelScope.launch {
            try {
                _isLoadingApps.value = true
                logger.i { "Manually refreshing installed apps..." }
                platformSyncService.syncInstalledApps()
                logger.i { "Manual app refresh completed" }
                // Flow will emit and update the list, but ensure loading state is cleared
                _isLoadingApps.value = false
            } catch (e: Exception) {
                logger.e(e) { "Failed to manually refresh apps" }
                _isLoadingApps.value = false
            }
        }
    }

    /**
     * Calculate yearly hours based on daily screen time
     */
    fun calculateYearlyHours(): Int {
        val dailyHours = _dailyScreenTimeHours.value ?: 0
        return dailyHours * 365
    }

    /**
     * Calculate lifetime projection years (from current age to 80)
     */
    fun calculateLifetimeProjection(): Double {
        val dailyHours = _dailyScreenTimeHours.value ?: 0
        val currentAge = _userAge.value ?: 0
        val yearsRemaining = 80 - currentAge
        if (yearsRemaining <= 0) return 0.0

        val totalHoursRemaining = dailyHours * 365 * yearsRemaining
        return totalHoursRemaining / 24.0 / 365.0 // Convert to years
    }

    /**
     * Mark onboarding as completed
     */
    fun completeOnboarding() {
        screenModelScope.launch {
            try {
                appPreferences.setOnboardingCompleted(true)
                _onboardingCompleted.value = true
                logger.i { "Onboarding completed" }
            } catch (e: Exception) {
                logger.e(e) { "Failed to save onboarding completion" }
            }
        }
    }
}

/**
 * State for permission management
 */
data class PermissionState(
    val hasAllPermissions: Boolean = false,
    val hasUsageStatsPermission: Boolean = false,
    val hasAccessibilityPermission: Boolean = false,
    val isChecking: Boolean = true,
    val isRequesting: Boolean = false,
    val permissionRequested: Boolean = false,
    val error: String? = null
)
