package id.compagnie.tawazn.feature.appblocking

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import id.compagnie.tawazn.design.icons.TawaznIcons
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import id.compagnie.tawazn.design.component.GlassCard
import id.compagnie.tawazn.design.component.GradientButton
import id.compagnie.tawazn.design.theme.TawaznTheme
import id.compagnie.tawazn.domain.model.AppInfo
import id.compagnie.tawazn.domain.repository.AppRepository
import id.compagnie.tawazn.domain.repository.BlockedAppRepository
import id.compagnie.tawazn.domain.usecase.BlockAppUseCase
import id.compagnie.tawazn.domain.usecase.UnblockAppUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AppBlockingScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel = koinScreenModel<AppBlockingScreenModel>()
        AppBlockingContent(screenModel)
    }
}

class AppBlockingScreenModel : ScreenModel, KoinComponent {
    private val appRepository: AppRepository by inject()
    private val blockedAppRepository: BlockedAppRepository by inject()
    private val blockAppUseCase: BlockAppUseCase by inject()
    private val unblockAppUseCase: UnblockAppUseCase by inject()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _apps = MutableStateFlow<List<AppInfo>>(emptyList())

    private val _blockedApps = MutableStateFlow<Set<String>>(emptySet())

    val filteredApps = combine(_apps, _blockedApps, _searchQuery) { apps, blocked, query ->
        val filtered = if (query.isBlank()) {
            apps
        } else {
            apps.filter { it.appName.contains(query, ignoreCase = true) }
        }
        filtered.map { AppBlockingState(it, blocked.contains(it.packageName)) }
    }.stateIn(screenModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        screenModelScope.launch {
            appRepository.getNonSystemApps().collect { _apps.value = it }
        }

        screenModelScope.launch {
            blockedAppRepository.getAllBlockedApps().collect { blocked ->
                _blockedApps.value = blocked.filter { it.isBlocked }.map { it.packageName }.toSet()
            }
        }
    }

    fun updateSearch(query: String) {
        _searchQuery.value = query
    }

    fun toggleBlock(app: AppInfo, isBlocked: Boolean) {
        screenModelScope.launch {
            if (isBlocked) {
                unblockAppUseCase(app.packageName)
            } else {
                blockAppUseCase(app.packageName, app.appName, app.iconPath)
            }
        }
    }
}

data class AppBlockingState(
    val app: AppInfo,
    val isBlocked: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBlockingContent(screenModel: AppBlockingScreenModel) {
    val apps by screenModel.filteredApps.collectAsState()
    val searchQuery by screenModel.searchQuery.collectAsState()
    val navigator = LocalNavigator.currentOrThrow

    TawaznTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Block Apps") },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(TawaznIcons.ArrowBack, "Back")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.9f)
                    )
                )
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { screenModel.updateSearch(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    placeholder = { Text("Search apps...") },
                    leadingIcon = { Icon(TawaznIcons.Search, "Search") },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { screenModel.updateSearch("") }) {
                                Icon(TawaznIcons.Clear, "Clear")
                            }
                        }
                    },
                    singleLine = true,
                    shape = MaterialTheme.shapes.large
                )

                // Stats
                val blockedCount = apps.count { it.isBlocked }
                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    useGradient = true
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "$blockedCount Apps Blocked",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "${apps.size} total apps",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Icon(
                            imageVector = TawaznIcons.Block,
                            contentDescription = "Blocked",
                            tint = TawaznTheme.colors.gradientMiddle,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }

                // App List
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(apps, key = { it.app.packageName }) { appState ->
                        AppBlockItem(
                            appState = appState,
                            onToggle = { screenModel.toggleBlock(appState.app, appState.isBlocked) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AppBlockItem(
    appState: AppBlockingState,
    onToggle: () -> Unit
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        borderWidth = if (appState.isBlocked) 2.dp else 1.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // App Icon Placeholder
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = TawaznIcons.Apps,
                        contentDescription = appState.app.appName,
                        tint = if (appState.isBlocked)
                            TawaznTheme.colors.gradientMiddle
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = appState.app.appName,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = appState.app.category.name.replace("_", " ").lowercase()
                            .replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Toggle Switch
            Switch(
                checked = appState.isBlocked,
                onCheckedChange = { onToggle() },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = TawaznTheme.colors.gradientMiddle,
                    uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                    uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        }
    }
}
