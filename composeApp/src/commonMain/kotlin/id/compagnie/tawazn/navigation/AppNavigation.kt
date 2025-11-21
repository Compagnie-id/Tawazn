@file:OptIn(ExperimentalVoyagerApi::class)

package id.compagnie.tawazn.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import cafe.adriel.voyager.navigator.tab.TabOptions
import cafe.adriel.voyager.transitions.SlideTransition
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Bold
import com.adamglin.phosphoricons.bold.ChartBar
import com.adamglin.phosphoricons.bold.Gear
import com.adamglin.phosphoricons.bold.House
import com.adamglin.phosphoricons.bold.SquaresFour
import id.compagnie.tawazn.design.theme.TawaznTheme
import id.compagnie.tawazn.feature.dashboard.DashboardNavigation
import id.compagnie.tawazn.feature.dashboard.LocalDashboardNavigation
import id.compagnie.tawazn.feature.analytics.AnalyticsNavigation
import id.compagnie.tawazn.feature.analytics.LocalAnalyticsNavigation

/**
 * Main navigation container with bottom tab navigation
 * Manages the primary app navigation structure
 */
@Composable
fun AppNavigation() {
    TabNavigator(DashboardTab) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            bottomBar = { BottomNavigationBar() }
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

/**
 * Bottom navigation bar with neubrutalism style
 * Displays 4 main tabs: Home, Apps, Insights, Settings
 */
@Composable
private fun BottomNavigationBar() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(TawaznTheme.colors.card)
    ) {
        // Top border only
        HorizontalDivider(
            thickness = TawaznTheme.colors.borderWidth,
            color = TawaznTheme.colors.border
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TabNavigationItem(DashboardTab)
            TabNavigationItem(AppsTab)
            TabNavigationItem(AnalyticsTab)
            TabNavigationItem(SettingsTab)
        }
    }
}

@Composable
private fun RowScope.TabNavigationItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current
    val isSelected = tabNavigator.current == tab
    val shape = RoundedCornerShape(8.dp)

    Box(
        modifier = Modifier
            .weight(1f)
            .padding(horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        // Inner Box to properly size the shadow
        Box {
            // Shadow for selected state (smaller than cards for compact tabs)
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .offset(x = 2.dp, y = 2.dp)
                        .clip(shape)
                        .background(
                            color = TawaznTheme.colors.shadow,
                            shape = shape
                        )
                )
            }

            // Main tab content
            Column(
                modifier = Modifier
                    .clip(shape)
                    .then(
                        if (isSelected) {
                            Modifier
                                .background(TawaznTheme.colors.cardYellow)
                                .border(
                                    width = 2.dp,
                                    color = TawaznTheme.colors.border,
                                    shape = shape
                                )
                        } else {
                            Modifier.background(Color.Transparent)
                        }
                    )
                    .clickable { tabNavigator.current = tab }
                    .padding(vertical = 8.dp, horizontal = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    painter = tab.options.icon!!,
                    contentDescription = tab.options.title,
                    modifier = Modifier.size(24.dp),
                    tint = if (isSelected) {
                        TawaznTheme.colors.border
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
                Text(
                    text = tab.options.title,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSelected) {
                        TawaznTheme.colors.border
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }
        }
    }
}

// Tab Definitions - Each tab wraps its feature screen in a Navigator

object DashboardTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(PhosphorIcons.Bold.House)
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
            // Provide navigation callbacks to Dashboard
            androidx.compose.runtime.CompositionLocalProvider(
                LocalDashboardNavigation provides DashboardNavigation(
                    onBlockAppsClick = { navigator.push(id.compagnie.tawazn.feature.appblocking.AppBlockingScreen()) },
                    onViewUsageClick = { navigator.push(id.compagnie.tawazn.feature.usagetracking.UsageTrackingScreen()) },
                    onManageSessionsClick = { navigator.push(id.compagnie.tawazn.feature.settings.FocusSessionListScreen()) }
                )
            ) {
                SlideTransition(
                    navigator = navigator,
                    disposeScreenAfterTransitionEnd = true
                )
            }
        }
    }
}

object AppsTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(PhosphorIcons.Bold.SquaresFour)
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
            val icon = rememberVectorPainter(PhosphorIcons.Bold.ChartBar)
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
            // Provide navigation callbacks to Analytics
            androidx.compose.runtime.CompositionLocalProvider(
                LocalAnalyticsNavigation provides AnalyticsNavigation(
                    onManageSessionsClick = { navigator.push(id.compagnie.tawazn.feature.settings.FocusSessionListScreen()) }
                )
            ) {
                SlideTransition(
                    navigator = navigator,
                    disposeScreenAfterTransitionEnd = true
                )
            }
        }
    }
}

object SettingsTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(PhosphorIcons.Bold.Gear)
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
