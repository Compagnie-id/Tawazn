package id.compagnie.tawazn

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import id.compagnie.tawazn.core.datastore.AppPreferences
import id.compagnie.tawazn.design.theme.TawaznTheme
import id.compagnie.tawazn.feature.onboarding.OnboardingScreen
import id.compagnie.tawazn.i18n.ProvideStrings
import id.compagnie.tawazn.navigation.AppNavigation
import org.koin.compose.koinInject

@Composable
fun App() {
    val appPreferences: AppPreferences = koinInject()
    val darkModeEnabled by appPreferences.darkMode.collectAsState(initial = false)
    val useSystemTheme by appPreferences.useSystemTheme.collectAsState(initial = true)
    val onboardingCompleted by appPreferences.onboardingCompleted.collectAsState(initial = null)
    val systemInDarkTheme = isSystemInDarkTheme()

    // Determine final dark theme value
    val darkTheme = if (useSystemTheme) systemInDarkTheme else darkModeEnabled

    ProvideStrings {
        TawaznTheme(darkTheme = darkTheme) {
            when (onboardingCompleted) {
                null -> {
                    // Loading state - show blank screen while DataStore loads
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background),
                        contentAlignment = Alignment.Center
                    ) {
                        // Empty - just show background color
                        // DataStore loads very quickly (~1-2ms), no need for spinner
                    }
                }
                true -> {
                    // Onboarding completed, show main app
                    AppNavigation()
                }
                false -> {
                    // Onboarding not completed, show onboarding
                    OnboardingScreen().Content()
                }
            }
        }
    }
}
