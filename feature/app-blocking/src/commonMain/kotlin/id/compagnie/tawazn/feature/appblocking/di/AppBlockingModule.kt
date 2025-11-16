package id.compagnie.tawazn.feature.appblocking.di

import id.compagnie.tawazn.feature.appblocking.AppBlockingScreenModel
import org.koin.dsl.module

val appBlockingModule = module {
    factory { AppBlockingScreenModel() }
}
