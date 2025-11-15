import id.compagnie.tawazn.design.icons.TawaznIcons
package id.compagnie.tawazn.feature.settings

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
import cafe.adriel.voyager.navigator.currentOrThrow
import id.compagnie.tawazn.core.datastore.AppPreferences
import id.compagnie.tawazn.design.component.GlassCard
import id.compagnie.tawazn.design.theme.TawaznTheme
import org.koin.compose.koinInject
class PrivacySecurityScreen : Screen {
    @Composable
    override fun Content() {
        PrivacySecurityContent()
    }
}
@Composable
fun PrivacySecurityContent() {
    val navigator = LocalNavigator.currentOrThrow
    val appPreferences: AppPreferences = koinInject()
    val analyticsEnabled by appPreferences.analyticsEnabled.collectAsState(initial = true)
    val crashReportsEnabled by appPreferences.crashReportsEnabled.collectAsState(initial = true)
    var showExportDialog by remember { mutableStateOf(false) }
    TawaznTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Privacy & Security") },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(TawaznIcons.ArrowBack, "Back")
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
                    PrivacySwitchItem(
                        icon = TawaznIcons.Analytics,
                        title = "Analytics",
                        subtitle = "Help improve Tawazn with anonymous usage data",
                        checked = analyticsEnabled,
                        onCheckedChange = {
                            kotlinx.coroutines.MainScope().kotlinx.coroutines.launch {
                                appPreferences.setAnalyticsEnabled(it)
                            }
                        icon = TawaznIcons.BugReport,
                        title = "Crash Reports",
                        subtitle = "Automatically send crash reports",
                        checked = crashReportsEnabled,
                                appPreferences.setCrashReportsEnabled(it)
                // Data Management Section
                    SectionHeader("Data Management")
                    PrivacyActionItem(
                        icon = TawaznIcons.Download,
                        title = "Export Data",
                        subtitle = "Download all your data",
                        onClick = { showExportDialog = true }
                        icon = TawaznIcons.History,
                        title = "View Data Usage",
                        subtitle = "See what data is collected",
                        onClick = { /* TODO: Show data usage details */ }
                // Security Section
                    SectionHeader("Security")
                    GlassCard(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = TawaznIcons.Shield,
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
                                    text = "Your data is encrypted at rest",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                imageVector = TawaznIcons.CheckCircle,
                                contentDescription = "Enabled",
                                tint = TawaznTheme.colors.success
                    }
                                imageVector = TawaznIcons.Lock,
                                contentDescription = "Local Storage",
                                tint = TawaznTheme.colors.info,
                                    text = "Local Storage Only",
                                    text = "All data stays on your device",
                                tint = TawaznTheme.colors.info
                // Permissions Section
                    SectionHeader("Permissions")
                    Text(
                        text = "Tawazn requires certain permissions to function properly. All permissions are used solely for app functionality and never for tracking.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    PermissionInfoCard(
                        icon = TawaznIcons.Visibility,
                        title = "Usage Access",
                        description = "Required to track app usage time and provide insights"
                        icon = TawaznIcons.Notifications,
                        title = "Notifications",
                        description = "Send you reminders and usage reports"
                        icon = TawaznIcons.PhoneAndroid,
                        title = "Accessibility (Android)",
                        description = "Block apps during focus sessions"
                // Privacy Policy
                    Spacer(Modifier.height(8.dp))
                        text = "Privacy Commitment",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = TawaznTheme.colors.gradientMiddle
                        text = "Tawazn is designed with privacy in mind:\n\n" +
                                "• All data is stored locally on your device\n" +
                                "• No personal information is collected\n" +
                                "• No third-party analytics or tracking\n" +
                                "• No ads or data selling\n" +
                                "• Open source and transparent",
        }
        // Export Dialog
        if (showExportDialog) {
            AlertDialog(
                onDismissRequest = { showExportDialog = false },
                icon = {
                    Icon(
                        imageVector = TawaznIcons.Info,
                        contentDescription = "Info",
                        tint = TawaznTheme.colors.info
                },
                title = { Text("Export Data") },
                text = {
                    Text("Data export feature coming soon! You'll be able to export all your usage history, sessions, and settings to a JSON file.")
                confirmButton = {
                    Button(onClick = { showExportDialog = false }) {
                        Text("OK")
            )
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
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = androidx.compose.ui.graphics.Color.White,
                    checkedTrackColor = TawaznTheme.colors.gradientMiddle
fun PrivacyActionItem(
    onClick: () -> Unit
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
                imageVector = TawaznIcons.ChevronRight,
                contentDescription = "Navigate",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
fun PermissionInfoCard(icon: ImageVector, title: String, description: String) {
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
                tint = TawaznTheme.colors.info,
                modifier = Modifier.size(20.dp)
            Column {
                    style = MaterialTheme.typography.bodyMedium,
                    text = description,
