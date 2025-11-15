import id.compagnie.tawazn.design.icons.TawaznIcons
package id.compagnie.tawazn.feature.settings

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
import cafe.adriel.voyager.navigator.currentOrThrow
import id.compagnie.tawazn.design.component.GlassCard
import id.compagnie.tawazn.design.theme.TawaznTheme
class AboutScreen : Screen {
    @Composable
    override fun Content() {
        AboutContent()
    }
}
@Composable
fun AboutContent() {
    val navigator = LocalNavigator.currentOrThrow
    var showLicensesDialog by remember { mutableStateOf(false) }
    TawaznTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("About") },
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
                                    imageVector = TawaznIcons.Balance,
                                    contentDescription = "Tawazn Logo",
                                    modifier = Modifier.size(56.dp),
                                    tint = TawaznTheme.colors.gradientMiddle
                                )
                            }
                        Text(
                            text = "Tawazn",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                            text = "Version 1.0.0",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                            text = "Balance your digital life",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                    }
                }
                // App Info
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        useGradient = true
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                            Text(
                                text = "About Tawazn",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                                text = "Tawazn (تَوازُن) means 'balance' in Arabic. This app helps you achieve a healthier relationship with technology by tracking your screen time, blocking distracting apps, and providing insights into your digital habits.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                // Features
                    SectionHeader("Features")
                    FeatureCard(
                        icon = TawaznIcons.TrendingUp,
                        title = "Usage Tracking",
                        description = "Monitor your app usage in real-time"
                        icon = TawaznIcons.Block,
                        title = "App Blocking",
                        description = "Block distracting apps when you need focus"
                        icon = TawaznIcons.Schedule,
                        title = "Focus Sessions",
                        description = "Schedule blocking sessions for maximum productivity"
                        icon = TawaznIcons.Analytics,
                        title = "Analytics",
                        description = "Get insights and trends about your digital habits"
                // Links Section
                    SectionHeader("Links")
                    AboutLinkItem(
                        icon = TawaznIcons.Code,
                        title = "Source Code",
                        subtitle = "View on GitHub",
                        onClick = { /* TODO: Open GitHub */ }
                        icon = TawaznIcons.BugReport,
                        title = "Report a Bug",
                        subtitle = "Help us improve",
                        onClick = { /* TODO: Open issue tracker */ }
                        icon = TawaznIcons.Description,
                        title = "Licenses",
                        subtitle = "Open source licenses",
                        onClick = { showLicensesDialog = true }
                // Developer Info
                    SectionHeader("Developer")
                    GlassCard(modifier = Modifier.fillMaxWidth()) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                    imageVector = TawaznIcons.Business,
                                    contentDescription = "Company",
                                    tint = TawaznTheme.colors.gradientMiddle,
                                    modifier = Modifier.size(24.dp)
                                Column {
                                    Text(
                                        text = "Compagnie.id",
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Medium
                                    )
                                        text = "Building tools for digital wellbeing",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                }
                // Copyright
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = "© 2024 Compagnie.id\nAll rights reserved",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                        text = "Made with ❤️ using Compose Multiplatform",
        }
        // Licenses Dialog
        if (showLicensesDialog) {
            AlertDialog(
                onDismissRequest = { showLicensesDialog = false },
                title = { Text("Open Source Licenses") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("This app uses the following open source libraries:")
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
                },
                confirmButton = {
                    Button(onClick = { showLicensesDialog = false }) {
                        Text("Close")
            )
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
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
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
            modifier = Modifier.fillMaxWidth(),
                modifier = Modifier.size(24.dp)
                    text = subtitle,
                imageVector = TawaznIcons.OpenInNew,
                contentDescription = "Open",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
fun LicenseItem(name: String, license: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
        Text(
            text = name,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(1f)
        )
            text = license,
            color = MaterialTheme.colorScheme.onSurfaceVariant
