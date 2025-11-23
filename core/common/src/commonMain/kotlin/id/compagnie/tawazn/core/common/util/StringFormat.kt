package id.compagnie.tawazn.core.common.util

/**
 * Multiplatform string formatting utility.
 * Uses platform-specific formatting implementations.
 */
expect fun String.formatString(vararg args: Any): String

/**
 * Format a decimal number with specified decimal places.
 * @param value The number to format
 * @param decimals Number of decimal places (default: 1)
 */
fun formatDecimal(value: Double, decimals: Int = 1): String {
    return "%.${decimals}f".formatString(value)
}
