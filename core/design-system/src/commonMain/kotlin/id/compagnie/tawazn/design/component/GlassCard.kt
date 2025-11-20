package id.compagnie.tawazn.design.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import id.compagnie.tawazn.design.theme.TawaznTheme

/**
 * Neubrutalism Card Component
 *
 * Features:
 * - Solid background color
 * - Bold black border
 * - Hard offset shadow (no blur)
 * - Customizable corner radius
 * - Optional accent color background
 */
@Composable
fun NeuCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = TawaznTheme.colors.card,
    cornerRadius: Dp = 12.dp,
    borderWidth: Dp = TawaznTheme.colors.borderWidth,
    shadowOffsetX: Dp = TawaznTheme.colors.shadowOffsetX,
    shadowOffsetY: Dp = TawaznTheme.colors.shadowOffsetY,
    content: @Composable BoxScope.() -> Unit
) {
    val borderColor = TawaznTheme.colors.border
    val shadowColor = TawaznTheme.colors.shadow

    Box(modifier = modifier) {
        // Shadow layer (hard offset, no blur)
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(x = shadowOffsetX, y = shadowOffsetY)
                .clip(RoundedCornerShape(cornerRadius))
                .background(shadowColor, RoundedCornerShape(cornerRadius))
        )

        // Main card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(cornerRadius))
                .background(backgroundColor, RoundedCornerShape(cornerRadius))
                .border(
                    width = borderWidth,
                    color = borderColor,
                    shape = RoundedCornerShape(cornerRadius)
                )
                .padding(16.dp),
            content = content
        )
    }
}

/**
 * Backwards compatibility alias for GlassCard
 * Use NeuCard for new code
 */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 12.dp,
    borderWidth: Dp = TawaznTheme.colors.borderWidth,
    useGradient: Boolean = false,
    content: @Composable BoxScope.() -> Unit
) {
    val backgroundColor = if (useGradient) {
        TawaznTheme.colors.cardYellow
    } else {
        TawaznTheme.colors.card
    }

    NeuCard(
        modifier = modifier,
        backgroundColor = backgroundColor,
        cornerRadius = cornerRadius,
        borderWidth = borderWidth,
        content = content
    )
}

/**
 * Elevated Neubrutalism Card with larger shadow
 */
@Composable
fun ElevatedNeuCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = TawaznTheme.colors.card,
    cornerRadius: Dp = 16.dp,
    content: @Composable BoxScope.() -> Unit
) {
    NeuCard(
        modifier = modifier,
        backgroundColor = backgroundColor,
        cornerRadius = cornerRadius,
        shadowOffsetX = 6.dp,
        shadowOffsetY = 6.dp,
        content = content
    )
}

/**
 * Backwards compatibility alias for ElevatedGlassCard
 * Use ElevatedNeuCard for new code
 */
@Composable
fun ElevatedGlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 16.dp,
    elevation: Dp = 8.dp,
    content: @Composable BoxScope.() -> Unit
) {
    ElevatedNeuCard(
        modifier = modifier,
        cornerRadius = cornerRadius,
        content = content
    )
}
