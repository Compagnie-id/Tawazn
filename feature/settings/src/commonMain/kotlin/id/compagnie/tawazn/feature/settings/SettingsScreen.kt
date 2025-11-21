package id.compagnie.tawazn.feature.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Bold
import com.adamglin.phosphoricons.bold.ArrowLeft
import com.adamglin.phosphoricons.bold.ArrowsCounterClockwise
import com.adamglin.phosphoricons.bold.Bell
import com.adamglin.phosphoricons.bold.Bug
import com.adamglin.phosphoricons.bold.Calendar
import com.adamglin.phosphoricons.bold.CaretRight
import com.adamglin.phosphoricons.bold.CloudArrowUp
import com.adamglin.phosphoricons.bold.DeviceMobile
import com.adamglin.phosphoricons.bold.FileText
import com.adamglin.phosphoricons.bold.Fire
import com.adamglin.phosphoricons.bold.Flag
import com.adamglin.phosphoricons.bold.Gavel
import com.adamglin.phosphoricons.bold.Gear
import com.adamglin.phosphoricons.bold.Globe
import com.adamglin.phosphoricons.bold.Info
import com.adamglin.phosphoricons.bold.Moon
import com.adamglin.phosphoricons.bold.Shield
import com.adamglin.phosphoricons.bold.Trash
import com.adamglin.phosphoricons.bold.User
import com.adamglin.phosphoricons.bold.Warning
import id.compagnie.tawazn.design.component.GlassCard
import id.compagnie.tawazn.design.component.PermissionStatusBadge
import id.compagnie.tawazn.design.component.PlatformInfoCard
import id.compagnie.tawazn.design.theme.TawaznTheme
import id.compagnie.tawazn.i18n.LanguageSelectorDialog
import id.compagnie.tawazn.i18n.LocalStringProvider
import id.compagnie.tawazn.i18n.stringResource

class SettingsScreen : Screen {
    @Composable
    override fun Content() {
        val screenModel = koinScreenModel<SettingsScreenModel>()
        SettingsContent(screenModel)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsContent(
    screenModel: SettingsScreenModel,
    showBackButton: Boolean = true
) {
    val navigator = LocalNavigator.current
    val platformState by screenModel.platformState.collectAsState()

    // Collect preference flows
    val useSystemTheme by screenModel.useSystemTheme.collectAsState(initial = true)
    val darkModeEnabled by screenModel.darkMode.collectAsState(initial = false)
    val notificationsEnabled by screenModel.notificationsEnabled.collectAsState(initial = true)
    val dailyReportEnabled by screenModel.dailyReportEnabled.collectAsState(initial = true)
    val weeklyReportEnabled by screenModel.weeklyReportEnabled.collectAsState(initial = true)

    // Clear data dialog state
    var showClearDataDialog by remember { mutableStateOf(false) }

    // Language selector dialog state
    var showLanguageDialog by remember { mutableStateOf(false) }
    val stringProvider = LocalStringProvider.current
    val currentLanguage by stringProvider.currentLanguage.collectAsState()

    Scaffold(
            topBar = {
                if (showBackButton) {
                    TopAppBar(
                        title = { Text(stringResource("settings.title")) },
                        navigationIcon = {
                            IconButton(onClick = { navigator?.pop() }) {
                                Icon(PhosphorIcons.Bold.ArrowLeft, stringResource("common.back"))
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.9f)
                        )
                    )
                }
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
                    SectionHeader(stringResource("settings.section.account"))
                }

                item {
                    SettingsItem(
                        icon = PhosphorIcons.Bold.User,
                        title = stringResource("settings.profile.title"),
                        subtitle = stringResource("settings.profile.subtitle"),
                        onClick = { navigator?.push(ProfileScreen()) }
                    )
                }

                item {
                    SettingsItem(
                        icon = PhosphorIcons.Bold.Shield,
                        title = stringResource("settings.privacy.title"),
                        subtitle = stringResource("settings.privacy.subtitle"),
                        onClick = { navigator?.push(PrivacySecurityScreen()) }
                    )
                }

                // Notifications Section
                item {
                    SectionHeader(stringResource("settings.section.notifications"))
                }

                item {
                    SettingsSwitchItem(
                        icon = PhosphorIcons.Bold.Bell,
                        title = stringResource("settings.notifications.title"),
                        subtitle = stringResource("settings.notifications.subtitle"),
                        checked = notificationsEnabled,
                        onCheckedChange = { screenModel.setNotificationsEnabled(it) }
                    )
                }

                item {
                    SettingsSwitchItem(
                        icon = PhosphorIcons.Bold.Calendar,
                        title = stringResource("settings.daily_report.title"),
                        subtitle = stringResource("settings.daily_report.subtitle"),
                        checked = dailyReportEnabled,
                        onCheckedChange = { screenModel.setDailyReportEnabled(it) }
                    )
                }

                item {
                    SettingsSwitchItem(
                        icon = PhosphorIcons.Bold.Calendar,
                        title = stringResource("settings.weekly_report.title"),
                        subtitle = stringResource("settings.weekly_report.subtitle"),
                        checked = weeklyReportEnabled,
                        onCheckedChange = { screenModel.setWeeklyReportEnabled(it) }
                    )
                }

                // Focus & Productivity Section
                item {
                    SectionHeader(stringResource("settings.section.focus_productivity"))
                }

                item {
                    SettingsItem(
                        icon = PhosphorIcons.Bold.Flag,
                        title = stringResource("settings.usage_goals.title"),
                        subtitle = stringResource("settings.usage_goals.subtitle"),
                        onClick = { navigator?.push(UsageGoalsScreen()) }
                    )
                }

                item {
                    SettingsItem(
                        icon = PhosphorIcons.Bold.Fire,
                        title = stringResource("settings.focus_sessions.title"),
                        subtitle = stringResource("settings.focus_sessions.subtitle"),
                        onClick = { navigator?.push(FocusSessionListScreen()) }
                    )
                }

                // Appearance Section
                item {
                    SectionHeader(stringResource("settings.section.appearance"))
                }

                item {
                    SettingsItem(
                        icon = PhosphorIcons.Bold.Globe,
                        title = stringResource("settings.language.title"),
                        subtitle = currentLanguage.nativeName,
                        onClick = { showLanguageDialog = true }
                    )
                }

                item {
                    SettingsSwitchItem(
                        icon = PhosphorIcons.Bold.Gear,
                        title = stringResource("settings.system_theme.title"),
                        subtitle = stringResource("settings.system_theme.subtitle"),
                        checked = useSystemTheme,
                        onCheckedChange = { screenModel.setUseSystemTheme(it) }
                    )
                }

                item {
                    SettingsSwitchItem(
                        icon = PhosphorIcons.Bold.Moon,
                        title = stringResource("settings.dark_mode.title"),
                        subtitle = if (useSystemTheme) stringResource("settings.dark_mode.subtitle_disabled") else stringResource("settings.dark_mode.subtitle"),
                        checked = darkModeEnabled,
                        onCheckedChange = { screenModel.setDarkMode(it) },
                        enabled = !useSystemTheme
                    )
                }

                // Data & Storage Section
                item {
                    SectionHeader(stringResource("settings.section.data_storage"))
                }

                item {
                    SettingsItem(
                        icon = PhosphorIcons.Bold.CloudArrowUp,
                        title = stringResource("settings.backup.title"),
                        subtitle = stringResource("settings.backup.subtitle"),
                        onClick = { /* TODO */ }
                    )
                }

                item {
                    SettingsItem(
                        icon = PhosphorIcons.Bold.Trash,
                        title = stringResource("settings.clear_data.title"),
                        subtitle = stringResource("settings.clear_data.subtitle"),
                        onClick = { showClearDataDialog = true }
                    )
                }

                // Platform Status Section
                item {
                    SectionHeader(stringResource("settings.section.platform_status"))
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
                                    text = stringResource("settings.permissions.title"),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                                PermissionStatusBadge(isGranted = platformState.hasPermissions)
                            }

                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))

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
                                    Text(if (platformState.hasPermissions) stringResource("settings.permissions.granted") else stringResource("settings.permissions.grant"))
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
                                        Spacer(modifier = Modifier.width(4.dp))
                                    }
                                    Icon(
                                        imageVector = PhosphorIcons.Bold.ArrowsCounterClockwise,
                                        contentDescription = stringResource("common.sync"),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(stringResource("common.sync"))
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
                    SectionHeader(stringResource("settings.permissions.title"))
                }

                item {
                    SettingsItem(
                        icon = PhosphorIcons.Bold.DeviceMobile,
                        title = stringResource("settings.usage_access.title"),
                        subtitle = stringResource("settings.usage_access.subtitle"),
                        onClick = { screenModel.requestPermissions() }
                    )
                }

                item {
                    SettingsItem(
                        icon = PhosphorIcons.Bold.User,
                        title = stringResource("settings.accessibility_service.title"),
                        subtitle = stringResource("settings.accessibility_service.subtitle"),
                        onClick = { screenModel.requestPermissions() }
                    )
                }

                // About Section
                item {
                    SectionHeader(stringResource("settings.section.about"))
                }

                item {
                    SettingsItem(
                        icon = PhosphorIcons.Bold.Info,
                        title = stringResource("settings.about.title"),
                        subtitle = stringResource("settings.about.subtitle"),
                        onClick = { navigator?.push(AboutScreen()) }
                    )
                }

                item {
                    SettingsItem(
                        icon = PhosphorIcons.Bold.FileText,
                        title = stringResource("settings.privacy_policy.title"),
                        subtitle = stringResource("settings.privacy_policy.subtitle"),
                        onClick = { /* TODO */ }
                    )
                }

                item {
                    SettingsItem(
                        icon = PhosphorIcons.Bold.Gavel,
                        title = stringResource("settings.terms.title"),
                        subtitle = stringResource("settings.terms.subtitle"),
                        onClick = { /* TODO */ }
                    )
                }

                item {
                    SettingsItem(
                        icon = PhosphorIcons.Bold.Bug,
                        title = stringResource("settings.report_bug.title"),
                        subtitle = stringResource("settings.report_bug.subtitle"),
                        onClick = { /* TODO */ }
                    )
                }

                // Spacer at the bottom
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                }

                item {
                    Text(
                        text = stringResource("settings.footer"),
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
                        imageVector = PhosphorIcons.Bold.Warning,
                        contentDescription = stringResource("settings.clear_data.dialog.title"),
                        tint = MaterialTheme.colorScheme.error
                    )
                },
                title = {
                    Text(
                        text = stringResource("settings.clear_data.dialog.title"),
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                text = {
                    Text(
                        text = stringResource("settings.clear_data.dialog.message"),
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
                        Text(stringResource("settings.clear_data.dialog.confirm"))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showClearDataDialog = false }) {
                        Text(stringResource("common.cancel"))
                    }
                }
            )
        }

        // Language Selector Dialog
        if (showLanguageDialog) {
            LanguageSelectorDialog(
                onDismiss = { showLanguageDialog = false },
                stringProvider = stringProvider
            )
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
                imageVector = PhosphorIcons.Bold.CaretRight,
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

/**
 * Settings screen for tab navigation (no back button)
 */
class TabSettingsScreen : Screen {
    @Composable
    override fun Content() {
        val screenModel = koinScreenModel<SettingsScreenModel>()
        SettingsContent(
            screenModel = screenModel,
            showBackButton = false
        )
    }
}
