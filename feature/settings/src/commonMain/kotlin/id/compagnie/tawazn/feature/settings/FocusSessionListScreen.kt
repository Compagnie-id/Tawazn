@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)

package id.compagnie.tawazn.feature.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import id.compagnie.tawazn.design.component.GlassCard
import id.compagnie.tawazn.design.component.GradientButton
import id.compagnie.tawazn.design.icons.TawaznIcons
import id.compagnie.tawazn.design.theme.TawaznTheme
import id.compagnie.tawazn.domain.model.BlockSession
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime

class FocusSessionListScreen : Screen {
    @Composable
    override fun Content() {
        val screenModel = getScreenModel<FocusSessionScreenModel>()
        FocusSessionListContent(screenModel)
    }
}

@Composable
fun FocusSessionListContent(screenModel: FocusSessionScreenModel) {
    val navigator = LocalNavigator.currentOrThrow
    val sessions by screenModel.allSessions.collectAsState(initial = emptyList())
    var showDeleteDialog by remember { mutableStateOf<BlockSession?>(null) }

    TawaznTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Focus Sessions") },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(TawaznIcons.ArrowBack, "Back")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navigator.push(CreateEditFocusSessionScreen()) },
                    containerColor = TawaznTheme.colors.gradientMiddle
                ) {
                    Icon(TawaznIcons.Add, "Add Session", tint = MaterialTheme.colorScheme.onPrimary)
                }
            }
        ) { padding ->
            if (sessions.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(
                            imageVector = TawaznIcons.EventBusy,
                            contentDescription = "No Sessions",
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "No Focus Sessions",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Create a session to schedule app blocking",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        GradientButton(
                            text = "Create Session",
                            onClick = { navigator.push(CreateEditFocusSessionScreen()) },
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .padding(padding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(sessions) { session ->
                        SessionCard(
                            session = session,
                            onToggle = { screenModel.toggleSession(session.id, !session.isActive) },
                            onEdit = { navigator.push(CreateEditFocusSessionScreen(session)) },
                            onDelete = { showDeleteDialog = session }
                        )
                    }
                }
            }
        }

        // Delete confirmation dialog
        showDeleteDialog?.let { session ->
            AlertDialog(
                onDismissRequest = { showDeleteDialog = null },
                icon = {
                    Icon(
                        imageVector = TawaznIcons.Warning,
                        contentDescription = "Warning",
                        tint = MaterialTheme.colorScheme.error
                    )
                },
                title = { Text("Delete Session?") },
                text = {
                    Text("Are you sure you want to delete \"${session.name}\"? This action cannot be undone.")
                },
                confirmButton = {
                    Button(
                        onClick = {
                            screenModel.deleteSession(session.id)
                            showDeleteDialog = null
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Delete")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = null }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun SessionCard(
    session: BlockSession,
    onToggle: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val startTime = session.startTime.toLocalDateTime(TimeZone.currentSystemDefault())
    val endTime = session.endTime.toLocalDateTime(TimeZone.currentSystemDefault())

    GlassCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = session.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    if (!session.description.isNullOrBlank()) {
                        Text(
                            text = session.description!!,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Switch(
                    checked = session.isActive,
                    onCheckedChange = { onToggle() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = androidx.compose.ui.graphics.Color.White,
                        checkedTrackColor = TawaznTheme.colors.gradientMiddle
                    )
                )
            }

            // Time info
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = TawaznIcons.Schedule,
                    contentDescription = "Time",
                    modifier = Modifier.size(16.dp),
                    tint = TawaznTheme.colors.gradientMiddle
                )
                Text(
                    text = "${startTime.hour.toString().padStart(2, '0')}:${startTime.minute.toString().padStart(2, '0')} - ${endTime.hour.toString().padStart(2, '0')}:${endTime.minute.toString().padStart(2, '0')}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Repeat info
            if (session.repeatDaily || session.repeatWeekly || session.repeatDays.isNotEmpty()) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = TawaznIcons.Repeat,
                        contentDescription = "Repeat",
                        modifier = Modifier.size(16.dp),
                        tint = TawaznTheme.colors.info
                    )
                    Text(
                        text = when {
                            session.repeatDaily -> "Daily"
                            session.repeatWeekly -> "Weekly"
                            session.repeatDays.isNotEmpty() -> session.repeatDays.joinToString(", ") {
                                it.name.take(3)
                            }
                            else -> "Once"
                        },
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // Blocked apps count
            if (session.blockedApps.isNotEmpty()) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = TawaznIcons.Block,
                        contentDescription = "Apps",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.error
                    )
                    Text(
                        text = "${session.blockedApps.size} app${if (session.blockedApps.size != 1) "s" else ""} blocked",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // Action buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onEdit,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(TawaznIcons.Edit, "Edit", modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Edit")
                }
                OutlinedButton(
                    onClick = onDelete,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(TawaznIcons.Delete, "Delete", modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Delete")
                }
            }
        }
    }
}
