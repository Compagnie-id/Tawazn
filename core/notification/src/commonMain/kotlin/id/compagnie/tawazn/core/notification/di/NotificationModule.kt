package id.compagnie.tawazn.core.notification.di

import id.compagnie.tawazn.core.notification.NotificationManager
import org.koin.dsl.module

/**
 * Koin dependency injection module for notification functionality.
 */
val notificationModule = module {
    single { NotificationManager() }
}
