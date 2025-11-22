package id.compagnie.tawazn.design.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Platform-specific app icon component
 * Displays app icon loaded from the system
 */
@Composable
expect fun AppIcon(
    packageName: String,
    contentDescription: String? = null,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp
)
