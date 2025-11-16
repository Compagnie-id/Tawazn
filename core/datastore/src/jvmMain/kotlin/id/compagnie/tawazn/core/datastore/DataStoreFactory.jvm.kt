package id.compagnie.tawazn.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import java.io.File

/**
 * JVM/Desktop implementation of DataStore factory
 */
actual class DataStoreFactory {
    actual fun createDataStore(): DataStore<Preferences> {
        return createDataStore(
            producePath = {
                val appDataDir = getAppDataDirectory()
                "$appDataDir/$DATA_STORE_FILE_NAME"
            }
        )
    }

    private fun getAppDataDirectory(): String {
        val os = System.getProperty("os.name").lowercase()
        val userHome = System.getProperty("user.home")

        val appDataPath = when {
            os.contains("win") -> {
                // Windows: %APPDATA%\Tawazn
                val appData = System.getenv("APPDATA") ?: "$userHome\\AppData\\Roaming"
                "$appData\\Tawazn"
            }
            os.contains("mac") -> {
                // macOS: ~/Library/Application Support/Tawazn
                "$userHome/Library/Application Support/Tawazn"
            }
            else -> {
                // Linux: ~/.config/tawazn
                "$userHome/.config/tawazn"
            }
        }

        // Create directory if it doesn't exist
        File(appDataPath).mkdirs()

        return appDataPath
    }
}

private fun createDataStore(producePath: () -> String): DataStore<Preferences> =
    androidx.datastore.preferences.core.PreferenceDataStoreFactory.createWithPath(
        produceFile = { File(producePath()) }
    )
