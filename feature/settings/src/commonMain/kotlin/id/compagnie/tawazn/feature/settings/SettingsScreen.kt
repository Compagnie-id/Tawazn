package id.compagnie.tawazn.feature.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import id.compagnie.tawazn.design.component.GlassCard
import id.compagnie.tawazn.design.theme.TawaznTheme

class SettingsScreen : Screen {

    @Composable
    override fun Content() {
        SettingsContent()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsContent() {
    val navigator = LocalNavigator.currentOrThrow
    var notificationsEnabled by remember { mutableStateOf(true) }
    var dailyReportEnabled by remember { mutableStateOf(true) }
    var weeklyReportEnabled by remember { mutableStateOf(true) }
    var darkModeEnabled by remember { mutableStateOf(false) }

    TawaznTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Settings") },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(Icons.Default.ArrowBack, "Back")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.9f)
                    )
                )
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Account Section
                item {
                    SectionHeader("Account")
                }

                item {
                    SettingsItem(
                        icon = Icons.Default.Person,
                        title = "Profile",
                        subtitle = "Manage your profile",
                        onClick = { /* TODO */ }
                    )
                }

                item {
                    SettingsItem(
                        icon = Icons.Default.Security,
                        title = "Privacy & Security",
                        subtitle = "Control your data",
                        onClick = { /* TODO */ }
                    )
                }

                // Notifications Section
                item {
                    SectionHeader("Notifications")
                }

                item {
                    SettingsSwitchItem(
                        icon = Icons.Default.Notifications,
                        title = "Notifications",
                        subtitle = "Enable notifications",
                        checked = notificationsEnabled,
                        onCheckedChange = { notificationsEnabled = it }
                    )
                }

                item {
                    SettingsSwitchItem(
                        icon = Icons.Default.Today,
                        title = "Daily Report",
                        subtitle = "Get daily screen time summary",
                        checked = dailyReportEnabled,
                        onCheckedChange = { dailyReportEnabled = it }
                    )
                }

                item {
                    SettingsSwitchItem(
                        icon = Icons.Default.CalendarMonth,
                        title = "Weekly Report",
                        subtitle = "Get weekly insights",
                        checked = weeklyReportEnabled,
                        onCheckedChange = { weeklyReportEnabled = it }
                    )
                }

                // Appearance Section
                item {
                    SectionHeader("Appearance")
                }

                item {
                    SettingsSwitchItem(
                        icon = Icons.Default.DarkMode,
                        title = "Dark Mode",
                        subtitle = "Use dark theme",
                        checked = darkModeEnabled,
                        onCheckedChange = { darkModeEnabled = it }
                    )
                }

                // Data & Storage Section
                item {
                    SectionHeader("Data & Storage")
                }

                item {
                    SettingsItem(
                        icon = Icons.Default.CloudUpload,
                        title = "Backup & Sync",
                        subtitle = "Cloud backup (Coming soon)",
                        onClick = { /* TODO */ }
                    )
                }

                item {
                    SettingsItem(
                        icon = Icons.Default.DeleteForever,
                        title = "Clear Data",
                        subtitle = "Delete all usage data",
                        onClick = { /* TODO: Show confirmation */ }
                    )
                }

                // Permissions Section
                item {
                    SectionHeader("Permissions")
                }

                item {
                    SettingsItem(
                        icon = Icons.Default.PermDeviceInformation,
                        title = "Usage Access",
                        subtitle = "Required for tracking",
                        onClick = { /* TODO: Open settings */ }
                    )
                }

                item {
                    SettingsItem(
                        icon = Icons.Default.Accessibility,
                        title = "Accessibility Service",
                        subtitle = "Required for blocking (Android)",
                        onClick = { /* TODO: Open settings */ }
                    )
                }

                // About Section
                item {
                    SectionHeader("About")
                }

                item {
                    SettingsItem(
                        icon = Icons.Default.Info,
                        title = "About Tawazn",
                        subtitle = "Version 1.0.0",
                        onClick = { /* TODO */ }
                    )
                }

                item {
                    SettingsItem(
                        icon = Icons.Default.Description,
                        title = "Privacy Policy",
                        subtitle = "Read our privacy policy",
                        onClick = { /* TODO */ }
                    )
                }

                item {
                    SettingsItem(
                        icon = Icons.Default.Gavel,
                        title = "Terms of Service",
                        subtitle = "Read our terms",
                        onClick = { /* TODO */ }
                    )
                }

                item {
                    SettingsItem(
                        icon = Icons.Default.BugReport,
                        title = "Report a Bug",
                        subtitle = "Help us improve",
                        onClick = { /* TODO */ }
                    )
                }

                // Spacer at the bottom
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                }

                item {
                    Text(
                        text = "Made with ❤️ using Compose Multiplatform",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = TawaznTheme.colors.gradientMiddle,
        modifier = Modifier.padding(top = 8.dp)
    )
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = TawaznTheme.colors.gradientMiddle,
                modifier = Modifier.size(24.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Navigate",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun SettingsSwitchItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = TawaznTheme.colors.gradientMiddle,
                modifier = Modifier.size(24.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = androidx.compose.ui.graphics.Color.White,
                    checkedTrackColor = TawaznTheme.colors.gradientMiddle,
                    uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                    uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        }
    }
}
