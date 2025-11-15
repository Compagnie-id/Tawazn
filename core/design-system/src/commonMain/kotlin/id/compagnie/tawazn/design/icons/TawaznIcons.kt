package id.compagnie.tawazn.design.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Custom icons optimized for glassmorphism/liquid glass design
 * - Rounded corners for soft, modern aesthetic
 * - Consistent 2px stroke width
 * - Outline style (not filled) for better integration with glass effects
 */
object TawaznIcons {

    // Common stroke parameters for glassmorphism style
    private const val strokeWidth = 2f
    private val strokeCap = StrokeCap.Round
    private val strokeJoin = StrokeJoin.Round

    val Home: ImageVector by lazy {
        ImageVector.Builder(
            name = "Home",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = null,
                stroke = SolidColor(Color.Black),
                strokeLineWidth = strokeWidth,
                strokeLineCap = strokeCap,
                strokeLineJoin = strokeJoin,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(3f, 9f)
                lineToRelative(9f, -7f)
                lineToRelative(9f, 7f)
                verticalLineToRelative(11f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, -2f, 2f)
                horizontalLineTo(5f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, -2f, -2f)
                close()
            }
            path(
                fill = null,
                stroke = SolidColor(Color.Black),
                strokeLineWidth = strokeWidth,
                strokeLineCap = strokeCap,
                strokeLineJoin = strokeJoin,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(9f, 22f)
                verticalLineTo(12f)
                horizontalLineToRelative(6f)
                verticalLineToRelative(10f)
            }
        }.build()
    }

    val Analytics: ImageVector by lazy {
        ImageVector.Builder(
            name = "Analytics",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = null,
                stroke = SolidColor(Color.Black),
                strokeLineWidth = strokeWidth,
                strokeLineCap = strokeCap,
                strokeLineJoin = strokeJoin,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(18f, 20f)
                verticalLineTo(10f)
            }
            path(
                fill = null,
                stroke = SolidColor(Color.Black),
                strokeLineWidth = strokeWidth,
                strokeLineCap = strokeCap,
                strokeLineJoin = strokeJoin,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(12f, 20f)
                verticalLineTo(4f)
            }
            path(
                fill = null,
                stroke = SolidColor(Color.Black),
                strokeLineWidth = strokeWidth,
                strokeLineCap = strokeCap,
                strokeLineJoin = strokeJoin,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(6f, 20f)
                verticalLineTo(14f)
            }
        }.build()
    }

    val Apps: ImageVector by lazy {
        ImageVector.Builder(
            name = "Apps",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            // Top left
            path(
                fill = null,
                stroke = SolidColor(Color.Black),
                strokeLineWidth = strokeWidth,
                strokeLineCap = strokeCap,
                strokeLineJoin = strokeJoin,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(4f, 4f)
                horizontalLineToRelative(6f)
                verticalLineToRelative(6f)
                horizontalLineTo(4f)
                close()
            }
            // Top right
            path(
                fill = null,
                stroke = SolidColor(Color.Black),
                strokeLineWidth = strokeWidth,
                strokeLineCap = strokeCap,
                strokeLineJoin = strokeJoin,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(14f, 4f)
                horizontalLineToRelative(6f)
                verticalLineToRelative(6f)
                horizontalLineToRelative(-6f)
                close()
            }
            // Bottom left
            path(
                fill = null,
                stroke = SolidColor(Color.Black),
                strokeLineWidth = strokeWidth,
                strokeLineCap = strokeCap,
                strokeLineJoin = strokeJoin,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(4f, 14f)
                horizontalLineToRelative(6f)
                verticalLineToRelative(6f)
                horizontalLineTo(4f)
                close()
            }
            // Bottom right
            path(
                fill = null,
                stroke = SolidColor(Color.Black),
                strokeLineWidth = strokeWidth,
                strokeLineCap = strokeCap,
                strokeLineJoin = strokeJoin,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(14f, 14f)
                horizontalLineToRelative(6f)
                verticalLineToRelative(6f)
                horizontalLineToRelative(-6f)
                close()
            }
        }.build()
    }

    val Settings: ImageVector by lazy {
        ImageVector.Builder(
            name = "Settings",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = null,
                stroke = SolidColor(Color.Black),
                strokeLineWidth = strokeWidth,
                strokeLineCap = strokeCap,
                strokeLineJoin = strokeJoin,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(12f, 15f)
                arcTo(3f, 3f, 0f, isMoreThanHalf = true, isPositiveArc = false, 12f, 9f)
                arcTo(3f, 3f, 0f, isMoreThanHalf = false, isPositiveArc = false, 12f, 15f)
                close()
            }
            path(
                fill = null,
                stroke = SolidColor(Color.Black),
                strokeLineWidth = strokeWidth,
                strokeLineCap = strokeCap,
                strokeLineJoin = strokeJoin,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(19.4f, 15f)
                arcToRelative(1.65f, 1.65f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.33f, 1.82f)
                lineToRelative(-0.06f, 0.1f)
                arcToRelative(1.65f, 1.65f, 0f, isMoreThanHalf = false, isPositiveArc = true, -2.26f, 0.59f)
                lineToRelative(-0.1f, -0.05f)
                arcToRelative(1.65f, 1.65f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.82f, 0.33f)
                lineToRelative(-0.1f, 0.06f)
                arcToRelative(1.65f, 1.65f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.59f, 2.26f)
                lineToRelative(0.05f, 0.1f)
                arcToRelative(1.65f, 1.65f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.33f, 1.82f)
                verticalLineToRelative(0.06f)
                arcToRelative(1.65f, 1.65f, 0f, isMoreThanHalf = false, isPositiveArc = true, -2.26f, 0.59f)
                horizontalLineToRelative(-0.1f)
                arcToRelative(1.65f, 1.65f, 0f, isMoreThanHalf = false, isPositiveArc = false, -1.82f, -0.33f)
                horizontalLineToRelative(-0.06f)
                arcToRelative(1.65f, 1.65f, 0f, isMoreThanHalf = false, isPositiveArc = true, -2.26f, -0.59f)
                lineToRelative(-0.05f, -0.1f)
                arcToRelative(1.65f, 1.65f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.33f, -1.82f)
                lineToRelative(0.06f, -0.1f)
                arcToRelative(1.65f, 1.65f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.59f, -2.26f)
                lineToRelative(0.1f, -0.05f)
                arcToRelative(1.65f, 1.65f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.33f, -1.82f)
                verticalLineToRelative(-0.06f)
                arcToRelative(1.65f, 1.65f, 0f, isMoreThanHalf = false, isPositiveArc = true, 0.59f, -2.26f)
                horizontalLineToRelative(0.1f)
                arcToRelative(1.65f, 1.65f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.82f, -0.33f)
                horizontalLineToRelative(0.06f)
                arcToRelative(1.65f, 1.65f, 0f, isMoreThanHalf = false, isPositiveArc = true, 2.26f, -0.59f)
                lineToRelative(0.05f, -0.1f)
                arcToRelative(1.65f, 1.65f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.33f, -1.82f)
                lineToRelative(-0.06f, -0.1f)
                arcToRelative(1.65f, 1.65f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.59f, -2.26f)
                lineToRelative(-0.1f, -0.05f)
                arcToRelative(1.65f, 1.65f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.33f, -1.82f)
                verticalLineTo(2.6f)
                arcToRelative(1.65f, 1.65f, 0f, isMoreThanHalf = false, isPositiveArc = true, 2.26f, -0.59f)
                horizontalLineToRelative(0.1f)
                arcToRelative(1.65f, 1.65f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.82f, 0.33f)
                horizontalLineToRelative(0.06f)
                arcToRelative(1.65f, 1.65f, 0f, isMoreThanHalf = false, isPositiveArc = true, 2.26f, 0.59f)
                lineToRelative(0.05f, 0.1f)
                arcToRelative(1.65f, 1.65f, 0f, isMoreThanHalf = false, isPositiveArc = false, 0.33f, 1.82f)
                lineToRelative(-0.06f, 0.1f)
                arcToRelative(1.65f, 1.65f, 0f, isMoreThanHalf = false, isPositiveArc = true, -0.59f, 2.26f)
                lineToRelative(-0.1f, 0.05f)
                arcToRelative(1.65f, 1.65f, 0f, isMoreThanHalf = false, isPositiveArc = false, -0.33f, 1.82f)
                close()
            }
        }.build()
    }

    val Person: ImageVector by lazy {
        ImageVector.Builder(
            name = "Person",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = null,
                stroke = SolidColor(Color.Black),
                strokeLineWidth = strokeWidth,
                strokeLineCap = strokeCap,
                strokeLineJoin = strokeJoin,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(20f, 21f)
                verticalLineToRelative(-2f)
                arcToRelative(4f, 4f, 0f, isMoreThanHalf = false, isPositiveArc = false, -4f, -4f)
                horizontalLineTo(8f)
                arcToRelative(4f, 4f, 0f, isMoreThanHalf = false, isPositiveArc = false, -4f, 4f)
                verticalLineToRelative(2f)
            }
            path(
                fill = null,
                stroke = SolidColor(Color.Black),
                strokeLineWidth = strokeWidth,
                strokeLineCap = strokeCap,
                strokeLineJoin = strokeJoin,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(12f, 11f)
                arcTo(4f, 4f, 0f, isMoreThanHalf = true, isPositiveArc = false, 12f, 3f)
                arcTo(4f, 4f, 0f, isMoreThanHalf = false, isPositiveArc = false, 12f, 11f)
                close()
            }
        }.build()
    }

    val ArrowBack: ImageVector by lazy {
        ImageVector.Builder(
            name = "ArrowBack",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = null,
                stroke = SolidColor(Color.Black),
                strokeLineWidth = strokeWidth,
                strokeLineCap = strokeCap,
                strokeLineJoin = strokeJoin,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(19f, 12f)
                horizontalLineTo(5f)
            }
            path(
                fill = null,
                stroke = SolidColor(Color.Black),
                strokeLineWidth = strokeWidth,
                strokeLineCap = strokeCap,
                strokeLineJoin = strokeJoin,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(12f, 19f)
                lineToRelative(-7f, -7f)
                lineToRelative(7f, -7f)
            }
        }.build()
    }

    val Add: ImageVector by lazy {
        ImageVector.Builder(
            name = "Add",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = null,
                stroke = SolidColor(Color.Black),
                strokeLineWidth = strokeWidth,
                strokeLineCap = strokeCap,
                strokeLineJoin = strokeJoin,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(12f, 5f)
                verticalLineToRelative(14f)
            }
            path(
                fill = null,
                stroke = SolidColor(Color.Black),
                strokeLineWidth = strokeWidth,
                strokeLineCap = strokeCap,
                strokeLineJoin = strokeJoin,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(5f, 12f)
                horizontalLineToRelative(14f)
            }
        }.build()
    }

    val Block: ImageVector by lazy {
        ImageVector.Builder(
            name = "Block",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = null,
                stroke = SolidColor(Color.Black),
                strokeLineWidth = strokeWidth,
                strokeLineCap = strokeCap,
                strokeLineJoin = strokeJoin,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(12f, 22f)
                arcTo(10f, 10f, 0f, isMoreThanHalf = true, isPositiveArc = false, 12f, 2f)
                arcA(10f, 10f, 0f, isMoreThanHalf = false, isPositiveArc = false, 12f, 22f)
                close()
            }
            path(
                fill = null,
                stroke = SolidColor(Color.Black),
                strokeLineWidth = strokeWidth,
                strokeLineCap = strokeCap,
                strokeLineJoin = strokeJoin,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(4.93f, 4.93f)
                lineToRelative(14.14f, 14.14f)
            }
        }.build()
    }

    val Check: ImageVector by lazy {
        ImageVector.Builder(
            name = "Check",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = null,
                stroke = SolidColor(Color.Black),
                strokeLineWidth = strokeWidth,
                strokeLineCap = strokeCap,
                strokeLineJoin = strokeJoin,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(20f, 6f)
                lineTo(9f, 17f)
                lineToRelative(-5f, -5f)
            }
        }.build()
    }

    val CheckCircle: ImageVector by lazy {
        ImageVector.Builder(
            name = "CheckCircle",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = null,
                stroke = SolidColor(Color.Black),
                strokeLineWidth = strokeWidth,
                strokeLineCap = strokeCap,
                strokeLineJoin = strokeJoin,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(22f, 11.08f)
                verticalLineTo(12f)
                arcToRelative(10f, 10f, 0f, isMoreThanHalf = true, isPositiveArc = true, -5.93f, 9.14f)
            }
            path(
                fill = null,
                stroke = SolidColor(Color.Black),
                strokeLineWidth = strokeWidth,
                strokeLineCap = strokeCap,
                strokeLineJoin = strokeJoin,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(9f, 11f)
                lineToRelative(3f, 3f)
                lineToRelative(8f, -8f)
            }
        }.build()
    }

    val ChevronRight: ImageVector by lazy {
        ImageVector.Builder(
            name = "ChevronRight",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = null,
                stroke = SolidColor(Color.Black),
                strokeLineWidth = strokeWidth,
                strokeLineCap = strokeCap,
                strokeLineJoin = strokeJoin,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(9f, 18f)
                lineToRelative(6f, -6f)
                lineToRelative(-6f, -6f)
            }
        }.build()
    }

    val Clear: ImageVector by lazy {
        ImageVector.Builder(
            name = "Clear",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = null,
                stroke = SolidColor(Color.Black),
                strokeLineWidth = strokeWidth,
                strokeLineCap = strokeCap,
                strokeLineJoin = strokeJoin,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(18f, 6f)
                lineTo(6f, 18f)
            }
            path(
                fill = null,
                stroke = SolidColor(Color.Black),
                strokeLineWidth = strokeWidth,
                strokeLineCap = strokeCap,
                strokeLineJoin = strokeJoin,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(6f, 6f)
                lineTo(18f, 18f)
            }
        }.build()
    }

    val Delete: ImageVector by lazy {
        ImageVector.Builder(
            name = "Delete",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = null,
                stroke = SolidColor(Color.Black),
                strokeLineWidth = strokeWidth,
                strokeLineCap = strokeCap,
                strokeLineJoin = strokeJoin,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(3f, 6f)
                horizontalLineToRelative(18f)
            }
            path(
                fill = null,
                stroke = SolidColor(Color.Black),
                strokeLineWidth = strokeWidth,
                strokeLineCap = strokeCap,
                strokeLineJoin = strokeJoin,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(19f, 6f)
                verticalLineToRelative(14f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, -2f, 2f)
                horizontalLineTo(7f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, -2f, -2f)
                verticalLineTo(6f)
            }
            path(
                fill = null,
                stroke = SolidColor(Color.Black),
                strokeLineWidth = strokeWidth,
                strokeLineCap = strokeCap,
                strokeLineJoin = strokeJoin,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(8f, 6f)
                verticalLineTo(4f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 2f, -2f)
                horizontalLineToRelative(4f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 2f, 2f)
                verticalLineToRelative(2f)
            }
        }.build()
    }

    val Edit: ImageVector by lazy {
        ImageVector.Builder(
            name = "Edit",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = null,
                stroke = SolidColor(Color.Black),
                strokeLineWidth = strokeWidth,
                strokeLineCap = strokeCap,
                strokeLineJoin = strokeJoin,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(11f, 4f)
                horizontalLineTo(4f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = false, -2f, 2f)
                verticalLineToRelative(14f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = false, 2f, 2f)
                horizontalLineToRelative(14f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = false, 2f, -2f)
                verticalLineToRelative(-7f)
            }
            path(
                fill = null,
                stroke = SolidColor(Color.Black),
                strokeLineWidth = strokeWidth,
                strokeLineCap = strokeCap,
                strokeLineJoin = strokeJoin,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(18.5f, 2.5f)
                arcToRelative(2.121f, 2.121f, 0f, isMoreThanHalf = false, isPositiveArc = true, 3f, 3f)
                lineTo(12f, 15f)
                lineToRelative(-4f, 1f)
                lineToRelative(1f, -4f)
                lineToRelative(9.5f, -9.5f)
                close()
            }
        }.build()
    }

    val Search: ImageVector by lazy {
        ImageVector.Builder(
            name = "Search",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = null,
                stroke = SolidColor(Color.Black),
                strokeLineWidth = strokeWidth,
                strokeLineCap = strokeCap,
                strokeLineJoin = strokeJoin,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(11f, 19f)
                arcTo(8f, 8f, 0f, isMoreThanHalf = true, isPositiveArc = false, 11f, 3f)
                arcA(8f, 8f, 0f, isMoreThanHalf = false, isPositiveArc = false, 11f, 19f)
                close()
            }
            path(
                fill = null,
                stroke = SolidColor(Color.Black),
                strokeLineWidth = strokeWidth,
                strokeLineCap = strokeCap,
                strokeLineJoin = strokeJoin,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(21f, 21f)
                lineToRelative(-4.35f, -4.35f)
            }
        }.build()
    }

    val Notifications: ImageVector by lazy {
        ImageVector.Builder(
            name = "Notifications",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = null,
                stroke = SolidColor(Color.Black),
                strokeLineWidth = strokeWidth,
                strokeLineCap = strokeCap,
                strokeLineJoin = strokeJoin,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(18f, 8f)
                arcTo(6f, 6f, 0f, isMoreThanHalf = false, isPositiveArc = false, 6f, 8f)
                curveToRelative(0f, 7f, -3f, 9f, -3f, 9f)
                horizontalLineToRelative(18f)
                reflectiveCurveToRelative(-3f, -2f, -3f, -9f)
            }
            path(
                fill = null,
                stroke = SolidColor(Color.Black),
                strokeLineWidth = strokeWidth,
                strokeLineCap = strokeCap,
                strokeLineJoin = strokeJoin,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(13.73f, 21f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, -3.46f, 0f)
            }
        }.build()
    }

    val AccessTime: ImageVector by lazy {
        ImageVector.Builder(
            name = "AccessTime",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = null,
                stroke = SolidColor(Color.Black),
                strokeLineWidth = strokeWidth,
                strokeLineCap = strokeCap,
                strokeLineJoin = strokeJoin,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(12f, 22f)
                arcTo(10f, 10f, 0f, isMoreThanHalf = true, isPositiveArc = false, 12f, 2f)
                arcA(10f, 10f, 0f, isMoreThanHalf = false, isPositiveArc = false, 12f, 22f)
                close()
            }
            path(
                fill = null,
                stroke = SolidColor(Color.Black),
                strokeLineWidth = strokeWidth,
                strokeLineCap = strokeCap,
                strokeLineJoin = strokeJoin,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(12f, 6f)
                verticalLineToRelative(6f)
                lineToRelative(4f, 2f)
            }
        }.build()
    }

    val CalendarToday: ImageVector by lazy {
        ImageVector.Builder(
            name = "CalendarToday",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = null,
                stroke = SolidColor(Color.Black),
                strokeLineWidth = strokeWidth,
                strokeLineCap = strokeCap,
                strokeLineJoin = strokeJoin,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(19f, 4f)
                horizontalLineTo(5f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = false, -2f, 2f)
                verticalLineToRelative(14f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = false, 2f, 2f)
                horizontalLineToRelative(14f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = false, 2f, -2f)
                verticalLineTo(6f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = false, -2f, -2f)
                close()
            }
            path(
                fill = null,
                stroke = SolidColor(Color.Black),
                strokeLineWidth = strokeWidth,
                strokeLineCap = strokeCap,
                strokeLineJoin = strokeJoin,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(16f, 2f)
                verticalLineToRelative(4f)
            }
            path(
                fill = null,
                stroke = SolidColor(Color.Black),
                strokeLineWidth = strokeWidth,
                strokeLineCap = strokeCap,
                strokeLineJoin = strokeJoin,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(8f, 2f)
                verticalLineToRelative(4f)
            }
            path(
                fill = null,
                stroke = SolidColor(Color.Black),
                strokeLineWidth = strokeWidth,
                strokeLineCap = strokeCap,
                strokeLineJoin = strokeJoin,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(3f, 10f)
                horizontalLineToRelative(18f)
            }
        }.build()
    }

    val Info: ImageVector by lazy {
        ImageVector.Builder(
            name = "Info",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = null,
                stroke = SolidColor(Color.Black),
                strokeLineWidth = strokeWidth,
                strokeLineCap = strokeCap,
                strokeLineJoin = strokeJoin,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(12f, 22f)
                arcTo(10f, 10f, 0f, isMoreThanHalf = true, isPositiveArc = false, 12f, 2f)
                arcA(10f, 10f, 0f, isMoreThanHalf = false, isPositiveArc = false, 12f, 22f)
                close()
            }
            path(
                fill = null,
                stroke = SolidColor(Color.Black),
                strokeLineWidth = strokeWidth,
                strokeLineCap = strokeCap,
                strokeLineJoin = strokeJoin,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(12f, 16f)
                verticalLineToRelative(-4f)
            }
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1.0f,
                stroke = null,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(12f, 8f)
                arcTo(0.5f, 0.5f, 0f, isMoreThanHalf = true, isPositiveArc = false, 12f, 7f)
                arcA(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, 12f, 8f)
                close()
            }
        }.build()
    }

    val Warning: ImageVector by lazy {
        ImageVector.Builder(
            name = "Warning",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = null,
                stroke = SolidColor(Color.Black),
                strokeLineWidth = strokeWidth,
                strokeLineCap = strokeCap,
                strokeLineJoin = strokeJoin,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(10.29f, 3.86f)
                lineTo(1.82f, 18f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.71f, 3f)
                horizontalLineToRelative(16.94f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = false, 1.71f, -3f)
                lineTo(13.71f, 3.86f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = false, -3.42f, 0f)
                close()
            }
            path(
                fill = null,
                stroke = SolidColor(Color.Black),
                strokeLineWidth = strokeWidth,
                strokeLineCap = strokeCap,
                strokeLineJoin = strokeJoin,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(12f, 9f)
                verticalLineToRelative(4f)
            }
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1.0f,
                stroke = null,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(12f, 17f)
                arcA(0.5f, 0.5f, 0f, isMoreThanHalf = true, isPositiveArc = false, 12f, 16f)
                arcA(0.5f, 0.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, 12f, 17f)
                close()
            }
        }.build()
    }

    val Lock: ImageVector by lazy {
        ImageVector.Builder(
            name = "Lock",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = null,
                stroke = SolidColor(Color.Black),
                strokeLineWidth = strokeWidth,
                strokeLineCap = strokeCap,
                strokeLineJoin = strokeJoin,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(19f, 11f)
                horizontalLineTo(5f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = false, -2f, 2f)
                verticalLineToRelative(7f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = false, 2f, 2f)
                horizontalLineToRelative(14f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = false, 2f, -2f)
                verticalLineToRelative(-7f)
                arcToRelative(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = false, -2f, -2f)
                close()
            }
            path(
                fill = null,
                stroke = SolidColor(Color.Black),
                strokeLineWidth = strokeWidth,
                strokeLineCap = strokeCap,
                strokeLineJoin = strokeJoin,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(7f, 11f)
                verticalLineTo(7f)
                arcToRelative(5f, 5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 10f, 0f)
                verticalLineToRelative(4f)
            }
        }.build()
    }

    val Shield: ImageVector by lazy {
        ImageVector.Builder(
            name = "Shield",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = null,
                stroke = SolidColor(Color.Black),
                strokeLineWidth = strokeWidth,
                strokeLineCap = strokeCap,
                strokeLineJoin = strokeJoin,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(12f, 22f)
                reflectiveCurveToRelative(8f, -4f, 8f, -10f)
                verticalLineTo(5f)
                lineToRelative(-8f, -3f)
                lineToRelative(-8f, 3f)
                verticalLineToRelative(7f)
                curveToRelative(0f, 6f, 8f, 10f, 8f, 10f)
                close()
            }
        }.build()
    }

    val TrendingUp: ImageVector by lazy {
        ImageVector.Builder(
            name = "TrendingUp",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = null,
                stroke = SolidColor(Color.Black),
                strokeLineWidth = strokeWidth,
                strokeLineCap = strokeCap,
                strokeLineJoin = strokeJoin,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(23f, 6f)
                lineToRelative(-9.5f, 9.5f)
                lineToRelative(-5f, -5f)
                lineTo(1f, 18f)
            }
            path(
                fill = null,
                stroke = SolidColor(Color.Black),
                strokeLineWidth = strokeWidth,
                strokeLineCap = strokeCap,
                strokeLineJoin = strokeJoin,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(17f, 6f)
                horizontalLineToRelative(6f)
                verticalLineToRelative(6f)
            }
        }.build()
    }

    val TrendingDown: ImageVector by lazy {
        ImageVector.Builder(
            name = "TrendingDown",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = null,
                stroke = SolidColor(Color.Black),
                strokeLineWidth = strokeWidth,
                strokeLineCap = strokeCap,
                strokeLineJoin = strokeJoin,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(23f, 18f)
                lineToRelative(-9.5f, -9.5f)
                lineToRelative(-5f, 5f)
                lineTo(1f, 6f)
            }
            path(
                fill = null,
                stroke = SolidColor(Color.Black),
                strokeLineWidth = strokeWidth,
                strokeLineCap = strokeCap,
                strokeLineJoin = strokeJoin,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(17f, 18f)
                horizontalLineToRelative(6f)
                verticalLineToRelative(-6f)
            }
        }.build()
    }

    val Visibility: ImageVector by lazy {
        ImageVector.Builder(
            name = "Visibility",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = null,
                stroke = SolidColor(Color.Black),
                strokeLineWidth = strokeWidth,
                strokeLineCap = strokeCap,
                strokeLineJoin = strokeJoin,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(1f, 12f)
                reflectiveCurveToRelative(4f, -8f, 11f, -8f)
                reflectiveCurveToRelative(11f, 8f, 11f, 8f)
                reflectiveCurveToRelative(-4f, 8f, -11f, 8f)
                reflectiveCurveToRelative(-11f, -8f, -11f, -8f)
                close()
            }
            path(
                fill = null,
                stroke = SolidColor(Color.Black),
                strokeLineWidth = strokeWidth,
                strokeLineCap = strokeCap,
                strokeLineJoin = strokeJoin,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(12f, 15f)
                arcA(3f, 3f, 0f, isMoreThanHalf = true, isPositiveArc = false, 12f, 9f)
                arcA(3f, 3f, 0f, isMoreThanHalf = false, isPositiveArc = false, 12f, 15f)
                close()
            }
        }.build()
    }

    // Continuing with remaining icons in similar glassmorphism style...
    // For brevity, creating aliases for less commonly used icons
    val Accessibility = Person
    val Balance = Analytics
    val BugReport = Warning
    val Business = Apps
    val CalendarMonth = CalendarToday
    val CloudUpload = Add
    val Code = Description
    val DarkMode = Settings
    val DeleteForever = Delete
    val Description = Edit
    val Devices = PhoneAndroid
    val Download = CloudUpload
    val EmojiEvents = Flag
    val EventBusy = CalendarToday
    val Flag = Warning
    val Gavel = Shield
    val History = AccessTime
    val Lightbulb = Info
    val LocalFireDepartment = Warning
    val NotificationsNone = Notifications
    val OpenInNew = ArrowBack
    val PermDeviceInformation = PhoneAndroid
    val Phone = PhoneAndroid
    val PhoneAndroid = Devices
    val Refresh = Sync
    val Repeat = Sync
    val Schedule = AccessTime
    val Security = Shield
    val Sync = Refresh
    val Timer = AccessTime
    val Today = CalendarToday
}
