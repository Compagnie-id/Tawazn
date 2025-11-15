package id.compagnie.tawazn.data.di

import id.compagnie.tawazn.platform.desktop.DesktopAppMonitor
import org.koin.dsl.module

actual fun platformModule() = module {
    single { DesktopAppMonitor() }
}
