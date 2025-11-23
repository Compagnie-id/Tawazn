package id.compagnie.tawazn

import android.app.Application
import id.compagnie.tawazn.data.di.dataModule
import id.compagnie.tawazn.data.di.platformModule
import id.compagnie.tawazn.domain.di.domainModule
import id.compagnie.tawazn.feature.analytics.di.analyticsModule
import id.compagnie.tawazn.feature.appblocking.di.appBlockingModule
import id.compagnie.tawazn.feature.dashboard.di.dashboardModule
import id.compagnie.tawazn.feature.onboarding.di.onboardingModule
import id.compagnie.tawazn.feature.settings.di.settingsModule
import id.compagnie.tawazn.feature.usagetracking.di.usageTrackingModule
import id.compagnie.tawazn.i18n.StringProvider
import id.compagnie.tawazn.i18n.StringProviderImpl
import id.compagnie.tawazn.i18n.di.i18nModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
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
                domainModule,
                i18nModule,
                id.compagnie.tawazn.i18n.di.platformLocaleModule,
                onboardingModule,
                dashboardModule,
                appBlockingModule,
                analyticsModule,
                settingsModule,
                usageTrackingModule
            )
        }

        // Initialize StringProvider asynchronously
        // Note: StringProviderImpl loads system locale translations synchronously in init,
        // so the UI will have correct translations immediately. This async call only
        // checks for saved user preferences and switches if needed.
        val stringProvider: StringProvider by inject()
        CoroutineScope(Dispatchers.Default).launch {
            try {
                (stringProvider as? StringProviderImpl)?.initialize()
            } catch (e: Exception) {
                android.util.Log.e("TawaznApp", "Failed to initialize i18n", e)
            }
        }
    }
}
