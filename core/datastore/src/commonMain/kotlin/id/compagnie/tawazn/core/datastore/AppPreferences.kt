package id.compagnie.tawazn.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * App preferences using DataStore
 * Manages user preferences like theme, notifications, etc.
 */
class AppPreferences(private val dataStore: DataStore<Preferences>) {

    companion object {
        // Theme
        val DARK_MODE = booleanPreferencesKey("dark_mode")
        val USE_SYSTEM_THEME = booleanPreferencesKey("use_system_theme")

        // Notifications
        val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
        val DAILY_REPORT_ENABLED = booleanPreferencesKey("daily_report_enabled")
        val WEEKLY_REPORT_ENABLED = booleanPreferencesKey("weekly_report_enabled")
        val DAILY_REPORT_TIME_HOUR = intPreferencesKey("daily_report_time_hour")
        val DAILY_REPORT_TIME_MINUTE = intPreferencesKey("daily_report_time_minute")

        // Goals
        val DAILY_USAGE_GOAL_MINUTES = intPreferencesKey("daily_usage_goal_minutes")
        val WEEKLY_USAGE_GOAL_MINUTES = intPreferencesKey("weekly_usage_goal_minutes")

        // Onboarding
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
        val FIRST_LAUNCH_TIME = longPreferencesKey("first_launch_time")

        // Analytics & Privacy
        val CURRENT_STREAK = intPreferencesKey("current_streak")
        val LONGEST_STREAK = intPreferencesKey("longest_streak")
        val LAST_STREAK_UPDATE = longPreferencesKey("last_streak_update")
        val ANALYTICS_ENABLED = booleanPreferencesKey("analytics_enabled")
        val CRASH_REPORTS_ENABLED = booleanPreferencesKey("crash_reports_enabled")

        // User Profile
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_EMAIL = stringPreferencesKey("user_email")
        val USER_AGE = intPreferencesKey("user_age")
        val DAILY_SCREEN_TIME_HOURS = intPreferencesKey("daily_screen_time_hours")
        val SELECTED_HABITS = stringPreferencesKey("selected_habits") // Comma-separated habit keys
        val GUESSED_YEARLY_HOURS = intPreferencesKey("guessed_yearly_hours")
        val DISTRACTING_APPS = stringPreferencesKey("distracting_apps") // JSON encoded list
    }

    // Theme preferences
    val darkMode: Flow<Boolean> = dataStore.data.map { it[DARK_MODE] ?: false }
    val useSystemTheme: Flow<Boolean> = dataStore.data.map { it[USE_SYSTEM_THEME] ?: true }

    suspend fun setDarkMode(enabled: Boolean) {
        dataStore.edit { it[DARK_MODE] = enabled }
    }

    suspend fun setUseSystemTheme(enabled: Boolean) {
        dataStore.edit { it[USE_SYSTEM_THEME] = enabled }
    }

    // Notification preferences
    val notificationsEnabled: Flow<Boolean> = dataStore.data.map { it[NOTIFICATIONS_ENABLED] ?: true }
    val dailyReportEnabled: Flow<Boolean> = dataStore.data.map { it[DAILY_REPORT_ENABLED] ?: true }
    val weeklyReportEnabled: Flow<Boolean> = dataStore.data.map { it[WEEKLY_REPORT_ENABLED] ?: true }

    suspend fun setNotificationsEnabled(enabled: Boolean) {
        dataStore.edit { it[NOTIFICATIONS_ENABLED] = enabled }
    }

    suspend fun setDailyReportEnabled(enabled: Boolean) {
        dataStore.edit { it[DAILY_REPORT_ENABLED] = enabled }
    }

    suspend fun setWeeklyReportEnabled(enabled: Boolean) {
        dataStore.edit { it[WEEKLY_REPORT_ENABLED] = enabled }
    }

    suspend fun setDailyReportTime(hour: Int, minute: Int) {
        dataStore.edit {
            it[DAILY_REPORT_TIME_HOUR] = hour
            it[DAILY_REPORT_TIME_MINUTE] = minute
        }
    }

    // Goal preferences
    val dailyUsageGoalMinutes: Flow<Int> = dataStore.data.map { it[DAILY_USAGE_GOAL_MINUTES] ?: 180 } // Default 3 hours
    val weeklyUsageGoalMinutes: Flow<Int> = dataStore.data.map { it[WEEKLY_USAGE_GOAL_MINUTES] ?: 1260 } // Default 21 hours

    suspend fun setDailyUsageGoal(minutes: Int) {
        dataStore.edit { it[DAILY_USAGE_GOAL_MINUTES] = minutes }
    }

    suspend fun setWeeklyUsageGoal(minutes: Int) {
        dataStore.edit { it[WEEKLY_USAGE_GOAL_MINUTES] = minutes }
    }

    // Onboarding
    val onboardingCompleted: Flow<Boolean> = dataStore.data.map { it[ONBOARDING_COMPLETED] ?: false }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        dataStore.edit {
            it[ONBOARDING_COMPLETED] = completed
            if (completed && it[FIRST_LAUNCH_TIME] == null) {
                it[FIRST_LAUNCH_TIME] = System.currentTimeMillis()
            }
        }
    }

    // Streak tracking
    val currentStreak: Flow<Int> = dataStore.data.map { it[CURRENT_STREAK] ?: 0 }
    val longestStreak: Flow<Int> = dataStore.data.map { it[LONGEST_STREAK] ?: 0 }

    suspend fun updateStreak(currentStreak: Int) {
        dataStore.edit {
            it[CURRENT_STREAK] = currentStreak
            val longest = it[LONGEST_STREAK] ?: 0
            if (currentStreak > longest) {
                it[LONGEST_STREAK] = currentStreak
            }
            it[LAST_STREAK_UPDATE] = System.currentTimeMillis()
        }
    }

    suspend fun resetStreak() {
        dataStore.edit { it[CURRENT_STREAK] = 0 }
    }

    // Analytics & Privacy
    val analyticsEnabled: Flow<Boolean> = dataStore.data.map { it[ANALYTICS_ENABLED] ?: true }
    val crashReportsEnabled: Flow<Boolean> = dataStore.data.map { it[CRASH_REPORTS_ENABLED] ?: true }

    suspend fun setAnalyticsEnabled(enabled: Boolean) {
        dataStore.edit { it[ANALYTICS_ENABLED] = enabled }
    }

    suspend fun setCrashReportsEnabled(enabled: Boolean) {
        dataStore.edit { it[CRASH_REPORTS_ENABLED] = enabled }
    }

    // User profile
    val userName: Flow<String> = dataStore.data.map { it[USER_NAME] ?: "" }
    val userEmail: Flow<String> = dataStore.data.map { it[USER_EMAIL] ?: "" }

    // Aliases for consistency
    val username: Flow<String> = userName
    val email: Flow<String> = userEmail

    suspend fun setUserName(name: String) {
        dataStore.edit { it[USER_NAME] = name }
    }

    suspend fun setUserEmail(email: String) {
        dataStore.edit { it[USER_EMAIL] = email }
    }

    suspend fun setUsername(name: String) = setUserName(name)
    suspend fun setEmail(email: String) = setUserEmail(email)

    // Clear all data
    suspend fun clearAll() {
        dataStore.edit { it.clear() }
    }
}
