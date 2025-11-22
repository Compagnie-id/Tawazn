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
 * Type of time limit for distracting apps
 */
enum class TimeLimitType {
    DURATION,  // Limit by total daily duration (e.g., 1 hour per day)
    SCHEDULE   // Limit by time schedule (e.g., only 8am-10am and 6pm-8pm)
}

/**
 * Time schedule for app usage
 * Represents allowed usage windows during the day
 */
data class TimeSchedule(
    val allowedWindows: List<TimeWindow>
)

/**
 * A time window during which app usage is allowed
 */
data class TimeWindow(
    val startHour: Int,    // 0-23
    val startMinute: Int,  // 0-59
    val endHour: Int,      // 0-23
    val endMinute: Int     // 0-59
)

/**
 * Represents a distracting app with time limit configuration
 */
data class DistractingApp(
    val packageName: String,
    val appName: String,
    val category: String,
    val limitType: TimeLimitType = TimeLimitType.DURATION,
    val dailyLimitMinutes: Int? = null,  // For DURATION type
    val schedule: TimeSchedule? = null    // For SCHEDULE type
)
