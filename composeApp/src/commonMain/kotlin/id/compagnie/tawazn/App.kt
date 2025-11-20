package id.compagnie.tawazn

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import id.compagnie.tawazn.core.datastore.AppPreferences
import id.compagnie.tawazn.design.theme.TawaznTheme
import id.compagnie.tawazn.feature.onboarding.OnboardingScreen
import id.compagnie.tawazn.navigation.AppNavigation
import org.koin.compose.KoinContext
import org.koin.compose.koinInject

@Composable
fun App() {
    KoinContext {
        val appPreferences: AppPreferences = koinInject()
        val darkModeEnabled by appPreferences.darkMode.collectAsState(initial = false)
        val useSystemTheme by appPreferences.useSystemTheme.collectAsState(initial = true)
        val onboardingCompleted by appPreferences.onboardingCompleted.collectAsState(initial = false)
        val systemInDarkTheme = isSystemInDarkTheme()

        // Determine final dark theme value
        val darkTheme = if (useSystemTheme) systemInDarkTheme else darkModeEnabled

        TawaznTheme(darkTheme = darkTheme) {
            // Conditionally show screens without Navigator wrapper
            // AppNavigation contains TabNavigator, tabs contain their own Navigators
            if (onboardingCompleted) {
                AppNavigation()
            } else {
                OnboardingScreen().Content()
            }
        }
    }
}
