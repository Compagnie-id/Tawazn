package id.compagnie.tawazn.design.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import id.compagnie.tawazn.design.theme.NeuBlack
import id.compagnie.tawazn.design.theme.Primary
import id.compagnie.tawazn.design.theme.TawaznTheme

/**
 * Neubrutalism Button with solid color and hard shadow
 */
@Composable
fun NeuButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    backgroundColor: Color = Primary,
    textColor: Color = Color.White,
    cornerRadius: Dp = 12.dp,
    contentPadding: PaddingValues = PaddingValues(horizontal = 24.dp, vertical = 14.dp)
) {
    val borderColor = TawaznTheme.colors.border
    val shadowColor = TawaznTheme.colors.shadow
    val shadowOffsetX = TawaznTheme.colors.shadowOffsetX
    val shadowOffsetY = TawaznTheme.colors.shadowOffsetY
    val borderWidth = TawaznTheme.colors.borderWidth

    val actualBackgroundColor = if (enabled) backgroundColor else Color.Gray
    val actualTextColor = if (enabled) textColor else Color.DarkGray

    Box(modifier = modifier) {
        // Shadow layer
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(x = shadowOffsetX, y = shadowOffsetY)
                .clip(RoundedCornerShape(cornerRadius))
                .background(shadowColor, RoundedCornerShape(cornerRadius))
        )

        // Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(cornerRadius))
                .background(actualBackgroundColor, RoundedCornerShape(cornerRadius))
                .border(
                    width = borderWidth,
                    color = borderColor,
                    shape = RoundedCornerShape(cornerRadius)
                )
                .clickable(
                    enabled = enabled,
                    onClick = onClick,
                    indication = ripple(),
                    interactionSource = remember { MutableInteractionSource() }
                )
                .padding(contentPadding),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = actualTextColor
            )
        }
    }
}

/**
 * Backwards compatibility alias for GradientButton
 * Use NeuButton for new code
 */
@Composable
fun GradientButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    cornerRadius: Dp = 12.dp,
    contentPadding: PaddingValues = PaddingValues(horizontal = 24.dp, vertical = 14.dp)
) {
    NeuButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        backgroundColor = Primary,
        cornerRadius = cornerRadius,
        contentPadding = contentPadding
    )
}

/**
 * Outlined Neubrutalism Button
 */
@Composable
fun OutlinedNeuButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    cornerRadius: Dp = 12.dp
) {
    val borderColor = TawaznTheme.colors.border
    val shadowColor = TawaznTheme.colors.shadow
    val shadowOffsetX = TawaznTheme.colors.shadowOffsetX
    val shadowOffsetY = TawaznTheme.colors.shadowOffsetY
    val borderWidth = TawaznTheme.colors.borderWidth

    Box(modifier = modifier) {
        // Shadow layer
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(x = shadowOffsetX, y = shadowOffsetY)
                .clip(RoundedCornerShape(cornerRadius))
                .background(shadowColor, RoundedCornerShape(cornerRadius))
        )

        // Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(cornerRadius))
                .background(TawaznTheme.colors.card, RoundedCornerShape(cornerRadius))
                .border(
                    width = borderWidth,
                    color = borderColor,
                    shape = RoundedCornerShape(cornerRadius)
                )
                .clickable(
                    enabled = enabled,
                    onClick = onClick,
                    indication = ripple(),
                    interactionSource = remember { MutableInteractionSource() }
                )
                .padding(horizontal = 24.dp, vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = if (enabled) MaterialTheme.colorScheme.primary else Color.Gray
            )
        }
    }
}

/**
 * Backwards compatibility alias for OutlinedGlassButton
 * Use OutlinedNeuButton for new code
 */
@Composable
fun OutlinedGlassButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    cornerRadius: Dp = 12.dp
) {
    OutlinedNeuButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        cornerRadius = cornerRadius
    )
}

/**
 * Secondary Neubrutalism Button with accent color
 */
@Composable
fun SecondaryNeuButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    cornerRadius: Dp = 12.dp,
    contentPadding: PaddingValues = PaddingValues(horizontal = 24.dp, vertical = 14.dp)
) {
    NeuButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        backgroundColor = TawaznTheme.colors.cardYellow,
        textColor = MaterialTheme.colorScheme.onSurface,
        cornerRadius = cornerRadius,
        contentPadding = contentPadding
    )
}
