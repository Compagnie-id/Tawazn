package id.compagnie.tawazn.feature.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import id.compagnie.tawazn.design.component.GlassCard
import id.compagnie.tawazn.design.component.GradientButton
import id.compagnie.tawazn.design.component.PermissionCard
import id.compagnie.tawazn.i18n.Language
import id.compagnie.tawazn.i18n.StringProvider
import id.compagnie.tawazn.i18n.stringResource
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Bold
import com.adamglin.phosphoricons.bold.DeviceMobile
import com.adamglin.phosphoricons.bold.Clock
import com.adamglin.phosphoricons.bold.Prohibit
import com.adamglin.phosphoricons.bold.ClockCountdown
import com.adamglin.phosphoricons.bold.ChartBar
import com.adamglin.phosphoricons.bold.Shield
import com.adamglin.phosphoricons.bold.CheckCircle
import com.adamglin.phosphoricons.bold.User
import com.adamglin.phosphoricons.bold.ArrowsClockwise
import com.adamglin.phosphoricons.bold.Lock
import com.adamglin.phosphoricons.bold.Warning
import com.adamglin.phosphoricons.bold.Check
import com.adamglin.phosphoricons.bold.Translate
import id.compagnie.tawazn.design.theme.TawaznTheme
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

class OnboardingScreen : Screen {
    @Composable
    override fun Content() {
        val screenModel = koinScreenModel<OnboardingScreenModel>()
        OnboardingContent(screenModel)
    }
}

@Composable
fun OnboardingContent(screenModel: OnboardingScreenModel) {
    var currentPage by remember { mutableStateOf(0) }
    val permissionState by screenModel.permissionState.collectAsState()

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
                    repeat(5) { index ->
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
                        )
                    }
                }

                // Content
                AnimatedVisibility(
                    visible = true,
                    enter = slideInHorizontally() + fadeIn(),
                    exit = slideOutHorizontally() + fadeOut()
                ) {
                    when (currentPage) {
                        0 -> WelcomePage()
                        1 -> LanguageSelectionPage()
                        2 -> FeaturePage()
                        3 -> PermissionPage(
                            permissionState = permissionState,
                            onRequestPermissions = { screenModel.requestPermissions() },
                            onCheckPermissions = { screenModel.checkPermissions() }
                        )
                        4 -> ReadyPage(
                            permissionState = permissionState,
                            onStartServices = { screenModel.startBackgroundServices() }
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Navigation Buttons
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    GradientButton(
                        text = if (currentPage == 4) stringResource("onboarding.get_started") else stringResource("common.continue"),
                        onClick = {
                            if (currentPage < 4) {
                                currentPage++
                            } else {
                                // Complete onboarding - App.kt will automatically show the main app
                                screenModel.completeOnboarding()
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (currentPage > 0 && currentPage < 4) {
                        TextButton(
                            onClick = { currentPage-- },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(stringResource("common.back"))
                        }
                    }

                    if (currentPage < 4) {
                        TextButton(
                            onClick = {
                                // Skip onboarding - App.kt will automatically show the main app
                                screenModel.completeOnboarding()
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(stringResource("common.skip"), color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }
}

@Composable
fun WelcomePage() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Icon(
            imageVector = PhosphorIcons.Bold.DeviceMobile,
            contentDescription = "Tawazn",
            modifier = Modifier.size(120.dp),
            tint = TawaznTheme.colors.gradientMiddle
        )

        Text(
            text = stringResource("onboarding.welcome.title"),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Text(
            text = stringResource("onboarding.welcome.subtitle"),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Text(
            text = stringResource("onboarding.welcome.description"),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
fun LanguageSelectionPage() {
    val stringProvider: StringProvider = koinInject()
    val currentLanguage by stringProvider.currentLanguage.collectAsState()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Icon(
            imageVector = PhosphorIcons.Bold.Translate,
            contentDescription = "Language",
            modifier = Modifier.size(100.dp),
            tint = TawaznTheme.colors.gradientMiddle
        )

        Text(
            text = stringResource("onboarding.language.title"),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Text(
            text = stringResource("onboarding.language.description"),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Language Options
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Language.entries.forEach { language ->
                LanguageOption(
                    language = language,
                    isSelected = language == currentLanguage,
                    onClick = {
                        scope.launch {
                            stringProvider.setLanguage(language)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun LanguageOption(
    language: Language,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = language.nativeName,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isSelected) TawaznTheme.colors.gradientMiddle else MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = language.displayName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (isSelected) {
                Icon(
                    imageVector = PhosphorIcons.Bold.CheckCircle,
                    contentDescription = "Selected",
                    tint = TawaznTheme.colors.gradientMiddle,
                    modifier = Modifier.size(28.dp)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .background(
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f),
                            shape = MaterialTheme.shapes.small
                        )
                )
            }
        }
    }
}

@Composable
fun FeaturePage() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource("onboarding.features.title"),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        FeatureItem(
            icon = PhosphorIcons.Bold.Clock,
            title = stringResource("onboarding.feature.usage_tracking.title"),
            description = stringResource("onboarding.feature.usage_tracking.description")
        )

        FeatureItem(
            icon = PhosphorIcons.Bold.Prohibit,
            title = stringResource("onboarding.feature.app_blocking.title"),
            description = stringResource("onboarding.feature.app_blocking.description")
        )

        FeatureItem(
            icon = PhosphorIcons.Bold.ClockCountdown,
            title = stringResource("onboarding.feature.smart_scheduling.title"),
            description = stringResource("onboarding.feature.smart_scheduling.description")
        )

        FeatureItem(
            icon = PhosphorIcons.Bold.ChartBar,
            title = stringResource("onboarding.feature.insights.title"),
            description = stringResource("onboarding.feature.insights.description")
        )
    }
}

@Composable
fun PermissionPage(
    permissionState: PermissionState,
    onRequestPermissions: () -> Unit,
    onCheckPermissions: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Icon(
            imageVector = PhosphorIcons.Bold.Shield,
            contentDescription = "Permissions",
            modifier = Modifier.size(80.dp),
            tint = TawaznTheme.colors.gradientMiddle
        )

        Text(
            text = stringResource("onboarding.permissions.title"),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        // Status Banner
        if (permissionState.hasAllPermissions) {
            GlassCard(
                useGradient = false,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = PhosphorIcons.Bold.CheckCircle,
                        contentDescription = "Success",
                        tint = TawaznTheme.colors.success,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = stringResource("onboarding.permissions.all_granted"),
                        style = MaterialTheme.typography.titleMedium,
                        color = TawaznTheme.colors.success,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // Permission Cards
        PermissionCard(
            title = stringResource("onboarding.permissions.screen_time.title"),
            description = stringResource("onboarding.permissions.screen_time.description"),
            icon = PhosphorIcons.Bold.DeviceMobile,
            isGranted = permissionState.hasUsageStatsPermission,
            isRequired = true,
            onRequestClick = onRequestPermissions
        )

        PermissionCard(
            title = stringResource("onboarding.permissions.app_blocking.title"),
            description = stringResource("onboarding.permissions.app_blocking.description"),
            icon = PhosphorIcons.Bold.User,
            isGranted = permissionState.hasAccessibilityPermission,
            isRequired = true,
            onRequestClick = onRequestPermissions
        )

        // Loading indicator
        if (permissionState.isRequesting) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
                Text(
                    text = stringResource("onboarding.permissions.requesting"),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Refresh permissions button
        if (permissionState.permissionRequested && !permissionState.hasAllPermissions) {
            TextButton(onClick = onCheckPermissions) {
                Icon(
                    imageVector = PhosphorIcons.Bold.ArrowsClockwise,
                    contentDescription = "Refresh",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(stringResource("common.check_again"))
            }
        }

        // Privacy note
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = PhosphorIcons.Bold.Lock,
                contentDescription = "Privacy",
                tint = TawaznTheme.colors.success,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = stringResource("onboarding.permissions.privacy_note"),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ReadyPage(
    permissionState: PermissionState,
    onStartServices: () -> Unit
) {
    // Start background services when page is shown
    LaunchedEffect(Unit) {
        onStartServices()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Icon(
            imageVector = PhosphorIcons.Bold.CheckCircle,
            contentDescription = "Ready",
            modifier = Modifier.size(100.dp),
            tint = TawaznTheme.colors.success
        )

        Text(
            text = stringResource("onboarding.ready.title"),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        GlassCard(useGradient = true) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = stringResource("onboarding.ready.subtitle"),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = stringResource("onboarding.ready.description"),
                    style = MaterialTheme.typography.bodyMedium
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    QuickTip(stringResource("onboarding.ready.tip1"))
                    QuickTip(stringResource("onboarding.ready.tip2"))
                    QuickTip(stringResource("onboarding.ready.tip3"))
                    QuickTip(stringResource("onboarding.ready.tip4"))
                }
            }
        }

        // Show permission warning if not granted
        if (!permissionState.hasAllPermissions) {
            GlassCard(useGradient = false) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = PhosphorIcons.Bold.Warning,
                        contentDescription = "Warning",
                        tint = TawaznTheme.colors.warning,
                        modifier = Modifier.size(24.dp)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource("onboarding.ready.limited_functionality"),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = TawaznTheme.colors.warning
                        )
                        Text(
                            text = stringResource("onboarding.ready.limited_description"),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FeatureItem(
    icon: ImageVector,
    title: String,
    description: String
) {
    GlassCard {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = TawaznTheme.colors.gradientMiddle,
                modifier = Modifier.size(40.dp)
            )
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun QuickTip(text: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = PhosphorIcons.Bold.Check,
            contentDescription = null,
            tint = TawaznTheme.colors.gradientMiddle,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
