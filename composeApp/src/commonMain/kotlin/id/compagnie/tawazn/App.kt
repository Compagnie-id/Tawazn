package id.compagnie.tawazn

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import id.compagnie.tawazn.core.datastore.AppPreferences
import id.compagnie.tawazn.design.theme.TawaznTheme
import id.compagnie.tawazn.feature.main.MainScreen
import id.compagnie.tawazn.feature.onboarding.OnboardingScreen
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

        // Track if we've already handled onboarding completion to avoid repeated navigation
        var hasNavigatedToMain by remember { mutableStateOf(onboardingCompleted) }

        TawaznTheme(darkTheme = darkTheme) {
            // Show onboarding screen if not completed, otherwise show main screen with tabs
            val initialScreen = if (onboardingCompleted) MainScreen() else OnboardingScreen()
            Navigator(initialScreen) { navigator ->
                // Handle navigation when onboarding is completed
                LaunchedEffect(onboardingCompleted) {
                    if (onboardingCompleted && !hasNavigatedToMain) {
                        hasNavigatedToMain = true
                        navigator.replaceAll(MainScreen())
                    }
                }

                SlideTransition(navigator)
            }
        }
    }
}
