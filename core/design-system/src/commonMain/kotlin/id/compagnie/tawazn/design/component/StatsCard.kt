package id.compagnie.tawazn.design.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import id.compagnie.tawazn.design.theme.TawaznTheme

/**
 * Neubrutalism Stats Card
 * Displays a statistic with bold styling
 */
@Composable
fun StatsCard(
    title: String,
    value: String,
    subtitle: String? = null,
    icon: ImageVector? = null,
    modifier: Modifier = Modifier,
    useGradient: Boolean = true,
    backgroundColor: Color? = null
) {
    val cardColor = backgroundColor ?: if (useGradient) {
        TawaznTheme.colors.cardYellow
    } else {
        TawaznTheme.colors.card
    }

    NeuCard(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = cardColor
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onSurface
                )

                subtitle?.let {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            icon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = title,
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

/**
 * Colored Stats Card variants for different data types
 */
@Composable
fun PrimaryStatsCard(
    title: String,
    value: String,
    subtitle: String? = null,
    icon: ImageVector? = null,
    modifier: Modifier = Modifier
) {
    StatsCard(
        title = title,
        value = value,
        subtitle = subtitle,
        icon = icon,
        modifier = modifier,
        backgroundColor = TawaznTheme.colors.cardCyan
    )
}

@Composable
fun SuccessStatsCard(
    title: String,
    value: String,
    subtitle: String? = null,
    icon: ImageVector? = null,
    modifier: Modifier = Modifier
) {
    StatsCard(
        title = title,
        value = value,
        subtitle = subtitle,
        icon = icon,
        modifier = modifier,
        backgroundColor = TawaznTheme.colors.cardGreen
    )
}

@Composable
fun WarningStatsCard(
    title: String,
    value: String,
    subtitle: String? = null,
    icon: ImageVector? = null,
    modifier: Modifier = Modifier
) {
    StatsCard(
        title = title,
        value = value,
        subtitle = subtitle,
        icon = icon,
        modifier = modifier,
        backgroundColor = TawaznTheme.colors.cardOrange
    )
}

@Composable
fun AccentStatsCard(
    title: String,
    value: String,
    subtitle: String? = null,
    icon: ImageVector? = null,
    modifier: Modifier = Modifier
) {
    StatsCard(
        title = title,
        value = value,
        subtitle = subtitle,
        icon = icon,
        modifier = modifier,
        backgroundColor = TawaznTheme.colors.cardPink
    )
}
