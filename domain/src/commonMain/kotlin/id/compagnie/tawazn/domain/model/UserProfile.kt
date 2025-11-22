package id.compagnie.tawazn.domain.model

/**
 * Domain model representing user profile information
 */
data class UserProfile(
    val name: String,
    val age: Int,
    val dailyScreenTimeHours: Int,
    val selectedHabits: List<PhoneHabit>,
    val guessedYearlyHours: Int? = null,
    val distractingApps: List<DistractingApp> = emptyList()
)

/**
 * Phone habits that users want to change
 */
enum class PhoneHabit(val key: String) {
    FEELING_BAD("feeling_bad"),
    USING_IN_BED("using_in_bed"),
    SCROLLING_MORNING("scrolling_morning"),
    CONSTANTLY_CHECKING("constantly_checking"),
    MINDLESS_SCROLLING("mindless_scrolling"),
    IGNORING_PEOPLE("ignoring_people"),
    USING_WHEN_GATHERING("using_when_gathering");

    companion object {
        fun fromKey(key: String): PhoneHabit? = entries.find { it.key == key }
    }
}

/**
 * Represents a distracting app with time limit
 */
data class DistractingApp(
    val packageName: String,
    val appName: String,
    val category: String,
    val dailyLimitMinutes: Int // Total daily limit in minutes
)
