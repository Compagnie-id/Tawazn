package id.compagnie.tawazn.feature.settings

import id.compagnie.tawazn.design.icons.TawaznIcons

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
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import kotlin.math.roundToInt
class UsageGoalsScreen : Screen {
    @Composable
    override fun Content() {
        UsageGoalsContent()
    }
}
@Composable
fun UsageGoalsContent() {
    val navigator = LocalNavigator.currentOrThrow
    val appPreferences: AppPreferences = koinInject()
    val scope = rememberCoroutineScope()
    // Collect preferences
    val dailyGoalMinutes by appPreferences.dailyUsageGoalMinutes.collectAsState(initial = 180)
    val weeklyGoalMinutes by appPreferences.weeklyUsageGoalMinutes.collectAsState(initial = 1260)
    // Local state for editing
    var dailyGoalHours by remember { mutableStateOf(dailyGoalMinutes / 60f) }
    var weeklyGoalHours by remember { mutableStateOf(weeklyGoalMinutes / 60f) }
    var hasChanges by remember { mutableStateOf(false) }
    // Update local state when preferences change
    LaunchedEffect(dailyGoalMinutes, weeklyGoalMinutes) {
        if (!hasChanges) {
            dailyGoalHours = dailyGoalMinutes / 60f
            weeklyGoalHours = weeklyGoalMinutes / 60f
        }
    TawaznTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Usage Goals") },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(TawaznIcons.ArrowBack, "Back")
                        }
                    },
                    actions = {
                        if (hasChanges) {
                            TextButton(
                                onClick = {
                                    scope.launch {
                                        appPreferences.setDailyUsageGoal((dailyGoalHours * 60).roundToInt())
                                        appPreferences.setWeeklyUsageGoal((weeklyGoalHours * 60).roundToInt())
                                        hasChanges = false
                                    }
                                }
                            ) {
                                Text("Save")
                            }
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
                // Header
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    useGradient = true
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                imageVector = TawaznIcons.Flag,
                                contentDescription = "Goals",
                                modifier = Modifier.size(32.dp),
                                tint = TawaznTheme.colors.gradientMiddle
                            )
                            Text(
                                text = "Set Your Goals",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                        Text(
                            text = "Set daily and weekly screen time goals to help maintain a healthy digital balance.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                // Daily Goal
                Text(
                    text = "Daily Goal",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = TawaznTheme.colors.gradientMiddle
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                            Column {
                                Text(
                                    text = "Target Screen Time",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Medium
                                )
                                    text = "Maximum daily usage",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                text = formatHours(dailyGoalHours),
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = TawaznTheme.colors.gradientMiddle
                        Slider(
                            value = dailyGoalHours,
                            onValueChange = {
                                dailyGoalHours = it
                                hasChanges = true
                            },
                            valueRange = 0.5f..12f,
                            steps = 22, // 0.5 hour increments
                            colors = SliderDefaults.colors(
                                thumbColor = TawaznTheme.colors.gradientMiddle,
                                activeTrackColor = TawaznTheme.colors.gradientMiddle
                            horizontalArrangement = Arrangement.SpaceBetween
                                text = "30 min",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                text = "12 hours",
                // Quick Presets - Daily
                    text = "Quick Presets",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                    PresetButton(
                        text = "1h",
                        onClick = {
                            dailyGoalHours = 1f
                            hasChanges = true
                        },
                        modifier = Modifier.weight(1f)
                        text = "2h",
                            dailyGoalHours = 2f
                        text = "3h",
                            dailyGoalHours = 3f
                        text = "4h",
                            dailyGoalHours = 4f
                // Weekly Goal
                Spacer(Modifier.height(8.dp))
                    text = "Weekly Goal",
                                    text = "Maximum weekly usage",
                                text = formatHours(weeklyGoalHours),
                                color = TawaznTheme.colors.info
                            value = weeklyGoalHours,
                                weeklyGoalHours = it
                            valueRange = 3f..84f, // 3 hours to 84 hours (12h/day * 7)
                            steps = 80,
                                thumbColor = TawaznTheme.colors.info,
                                activeTrackColor = TawaznTheme.colors.info
                                text = "3 hours",
                                text = "84 hours",
                // Quick Presets - Weekly
                        text = "14h",
                            weeklyGoalHours = 14f
                        text = "21h",
                            weeklyGoalHours = 21f
                        text = "28h",
                            weeklyGoalHours = 28f
                        text = "35h",
                            weeklyGoalHours = 35f
                // Tips
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                imageVector = TawaznIcons.Lightbulb,
                                contentDescription = "Tips",
                                tint = TawaznTheme.colors.warning,
                                modifier = Modifier.size(20.dp)
                                text = "Tips for Success",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold
                            text = "• Start with realistic goals you can achieve\n" +
                                    "• Gradually reduce screen time over weeks\n" +
                                    "• Use focus sessions during peak usage times\n" +
                                    "• Review your analytics regularly for insights",
                            style = MaterialTheme.typography.bodySmall,
                // Save button (if changes)
                if (hasChanges) {
                    GradientButton(
                        text = "Save Goals",
                            scope.launch {
                                appPreferences.setDailyUsageGoal((dailyGoalHours * 60).roundToInt())
                                appPreferences.setWeeklyUsageGoal((weeklyGoalHours * 60).roundToInt())
                                hasChanges = false
                        modifier = Modifier.fillMaxWidth()
fun PresetButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        contentPadding = PaddingValues(8.dp)
    ) {
        Text(text, style = MaterialTheme.typography.bodySmall)
fun formatHours(hours: Float): String {
    val wholeHours = hours.toInt()
    val minutes = ((hours - wholeHours) * 60).roundToInt()
    return when {
        wholeHours > 0 && minutes > 0 -> "${wholeHours}h ${minutes}m"
        wholeHours > 0 -> "${wholeHours}h"
        minutes > 0 -> "${minutes}m"
        else -> "0m"
