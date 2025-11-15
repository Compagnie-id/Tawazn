package id.compagnie.tawazn.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import java.io.File

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        val databasePath = File(System.getProperty("java.io.tmpdir"), "tawazn.db")
        val driver = JdbcSqliteDriver("jdbc:sqlite:${databasePath.absolutePath}")
        TawaznDatabase.Schema.create(driver)
        return driver
    }
}
