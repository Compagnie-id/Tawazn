import id.compagnie.tawazn.design.icons.TawaznIcons
package id.compagnie.tawazn.feature.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import id.compagnie.tawazn.core.datastore.AppPreferences
import id.compagnie.tawazn.design.component.GlassCard
import id.compagnie.tawazn.design.component.GradientButton
import id.compagnie.tawazn.design.theme.TawaznTheme
import org.koin.compose.koinInject
class ProfileScreen : Screen {
    @Composable
    override fun Content() {
        ProfileContent()
    }
}
@Composable
fun ProfileContent() {
    val navigator = LocalNavigator.currentOrThrow
    val appPreferences: AppPreferences = koinInject()
    // Profile state
    val username by appPreferences.username.collectAsState(initial = "")
    val email by appPreferences.email.collectAsState(initial = "")
    val currentStreak by appPreferences.currentStreak.collectAsState(initial = 0)
    val longestStreak by appPreferences.longestStreak.collectAsState(initial = 0)
    var editMode by remember { mutableStateOf(false) }
    var editUsername by remember { mutableStateOf(username) }
    var editEmail by remember { mutableStateOf(email) }
    LaunchedEffect(username, email) {
        if (!editMode) {
            editUsername = username
            editEmail = email
        }
    TawaznTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Profile") },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(TawaznIcons.ArrowBack, "Back")
                        }
                    },
                    actions = {
                        if (editMode) {
                            TextButton(
                                onClick = {
                                    kotlinx.coroutines.MainScope().kotlinx.coroutines.launch {
                                        appPreferences.setUsername(editUsername)
                                        appPreferences.setEmail(editEmail)
                                        editMode = false
                                    }
                                }
                            ) {
                                Text("Save")
                            }
                            TextButton(onClick = {
                                editUsername = username
                                editEmail = email
                                editMode = false
                            }) {
                                Text("Cancel")
                        } else {
                            IconButton(onClick = { editMode = true }) {
                                Icon(TawaznIcons.Edit, "Edit")
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Profile Avatar Card
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    useGradient = true
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            modifier = Modifier.size(80.dp),
                            shape = MaterialTheme.shapes.extraLarge,
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = TawaznIcons.Person,
                                    contentDescription = "Profile",
                                    modifier = Modifier.size(48.dp),
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            OutlinedTextField(
                                value = editUsername,
                                onValueChange = { editUsername = it },
                                placeholder = { Text("Your Name") },
                                modifier = Modifier.fillMaxWidth(0.8f),
                                singleLine = true
                            )
                            Text(
                                text = username.ifBlank { "Set your name" },
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                                value = editEmail,
                                onValueChange = { editEmail = it },
                                placeholder = { Text("your.email@example.com") },
                                text = email.ifBlank { "Add email" },
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                    }
                }
                // Stats Section
                Text(
                    text = "Your Progress",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = TawaznTheme.colors.gradientMiddle
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                    // Current Streak
                    GlassCard(modifier = Modifier.weight(1f)) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                            Icon(
                                imageVector = TawaznIcons.LocalFireDepartment,
                                contentDescription = "Streak",
                                tint = TawaznTheme.colors.warning,
                                modifier = Modifier.size(32.dp)
                                text = currentStreak.toString(),
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = TawaznTheme.colors.warning
                                text = "Day Streak",
                                style = MaterialTheme.typography.bodySmall,
                    // Longest Streak
                                imageVector = TawaznIcons.EmojiEvents,
                                contentDescription = "Best",
                                tint = TawaznTheme.colors.success,
                                text = longestStreak.toString(),
                                color = TawaznTheme.colors.success
                                text = "Best Streak",
                // Account Information
                    text = "Account Information",
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        InfoRow(
                            icon = TawaznIcons.CalendarToday,
                            label = "Member Since",
                            value = "January 2024"
                        )
                        Divider()
                            icon = TawaznIcons.Devices,
                            label = "Platform",
                            value = getPlatformName()
                Spacer(Modifier.height(8.dp))
                // Note about data
                    text = "Your profile data is stored locally on your device and is never shared with third parties.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 8.dp)
fun InfoRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = TawaznTheme.colors.gradientMiddle,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
expect fun getPlatformName(): String
