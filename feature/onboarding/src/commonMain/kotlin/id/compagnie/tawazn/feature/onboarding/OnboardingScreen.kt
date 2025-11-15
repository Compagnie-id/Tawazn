package id.compagnie.tawazn.feature.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import id.compagnie.tawazn.design.component.GlassCard
import id.compagnie.tawazn.design.component.GradientButton
import id.compagnie.tawazn.design.theme.TawaznTheme
import id.compagnie.tawazn.MainScreen

class OnboardingScreen : Screen {

    @Composable
    override fun Content() {
        OnboardingContent()
    }
}

@Composable
fun OnboardingContent() {
    var currentPage by remember { mutableStateOf(0) }
    val navigator = LocalNavigator.currentOrThrow

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
                        1 -> FeaturePage()
                        2 -> PermissionPage()
                        3 -> ReadyPage()
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Navigation Buttons
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    GradientButton(
                        text = if (currentPage == 3) "Get Started" else "Continue",
                        onClick = {
                            if (currentPage < 3) {
                                currentPage++
                            } else {
                                // Navigate to dashboard
                                navigator.replace(MainScreen())
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (currentPage > 0 && currentPage < 3) {
                        TextButton(
                            onClick = { currentPage-- },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Back")
                        }
                    }

                    if (currentPage < 3) {
                        TextButton(
                            onClick = {
                                navigator.replace(MainScreen())
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Skip", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
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
            imageVector = Icons.Default.Phone,
            contentDescription = "Tawazn",
            modifier = Modifier.size(120.dp),
            tint = TawaznTheme.colors.gradientMiddle
        )

        Text(
            text = "Welcome to Tawazn",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Text(
            text = "توازن",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Find balance in your digital life. Track your screen time, block distracting apps, and achieve digital wellness.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
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
            text = "Powerful Features",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        FeatureItem(
            icon = Icons.Default.Timer,
            title = "Usage Tracking",
            description = "Monitor your screen time with detailed statistics and insights"
        )

        FeatureItem(
            icon = Icons.Default.Block,
            title = "App Blocking",
            description = "Block distracting apps and create focus sessions"
        )

        FeatureItem(
            icon = Icons.Default.Schedule,
            title = "Smart Scheduling",
            description = "Set up automatic blocking schedules for better productivity"
        )

        FeatureItem(
            icon = Icons.Default.Analytics,
            title = "Insights & Analytics",
            description = "Understand your digital habits with weekly reports"
        )
    }
}

@Composable
fun PermissionPage() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Security,
            contentDescription = "Permissions",
            modifier = Modifier.size(100.dp),
            tint = TawaznTheme.colors.gradientMiddle
        )

        Text(
            text = "Required Permissions",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        GlassCard(useGradient = true) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Usage Access",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = "To track your screen time and app usage, Tawazn needs access to usage statistics. This data stays on your device and is never shared.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Privacy",
                        tint = TawaznTheme.colors.success,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Your data is private and secure",
                        style = MaterialTheme.typography.bodySmall,
                        color = TawaznTheme.colors.success
                    )
                }
            }
        }

        Text(
            text = "You'll be asked to grant this permission in the next step",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ReadyPage() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = "Ready",
            modifier = Modifier.size(120.dp),
            tint = TawaznTheme.colors.success
        )

        Text(
            text = "You're All Set!",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        GlassCard(useGradient = true) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Start Your Journey",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = "Tawazn is ready to help you achieve digital wellness. Here's what you can do:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    QuickTip("View your daily screen time")
                    QuickTip("Block distracting apps instantly")
                    QuickTip("Create focus sessions")
                    QuickTip("Track your progress")
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
            imageVector = Icons.Default.Check,
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
