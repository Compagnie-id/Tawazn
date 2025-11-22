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
                    repeat(12) { index ->
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
                        2 -> UserProfilePage(screenModel)
                        3 -> ScreenTimeInputPage(screenModel)
                        4 -> HabitChangePage(screenModel)
                        5 -> GuessScreenTimePage(screenModel)
                        6 -> ScreenTimeRevealPage(screenModel)
                        7 -> TawagnIntroPage()
                        8 -> FeaturePage()
                        9 -> PermissionPage(
                            permissionState = permissionState,
                            onRequestPermissions = { screenModel.requestPermissions() },
                            onCheckPermissions = { screenModel.checkPermissions() }
                        )
                        10 -> DistractingAppsPage(screenModel)
                        11 -> ReadyPage(
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
                        text = if (currentPage == 11) stringResource("onboarding.get_started") else stringResource("common.continue"),
                        onClick = {
                            if (currentPage < 11) {
                                currentPage++
                            } else {
                                // Complete onboarding - App.kt will automatically show the main app
                                screenModel.completeOnboarding()
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (currentPage > 0 && currentPage < 11) {
                        TextButton(
                            onClick = { currentPage-- },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(stringResource("common.back"))
                        }
                    }

                    if (currentPage < 11) {
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
            isRequired = false,
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

// NEW ONBOARDING PAGES

@Composable
fun UserProfilePage(screenModel: OnboardingScreenModel) {
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    val userName by screenModel.userName.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Icon(
            imageVector = PhosphorIcons.Bold.User,
            contentDescription = "Profile",
            modifier = Modifier.size(100.dp),
            tint = TawaznTheme.colors.gradientMiddle
        )

        Text(
            text = stringResource("onboarding.profile.title"),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Text(
            text = stringResource("onboarding.profile.description"),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Name Input
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(stringResource("onboarding.profile.name_label")) },
            placeholder = { Text(stringResource("onboarding.profile.name_placeholder")) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // Age Input
        OutlinedTextField(
            value = age,
            onValueChange = { if (it.all { char -> char.isDigit() } && it.length <= 3) age = it },
            label = { Text(stringResource("onboarding.profile.age_label")) },
            placeholder = { Text(stringResource("onboarding.profile.age_placeholder")) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // Save button
        if (name.isNotBlank() && age.isNotBlank()) {
            LaunchedEffect(name, age) {
                val ageInt = age.toIntOrNull()
                if (ageInt != null && ageInt > 0) {
                    screenModel.saveUserInfo(name, ageInt)
                }
            }
        }

        if (userName.isNotBlank()) {
            Text(
                text = "${stringResource("onboarding.profile.greeting")} $userName! 👋",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = TawaznTheme.colors.gradientMiddle
            )
        }
    }
}

@Composable
fun ScreenTimeInputPage(screenModel: OnboardingScreenModel) {
    var selectedHours by remember { mutableStateOf(4) }
    val userName by screenModel.userName.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Icon(
            imageVector = PhosphorIcons.Bold.Clock,
            contentDescription = "Screen Time",
            modifier = Modifier.size(100.dp),
            tint = TawaznTheme.colors.gradientMiddle
        )

        if (userName.isNotBlank()) {
            Text(
                text = "$userName,",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Text(
            text = stringResource("onboarding.screentime.title"),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Text(
            text = stringResource("onboarding.screentime.description"),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Hours display
        Text(
            text = if (selectedHours <= 16) "$selectedHours ${stringResource("onboarding.screentime.hours_suffix")}"
                  else stringResource("onboarding.screentime.16plus"),
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold,
            color = TawaznTheme.colors.gradientMiddle
        )

        // Slider
        Slider(
            value = selectedHours.toFloat(),
            onValueChange = { selectedHours = it.toInt() },
            valueRange = 1f..17f,
            steps = 15,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        )

        LaunchedEffect(selectedHours) {
            val hours = if (selectedHours > 16) 16 else selectedHours
            screenModel.saveDailyScreenTime(hours)
        }
    }
}

@Composable
fun HabitChangePage(screenModel: OnboardingScreenModel) {
    val selectedHabits by screenModel.selectedHabits.collectAsState()
    val userName by screenModel.userName.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        if (userName.isNotBlank()) {
            Text(
                text = "$userName,",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Text(
            text = stringResource("onboarding.habits.title"),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Text(
            text = stringResource("onboarding.habits.description"),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            id.compagnie.tawazn.domain.model.PhoneHabit.entries.forEach { habit ->
                HabitOption(
                    habit = habit,
                    isSelected = selectedHabits.contains(habit),
                    onClick = { screenModel.toggleHabit(habit) }
                )
            }
        }

        LaunchedEffect(selectedHabits) {
            if (selectedHabits.isNotEmpty()) {
                screenModel.saveSelectedHabits()
            }
        }
    }
}

@Composable
fun HabitOption(
    habit: id.compagnie.tawazn.domain.model.PhoneHabit,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource("onboarding.habits.${habit.key}"),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )

            if (isSelected) {
                Icon(
                    imageVector = PhosphorIcons.Bold.CheckCircle,
                    contentDescription = "Selected",
                    tint = TawaznTheme.colors.gradientMiddle,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun GuessScreenTimePage(screenModel: OnboardingScreenModel) {
    var guessedDays by remember { mutableStateOf(100f) }
    val userName by screenModel.userName.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        if (userName.isNotBlank()) {
            Text(
                text = "$userName,",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Text(
            text = stringResource("onboarding.guess.title"),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Text(
            text = stringResource("onboarding.guess.description"),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Guessed days display
        Text(
            text = "${guessedDays.toInt()}",
            style = MaterialTheme.typography.displayLarge,
            fontWeight = FontWeight.Bold,
            color = TawaznTheme.colors.gradientMiddle
        )

        Text(
            text = stringResource("onboarding.guess.days_label"),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Slider (range 0-365 days, step by 10)
        Slider(
            value = guessedDays,
            onValueChange = { guessedDays = it },
            valueRange = 0f..365f,
            steps = 36,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        )

        LaunchedEffect(guessedDays) {
            val hours = (guessedDays.toInt() * 24)
            screenModel.saveGuessedYearlyHours(hours)
        }
    }
}

@Composable
fun ScreenTimeRevealPage(screenModel: OnboardingScreenModel) {
    val dailyHours by screenModel.dailyScreenTimeHours.collectAsState()
    val userAge by screenModel.userAge.collectAsState()
    val userName by screenModel.userName.collectAsState()

    val yearlyHours = (dailyHours ?: 0) * 365
    val yearlyDays = yearlyHours / 24
    val lifetimeYears = screenModel.calculateLifetimeProjection()
    val remainingYears = 80 - (userAge ?: 0)
    val percentage = if (remainingYears > 0) (lifetimeYears / remainingYears * 100) else 0.0

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        if (userName.isNotBlank()) {
            Text(
                text = "$userName,",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Text(
            text = stringResource("onboarding.reveal.title"),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Yearly Stats
        GlassCard {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = stringResource("onboarding.reveal.yearly_title"),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$yearlyHours",
                            style = MaterialTheme.typography.displayMedium,
                            fontWeight = FontWeight.Bold,
                            color = TawaznTheme.colors.gradientMiddle
                        )
                        Text(
                            text = stringResource("onboarding.reveal.yearly_hours"),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Text(
                        text = "=",
                        style = MaterialTheme.typography.headlineLarge
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$yearlyDays",
                            style = MaterialTheme.typography.displayMedium,
                            fontWeight = FontWeight.Bold,
                            color = TawaznTheme.colors.gradientEnd
                        )
                        Text(
                            text = stringResource("onboarding.reveal.yearly_days"),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Lifetime Projection
        GlassCard {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = stringResource("onboarding.reveal.projection_title"),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = stringResource("onboarding.reveal.projection_description")
                        .replace("{age}", (userAge ?: 0).toString()),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "${stringResource("onboarding.reveal.thats")} ${String.format("%.1f", lifetimeYears)}",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error
                )

                Text(
                    text = stringResource("onboarding.reveal.projection_years"),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = "${stringResource("onboarding.reveal.or")} ${String.format("%.1f", percentage)}%",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error
                )

                Text(
                    text = stringResource("onboarding.reveal.projection_percentage"),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun TawagnIntroPage() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Icon(
            imageVector = PhosphorIcons.Bold.Shield,
            contentDescription = "Tawazn",
            modifier = Modifier.size(120.dp),
            tint = TawaznTheme.colors.gradientMiddle
        )

        Text(
            text = stringResource("onboarding.intro.title"),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Text(
            text = stringResource("onboarding.intro.subtitle"),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            color = TawaznTheme.colors.gradientMiddle
        )

        Text(
            text = stringResource("onboarding.intro.description"),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        GlassCard {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                listOf(
                    "onboarding.intro.benefit1",
                    "onboarding.intro.benefit2",
                    "onboarding.intro.benefit3",
                    "onboarding.intro.benefit4"
                ).forEach { benefit ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = PhosphorIcons.Bold.CheckCircle,
                            contentDescription = null,
                            tint = TawaznTheme.colors.gradientMiddle,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = stringResource(benefit),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }

        Text(
            text = stringResource("onboarding.intro.cta"),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            color = TawaznTheme.colors.gradientMiddle
        )
    }
}

@Composable
fun DistractingAppsPage(screenModel: OnboardingScreenModel) {
    val installedApps by screenModel.installedApps.collectAsState()
    val distractingApps by screenModel.distractingApps.collectAsState()
    val isLoadingApps by screenModel.isLoadingApps.collectAsState()
    var showAppPicker by remember { mutableStateOf(false) }

    // Auto-refresh apps when page is shown if installedApps is empty
    LaunchedEffect(Unit) {
        if (installedApps.isEmpty()) {
            screenModel.refreshInstalledApps()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Icon(
            imageVector = PhosphorIcons.Bold.Prohibit,
            contentDescription = "Distracting Apps",
            modifier = Modifier.size(100.dp),
            tint = TawaznTheme.colors.gradientMiddle
        )

        Text(
            text = stringResource("onboarding.distracting_apps.title"),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Text(
            text = stringResource("onboarding.distracting_apps.description"),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        // Selected apps count
        Text(
            text = if (distractingApps.isEmpty())
                stringResource("onboarding.distracting_apps.no_apps_selected")
            else
                stringResource("onboarding.distracting_apps.apps_selected")
                    .replace("{count}", distractingApps.size.toString()),
            style = MaterialTheme.typography.titleMedium,
            color = TawaznTheme.colors.gradientMiddle
        )

        // Add app button
        GradientButton(
            text = "+ ${stringResource("onboarding.distracting_apps.select_all")}",
            onClick = { showAppPicker = true },
            modifier = Modifier.fillMaxWidth()
        )

        // Show selected apps
        if (distractingApps.isNotEmpty()) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                distractingApps.take(5).forEach { app ->
                    GlassCard {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = app.appName,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "${app.dailyLimitMinutes / 60}${stringResource("onboarding.distracting_apps.hours")} " +
                                          "${app.dailyLimitMinutes % 60}${stringResource("onboarding.distracting_apps.minutes")}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            IconButton(onClick = { screenModel.removeDistractingApp(app.packageName) }) {
                                Icon(
                                    imageVector = PhosphorIcons.Bold.Warning,
                                    contentDescription = "Remove",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }

                if (distractingApps.size > 5) {
                    Text(
                        text = "+${distractingApps.size - 5} more",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                }
            }
        }

        // Show loading indicator or empty state
        when {
            isLoadingApps -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp),
                    color = TawaznTheme.colors.gradientMiddle
                )
                Text(
                    text = stringResource("onboarding.distracting_apps.loading"),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(16.dp)
                )
            }
            installedApps.isEmpty() && !isLoadingApps -> {
                Text(
                    text = stringResource("onboarding.distracting_apps.no_apps_found"),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(16.dp)
                )
                OutlinedButton(
                    onClick = { screenModel.refreshInstalledApps() },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Icon(
                        imageVector = PhosphorIcons.Bold.ArrowsClockwise,
                        contentDescription = "Refresh",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource("onboarding.distracting_apps.refresh"))
                }
            }
        }

        LaunchedEffect(distractingApps) {
            if (distractingApps.isNotEmpty()) {
                screenModel.saveDistractingApps()
            }
        }
    }

    // App Picker Dialog
    if (showAppPicker && installedApps.isNotEmpty()) {
        AppPickerDialog(
            installedApps = installedApps,
            selectedApps = distractingApps.map { it.packageName },
            onAppSelected = { appInfo, timeLimit ->
                screenModel.updateDistractingApp(
                    DistractingApp(
                        packageName = appInfo.packageName,
                        appName = appInfo.appName,
                        category = appInfo.category.name,
                        dailyLimitMinutes = timeLimit
                    )
                )
            },
            onDismiss = { showAppPicker = false }
        )
    }
}

@Composable
fun AppPickerDialog(
    installedApps: List<id.compagnie.tawazn.domain.model.AppInfo>,
    selectedApps: List<String>,
    onAppSelected: (id.compagnie.tawazn.domain.model.AppInfo, Int) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedCategory by remember { mutableStateOf<id.compagnie.tawazn.domain.model.AppCategory?>(null) }
    var timeLimit by remember { mutableStateOf(60) } // minutes

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Distracting Apps") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Category filter
                Text(
                    text = "Filter by category:",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = selectedCategory == null,
                        onClick = { selectedCategory = null },
                        label = { Text("All") }
                    )
                    FilterChip(
                        selected = selectedCategory == id.compagnie.tawazn.domain.model.AppCategory.SOCIAL_MEDIA,
                        onClick = { selectedCategory = id.compagnie.tawazn.domain.model.AppCategory.SOCIAL_MEDIA },
                        label = { Text("Social") }
                    )
                    FilterChip(
                        selected = selectedCategory == id.compagnie.tawazn.domain.model.AppCategory.GAMES,
                        onClick = { selectedCategory = id.compagnie.tawazn.domain.model.AppCategory.GAMES },
                        label = { Text("Games") }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Time limit slider
                Text(
                    text = "Daily limit: ${timeLimit / 60}h ${timeLimit % 60}m",
                    style = MaterialTheme.typography.labelMedium
                )
                Slider(
                    value = timeLimit.toFloat(),
                    onValueChange = { timeLimit = it.toInt() },
                    valueRange = 15f..480f,
                    steps = 30,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // App list
                val filteredApps = if (selectedCategory != null) {
                    installedApps.filter { it.category == selectedCategory }
                } else {
                    installedApps
                }

                filteredApps.forEach { app ->
                    val isSelected = selectedApps.contains(app.packageName)

                    GlassCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(MaterialTheme.shapes.medium)
                            .clickable {
                                if (!isSelected) {
                                    onAppSelected(app, timeLimit)
                                }
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = app.appName,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                                Text(
                                    text = app.category.name,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            if (isSelected) {
                                Icon(
                                    imageVector = PhosphorIcons.Bold.CheckCircle,
                                    contentDescription = "Selected",
                                    tint = TawaznTheme.colors.gradientMiddle,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }

                if (filteredApps.isEmpty()) {
                    Text(
                        text = "No apps in this category",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Done")
            }
        }
    )
}
