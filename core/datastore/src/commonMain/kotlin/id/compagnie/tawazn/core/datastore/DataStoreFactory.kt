package id.compagnie.tawazn.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

/**
 * Factory for creating DataStore instance
 * Platform-specific implementations provide the actual DataStore creation
 */
expect class DataStoreFactory {
    fun createDataStore(): DataStore<Preferences>
}

/**
 * DataStore file name
 */
internal const val DATA_STORE_FILE_NAME = "tawazn_preferences.preferences_pb"
