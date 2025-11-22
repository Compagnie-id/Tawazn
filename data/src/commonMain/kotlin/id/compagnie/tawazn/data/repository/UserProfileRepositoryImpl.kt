package id.compagnie.tawazn.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import id.compagnie.tawazn.core.datastore.AppPreferences
import id.compagnie.tawazn.domain.model.DistractingApp
import id.compagnie.tawazn.domain.model.PhoneHabit
import id.compagnie.tawazn.domain.model.UserProfile
import id.compagnie.tawazn.domain.repository.UserProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class UserProfileRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
    private val json: Json
) : UserProfileRepository {

    override fun getUserProfile(): Flow<UserProfile?> {
        return dataStore.data.map { prefs ->
            val name = prefs[AppPreferences.USER_NAME] ?: return@map null
            val age = prefs[AppPreferences.USER_AGE] ?: return@map null
            val dailyScreenTimeHours = prefs[AppPreferences.DAILY_SCREEN_TIME_HOURS] ?: return@map null

            val habitsString = prefs[AppPreferences.SELECTED_HABITS] ?: ""
            val selectedHabits = if (habitsString.isNotEmpty()) {
                habitsString.split(",").mapNotNull { PhoneHabit.fromKey(it) }
            } else {
                emptyList()
            }

            val guessedYearlyHours = prefs[AppPreferences.GUESSED_YEARLY_HOURS]

            val distractingAppsJson = prefs[AppPreferences.DISTRACTING_APPS] ?: ""
            val distractingApps = if (distractingAppsJson.isNotEmpty()) {
                try {
                    json.decodeFromString<List<DistractingAppDto>>(distractingAppsJson)
                        .map { it.toDomain() }
                } catch (e: Exception) {
                    emptyList()
                }
            } else {
                emptyList()
            }

            UserProfile(
                name = name,
                age = age,
                dailyScreenTimeHours = dailyScreenTimeHours,
                selectedHabits = selectedHabits,
                guessedYearlyHours = guessedYearlyHours,
                distractingApps = distractingApps
            )
        }
    }

    override suspend fun saveUserInfo(name: String, age: Int) {
        dataStore.edit { prefs ->
            prefs[AppPreferences.USER_NAME] = name
            prefs[AppPreferences.USER_AGE] = age
        }
    }

    override suspend fun saveDailyScreenTime(hours: Int) {
        dataStore.edit { prefs ->
            prefs[AppPreferences.DAILY_SCREEN_TIME_HOURS] = hours
        }
    }

    override suspend fun saveSelectedHabits(habits: List<PhoneHabit>) {
        dataStore.edit { prefs ->
            prefs[AppPreferences.SELECTED_HABITS] = habits.joinToString(",") { it.key }
        }
    }

    override suspend fun saveGuessedYearlyHours(hours: Int) {
        dataStore.edit { prefs ->
            prefs[AppPreferences.GUESSED_YEARLY_HOURS] = hours
        }
    }

    override suspend fun saveDistractingApps(apps: List<DistractingApp>) {
        val dtoList = apps.map { DistractingAppDto.fromDomain(it) }
        val jsonString = json.encodeToString(dtoList)
        dataStore.edit { prefs ->
            prefs[AppPreferences.DISTRACTING_APPS] = jsonString
        }
    }

    override suspend fun getUserName(): String? {
        return try {
            dataStore.data
                .map { prefs -> prefs[AppPreferences.USER_NAME] }
                .firstOrNull()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun clearUserProfile() {
        dataStore.edit { prefs ->
            prefs.remove(AppPreferences.USER_NAME)
            prefs.remove(AppPreferences.USER_AGE)
            prefs.remove(AppPreferences.DAILY_SCREEN_TIME_HOURS)
            prefs.remove(AppPreferences.SELECTED_HABITS)
            prefs.remove(AppPreferences.GUESSED_YEARLY_HOURS)
            prefs.remove(AppPreferences.DISTRACTING_APPS)
        }
    }

    @Serializable
    private data class DistractingAppDto(
        val packageName: String,
        val appName: String,
        val category: String,
        val dailyLimitMinutes: Int
    ) {
        fun toDomain() = DistractingApp(
            packageName = packageName,
            appName = appName,
            category = category,
            dailyLimitMinutes = dailyLimitMinutes
        )

        companion object {
            fun fromDomain(app: DistractingApp) = DistractingAppDto(
                packageName = app.packageName,
                appName = app.appName,
                category = app.category,
                dailyLimitMinutes = app.dailyLimitMinutes
            )
        }
    }
}
