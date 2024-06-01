package edu.uoc.rng2

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import edu.uoc.rng2.Notifications.CHANNEL_GAME_RESULT
import edu.uoc.rng2.db.AppDatabase
import javax.inject.Inject


@HiltAndroidApp
class RngApplication : Application(), Configuration.Provider {
    @Inject
    lateinit var notificationManager: NotificationManager

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()

        createChannels()
    }

    private fun createChannels() {
        val channel = NotificationChannel(
            CHANNEL_GAME_RESULT,
            getString(R.string.game_result),
            NotificationManager.IMPORTANCE_DEFAULT,
        )

        notificationManager.createNotificationChannel(channel)
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .setWorkerFactory(workerFactory)
            .build()
}