package id.compagnie.tawazn.design.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = Color.White,
    primaryContainer = NeuYellow,
    onPrimaryContainer = NeuBlack,

    secondary = Secondary,
    onSecondary = Color.White,
    secondaryContainer = NeuLavender,
    onSecondaryContainer = NeuBlack,

    tertiary = Accent,
    onTertiary = Color.White,
    tertiaryContainer = NeuCyan,
    onTertiaryContainer = NeuBlack,

    background = BackgroundLight,
    onBackground = TextPrimaryLight,

    surface = SurfaceLight,
    onSurface = TextPrimaryLight,
    surfaceVariant = Color(0xFFF5F5F5),
    onSurfaceVariant = TextSecondaryLight,

    error = Error,
    onError = Color.White,
    errorContainer = NeuRed,
    onErrorContainer = NeuBlack,

    outline = NeuBorder,
    outlineVariant = NeuBlack
)

private val DarkColorScheme = darkColorScheme(
    primary = Primary,
    onPrimary = Color.White,
    primaryContainer = PrimaryVariant,
    onPrimaryContainer = Color.White,

    secondary = Secondary,
    onSecondary = Color.White,
    secondaryContainer = SecondaryVariant,
    onSecondaryContainer = Color.White,

    tertiary = Accent,
    onTertiary = Color.White,
    tertiaryContainer = AccentVariant,
    onTertiaryContainer = Color.White,

    background = BackgroundDark,
    onBackground = TextPrimaryDark,

    surface = SurfaceDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = Color(0xFF3D3D4F),
    onSurfaceVariant = TextSecondaryDark,

    error = Error,
    onError = Color.White,
    errorContainer = Error,
    onErrorContainer = Color.White,

    outline = Color(0xFF4B5563),
    outlineVariant = Color(0xFF374151)
)

/**
 * Neubrutalism Design System Colors
 */
data class TawaznColors(
    // Core neubrutalism colors
    val border: Color,
    val shadow: Color,
    val card: Color,

    // Shadow configuration
    val shadowOffsetX: Dp,
    val shadowOffsetY: Dp,
    val borderWidth: Dp,

    // Accent colors for cards
    val cardYellow: Color,
    val cardGreen: Color,
    val cardOrange: Color,
    val cardCyan: Color,
    val cardPink: Color,
    val cardLavender: Color,

    // Status colors
    val success: Color,
    val warning: Color,
    val error: Color,
    val info: Color,

    // Background
    val background: Color,

    // Chart colors
    val chartColors: List<Color>
)

val LocalTawaznColors = staticCompositionLocalOf {
    TawaznColors(
        border = NeuBorder,
        shadow = NeuBlack,
        card = CardLight,
        shadowOffsetX = 4.dp,
        shadowOffsetY = 4.dp,
        borderWidth = 3.dp,
        cardYellow = NeuYellow,
        cardGreen = NeuGreen,
        cardOrange = NeuOrange,
        cardCyan = NeuCyan,
        cardPink = Accent,
        cardLavender = NeuLavender,
        success = Success,
        warning = Warning,
        error = Error,
        info = Info,
        background = BackgroundLight,
        chartColors = listOf(ChartColor1, ChartColor2, ChartColor3, ChartColor4, ChartColor5)
    )
}

@Composable
fun TawaznTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val tawaznColors = TawaznColors(
        border = if (darkTheme) Color(0xFF4B5563) else NeuBorder,
        shadow = if (darkTheme) Color(0xFF000000) else NeuBlack,
        card = if (darkTheme) CardDark else CardLight,
        shadowOffsetX = 4.dp,
        shadowOffsetY = 4.dp,
        borderWidth = 3.dp,
        cardYellow = NeuYellow,
        cardGreen = NeuGreen,
        cardOrange = NeuOrange,
        cardCyan = NeuCyan,
        cardPink = Accent,
        cardLavender = NeuLavender,
        success = Success,
        warning = Warning,
        error = Error,
        info = Info,
        background = if (darkTheme) BackgroundDark else BackgroundLight,
        chartColors = listOf(ChartColor1, ChartColor2, ChartColor3, ChartColor4, ChartColor5)
    )

    CompositionLocalProvider(LocalTawaznColors provides tawaznColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = TawaznTypography,
            shapes = TawaznShapes,
            content = content
        )
    }
}

object TawaznTheme {
    val colors: TawaznColors
        @Composable
        get() = LocalTawaznColors.current
}
