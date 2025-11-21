@file:OptIn(ExperimentalMaterial3Api::class)

package id.compagnie.tawazn.feature.settings

import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Bold
import com.adamglin.phosphoricons.bold.ArrowLeft
import com.adamglin.phosphoricons.bold.Bell
import com.adamglin.phosphoricons.bold.Bug
import com.adamglin.phosphoricons.bold.CaretRight
import com.adamglin.phosphoricons.bold.ChartBar
import com.adamglin.phosphoricons.bold.CheckCircle
import com.adamglin.phosphoricons.bold.ClockCounterClockwise
import com.adamglin.phosphoricons.bold.DeviceMobile
import com.adamglin.phosphoricons.bold.DownloadSimple
import com.adamglin.phosphoricons.bold.Eye
import com.adamglin.phosphoricons.bold.Info
import com.adamglin.phosphoricons.bold.Lock
import com.adamglin.phosphoricons.bold.Shield

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import id.compagnie.tawazn.core.datastore.AppPreferences
import id.compagnie.tawazn.design.component.GlassCard
import id.compagnie.tawazn.design.theme.TawaznTheme
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

class PrivacySecurityScreen : Screen {
    @Composable
    override fun Content() {
        PrivacySecurityContent()
    }
}

@Composable
fun PrivacySecurityContent() {
    val navigator = LocalNavigator.current
    val appPreferences: AppPreferences = koinInject()
    val scope = rememberCoroutineScope()
    val analyticsEnabled by appPreferences.analyticsEnabled.collectAsState(initial = true)
    val crashReportsEnabled by appPreferences.crashReportsEnabled.collectAsState(initial = true)
    var showExportDialog by remember { mutableStateOf(false) }

    Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Privacy & Security") },
                    navigationIcon = {
                        IconButton(onClick = { navigator?.pop() }) {
                            Icon(PhosphorIcons.Bold.ArrowLeft, "Back")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                )
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Data Privacy Section
                item {
                    SectionHeader("Data Privacy")
                }

                item {
                    PrivacySwitchItem(
                        icon = PhosphorIcons.Bold.ChartBar,
                        title = "Analytics",
                        subtitle = "Help improve Tawazn with anonymous usage data",
                        checked = analyticsEnabled,
                        onCheckedChange = {
                            scope.launch {
                                appPreferences.setAnalyticsEnabled(it)
                            }
                        }
                    )
                }

                item {
                    PrivacySwitchItem(
                        icon = PhosphorIcons.Bold.Bug,
                        title = "Crash Reports",
                        subtitle = "Automatically send crash reports",
                        checked = crashReportsEnabled,
                        onCheckedChange = {
                            scope.launch {
                                appPreferences.setCrashReportsEnabled(it)
                            }
                        }
                    )
                }

                // Data Management Section
                item {
                    SectionHeader("Data Management")
                }

                item {
                    PrivacyActionItem(
                        icon = PhosphorIcons.Bold.DownloadSimple,
                        title = "Export Data",
                        subtitle = "Download all your data",
                        onClick = { showExportDialog = true }
                    )
                }

                item {
                    PrivacyActionItem(
                        icon = PhosphorIcons.Bold.ClockCounterClockwise,
                        title = "View Data Usage",
                        subtitle = "See what data is collected",
                        onClick = { /* TODO: Show data usage details */ }
                    )
                }

                // Security Section
                item {
                    SectionHeader("Security")
                }

                item {
                    GlassCard(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = PhosphorIcons.Bold.Shield,
                                    contentDescription = "Security",
                                    tint = TawaznTheme.colors.success,
                                    modifier = Modifier.size(24.dp)
                                )
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Data Encryption",
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = "Your data is encrypted at rest",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Icon(
                                    imageVector = PhosphorIcons.Bold.CheckCircle,
                                    contentDescription = "Enabled",
                                    tint = TawaznTheme.colors.success
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = PhosphorIcons.Bold.Lock,
                                    contentDescription = "Local Storage",
                                    tint = TawaznTheme.colors.info,
                                    modifier = Modifier.size(24.dp)
                                )
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Local Storage Only",
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = "All data stays on your device",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Icon(
                                    imageVector = PhosphorIcons.Bold.CheckCircle,
                                    contentDescription = "Enabled",
                                    tint = TawaznTheme.colors.info
                                )
                            }
                        }
                    }
                }

                // Permissions Section
                item {
                    SectionHeader("Permissions")
                }

                item {
                    Text(
                        text = "Tawazn requires certain permissions to function properly. All permissions are used solely for app functionality and never for tracking.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }

                item {
                    PermissionInfoCard(
                        icon = PhosphorIcons.Bold.Eye,
                        title = "Usage Access",
                        description = "Required to track app usage time and provide insights"
                    )
                }

                item {
                    PermissionInfoCard(
                        icon = PhosphorIcons.Bold.Bell,
                        title = "Notifications",
                        description = "Send you reminders and usage reports"
                    )
                }

                item {
                    PermissionInfoCard(
                        icon = PhosphorIcons.Bold.DeviceMobile,
                        title = "Accessibility (Android)",
                        description = "Block apps during focus sessions"
                    )
                }

                // Privacy Policy
                item {
                    Spacer(Modifier.height(8.dp))
                }

                item {
                    Text(
                        text = "Privacy Commitment",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = TawaznTheme.colors.gradientMiddle
                    )
                }

                item {
                    Text(
                        text = "Tawazn is designed with privacy in mind:\n\n" +
                                "• All data is stored locally on your device\n" +
                                "• No personal information is collected\n" +
                                "• No third-party analytics or tracking\n" +
                                "• No ads or data selling\n" +
                                "• Open source and transparent",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Export Dialog
        if (showExportDialog) {
            AlertDialog(
                onDismissRequest = { showExportDialog = false },
                icon = {
                    Icon(
                        imageVector = PhosphorIcons.Bold.Info,
                        contentDescription = "Info",
                        tint = TawaznTheme.colors.info
                    )
                },
                title = { Text("Export Data") },
                text = {
                    Text("Data export feature coming soon! You'll be able to export all your usage history, sessions, and settings to a JSON file.")
                },
                confirmButton = {
                    Button(onClick = { showExportDialog = false }) {
                        Text("OK")
                    }
                }
            )
        }
    }

@Composable
fun PrivacySwitchItem(
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
                    checkedTrackColor = TawaznTheme.colors.gradientMiddle
                )
            )
        }
    }
}

@Composable
fun PrivacyActionItem(
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
                imageVector = PhosphorIcons.Bold.CaretRight,
                contentDescription = "Navigate",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun PermissionInfoCard(icon: ImageVector, title: String, description: String) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = TawaznTheme.colors.info,
                modifier = Modifier.size(20.dp)
            )
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
