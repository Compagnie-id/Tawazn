package id.compagnie.tawazn.database

import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import app.cash.sqldelight.db.SqlDriver

object TawaznDatabaseFactory {
    fun createDatabase(driver: SqlDriver): TawaznDatabase {
        return TawaznDatabase(
            driver = driver,
            AppAdapter = App.Adapter(
                isSystemAppAdapter = IntColumnAdapter
            ),
            BlockSessionAdapter = BlockSession.Adapter(
                isActiveAdapter = IntColumnAdapter,
                repeatDailyAdapter = IntColumnAdapter,
                repeatWeeklyAdapter = IntColumnAdapter
            ),
            BlockedAppAdapter = BlockedApp.Adapter(
                isBlockedAdapter = IntColumnAdapter
            )
        )
    }
}
