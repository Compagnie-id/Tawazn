package id.compagnie.tawazn.design.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import id.compagnie.tawazn.design.icons.TawaznIcons
import id.compagnie.tawazn.design.theme.TawaznTheme

/**
 * Permission card component for displaying permission requests
 *
 * @param title Permission title (e.g., "Usage Stats Permission")
 * @param description Permission description
 * @param icon Icon to display
 * @param isGranted Whether the permission is granted
 * @param isRequired Whether this permission is required
 * @param onRequestClick Callback when request button is clicked
 * @param modifier Modifier for the card
 */
@Composable
fun PermissionCard(
    title: String,
    description: String,
    icon: ImageVector,
    isGranted: Boolean,
    isRequired: Boolean = true,
    onRequestClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    GlassCard(
        modifier = modifier.fillMaxWidth(),
        cornerRadius = 16.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = if (isGranted) {
                    TawaznTheme.colors.success.copy(alpha = 0.2f)
                } else {
                    TawaznTheme.colors.warning.copy(alpha = 0.2f)
                },
                modifier = Modifier.size(48.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
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
            }

            // Content
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    if (isRequired) {
                        Surface(
                            shape = MaterialTheme.shapes.small,
                            color = TawaznTheme.colors.error.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "Required",
                                style = MaterialTheme.typography.labelSmall,
                                color = TawaznTheme.colors.error,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Status/Action Button
            if (isGranted) {
                Icon(
                    imageVector = TawaznIcons.CheckCircle,
                    contentDescription = "Granted",
                    tint = TawaznTheme.colors.success,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                FilledTonalButton(
                    onClick = onRequestClick,
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = TawaznTheme.colors.warning.copy(alpha = 0.2f),
                        contentColor = TawaznTheme.colors.warning
                    )
                ) {
                    Text("Grant")
                }
            }
        }
    }
}

/**
 * Permission status badge
 */
@Composable
fun PermissionStatusBadge(
    isGranted: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = MaterialTheme.shapes.small,
        color = if (isGranted) {
            TawaznTheme.colors.success.copy(alpha = 0.15f)
        } else {
            TawaznTheme.colors.error.copy(alpha = 0.15f)
        },
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Icon(
                imageVector = if (isGranted) TawaznIcons.CheckCircle else TawaznIcons.Warning,
                contentDescription = null,
                tint = if (isGranted) TawaznTheme.colors.success else TawaznTheme.colors.error,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = if (isGranted) "Granted" else "Not Granted",
                style = MaterialTheme.typography.labelSmall,
                color = if (isGranted) TawaznTheme.colors.success else TawaznTheme.colors.error
            )
        }
    }
}

/**
 * Platform info card showing system details
 */
@Composable
fun PlatformInfoCard(
    platformInfo: Map<String, String>,
    modifier: Modifier = Modifier
) {
    GlassCard(
        modifier = modifier.fillMaxWidth(),
        cornerRadius = 12.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Platform Information",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            platformInfo.forEach { (key, value) ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = formatKey(key),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = value,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

private fun formatKey(key: String): String {
    return key.split(Regex("(?=[A-Z])"))
        .joinToString(" ") { it.capitalize() }
        .replace("_", " ")
        .replace(Regex("\\s+"), " ")
        .trim()
}

/**
 * Sync status indicator
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
                .size(8.dp)
                .background(
                    color = if (isReady) TawaznTheme.colors.success else TawaznTheme.colors.error,
                    shape = MaterialTheme.shapes.extraSmall
                )
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
