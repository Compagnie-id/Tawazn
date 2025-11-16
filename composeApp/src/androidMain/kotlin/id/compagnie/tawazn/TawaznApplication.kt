package id.compagnie.tawazn

import android.app.Application
import id.compagnie.tawazn.data.di.dataModule
import id.compagnie.tawazn.data.di.platformModule
import id.compagnie.tawazn.domain.di.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class TawaznApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@TawaznApplication)
            modules(
                platformModule(),
                dataModule,
                domainModule
            )
        }
    }
}
