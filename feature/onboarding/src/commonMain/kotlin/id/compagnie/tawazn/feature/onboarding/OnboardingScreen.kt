package id.compagnie.tawazn.feature.onboarding

import id.compagnie.tawazn.design.icons.TawaznIcons

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import id.compagnie.tawazn.design.component.GlassCard
import id.compagnie.tawazn.design.component.GradientButton
import id.compagnie.tawazn.design.component.PermissionCard
import id.compagnie.tawazn.design.theme.TawaznTheme
class OnboardingScreen : Screen {
    @Composable
    override fun Content() {
        val screenModel = getScreenModel<OnboardingScreenModel>()
        OnboardingContent(screenModel)
    }
}
@Composable
fun OnboardingContent(screenModel: OnboardingScreenModel) {
    var currentPage by remember { mutableStateOf(0) }
    val navigator = LocalNavigator.currentOrThrow
    val permissionState by screenModel.permissionState.collectAsState()
    TawaznTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            TawaznTheme.colors.gradientStart.copy(alpha = 0.1f),
                            TawaznTheme.colors.gradientMiddle.copy(alpha = 0.05f),
                            MaterialTheme.colorScheme.background
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Page Indicator
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(4) { index ->
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .size(if (index == currentPage) 12.dp else 8.dp)
                                .background(
                                    color = if (index == currentPage)
                                        TawaznTheme.colors.gradientMiddle
                                    else
                                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f),
                                    shape = MaterialTheme.shapes.small
                                )
                    }
                }
                // Content
                AnimatedVisibility(
                    visible = true,
                    enter = slideInHorizontally() + fadeIn(),
                    exit = slideOutHorizontally() + fadeOut()
                    when (currentPage) {
                        0 -> WelcomePage()
                        1 -> FeaturePage()
                        2 -> PermissionPage(
                            permissionState = permissionState,
                            onRequestPermissions = { screenModel.requestPermissions() },
                            onCheckPermissions = { screenModel.checkPermissions() }
                        3 -> ReadyPage(
                            onStartServices = { screenModel.startBackgroundServices() }
                Spacer(modifier = Modifier.weight(1f))
                // Navigation Buttons
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                    GradientButton(
                        text = if (currentPage == 3) "Get Started" else "Continue",
                        onClick = {
                            if (currentPage < 3) {
                                currentPage++
                            } else {
                                // Complete onboarding and return to main screen
                                screenModel.startBackgroundServices()
                                navigator.popAll()
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    if (currentPage > 0 && currentPage < 3) {
                        TextButton(
                            onClick = { currentPage-- },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Back")
                        }
                    if (currentPage < 3) {
                            onClick = {
                            },
                            Text("Skip", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
fun WelcomePage() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Icon(
            imageVector = TawaznIcons.Phone,
            contentDescription = "Tawazn",
            modifier = Modifier.size(120.dp),
            tint = TawaznTheme.colors.gradientMiddle
        )
        Text(
            text = "Welcome to Tawazn",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
            text = "توازن",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            text = "Find balance in your digital life. Track your screen time, block distracting apps, and achieve digital wellness.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
fun FeaturePage() {
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(16.dp)
            text = "Powerful Features",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        FeatureItem(
            icon = TawaznIcons.AccessTime,
            title = "Usage Tracking",
            description = "Monitor your screen time with detailed statistics and insights"
            icon = TawaznIcons.Block,
            title = "App Blocking",
            description = "Block distracting apps and create focus sessions"
            icon = TawaznIcons.Schedule,
            title = "Smart Scheduling",
            description = "Set up automatic blocking schedules for better productivity"
            icon = TawaznIcons.Analytics,
            title = "Insights & Analytics",
            description = "Understand your digital habits with weekly reports"
fun PermissionPage(
    permissionState: PermissionState,
    onRequestPermissions: () -> Unit,
    onCheckPermissions: () -> Unit
) {
        verticalArrangement = Arrangement.spacedBy(20.dp)
            imageVector = TawaznIcons.Security,
            contentDescription = "Permissions",
            modifier = Modifier.size(80.dp),
            text = "Required Permissions",
        // Status Banner
        if (permissionState.hasAllPermissions) {
            GlassCard(
                useGradient = false,
                modifier = Modifier.fillMaxWidth()
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    Icon(
                        imageVector = TawaznIcons.CheckCircle,
                        contentDescription = "Success",
                        tint = TawaznTheme.colors.success,
                        modifier = Modifier.size(24.dp)
                    Text(
                        text = "All permissions granted!",
                        style = MaterialTheme.typography.titleMedium,
                        color = TawaznTheme.colors.success,
                        fontWeight = FontWeight.SemiBold
        // Permission Cards
        PermissionCard(
            title = "Screen Time Access",
            description = "Track app usage and screen time statistics. Your data stays private on this device.",
            isGranted = permissionState.hasAllPermissions,
            isRequired = true,
            onRequestClick = onRequestPermissions
            description = "Allow Tawazn to block distracting apps and help you stay focused.",
        // Loading indicator
        if (permissionState.isRequesting) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                Text(
                    text = "Requesting permissions...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
        // Refresh permissions button
        if (permissionState.permissionRequested && !permissionState.hasAllPermissions) {
            TextButton(onClick = onCheckPermissions) {
                Icon(
                    imageVector = TawaznIcons.Refresh,
                    contentDescription = "Refresh",
                    modifier = Modifier.size(16.dp)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Check Again")
        // Privacy note
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
            Icon(
                imageVector = TawaznIcons.Lock,
                contentDescription = "Privacy",
                tint = TawaznTheme.colors.success,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = "Your data is private and secure",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
fun ReadyPage(
    onStartServices: () -> Unit
    // Start background services when page is shown
    LaunchedEffect(Unit) {
            onStartServices()
            imageVector = TawaznIcons.CheckCircle,
            contentDescription = "Ready",
            tint = TawaznTheme.colors.success
            text = "You're All Set!",
        GlassCard(useGradient = true) {
                verticalArrangement = Arrangement.spacedBy(16.dp)
                    text = "Start Your Journey",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                    text = "Tawazn is ready to help you achieve digital wellness. Here's what you can do:",
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    QuickTip("View your daily screen time")
                    QuickTip("Block distracting apps instantly")
                    QuickTip("Create focus sessions")
                    QuickTip("Track your progress")
        // Show permission warning if not granted
        if (!permissionState.hasAllPermissions) {
                        imageVector = TawaznIcons.Warning,
                        contentDescription = "Warning",
                        tint = TawaznTheme.colors.warning,
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Limited Functionality",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = TawaznTheme.colors.warning
                            text = "Some features require permissions. You can grant them later in Settings.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
fun FeatureItem(
    icon: ImageVector,
    title: String,
    description: String
    GlassCard {
            horizontalArrangement = Arrangement.spacedBy(16.dp),
                imageVector = icon,
                contentDescription = title,
                tint = TawaznTheme.colors.gradientMiddle,
                modifier = Modifier.size(40.dp)
            Column {
                    text = title,
                    text = description,
fun QuickTip(text: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
            imageVector = TawaznIcons.Check,
            contentDescription = null,
            tint = TawaznTheme.colors.gradientMiddle,
            modifier = Modifier.size(16.dp)
            text = text,
            style = MaterialTheme.typography.bodyMedium
