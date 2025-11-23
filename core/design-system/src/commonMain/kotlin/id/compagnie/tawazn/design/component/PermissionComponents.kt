package id.compagnie.tawazn.design.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Bold
import com.adamglin.phosphoricons.bold.Check
import com.adamglin.phosphoricons.bold.Warning
import com.adamglin.phosphoricons.bold.X
import id.compagnie.tawazn.design.theme.NeuBlack
import id.compagnie.tawazn.design.theme.TawaznTheme

/**
 * Permission card component for displaying permission status
 * Neubrutalism style with bold borders and hard shadows
 *
 * @param title Permission title (e.g., "Usage Stats Permission")
 * @param description Permission description
 * @param icon Icon to display
 * @param isGranted Whether the permission is granted (shows checkmark if true, X if false)
 * @param isRequired Whether this permission is required (shows "Required" badge)
 * @param modifier Modifier for the card
 */
@Composable
fun PermissionCard(
    title: String,
    description: String,
    icon: ImageVector,
    isGranted: Boolean,
    isRequired: Boolean = true,
    modifier: Modifier = Modifier
) {
    val cardColor = if (isGranted) {
        TawaznTheme.colors.cardGreen
    } else {
        TawaznTheme.colors.cardOrange
    }

    NeuCard(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = cardColor,
        cornerRadius = 12.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon with neubrutalism style
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(TawaznTheme.colors.card)
                    .border(
                        width = 2.dp,
                        color = TawaznTheme.colors.border,
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isGranted) {
                        TawaznTheme.colors.success
                    } else {
                        TawaznTheme.colors.warning
                    },
                    modifier = Modifier.size(24.dp)
                )
            }

            // Content
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = NeuBlack
                )

                if (isRequired) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(TawaznTheme.colors.error)
                            .border(
                                width = 1.dp,
                                color = NeuBlack,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "Required",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = NeuBlack.copy(alpha = 0.8f)
                )
            }

            // Status Indicator
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(
                        if (isGranted) TawaznTheme.colors.success
                        else TawaznTheme.colors.error
                    )
                    .border(
                        width = 2.dp,
                        color = NeuBlack,
                        shape = RoundedCornerShape(6.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isGranted) PhosphorIcons.Bold.Check else PhosphorIcons.Bold.X,
                    contentDescription = if (isGranted) "Granted" else "Not Granted",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

/**
 * Permission status badge - Neubrutalism style
 */
@Composable
fun PermissionStatusBadge(
    isGranted: Boolean,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isGranted) {
        TawaznTheme.colors.success
    } else {
        TawaznTheme.colors.error
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(backgroundColor)
            .border(
                width = 2.dp,
                color = NeuBlack,
                shape = RoundedCornerShape(6.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isGranted) PhosphorIcons.Bold.Check else PhosphorIcons.Bold.Warning,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = if (isGranted) "Granted" else "Not Granted",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

/**
 * Platform info card showing system details - Neubrutalism style
 */
@Composable
fun PlatformInfoCard(
    platformInfo: Map<String, String>,
    modifier: Modifier = Modifier
) {
    NeuCard(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = TawaznTheme.colors.cardLavender,
        cornerRadius = 12.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Platform Information",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = NeuBlack
            )

            platformInfo.forEach { (key, value) ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = formatKey(key),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        color = NeuBlack.copy(alpha = 0.7f)
                    )
                    Text(
                        text = value,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = NeuBlack
                    )
                }
            }
        }
    }
}

private fun formatKey(key: String): String {
    return key.split(Regex("(?=[A-Z])"))
        .joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } }
        .replace("_", " ")
        .replace(Regex("\\s+"), " ")
        .trim()
}

/**
 * Sync status indicator - Neubrutalism style
 */
@Composable
fun SyncStatusIndicator(
    isReady: Boolean,
    label: String,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(
                    color = if (isReady) TawaznTheme.colors.success else TawaznTheme.colors.error
                )
                .border(
                    width = 2.dp,
                    color = NeuBlack,
                    shape = RoundedCornerShape(3.dp)
                )
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
