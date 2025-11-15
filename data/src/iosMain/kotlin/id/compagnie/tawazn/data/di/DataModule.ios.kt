package id.compagnie.tawazn.data.di

import id.compagnie.tawazn.platform.ios.IOSAppMonitorImpl
import org.koin.dsl.module

actual fun platformModule() = module {
    single { IOSAppMonitorImpl() }
}
