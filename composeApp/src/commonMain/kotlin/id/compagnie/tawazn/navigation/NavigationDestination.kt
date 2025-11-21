package id.compagnie.tawazn.navigation

import cafe.adriel.voyager.core.screen.Screen
import id.compagnie.tawazn.domain.model.BlockSession

/**
 * Navigation destinations for the app
 * This sealed interface defines all possible navigation targets
 * and eliminates the need for feature-to-feature dependencies
 */
sealed interface NavigationDestination {
    data object AppBlocking : NavigationDestination
    data object UsageTracking : NavigationDestination
    data object Analytics : NavigationDestination
    data object FocusSessionList : NavigationDestination
    data class FocusSessionEdit(val session: BlockSession? = null) : NavigationDestination
    data object Profile : NavigationDestination
    data object PrivacySecurity : NavigationDestination
    data object UsageGoals : NavigationDestination
    data object About : NavigationDestination
}

/**
 * Navigator interface that features can use without depending on other features
 */
interface AppNavigator {
    fun navigateTo(destination: NavigationDestination)
}

/**
 * Provider function to convert NavigationDestination to actual Screen
 * This lives in composeApp where all features are available
 */
fun NavigationDestination.toScreen(): Screen = when (this) {
    NavigationDestination.AppBlocking -> id.compagnie.tawazn.feature.appblocking.AppBlockingScreen()
    NavigationDestination.UsageTracking -> id.compagnie.tawazn.feature.usagetracking.UsageTrackingScreen()
    NavigationDestination.Analytics -> id.compagnie.tawazn.feature.analytics.AnalyticsScreen()
    NavigationDestination.FocusSessionList -> id.compagnie.tawazn.feature.settings.FocusSessionListScreen()
    is NavigationDestination.FocusSessionEdit -> id.compagnie.tawazn.feature.settings.CreateEditFocusSessionScreen(session)
    NavigationDestination.Profile -> id.compagnie.tawazn.feature.settings.ProfileScreen()
    NavigationDestination.PrivacySecurity -> id.compagnie.tawazn.feature.settings.PrivacySecurityScreen()
    NavigationDestination.UsageGoals -> id.compagnie.tawazn.feature.settings.UsageGoalsScreen()
    NavigationDestination.About -> id.compagnie.tawazn.feature.settings.AboutScreen()
}
