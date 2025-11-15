package id.compagnie.tawazn.data.di

import id.compagnie.tawazn.platform.android.AndroidAppMonitor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual fun platformModule() = module {
    single { AndroidAppMonitor(androidContext()) }
}
