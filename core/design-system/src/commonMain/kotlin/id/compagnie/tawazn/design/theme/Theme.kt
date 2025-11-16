package id.compagnie.tawazn.design.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = Color.White,
    primaryContainer = Primary.copy(alpha = 0.1f),
    onPrimaryContainer = PrimaryVariant,

    secondary = Secondary,
    onSecondary = Color.White,
    secondaryContainer = Secondary.copy(alpha = 0.1f),
    onSecondaryContainer = SecondaryVariant,

    tertiary = Accent,
    onTertiary = Color.White,
    tertiaryContainer = Accent.copy(alpha = 0.1f),
    onTertiaryContainer = AccentVariant,

    background = BackgroundLight,
    onBackground = TextPrimaryLight,

    surface = SurfaceLight,
    onSurface = TextPrimaryLight,
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = TextSecondaryLight,

    error = Error,
    onError = Color.White,
    errorContainer = Error.copy(alpha = 0.1f),
    onErrorContainer = Error,

    outline = Color(0xFFCBD5E1),
    outlineVariant = Color(0xFFE2E8F0)
)

private val DarkColorScheme = darkColorScheme(
    primary = Primary,
    onPrimary = Color.White,
    primaryContainer = Primary.copy(alpha = 0.2f),
    onPrimaryContainer = Primary,

    secondary = Secondary,
    onSecondary = Color.White,
    secondaryContainer = Secondary.copy(alpha = 0.2f),
    onSecondaryContainer = Secondary,

    tertiary = Accent,
    onTertiary = Color.White,
    tertiaryContainer = Accent.copy(alpha = 0.2f),
    onTertiaryContainer = Accent,

    background = BackgroundDark,
    onBackground = TextPrimaryDark,

    surface = SurfaceDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = Color(0xFF334155),
    onSurfaceVariant = TextSecondaryDark,

    error = Error,
    onError = Color.White,
    errorContainer = Error.copy(alpha = 0.2f),
    onErrorContainer = Error,

    outline = Color(0xFF475569),
    outlineVariant = Color(0xFF334155)
)

data class TawaznColors(
    val glass: Color,
    val glassBorder: Color,
    val gradientStart: Color,
    val gradientMiddle: Color,
    val gradientEnd: Color,
    val success: Color,
    val warning: Color,
    val error: Color,
    val info: Color,
    val background: Color,
    val chartColors: List<Color>
)

val LocalTawaznColors = staticCompositionLocalOf {
    TawaznColors(
        glass = GlassLight,
        glassBorder = GlassBorderLight,
        gradientStart = GradientStart,
        gradientMiddle = GradientMiddle,
        gradientEnd = GradientEnd,
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
        glass = if (darkTheme) GlassDark else GlassLight,
        glassBorder = if (darkTheme) GlassBorderDark else GlassBorderLight,
        gradientStart = GradientStart,
        gradientMiddle = GradientMiddle,
        gradientEnd = GradientEnd,
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
