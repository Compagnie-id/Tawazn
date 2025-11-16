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
import java.io.InputStreamReader
import kotlin.time.Duration.Companion.milliseconds

/**
 * Desktop (JVM) implementation of app monitoring
 *
 * Platform-specific implementations:
 * - Windows: Uses PowerShell and Windows Management Instrumentation (WMI)
 * - macOS: Uses AppleScript and system commands
 * - Linux: Uses process monitoring and window manager queries
 *
 * Note: Desktop app monitoring is more limited than mobile due to:
 * 1. Privacy restrictions (especially macOS)
 * 2. Variety of window managers (Linux)
 * 3. Different app installation methods
 */
class DesktopAppMonitor {

    private val logger = Logger.withTag("DesktopAppMonitor")
    private val osName = System.getProperty("os.name").lowercase()

    suspend fun getInstalledApps(): List<AppInfo> = withContext(Dispatchers.IO) {
        logger.i { "Getting installed apps for OS: $osName" }

        when {
            osName.contains("windows") -> getWindowsApps()
            osName.contains("mac") -> getMacApps()
            osName.contains("linux") -> getLinuxApps()
            else -> {
                logger.w { "Unsupported OS: $osName" }
                emptyList()
            }
        }
    }

    suspend fun getAppUsageStats(startDate: LocalDate, endDate: LocalDate): List<AppUsage> {
        logger.w { "App usage tracking on desktop requires platform-specific implementation" }
        // Desktop usage tracking would require:
        // - Windows: Event Viewer or third-party tools
        // - macOS: Screen Time API (requires special permissions)
        // - Linux: Custom window manager integration
        return emptyList()
    }

    fun hasPermissions(): Boolean {
        logger.i { "Checking desktop permissions" }
        // Desktop permissions vary by OS
        return true // Most desktop apps don't require special permissions
    }

    suspend fun isAppRunning(appName: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val command = when {
                osName.contains("windows") -> arrayOf("tasklist")
                osName.contains("mac") -> arrayOf("ps", "-A")
                osName.contains("linux") -> arrayOf("ps", "-ef")
                else -> return@withContext false
            }

            val process = Runtime.getRuntime().exec(command)
            val reader = BufferedReader(InputStreamReader(process.inputStream))

            reader.useLines { lines ->
                lines.any { it.contains(appName, ignoreCase = true) }
            }
        } catch (e: Exception) {
            logger.e(e) { "Error checking if app is running" }
            false
        }
    }

    private fun getWindowsApps(): List<AppInfo> {
        logger.i { "Getting Windows apps" }

        try {
            // Use PowerShell to get installed applications
            val command = arrayOf(
                "powershell.exe",
                "-Command",
                """
                Get-ItemProperty HKLM:\Software\Microsoft\Windows\CurrentVersion\Uninstall\*,
                                 HKLM:\Software\Wow6432Node\Microsoft\Windows\CurrentVersion\Uninstall\* |
                Where-Object { $_.DisplayName -ne $null } |
                Select-Object DisplayName, Publisher, InstallDate |
                ConvertTo-Json
                """.trimIndent()
            )

            val process = Runtime.getRuntime().exec(command)
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val output = reader.readText()
            process.waitFor()

            // Parse JSON output and convert to AppInfo
            // For now, return empty list - full JSON parsing would require kotlinx.serialization
            logger.i { "Successfully queried Windows apps" }
            return emptyList()

        } catch (e: Exception) {
            logger.e(e) { "Failed to get Windows apps" }
            return emptyList()
        }
    }

    private fun getMacApps(): List<AppInfo> {
        logger.i { "Getting macOS apps" }

        try {
            // Use system_profiler to get installed applications
            val command = arrayOf(
                "/usr/sbin/system_profiler",
                "SPApplicationsDataType",
                "-json"
            )

            val process = Runtime.getRuntime().exec(command)
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val output = reader.readText()
            process.waitFor()

            // Alternative: List /Applications directory
            val appsDir = java.io.File("/Applications")
            val apps = appsDir.listFiles { file ->
                file.isDirectory && file.name.endsWith(".app")
            }?.map { appFile ->
                AppInfo(
                    packageName = appFile.name.removeSuffix(".app"),
                    appName = appFile.name.removeSuffix(".app"),
                    category = AppCategory.OTHER,
                    iconPath = "",
                    isSystemApp = false
                )
            } ?: emptyList()

            logger.i { "Found ${apps.size} macOS apps" }
            return apps

        } catch (e: Exception) {
            logger.e(e) { "Failed to get macOS apps" }
            return emptyList()
        }
    }

    private fun getLinuxApps(): List<AppInfo> {
        // Linux implementation would use:
        // - dpkg -l (Debian/Ubuntu)
        // - rpm -qa (RedHat/Fedora)
        // - flatpak list
        // - snap list
        // - /usr/share/applications for .desktop files

        logger.i { "Getting Linux apps" }
        return emptyList()
    }
}

/**
 * Documentation for Desktop App Monitoring:
 *
 * WINDOWS:
 * - Use WMI (Windows Management Instrumentation) for app information
 * - PowerShell commands for queries
 * - Event Viewer for usage tracking
 * - Registry for installed apps
 *
 * Commands:
 * - List apps: Get-WmiObject -Class Win32_Product
 * - Running processes: Get-Process
 *
 * MACOS:
 * - Screen Time API (requires user permission)
 * - system_profiler for app information
 * - Process monitoring with 'ps' command
 * - /Applications directory
 *
 * Commands:
 * - List apps: system_profiler SPApplicationsDataType -json
 * - Running apps: ps -A
 *
 * LINUX:
 * - Varies by distribution and package manager
 * - .desktop files in /usr/share/applications
 * - Process monitoring
 * - Window manager integration (X11, Wayland)
 *
 * Commands:
 * - List apps: dpkg -l (Debian), rpm -qa (RedHat)
 * - Running apps: ps -ef
 * - Desktop files: find /usr/share/applications -name "*.desktop"
 */
