package id.compagnie.tawazn.feature.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import cafe.adriel.voyager.navigator.tab.*
import id.compagnie.tawazn.design.icons.TawaznIcons
import id.compagnie.tawazn.design.theme.TawaznTheme

/**
 * Main application screen with tab-based navigation
 * Provides bottom navigation bar with 4 main tabs
 */
class MainScreen : Screen {

    @Composable
    override fun Content() {
        MainContent()
    }
}

@Composable
fun MainContent() {
    TawaznTheme {
        TabNavigator(DashboardTab) {
            Scaffold(
                containerColor = MaterialTheme.colorScheme.background,
                bottomBar = {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                    ) {
                        TabNavigationItem(DashboardTab)
                        TabNavigationItem(AppsTab)
                        TabNavigationItem(AnalyticsTab)
                        TabNavigationItem(SettingsTab)
                    }
                }
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    CurrentTab()
                }
            }
        }
    }
}

@Composable
private fun RowScope.TabNavigationItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current

    NavigationBarItem(
        selected = tabNavigator.current == tab,
        onClick = { tabNavigator.current = tab },
        icon = {
            Icon(
                painter = tab.options.icon!!,
                contentDescription = tab.options.title
            )
        },
        label = { Text(tab.options.title) },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.primary,
            selectedTextColor = MaterialTheme.colorScheme.primary,
            indicatorColor = MaterialTheme.colorScheme.primaryContainer,
            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
}

// Tab Objects
object DashboardTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(TawaznIcons.Home)
            return remember {
                TabOptions(
                    index = 0u,
                    title = "Home",
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        // Wrap in Navigator to enable navigation to sub-screens
        Navigator(
            screen = id.compagnie.tawazn.feature.dashboard.DashboardScreen(),
            disposeBehavior = cafe.adriel.voyager.navigator.NavigatorDisposeBehavior(
                disposeNestedNavigators = false,
                disposeSteps = false
            )
        ) { navigator ->
            SlideTransition(
                navigator = navigator,
                disposeScreenAfterTransitionEnd = true
            )
        }
    }
}

object AppsTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(TawaznIcons.Apps)
            return remember {
                TabOptions(
                    index = 1u,
                    title = "Apps",
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        // Wrap in Navigator to enable navigation to sub-screens
        Navigator(
            screen = id.compagnie.tawazn.feature.appblocking.AppBlockingScreen(),
            disposeBehavior = cafe.adriel.voyager.navigator.NavigatorDisposeBehavior(
                disposeNestedNavigators = false,
                disposeSteps = false
            )
        ) { navigator ->
            SlideTransition(
                navigator = navigator,
                disposeScreenAfterTransitionEnd = true
            )
        }
    }
}

object AnalyticsTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(TawaznIcons.Analytics)
            return remember {
                TabOptions(
                    index = 2u,
                    title = "Insights",
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        // Wrap in Navigator to enable navigation to sub-screens
        Navigator(
            screen = id.compagnie.tawazn.feature.analytics.AnalyticsScreen(),
            disposeBehavior = cafe.adriel.voyager.navigator.NavigatorDisposeBehavior(
                disposeNestedNavigators = false,
                disposeSteps = false
            )
        ) { navigator ->
            SlideTransition(
                navigator = navigator,
                disposeScreenAfterTransitionEnd = true
            )
        }
    }
}

object SettingsTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(TawaznIcons.Settings)
            return remember {
                TabOptions(
                    index = 3u,
                    title = "Settings",
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        // Wrap in Navigator to enable navigation to sub-screens (Profile, Privacy, etc.)
        Navigator(
            screen = id.compagnie.tawazn.feature.settings.TabSettingsScreen(),
            disposeBehavior = cafe.adriel.voyager.navigator.NavigatorDisposeBehavior(
                disposeNestedNavigators = false,
                disposeSteps = false
            )
        ) { navigator ->
            // Display current screen in the navigation stack
            SlideTransition(
                navigator = navigator,
                disposeScreenAfterTransitionEnd = true
            )
        }
    }
}
