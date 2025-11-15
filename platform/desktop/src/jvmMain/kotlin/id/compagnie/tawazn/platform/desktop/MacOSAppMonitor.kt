package id.compagnie.tawazn.platform.desktop

import co.touchlab.kermit.Logger
import id.compagnie.tawazn.domain.model.AppCategory
import id.compagnie.tawazn.domain.model.AppInfo
import id.compagnie.tawazn.domain.model.AppUsage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import kotlin.time.Duration.Companion.seconds

/**
 * macOS-specific app monitoring
 *
 * Features:
 * - List installed applications from /Applications
 * - Monitor running processes
 * - Track active application using AppleScript
 * - Integration with macOS Screen Time API (requires entitlement)
 *
 * Requirements:
 * - macOS 10.15 (Catalina) or later
 * - For Screen Time API: macOS 12.0 (Monterey) or later + entitlement
 */
class MacOSAppMonitor {

    private val logger = Logger.withTag("MacOSAppMonitor")

    /**
     * Get list of installed applications from /Applications and ~/Applications
     */
    suspend fun getInstalledApps(): List<AppInfo> = withContext(Dispatchers.IO) {
        logger.i { "Getting installed macOS applications" }

        try {
            val apps = mutableListOf<AppInfo>()

            // Check both system and user Applications directories
            val appDirectories = listOf(
                File("/Applications"),
                File(System.getProperty("user.home"), "Applications")
            )

            for (directory in appDirectories) {
                if (!directory.exists()) continue

                directory.listFiles { file ->
                    file.isDirectory && file.name.endsWith(".app")
                }?.forEach { appFile ->
                    val appName = appFile.name.removeSuffix(".app")
                    val bundleId = getBundleIdentifier(appFile) ?: appName.lowercase()

                    apps.add(
                        AppInfo(
                            packageName = bundleId,
                            appName = appName,
                            category = categorizeApp(appName),
                            iconPath = "${appFile.absolutePath}/Contents/Resources/AppIcon.icns",
                            isSystemApp = isSystemApp(appFile.absolutePath)
                        )
                    )
                }
            }

            logger.i { "Found ${apps.size} installed macOS applications" }
            apps.distinctBy { it.packageName }

        } catch (e: Exception) {
            logger.e(e) { "Failed to get macOS applications" }
            emptyList()
        }
    }

    /**
     * Get bundle identifier from Info.plist
     */
    private suspend fun getBundleIdentifier(appFile: File): String? = withContext(Dispatchers.IO) {
        try {
            val plistFile = File(appFile, "Contents/Info.plist")
            if (!plistFile.exists()) return@withContext null

            val command = arrayOf(
                "/usr/bin/plutil",
                "-extract",
                "CFBundleIdentifier",
                "raw",
                plistFile.absolutePath
            )

            val process = Runtime.getRuntime().exec(command)
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val bundleId = reader.readLine()
            process.waitFor()

            bundleId?.takeIf { it.isNotBlank() }

        } catch (e: Exception) {
            logger.e(e) { "Failed to get bundle identifier for ${appFile.name}" }
            null
        }
    }

    /**
     * Get list of running applications
     */
    suspend fun getRunningApps(): List<String> = withContext(Dispatchers.IO) {
        try {
            val command = arrayOf(
                "/usr/bin/osascript",
                "-e",
                "tell application \"System Events\" to get name of every process whose background only is false"
            )

            val process = Runtime.getRuntime().exec(command)
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val output = reader.readLine()
            process.waitFor()

            output?.split(", ")?.filter { it.isNotBlank() } ?: emptyList()

        } catch (e: Exception) {
            logger.e(e) { "Failed to get running apps" }
            emptyList()
        }
    }

    /**
     * Get the currently active (frontmost) application
     */
    suspend fun getActiveApp(): String? = withContext(Dispatchers.IO) {
        try {
            val command = arrayOf(
                "/usr/bin/osascript",
                "-e",
                "tell application \"System Events\" to get name of first process whose frontmost is true"
            )

            val process = Runtime.getRuntime().exec(command)
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val appName = reader.readLine()
            process.waitFor()

            appName?.takeIf { it.isNotBlank() }

        } catch (e: Exception) {
            logger.e(e) { "Failed to get active app" }
            null
        }
    }

    /**
     * Get active window title
     */
    suspend fun getActiveWindowTitle(): String? = withContext(Dispatchers.IO) {
        try {
            val command = arrayOf(
                "/usr/bin/osascript",
                "-e",
                """
                tell application "System Events"
                    set frontApp to first process whose frontmost is true
                    set appName to name of frontApp
                    try
                        set windowTitle to name of front window of frontApp
                        return appName & " - " & windowTitle
                    on error
                        return appName
                    end try
                end tell
                """.trimIndent()
            )

            val process = Runtime.getRuntime().exec(command)
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val title = reader.readLine()
            process.waitFor()

            title?.takeIf { it.isNotBlank() }

        } catch (e: Exception) {
            logger.e(e) { "Failed to get active window title" }
            null
        }
    }

    /**
     * Quit an application by name
     */
    suspend fun quitApp(appName: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val command = arrayOf(
                "/usr/bin/osascript",
                "-e",
                "tell application \"$appName\" to quit"
            )

            val process = Runtime.getRuntime().exec(command)
            val exitCode = process.waitFor()

            exitCode == 0

        } catch (e: Exception) {
            logger.e(e) { "Failed to quit app: $appName" }
            false
        }
    }

    /**
     * Force quit an application by name
     */
    suspend fun forceQuitApp(appName: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val command = arrayOf(
                "killall",
                "-9",
                appName
            )

            val process = Runtime.getRuntime().exec(command)
            val exitCode = process.waitFor()

            exitCode == 0

        } catch (e: Exception) {
            logger.e(e) { "Failed to force quit app: $appName" }
            false
        }
    }

    /**
     * Check if an app is running
     */
    suspend fun isAppRunning(appName: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val command = arrayOf(
                "pgrep",
                "-f",
                appName
            )

            val process = Runtime.getRuntime().exec(command)
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val hasOutput = reader.readLine() != null
            process.waitFor()

            hasOutput

        } catch (e: Exception) {
            logger.e(e) { "Failed to check if app is running" }
            false
        }
    }

    /**
     * Get app usage data using system_profiler
     * Note: This provides limited data. For full Screen Time data, use Screen Time API
     */
    suspend fun getAppInfo(appName: String): Map<String, String>? = withContext(Dispatchers.IO) {
        try {
            val command = arrayOf(
                "/usr/sbin/system_profiler",
                "SPApplicationsDataType",
                "-json"
            )

            val process = Runtime.getRuntime().exec(command)
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val output = reader.readText()
            process.waitFor()

            // TODO: Parse JSON output for specific app
            // For now, return null
            null

        } catch (e: Exception) {
            logger.e(e) { "Failed to get app info" }
            null
        }
    }

    /**
     * Request Screen Time permission (macOS 12+)
     * Note: This requires proper entitlement and should be handled via Swift bridge
     */
    suspend fun requestScreenTimePermission(): Boolean {
        logger.w { "Screen Time permission requires Swift implementation via IOSAppMonitor" }
        return false
    }

    private fun categorizeApp(appName: String): AppCategory {
        val lowerName = appName.lowercase()
        return when {
            lowerName.contains("safari") || lowerName.contains("chrome") ||
            lowerName.contains("firefox") || lowerName.contains("edge") -> AppCategory.WEB_BROWSER

            lowerName.contains("messages") || lowerName.contains("mail") ||
            lowerName.contains("slack") || lowerName.contains("discord") -> AppCategory.COMMUNICATION

            lowerName.contains("spotify") || lowerName.contains("music") ||
            lowerName.contains("tv") || lowerName.contains("podcasts") -> AppCategory.ENTERTAINMENT

            lowerName.contains("xcode") || lowerName.contains("vscode") ||
            lowerName.contains("terminal") || lowerName.contains("notes") -> AppCategory.PRODUCTIVITY

            lowerName.contains("steam") || lowerName.contains("game") -> AppCategory.GAMING

            else -> AppCategory.OTHER
        }
    }

    private fun isSystemApp(path: String): Boolean {
        return path.startsWith("/System/") ||
                path.contains("CoreServices") ||
                path.lowercase().contains("apple")
    }
}

/**
 * macOS Commands Reference:
 *
 * List installed apps:
 * ls /Applications | grep .app
 *
 * List running apps (AppleScript):
 * osascript -e 'tell application "System Events" to get name of every process'
 *
 * Get active app (AppleScript):
 * osascript -e 'tell application "System Events" to get name of first process whose frontmost is true'
 *
 * Get app info:
 * system_profiler SPApplicationsDataType -json
 *
 * Quit app (AppleScript):
 * osascript -e 'tell application "AppName" to quit'
 *
 * Force quit:
 * killall -9 "AppName"
 *
 * Check if running:
 * pgrep -f "AppName"
 *
 * Get bundle ID:
 * plutil -extract CFBundleIdentifier raw /path/to/App.app/Contents/Info.plist
 *
 * Screen Time (requires Swift bridge):
 * - Use FamilyControls.framework (iOS/macOS 12+)
 * - Requires entitlement: com.apple.developer.family-controls
 * - Must be approved by Apple
 */
