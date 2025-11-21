@file:OptIn(ExperimentalMaterial3Api::class)

package id.compagnie.tawazn.feature.settings

import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Bold
import com.adamglin.phosphoricons.bold.ArrowLeft
import com.adamglin.phosphoricons.bold.ArrowSquareOut
import com.adamglin.phosphoricons.bold.Bug
import com.adamglin.phosphoricons.bold.Buildings
import com.adamglin.phosphoricons.bold.ChartBar
import com.adamglin.phosphoricons.bold.ClockCountdown
import com.adamglin.phosphoricons.bold.Code
import com.adamglin.phosphoricons.bold.FileText
import com.adamglin.phosphoricons.bold.Prohibit
import com.adamglin.phosphoricons.bold.Scales
import com.adamglin.phosphoricons.bold.TrendUp

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import id.compagnie.tawazn.design.component.GlassCard
import id.compagnie.tawazn.design.theme.TawaznTheme
import id.compagnie.tawazn.i18n.stringResource

class AboutScreen : Screen {
    @Composable
    override fun Content() {
        AboutContent()
    }
}

@Composable
fun AboutContent() {
    val navigator = LocalNavigator.current
    var showLicensesDialog by remember { mutableStateOf(false) }

    Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource("about.title")) },
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
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // App Logo and Name
                item {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.padding(vertical = 24.dp)
                    ) {
                        Surface(
                            modifier = Modifier.size(100.dp),
                            shape = MaterialTheme.shapes.extraLarge,
                            color = TawaznTheme.colors.gradientMiddle.copy(alpha = 0.1f)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = PhosphorIcons.Bold.Scales,
                                    contentDescription = "Tawazn Logo",
                                    modifier = Modifier.size(56.dp),
                                    tint = TawaznTheme.colors.gradientMiddle
                                )
                            }
                        }

                        Text(
                            text = "Tawazn",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "${stringResource("about.version")} 1.0.0",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Text(
                            text = stringResource("about.description"),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // App Info
                item {
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        useGradient = true
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = stringResource("about.title"),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )

                            Text(
                                text = stringResource("about.balance_description"),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // Features
                item {
                    SectionHeader(stringResource("about.features"))
                }

                item {
                    FeatureCard(
                        icon = PhosphorIcons.Bold.TrendUp,
                        title = stringResource("onboarding.feature.usage_tracking.title"),
                        description = stringResource("about.usage_tracking_desc")
                    )
                }

                item {
                    FeatureCard(
                        icon = PhosphorIcons.Bold.Prohibit,
                        title = stringResource("onboarding.feature.app_blocking.title"),
                        description = stringResource("about.app_blocking_desc")
                    )
                }

                item {
                    FeatureCard(
                        icon = PhosphorIcons.Bold.ClockCountdown,
                        title = stringResource("focus_session.title"),
                        description = stringResource("about.focus_sessions_desc")
                    )
                }

                item {
                    FeatureCard(
                        icon = PhosphorIcons.Bold.ChartBar,
                        title = stringResource("analytics.title"),
                        description = stringResource("about.analytics_desc")
                    )
                }

                // Links Section
                item {
                    SectionHeader(stringResource("about.links"))
                }

                item {
                    AboutLinkItem(
                        icon = PhosphorIcons.Bold.Code,
                        title = stringResource("about.source_code"),
                        subtitle = stringResource("about.view_github"),
                        onClick = { /* TODO: Open GitHub */ }
                    )
                }

                item {
                    AboutLinkItem(
                        icon = PhosphorIcons.Bold.Bug,
                        title = stringResource("settings.report_bug.title"),
                        subtitle = stringResource("about.report_bug_subtitle"),
                        onClick = { /* TODO: Open issue tracker */ }
                    )
                }

                item {
                    AboutLinkItem(
                        icon = PhosphorIcons.Bold.FileText,
                        title = stringResource("about.licenses"),
                        subtitle = stringResource("about.licenses_subtitle"),
                        onClick = { showLicensesDialog = true }
                    )
                }

                // Developer Info
                item {
                    SectionHeader(stringResource("about.developer"))
                }

                item {
                    GlassCard(modifier = Modifier.fillMaxWidth()) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = PhosphorIcons.Bold.Buildings,
                                    contentDescription = stringResource("about.developer"),
                                    tint = TawaznTheme.colors.gradientMiddle,
                                    modifier = Modifier.size(24.dp)
                                )

                                Column {
                                    Text(
                                        text = stringResource("about.company_name"),
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Medium
                                    )

                                    Text(
                                        text = stringResource("about.company_tagline"),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }

                // Copyright
                item {
                    Spacer(Modifier.height(16.dp))
                }

                item {
                    Text(
                        text = stringResource("about.copyright"),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    Text(
                        text = stringResource("about.made_with"),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Licenses Dialog
        if (showLicensesDialog) {
            AlertDialog(
                onDismissRequest = { showLicensesDialog = false },
                title = { Text(stringResource("about.licenses_dialog_title")) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(stringResource("about.licenses_dialog_text"))
                        LicenseItem("Compose Multiplatform", "Apache 2.0")
                        LicenseItem("Kotlin", "Apache 2.0")
                        LicenseItem("Kotlinx Coroutines", "Apache 2.0")
                        LicenseItem("Kotlinx Serialization", "Apache 2.0")
                        LicenseItem("Kotlinx DateTime", "Apache 2.0")
                        LicenseItem("SQLDelight", "Apache 2.0")
                        LicenseItem("Koin", "Apache 2.0")
                        LicenseItem("Voyager", "MIT")
                        LicenseItem("Kermit", "Apache 2.0")
                        LicenseItem("DataStore", "Apache 2.0")
                    }
                },
                confirmButton = {
                    Button(onClick = { showLicensesDialog = false }) {
                        Text(stringResource("common.done"))
                    }
                }
            )
        }
    }

@Composable
fun FeatureCard(icon: ImageVector, title: String, description: String) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = TawaznTheme.colors.gradientMiddle,
                modifier = Modifier.size(32.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
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

@Composable
fun AboutLinkItem(
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
                imageVector = PhosphorIcons.Bold.ArrowSquareOut,
                contentDescription = "Open",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun LicenseItem(name: String, license: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = license,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
