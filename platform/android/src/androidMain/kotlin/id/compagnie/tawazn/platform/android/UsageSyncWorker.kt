@file:OptIn(ExperimentalTime::class)

package id.compagnie.tawazn.platform.android

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import co.touchlab.kermit.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.todayIn
import java.util.concurrent.TimeUnit
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * Background worker to sync usage data from Android to database
 */
class UsageSyncWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    private val logger = Logger.withTag("UsageSyncWorker")

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            logger.i { "Starting usage sync..." }

            // Get dependencies (in real app, use Koin or manual injection)
            val appMonitor = AndroidAppMonitor(applicationContext)
            // val usageRepository = get<UsageRepository>() // Would inject via Koin

            if (!appMonitor.hasUsageStatsPermission()) {
                logger.w { "Usage stats permission not granted, skipping sync" }
                return@withContext Result.retry()
            }

            // Sync last 7 days
            val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
            val weekAgo = today.minus(7, DateTimeUnit.DAY)

            val usageData = appMonitor.getAppUsageStats(weekAgo, today)

            logger.i { "Synced ${usageData.size} usage records" }

            // In real implementation, save to repository:
            // usageData.forEach { usageRepository.upsertUsage(it) }

            Result.success()
        } catch (e: Exception) {
            logger.e(e) { "Failed to sync usage data" }
            Result.retry()
        }
    }

    companion object {
        private const val WORK_NAME = "usage_sync"

        /**
         * Schedule periodic usage sync (runs daily)
         */
        fun schedule(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .setRequiresBatteryNotLow(true)
                .build()

            val syncRequest = PeriodicWorkRequestBuilder<UsageSyncWorker>(
                1, TimeUnit.DAYS
            )
                .setConstraints(constraints)
                .setBackoffCriteria(
                    BackoffPolicy.EXPONENTIAL,
                    15, TimeUnit.MINUTES
                )
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                syncRequest
            )
        }

        /**
         * Trigger immediate sync
         */
        fun syncNow(context: Context) {
            val syncRequest = OneTimeWorkRequestBuilder<UsageSyncWorker>()
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .build()

            WorkManager.getInstance(context).enqueue(syncRequest)
        }

        /**
         * Cancel all sync workers
         */
        fun cancel(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
        }
    }
}
