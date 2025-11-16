package id.compagnie.tawazn.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

/**
 * Android implementation of DataStore factory
 */
actual class DataStoreFactory(private val context: Context) {
    actual fun createDataStore(): DataStore<Preferences> {
        return context.dataStore
    }
}

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = DATA_STORE_FILE_NAME
)
