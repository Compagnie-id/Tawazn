package id.compagnie.tawazn.core.common.util

import platform.Foundation.NSString
import platform.Foundation.stringWithFormat

actual fun String.formatString(vararg args: Any): String {
    return when (args.size) {
        0 -> this
        1 -> NSString.stringWithFormat(this, args[0])
        2 -> NSString.stringWithFormat(this, args[0], args[1])
        3 -> NSString.stringWithFormat(this, args[0], args[1], args[2])
        4 -> NSString.stringWithFormat(this, args[0], args[1], args[2], args[3])
        5 -> NSString.stringWithFormat(this, args[0], args[1], args[2], args[3], args[4])
        else -> {
            // For more than 5 args, fall back to simple replacement
            var result = this
            args.forEachIndexed { index, arg ->
                result = result.replaceFirst("%", arg.toString())
            }
            result
        }
    } as String
}
