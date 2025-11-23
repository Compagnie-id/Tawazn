@file:OptIn(ExperimentalTime::class)

package id.compagnie.tawazn.feature.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import id.compagnie.tawazn.i18n.Language
import id.compagnie.tawazn.i18n.LocalStringProvider
import id.compagnie.tawazn.i18n.stringResource
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime

/**
 * Get the appropriate greeting based on the current time of day and language
 * Uses culturally appropriate time ranges for each language
 * @return The translation key for the current time-based greeting
 */
@Composable
fun getTimeBasedGreeting(): String {
    val stringProvider = LocalStringProvider.current
    val currentLanguage by stringProvider.currentLanguage.collectAsState()

    val now = kotlin.time.Clock.System.now()
    val localDateTime = now.toLocalDateTime(TimeZone.currentSystemDefault())
    val hour = localDateTime.hour

    return when (currentLanguage) {
        Language.INDONESIAN -> {
            when (hour) {
                in 3..10 -> "dashboard.greeting.morning"     // Pagi: 3:00-10:59
                in 11..14 -> "dashboard.greeting.afternoon"  // Siang: 11:00-14:59
                in 15..17 -> "dashboard.greeting.evening"    // Sore: 15:00-17:59
                in 18..19 -> "dashboard.greeting.dusk"       // Petang: 18:00-19:59
                else -> "dashboard.greeting.night"           // Malam: 20:00-2:59
            }
        }

        Language.ARABIC -> {
            when (hour) {
                in 3..11 -> "dashboard.greeting.morning"     // صباح: 3:00-11:59
                in 12..20 -> "dashboard.greeting.afternoon"  // مساء: 12:00-20:59
                else -> "dashboard.greeting.night"           // ليل: 21:00-3:59
            }
        }

        Language.ENGLISH -> {
            when (hour) {
                in 3..11 -> "dashboard.greeting.morning"     // Morning: 5:00-11:59
                in 12..16 -> "dashboard.greeting.afternoon"  // Afternoon: 12:00-16:59
                in 17..20 -> "dashboard.greeting.evening"    // Evening: 17:00-20:59
                else -> "dashboard.greeting.night"           // Night: 21:00-4:59
            }
        }
    }
}

/**
 * Get current date and time formatted with localization
 * @return Formatted string like "Friday, Nov 22, 2024 • 08:30"
 */
@Composable
fun getFormattedDateTime(): String {
    val now = kotlin.time.Clock.System.now()
    val localDateTime = now.toLocalDateTime(TimeZone.currentSystemDefault())

    // Use value properties which are 1-based (Monday=1, January=1)
    val dayName = getDayName(localDateTime.dayOfWeek.ordinal + 1)
    val monthName = getMonthNameShort(localDateTime.month.ordinal + 1)
    val day = localDateTime.day
    val year = localDateTime.year
    val hour = localDateTime.hour.toString().padStart(2, '0')
    val minute = localDateTime.minute.toString().padStart(2, '0')

    return "$dayName, $monthName $day, $year • $hour:$minute"
}

/**
 * Get localized day name from day number (1-7, Monday-Sunday)
 */
@Composable
private fun getDayName(dayOfWeek: Int): String {
    return when (dayOfWeek) {
        1 -> stringResource("day.monday")
        2 -> stringResource("day.tuesday")
        3 -> stringResource("day.wednesday")
        4 -> stringResource("day.thursday")
        5 -> stringResource("day.friday")
        6 -> stringResource("day.saturday")
        7 -> stringResource("day.sunday")
        else -> ""
    }
}

/**
 * Get localized month abbreviation from month number (1-12)
 */
@Composable
private fun getMonthNameShort(month: Int): String {
    return when (month) {
        1 -> stringResource("month.jan")
        2 -> stringResource("month.feb")
        3 -> stringResource("month.mar")
        4 -> stringResource("month.apr")
        5 -> stringResource("month.may_short")
        6 -> stringResource("month.jun")
        7 -> stringResource("month.jul")
        8 -> stringResource("month.aug")
        9 -> stringResource("month.sep")
        10 -> stringResource("month.oct")
        11 -> stringResource("month.nov")
        12 -> stringResource("month.dec")
        else -> ""
    }
}
