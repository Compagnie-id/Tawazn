package id.compagnie.tawazn.feature.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import id.compagnie.tawazn.design.component.GlassCard
import id.compagnie.tawazn.design.component.GradientButton
import id.compagnie.tawazn.design.theme.TawaznTheme
import id.compagnie.tawazn.domain.model.BlockSession
import id.compagnie.tawazn.domain.model.CreateBlockSessionRequest
import kotlinx.datetime.*

data class CreateEditFocusSessionScreen(
    val existingSession: BlockSession? = null
) : Screen {
    @Composable
    override fun Content() {
        val screenModel = koinScreenModel<FocusSessionScreenModel>()
        CreateEditFocusSessionContent(screenModel, existingSession)
    }
}

@Composable
fun CreateEditFocusSessionContent(
    screenModel: FocusSessionScreenModel,
    existingSession: BlockSession?
) {
    val navigator = LocalNavigator.currentOrThrow
    val isEditMode = existingSession != null

    // Form state
    var name by remember { mutableStateOf(existingSession?.name ?: "") }
    var description by remember { mutableStateOf(existingSession?.description ?: "") }
    var startHour by remember { mutableStateOf(existingSession?.startTime?.toLocalDateTime(TimeZone.currentSystemDefault())?.hour ?: 9) }
    var startMinute by remember { mutableStateOf(existingSession?.startTime?.toLocalDateTime(TimeZone.currentSystemDefault())?.minute ?: 0) }
    var endHour by remember { mutableStateOf(existingSession?.endTime?.toLocalDateTime(TimeZone.currentSystemDefault())?.hour ?: 17) }
    var endMinute by remember { mutableStateOf(existingSession?.endTime?.toLocalDateTime(TimeZone.currentSystemDefault())?.minute ?: 0) }
    var repeatDaily by remember { mutableStateOf(existingSession?.repeatDaily ?: false) }
    var repeatWeekly by remember { mutableStateOf(existingSession?.repeatWeekly ?: false) }
    var selectedDays by remember { mutableStateOf(existingSession?.repeatDays?.toSet() ?: emptySet()) }
    var showTimePickerDialog by remember { mutableStateOf<TimePickerType?>(null) }
    var showDayPickerDialog by remember { mutableStateOf(false) }

    val isFormValid = name.isNotBlank()

    TawaznTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(if (isEditMode) "Edit Session" else "Create Session") },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(Icons.Default.ArrowBack, "Back")
                        }
                    },
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
                // Basic Info Section
                Text(
                    text = "Basic Information",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = TawaznTheme.colors.gradientMiddle
                )

                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Session Name") },
                            placeholder = { Text("e.g., Work Focus, Study Time") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("Description (Optional)") },
                            placeholder = { Text("What's this session for?") },
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 3
                        )
                    }
                }

                // Time Section
                Text(
                    text = "Schedule",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = TawaznTheme.colors.gradientMiddle
                )

                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        // Start Time
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showTimePickerDialog = TimePickerType.START },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccessTime,
                                    contentDescription = "Start Time",
                                    tint = TawaznTheme.colors.gradientMiddle
                                )
                                Column {
                                    Text(
                                        text = "Start Time",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = "${startHour.toString().padStart(2, '0')}:${startMinute.toString().padStart(2, '0')}",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = TawaznTheme.colors.gradientMiddle
                                    )
                                }
                            }
                            Icon(Icons.Default.Edit, "Edit")
                        }

                        Divider()

                        // End Time
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showTimePickerDialog = TimePickerType.END },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccessTime,
                                    contentDescription = "End Time",
                                    tint = MaterialTheme.colorScheme.error
                                )
                                Column {
                                    Text(
                                        text = "End Time",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = "${endHour.toString().padStart(2, '0')}:${endMinute.toString().padStart(2, '0')}",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                            Icon(Icons.Default.Edit, "Edit")
                        }
                    }
                }

                // Repeat Section
                Text(
                    text = "Repeat",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = TawaznTheme.colors.gradientMiddle
                )

                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        // Daily toggle
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Repeat Daily",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Switch(
                                checked = repeatDaily,
                                onCheckedChange = {
                                    repeatDaily = it
                                    if (it) {
                                        repeatWeekly = false
                                        selectedDays = emptySet()
                                    }
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = androidx.compose.ui.graphics.Color.White,
                                    checkedTrackColor = TawaznTheme.colors.gradientMiddle
                                )
                            )
                        }

                        Divider()

                        // Weekly toggle
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Repeat Weekly",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Switch(
                                checked = repeatWeekly,
                                onCheckedChange = {
                                    repeatWeekly = it
                                    if (it) {
                                        repeatDaily = false
                                        selectedDays = emptySet()
                                    }
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = androidx.compose.ui.graphics.Color.White,
                                    checkedTrackColor = TawaznTheme.colors.gradientMiddle
                                )
                            )
                        }

                        Divider()

                        // Specific days
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(enabled = !repeatDaily && !repeatWeekly) {
                                    showDayPickerDialog = true
                                },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Specific Days",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = if (repeatDaily || repeatWeekly)
                                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                                    else MaterialTheme.colorScheme.onSurface
                                )
                                if (selectedDays.isNotEmpty()) {
                                    Text(
                                        text = selectedDays.joinToString(", ") { it.name.take(3) },
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TawaznTheme.colors.gradientMiddle
                                    )
                                }
                            }
                            Icon(
                                Icons.Default.ChevronRight,
                                "Select Days",
                                tint = if (repeatDaily || repeatWeekly)
                                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f)
                                else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                // Action buttons
                GradientButton(
                    text = if (isEditMode) "Update Session" else "Create Session",
                    onClick = {
                        if (!isFormValid) return@GradientButton

                        val now = Clock.System.now()
                        val today = now.toLocalDateTime(TimeZone.currentSystemDefault()).date

                        val startTime = LocalDateTime(
                            today,
                            LocalTime(startHour, startMinute)
                        ).toInstant(TimeZone.currentSystemDefault())

                        val endTime = LocalDateTime(
                            today,
                            LocalTime(endHour, endMinute)
                        ).toInstant(TimeZone.currentSystemDefault())

                        if (isEditMode && existingSession != null) {
                            val updatedSession = existingSession.copy(
                                name = name,
                                description = description.ifBlank { null },
                                startTime = startTime,
                                endTime = endTime,
                                repeatDaily = repeatDaily,
                                repeatWeekly = repeatWeekly,
                                repeatDays = selectedDays.toList(),
                                updatedAt = now
                            )
                            screenModel.updateSession(updatedSession) {
                                navigator.pop()
                            }
                        } else {
                            val request = CreateBlockSessionRequest(
                                name = name,
                                description = description.ifBlank { null },
                                startTime = startTime,
                                endTime = endTime,
                                repeatDaily = repeatDaily,
                                repeatWeekly = repeatWeekly,
                                repeatDays = selectedDays.toList(),
                                appPackageNames = existingSession?.blockedApps ?: emptyList()
                            )
                            screenModel.createSession(request) {
                                navigator.pop()
                            }
                        }
                    },
                    enabled = isFormValid,
                    modifier = Modifier.fillMaxWidth()
                )

                if (isEditMode) {
                    OutlinedButton(
                        onClick = { navigator.pop() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Cancel")
                    }
                }
            }
        }

        // Time Picker Dialog
        showTimePickerDialog?.let { type ->
            TimePickerDialog(
                initialHour = if (type == TimePickerType.START) startHour else endHour,
                initialMinute = if (type == TimePickerType.START) startMinute else endMinute,
                onDismiss = { showTimePickerDialog = null },
                onConfirm = { hour, minute ->
                    if (type == TimePickerType.START) {
                        startHour = hour
                        startMinute = minute
                    } else {
                        endHour = hour
                        endMinute = minute
                    }
                    showTimePickerDialog = null
                }
            )
        }

        // Day Picker Dialog
        if (showDayPickerDialog) {
            DayPickerDialog(
                selectedDays = selectedDays,
                onDismiss = { showDayPickerDialog = false },
                onConfirm = { days ->
                    selectedDays = days
                    if (days.isNotEmpty()) {
                        repeatDaily = false
                        repeatWeekly = false
                    }
                    showDayPickerDialog = false
                }
            )
        }
    }
}

enum class TimePickerType {
    START, END
}

@Composable
fun TimePickerDialog(
    initialHour: Int,
    initialMinute: Int,
    onDismiss: () -> Unit,
    onConfirm: (hour: Int, minute: Int) -> Unit
) {
    var hour by remember { mutableStateOf(initialHour) }
    var minute by remember { mutableStateOf(initialMinute) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Time") },
        text = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Hour picker
                OutlinedTextField(
                    value = hour.toString().padStart(2, '0'),
                    onValueChange = {
                        it.toIntOrNull()?.let { h ->
                            if (h in 0..23) hour = h
                        }
                    },
                    modifier = Modifier.width(80.dp),
                    textStyle = MaterialTheme.typography.headlineMedium,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )

                Text(
                    text = ":",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                // Minute picker
                OutlinedTextField(
                    value = minute.toString().padStart(2, '0'),
                    onValueChange = {
                        it.toIntOrNull()?.let { m ->
                            if (m in 0..59) minute = m
                        }
                    },
                    modifier = Modifier.width(80.dp),
                    textStyle = MaterialTheme.typography.headlineMedium,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(hour, minute) }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun DayPickerDialog(
    selectedDays: Set<DayOfWeek>,
    onDismiss: () -> Unit,
    onConfirm: (Set<DayOfWeek>) -> Unit
) {
    var selected by remember { mutableStateOf(selectedDays) }
    val allDays = remember {
        listOf(
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY,
            DayOfWeek.SATURDAY,
            DayOfWeek.SUNDAY
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Days") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                allDays.forEach { day ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selected = if (day in selected) {
                                    selected - day
                                } else {
                                    selected + day
                                }
                            },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(day.name.lowercase().replaceFirstChar { it.uppercase() })
                        Checkbox(
                            checked = day in selected,
                            onCheckedChange = {
                                selected = if (it) selected + day else selected - day
                            }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(selected) }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
