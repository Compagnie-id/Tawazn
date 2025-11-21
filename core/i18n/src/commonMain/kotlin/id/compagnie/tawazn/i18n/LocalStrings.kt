package id.compagnie.tawazn.i18n

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import org.koin.compose.koinInject

/**
 * CompositionLocal for accessing StringProvider in Compose UI
 */
val LocalStringProvider = compositionLocalOf<StringProvider> {
    error("No StringProvider provided")
}

/**
 * Provides StringProvider to the composition tree
 */
@Composable
fun ProvideStrings(
    stringProvider: StringProvider = koinInject(),
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalStringProvider provides stringProvider,
        content = content
    )
}

/**
 * Helper composable to get localized strings
 */
@Composable
fun rememberStrings(): StringProvider {
    return LocalStringProvider.current
}

/**
 * Extension function for easy string access
 */
@Composable
fun stringResource(key: String): String {
    val stringProvider = LocalStringProvider.current
    val language by stringProvider.currentLanguage.collectAsState()
    // Re-compose when language changes
    return remember(key, language) {
        stringProvider.getString(key)
    }
}

/**
 * Extension function for formatted strings
 */
@Composable
fun stringResource(key: String, vararg args: Any): String {
    val stringProvider = LocalStringProvider.current
    val language by stringProvider.currentLanguage.collectAsState()
    return remember(key, language, *args) {
        stringProvider.getString(key, *args)
    }
}
