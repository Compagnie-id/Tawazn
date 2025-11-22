package id.compagnie.tawazn.domain.repository

import id.compagnie.tawazn.domain.model.DistractingApp
import id.compagnie.tawazn.domain.model.PhoneHabit
import id.compagnie.tawazn.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing user profile data
 */
interface UserProfileRepository {
    /**
     * Get user profile as a Flow
     */
    fun getUserProfile(): Flow<UserProfile?>

    /**
     * Save user's name and age
     */
    suspend fun saveUserInfo(name: String, age: Int)

    /**
     * Save daily screen time hours
     */
    suspend fun saveDailyScreenTime(hours: Int)

    /**
     * Save selected phone habits
     */
    suspend fun saveSelectedHabits(habits: List<PhoneHabit>)

    /**
     * Save user's guessed yearly hours
     */
    suspend fun saveGuessedYearlyHours(hours: Int)

    /**
     * Save distracting apps with time limits
     */
    suspend fun saveDistractingApps(apps: List<DistractingApp>)

    /**
     * Get user's name
     */
    suspend fun getUserName(): String?

    /**
     * Clear all user profile data
     */
    suspend fun clearUserProfile()
}
