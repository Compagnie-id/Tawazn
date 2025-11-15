package id.compagnie.tawazn.database

import app.cash.sqldelight.db.SqlDriver

object TawaznDatabaseFactory {
    fun createDatabase(driver: SqlDriver): TawaznDatabase {
        return TawaznDatabase(driver)
    }
}
