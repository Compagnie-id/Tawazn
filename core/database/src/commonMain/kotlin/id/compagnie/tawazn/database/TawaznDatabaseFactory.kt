package id.compagnie.tawazn.database

import app.cash.sqldelight.ColumnAdapter
import app.cash.sqldelight.db.SqlDriver

object TawaznDatabaseFactory {
    private val booleanAdapter = object : ColumnAdapter<Boolean, Long> {
        override fun decode(databaseValue: Long): Boolean = databaseValue == 1L
        override fun encode(value: Boolean): Long = if (value) 1L else 0L
    }

    fun createDatabase(driver: SqlDriver): TawaznDatabase {
        return TawaznDatabase(
            driver = driver,
            AppAdapter = App.Adapter(
                isSystemAppAdapter = booleanAdapter
            ),
            BlockSessionAdapter = BlockSession.Adapter(
                isActiveAdapter = booleanAdapter,
                repeatDailyAdapter = booleanAdapter,
                repeatWeeklyAdapter = booleanAdapter
            ),
            BlockedAppAdapter = BlockedApp.Adapter(
                isBlockedAdapter = booleanAdapter
            )
        )
    }
}
