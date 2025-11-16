package id.compagnie.tawazn.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(
            schema = TawaznDatabase.Schema,
            name = "tawazn.db"
        )
    }
}
