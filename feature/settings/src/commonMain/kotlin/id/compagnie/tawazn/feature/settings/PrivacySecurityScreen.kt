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
import id.compagnie.tawazn.i18n.stringResource
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
                    title = { Text(stringResource("privacy_security.title")) },
                    navigationIcon = {
                        IconButton(onClick = { navigator?.pop() }) {
                            Icon(PhosphorIcons.Bold.ArrowLeft, stringResource("common.back"))
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
                    SectionHeader(stringResource("privacy_security.data_privacy"))
                }

                item {
                    PrivacySwitchItem(
                        icon = PhosphorIcons.Bold.ChartBar,
                        title = stringResource("privacy_security.analytics"),
                        subtitle = stringResource("privacy_security.analytics_desc"),
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
                        title = stringResource("privacy_security.crash_reports"),
                        subtitle = stringResource("privacy_security.crash_reports_desc"),
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
                    SectionHeader(stringResource("privacy_security.data_management"))
                }

                item {
                    PrivacyActionItem(
                        icon = PhosphorIcons.Bold.DownloadSimple,
                        title = stringResource("privacy_security.export_data"),
                        subtitle = stringResource("privacy_security.export_data_desc"),
                        onClick = { showExportDialog = true }
                    )
                }

                item {
                    PrivacyActionItem(
                        icon = PhosphorIcons.Bold.ClockCounterClockwise,
                        title = stringResource("privacy_security.view_data_usage"),
                        subtitle = stringResource("privacy_security.view_data_usage_desc"),
                        onClick = { /* TODO: Show data usage details */ }
                    )
                }

                // Security Section
                item {
                    SectionHeader(stringResource("privacy_security.security"))
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
                                    contentDescription = stringResource("privacy_security.security"),
                                    tint = TawaznTheme.colors.success,
                                    modifier = Modifier.size(24.dp)
                                )
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = stringResource("privacy_security.data_encryption"),
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = stringResource("privacy_security.data_encryption_desc"),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Icon(
                                    imageVector = PhosphorIcons.Bold.CheckCircle,
                                    contentDescription = stringResource("privacy_security.enabled"),
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
                                    contentDescription = stringResource("privacy_security.local_storage_title"),
                                    tint = TawaznTheme.colors.info,
                                    modifier = Modifier.size(24.dp)
                                )
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = stringResource("privacy_security.local_storage_title"),
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = stringResource("privacy_security.local_storage_desc"),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Icon(
                                    imageVector = PhosphorIcons.Bold.CheckCircle,
                                    contentDescription = stringResource("privacy_security.enabled"),
                                    tint = TawaznTheme.colors.info
                                )
                            }
                        }
                    }
                }

                // Permissions Section
                item {
                    SectionHeader(stringResource("privacy_security.permissions"))
                }

                item {
                    Text(
                        text = stringResource("privacy_security.permissions_note"),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }

                item {
                    PermissionInfoCard(
                        icon = PhosphorIcons.Bold.Eye,
                        title = stringResource("privacy_security.usage_access"),
                        description = stringResource("privacy_security.usage_access_desc")
                    )
                }

                item {
                    PermissionInfoCard(
                        icon = PhosphorIcons.Bold.Bell,
                        title = stringResource("privacy_security.notifications_permission"),
                        description = stringResource("privacy_security.notifications_desc")
                    )
                }

                item {
                    PermissionInfoCard(
                        icon = PhosphorIcons.Bold.DeviceMobile,
                        title = stringResource("privacy_security.accessibility_android"),
                        description = stringResource("privacy_security.accessibility_desc")
                    )
                }

                // Privacy Policy
                item {
                    Spacer(Modifier.height(8.dp))
                }

                item {
                    Text(
                        text = stringResource("privacy_security.commitment"),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = TawaznTheme.colors.gradientMiddle
                    )
                }

                item {
                    Text(
                        text = stringResource("privacy_security.commitment_text"),
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
                        contentDescription = stringResource("privacy_security.export_dialog_title"),
                        tint = TawaznTheme.colors.info
                    )
                },
                title = { Text(stringResource("privacy_security.export_dialog_title")) },
                text = {
                    Text(stringResource("privacy_security.export_dialog_message"))
                },
                confirmButton = {
                    Button(onClick = { showExportDialog = false }) {
                        Text(stringResource("common.ok"))
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
