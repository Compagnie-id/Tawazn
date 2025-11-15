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
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import id.compagnie.tawazn.design.component.GlassCard
import id.compagnie.tawazn.design.component.PermissionStatusBadge
import id.compagnie.tawazn.design.component.PlatformInfoCard
import id.compagnie.tawazn.design.theme.TawaznTheme

class SettingsScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel { SettingsScreenModel() }
        SettingsContent(screenModel)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsContent(screenModel: SettingsScreenModel) {
    val navigator = LocalNavigator.currentOrThrow
    val platformState by screenModel.platformState.collectAsState()

    // Collect preference flows
    val useSystemTheme by screenModel.useSystemTheme.collectAsState(initial = true)
    val darkModeEnabled by screenModel.darkMode.collectAsState(initial = false)
    val notificationsEnabled by screenModel.notificationsEnabled.collectAsState(initial = true)
    val dailyReportEnabled by screenModel.dailyReportEnabled.collectAsState(initial = true)
    val weeklyReportEnabled by screenModel.weeklyReportEnabled.collectAsState(initial = true)

    // Clear data dialog state
    var showClearDataDialog by remember { mutableStateOf(false) }

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
                        onClick = { navigator.push(ProfileScreen()) }
                    )
                }

                item {
                    SettingsItem(
                        icon = Icons.Default.Security,
                        title = "Privacy & Security",
                        subtitle = "Control your data",
                        onClick = { navigator.push(PrivacySecurityScreen()) }
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
                        onCheckedChange = { screenModel.setNotificationsEnabled(it) }
                    )
                }

                item {
                    SettingsSwitchItem(
                        icon = Icons.Default.Today,
                        title = "Daily Report",
                        subtitle = "Get daily screen time summary",
                        checked = dailyReportEnabled,
                        onCheckedChange = { screenModel.setDailyReportEnabled(it) }
                    )
                }

                item {
                    SettingsSwitchItem(
                        icon = Icons.Default.CalendarMonth,
                        title = "Weekly Report",
                        subtitle = "Get weekly insights",
                        checked = weeklyReportEnabled,
                        onCheckedChange = { screenModel.setWeeklyReportEnabled(it) }
                    )
                }

                // Focus & Productivity Section
                item {
                    SectionHeader("Focus & Productivity")
                }

                item {
                    SettingsItem(
                        icon = Icons.Default.LocalFireDepartment,
                        title = "Focus Sessions",
                        subtitle = "Manage scheduled blocking sessions",
                        onClick = { navigator.push(FocusSessionListScreen()) }
                    )
                }

                // Appearance Section
                item {
                    SectionHeader("Appearance")
                }

                item {
                    SettingsSwitchItem(
                        icon = Icons.Default.Settings,
                        title = "Use System Theme",
                        subtitle = "Follow system dark mode setting",
                        checked = useSystemTheme,
                        onCheckedChange = { screenModel.setUseSystemTheme(it) }
                    )
                }

                item {
                    SettingsSwitchItem(
                        icon = Icons.Default.DarkMode,
                        title = "Dark Mode",
                        subtitle = if (useSystemTheme) "Disabled (using system theme)" else "Use dark theme",
                        checked = darkModeEnabled,
                        onCheckedChange = { screenModel.setDarkMode(it) },
                        enabled = !useSystemTheme
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
                        onClick = { showClearDataDialog = true }
                    )
                }

                // Platform Status Section
                item {
                    SectionHeader("Platform Status")
                }

                item {
                    GlassCard(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Permissions",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                                PermissionStatusBadge(isGranted = platformState.hasPermissions)
                            }

                            Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = { screenModel.requestPermissions() },
                                    enabled = !platformState.isRequestingPermissions && !platformState.hasPermissions,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    if (platformState.isRequestingPermissions) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(16.dp),
                                            strokeWidth = 2.dp,
                                            color = MaterialTheme.colorScheme.onPrimary
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                    }
                                    Text(if (platformState.hasPermissions) "Granted" else "Grant Permissions")
                                }

                                OutlinedButton(
                                    onClick = { screenModel.performSync() },
                                    enabled = !platformState.isSyncing && platformState.hasPermissions,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    if (platformState.isSyncing) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(16.dp),
                                            strokeWidth = 2.dp
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                    }
                                    Icon(
                                        imageVector = Icons.Default.Sync,
                                        contentDescription = "Sync",
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Sync")
                                }
                            }
                        }
                    }
                }

                // Platform Info
                if (platformState.platformInfo.isNotEmpty()) {
                    item {
                        PlatformInfoCard(platformInfo = platformState.platformInfo)
                    }
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
                        onClick = { screenModel.requestPermissions() }
                    )
                }

                item {
                    SettingsItem(
                        icon = Icons.Default.Accessibility,
                        title = "Accessibility Service",
                        subtitle = "Required for blocking",
                        onClick = { screenModel.requestPermissions() }
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
                        onClick = { navigator.push(AboutScreen()) }
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

        // Clear Data Confirmation Dialog
        if (showClearDataDialog) {
            AlertDialog(
                onDismissRequest = { showClearDataDialog = false },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Warning",
                        tint = MaterialTheme.colorScheme.error
                    )
                },
                title = {
                    Text(
                        text = "Clear All Data?",
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                text = {
                    Text(
                        text = "This will permanently delete all your usage history, blocked apps, and settings. This action cannot be undone.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            screenModel.clearAllData()
                            showClearDataDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Clear All Data")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showClearDataDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
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
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true
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
                tint = if (enabled) TawaznTheme.colors.gradientMiddle else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f),
                modifier = Modifier.size(24.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = if (enabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (enabled) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f)
                )
            }

            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                enabled = enabled,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = androidx.compose.ui.graphics.Color.White,
                    checkedTrackColor = TawaznTheme.colors.gradientMiddle,
                    uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                    uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant,
                    disabledCheckedThumbColor = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.38f),
                    disabledCheckedTrackColor = TawaznTheme.colors.gradientMiddle.copy(alpha = 0.38f),
                    disabledUncheckedThumbColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.38f),
                    disabledUncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.38f)
                )
            )
        }
    }
}
