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
import kotlin.time.Duration.Companion.seconds

/**
 * Windows-specific app monitoring using PowerShell and WMI
 *
 * Features:
 * - List installed applications from registry
 * - Monitor running processes
 * - Track active window (requires additional implementation)
 *
 * Requirements:
 * - Windows 7 or later
 * - PowerShell 5.0 or later
 */
class WindowsAppMonitor {

    private val logger = Logger.withTag("WindowsAppMonitor")

    /**
     * Get list of installed applications from Windows Registry
     */
    suspend fun getInstalledApps(): List<AppInfo> = withContext(Dispatchers.IO) {
        logger.i { "Getting installed Windows applications" }

        try {
            val apps = mutableListOf<AppInfo>()

            // Query both 64-bit and 32-bit registry locations
            val registryPaths = listOf(
                "HKLM:\\Software\\Microsoft\\Windows\\CurrentVersion\\Uninstall\\*",
                "HKLM:\\Software\\Wow6432Node\\Microsoft\\Windows\\CurrentVersion\\Uninstall\\*"
            )

            for (path in registryPaths) {
                val command = arrayOf(
                    "powershell.exe",
                    "-Command",
                    """
                    Get-ItemProperty '$path' |
                    Where-Object { $_.DisplayName -ne $null } |
                    Select-Object DisplayName, Publisher |
                    ForEach-Object { "$($_.DisplayName)|$($_.Publisher)" }
                    """.trimIndent()
                )

                val process = Runtime.getRuntime().exec(command)
                val reader = BufferedReader(InputStreamReader(process.inputStream))

                reader.useLines { lines ->
                    lines.forEach { line ->
                        val parts = line.split("|")
                        if (parts.isNotEmpty() && parts[0].isNotBlank()) {
                            apps.add(
                                AppInfo(
                                    packageName = parts[0].replace(" ", "").lowercase(),
                                    appName = parts[0],
                                    category = categorizeApp(parts[0]),
                                    iconPath = "",
                                    isSystemApp = isSystemApp(parts[0])
                                )
                            )
                        }
                    }
                }

                process.waitFor()
            }

            logger.i { "Found ${apps.size} installed Windows applications" }
            apps.distinctBy { it.packageName }

        } catch (e: Exception) {
            logger.e(e) { "Failed to get Windows applications" }
            emptyList()
        }
    }

    /**
     * Get list of currently running processes
     */
    suspend fun getRunningProcesses(): List<String> = withContext(Dispatchers.IO) {
        try {
            val command = arrayOf(
                "powershell.exe",
                "-Command",
                "Get-Process | Select-Object -ExpandProperty ProcessName"
            )

            val process = Runtime.getRuntime().exec(command)
            val reader = BufferedReader(InputStreamReader(process.inputStream))

            val processes = reader.useLines { lines ->
                lines.filter { it.isNotBlank() }.toList()
            }

            process.waitFor()
            processes

        } catch (e: Exception) {
            logger.e(e) { "Failed to get running processes" }
            emptyList()
        }
    }

    /**
     * Get the currently active window title
     * Note: This requires a native Windows API call (User32.dll)
     */
    suspend fun getActiveWindow(): String? = withContext(Dispatchers.IO) {
        try {
            val command = arrayOf(
                "powershell.exe",
                "-Command",
                """
                Add-Type @"
                    using System;
                    using System.Runtime.InteropServices;
                    public class Win32 {
                        [DllImport("user32.dll")]
                        public static extern IntPtr GetForegroundWindow();
                        [DllImport("user32.dll")]
                        public static extern int GetWindowText(IntPtr hWnd, System.Text.StringBuilder text, int count);
                    }
"@
                ${'$'}handle = [Win32]::GetForegroundWindow()
                ${'$'}title = New-Object System.Text.StringBuilder 256
                [Win32]::GetWindowText(${'$'}handle, ${'$'}title, 256)
                ${'$'}title.ToString()
                """.trimIndent()
            )

            val process = Runtime.getRuntime().exec(command)
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val title = reader.readLine()
            process.waitFor()

            title?.takeIf { it.isNotBlank() }

        } catch (e: Exception) {
            logger.e(e) { "Failed to get active window" }
            null
        }
    }

    /**
     * Kill a process by name
     */
    suspend fun killProcess(processName: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val command = arrayOf(
                "taskkill",
                "/F",
                "/IM",
                "$processName.exe"
            )

            val process = Runtime.getRuntime().exec(command)
            val exitCode = process.waitFor()

            exitCode == 0

        } catch (e: Exception) {
            logger.e(e) { "Failed to kill process: $processName" }
            false
        }
    }

    /**
     * Check if a process is running
     */
    suspend fun isProcessRunning(processName: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val command = arrayOf(
                "tasklist",
                "/FI",
                "IMAGENAME eq $processName.exe"
            )

            val process = Runtime.getRuntime().exec(command)
            val reader = BufferedReader(InputStreamReader(process.inputStream))

            val isRunning = reader.useLines { lines ->
                lines.any { it.contains(processName, ignoreCase = true) }
            }

            process.waitFor()
            isRunning

        } catch (e: Exception) {
            logger.e(e) { "Failed to check if process is running" }
            false
        }
    }

    private fun categorizeApp(appName: String): AppCategory {
        val lowerName = appName.lowercase()
        return when {
            lowerName.contains("chrome") || lowerName.contains("firefox") ||
            lowerName.contains("edge") || lowerName.contains("browser") -> AppCategory.WEB_BROWSER

            lowerName.contains("facebook") || lowerName.contains("instagram") ||
            lowerName.contains("twitter") || lowerName.contains("tiktok") ||
            lowerName.contains("snapchat") -> AppCategory.SOCIAL_MEDIA

            lowerName.contains("spotify") || lowerName.contains("youtube") ||
            lowerName.contains("netflix") || lowerName.contains("media") -> AppCategory.ENTERTAINMENT

            lowerName.contains("office") || lowerName.contains("word") ||
            lowerName.contains("excel") || lowerName.contains("outlook") -> AppCategory.PRODUCTIVITY

            lowerName.contains("game") || lowerName.contains("steam") -> AppCategory.GAMING

            else -> AppCategory.OTHER
        }
    }

    private fun isSystemApp(appName: String): Boolean {
        val lowerName = appName.lowercase()
        return lowerName.contains("microsoft") ||
                lowerName.contains("windows") ||
                lowerName.contains("system") ||
                lowerName.startsWith("kb")
    }
}

/**
 * Windows PowerShell Commands Reference:
 *
 * List installed apps:
 * Get-ItemProperty HKLM:\Software\Microsoft\Windows\CurrentVersion\Uninstall\* |
 *   Where-Object { $_.DisplayName } |
 *   Select-Object DisplayName, Publisher, InstallDate
 *
 * List running processes:
 * Get-Process | Select-Object Name, CPU, WorkingSet
 *
 * Get active window:
 * (Get-Process | Where-Object {$_.MainWindowTitle -ne ""}).MainWindowTitle
 *
 * Kill process:
 * Stop-Process -Name "processname" -Force
 *
 * Monitor process start:
 * Register-WmiEvent -Query "SELECT * FROM __InstanceCreationEvent WITHIN 1 WHERE TargetInstance ISA 'Win32_Process'"
 */
