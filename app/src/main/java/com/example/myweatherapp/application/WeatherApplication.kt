package com.example.myweatherapp.application

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.icu.util.Calendar
import android.os.Build
import android.util.Log
import androidx.work.Configuration
import com.example.myweatherapp.AlarmReceiver
import com.example.myweatherapp.Constants
import com.example.myweatherapp.Constants.CITY_NAME
import com.example.myweatherapp.R
import com.example.myweatherapp.models.LocationModel
import com.example.myweatherapp.repositories.WeatherModelRepository
import com.example.myweatherapp.workmanager.MyWorkerFactory
import com.squareup.moshi.JsonAdapter
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import javax.inject.Named

@HiltAndroidApp
class WeatherApplication : Application(), Configuration.Provider {

    @Inject
    @Named("weather_repo")
    lateinit var weatherRepo: WeatherModelRepository

    @Inject
    @Named(Constants.SHARED_PREFS)
    lateinit var sharedPrefs: SharedPreferences

    @Inject
    @Named("json_adapter")
    lateinit var jsonAdapter: JsonAdapter<LocationModel>
    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        createNotificationChannel()
        scheduleNotification()
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.notif_channel_name)
            val desc = getString(R.string.notif_channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(Constants.CHANNEL_ID, name, importance)
            channel.description = desc
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    @SuppressLint("InlinedApi")
    private fun scheduleNotification() {

        val serializedLocation = sharedPrefs.getString(CITY_NAME, "")
        if (!serializedLocation.isNullOrBlank()) {
            val location = jsonAdapter.fromJson(
                serializedLocation
            )
            val title = "${getString(R.string.notification_title)} ${location?.locationName}"
            val message = getString(R.string.notification_text)
            Log.d("notif_service", "scheduleNotification: started ")
            val intent = Intent(this, AlarmReceiver::class.java).apply {
                putExtra(CITY_NAME, serializedLocation)
                putExtra(Constants.NOTIF_TITLE, title)
                putExtra(Constants.NOTIF_TEXT, message)
            }
            val pendingIntent = PendingIntent.getBroadcast(
                this,
                Constants.NOTIF_ID,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val time = getTime()
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                time,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        }
    }

    @SuppressLint("NewApi")
    private fun getTime(): Long {
        val timeNow = System.currentTimeMillis()
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 8)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        if (timeNow > calendar.timeInMillis)
            calendar.add(Calendar.DATE, 1)
        return calendar.timeInMillis
    }


    companion object {
        private lateinit var INSTANCE: Application
        fun getInstance() = INSTANCE
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setWorkerFactory(MyWorkerFactory(weatherRepo))
            .build()
    }
}