package id.compagnie.tawazn.design.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Bold
import com.adamglin.phosphoricons.bold.Package

/**
 * iOS implementation - displays fallback icon
 * TODO: Implement iOS app icon loading
 */
@Composable
actual fun AppIcon(
    packageName: String,
    contentDescription: String?,
    modifier: Modifier,
    size: Dp
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = PhosphorIcons.Bold.Package,
            contentDescription = contentDescription,
            modifier = Modifier.size(size * 0.6f),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
