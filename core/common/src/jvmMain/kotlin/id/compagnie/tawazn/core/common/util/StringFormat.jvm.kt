package id.compagnie.tawazn.core.common.util

actual fun String.formatString(vararg args: Any): String {
    return String.format(this, *args)
}
