package id.compagnie.tawazn.design.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import id.compagnie.tawazn.design.theme.TawaznTheme

/**
 * Liquid Glass Card Component with Glassmorphism effect
 *
 * Features:
 * - Semi-transparent background with blur effect
 * - Subtle border with gradient
 * - Customizable corner radius
 * - Supports gradient overlay
 */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 16.dp,
    borderWidth: Dp = 1.dp,
    useGradient: Boolean = false,
    content: @Composable BoxScope.() -> Unit
) {
    val glassColor = TawaznTheme.colors.glass
    val borderColor = TawaznTheme.colors.glassBorder

    val backgroundModifier = if (useGradient) {
        Modifier.background(
            brush = Brush.linearGradient(
                colors = listOf(
                    TawaznTheme.colors.gradientStart.copy(alpha = 0.1f),
                    TawaznTheme.colors.gradientMiddle.copy(alpha = 0.1f),
                    TawaznTheme.colors.gradientEnd.copy(alpha = 0.1f)
                )
            ),
            shape = RoundedCornerShape(cornerRadius)
        )
    } else {
        Modifier.background(
            color = glassColor,
            shape = RoundedCornerShape(cornerRadius)
        )
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .then(backgroundModifier)
            .border(
                width = borderWidth,
                color = borderColor,
                shape = RoundedCornerShape(cornerRadius)
            )
            .padding(16.dp),
        content = content
    )
}

/**
 * Elevated Glass Card with shadow effect
 */
@Composable
fun ElevatedGlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 20.dp,
    elevation: Dp = 8.dp,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
    ) {
        // Shadow layer
        Box(
            modifier = Modifier
                .matchParentSize()
                .blur(elevation)
                .background(
                    color = Color.Black.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(cornerRadius)
                )
        )

        // Glass card layer
        GlassCard(
            cornerRadius = cornerRadius,
            content = content
        )
    }
}
