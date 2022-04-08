package com.example.myweatherapp

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.myweatherapp.Constants.CHANNEL_ID
import com.example.myweatherapp.Constants.CITY_NAME
import com.example.myweatherapp.Constants.NOTIF_ID
import com.example.myweatherapp.Constants.NOTIF_TEXT
import com.example.myweatherapp.Constants.NOTIF_TITLE
import com.example.myweatherapp.activities.WeatherActivity

class AlarmReceiver : BroadcastReceiver() {

    @SuppressLint("UnspecifiedImmutableFlag", "InlinedApi")
    override fun onReceive(context: Context, intent: Intent) {
        val anotherIntent = Intent(context, WeatherActivity::class.java).apply {
            putExtra(CITY_NAME, intent.getStringExtra(CITY_NAME))
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context,
            0, anotherIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder(context, CHANNEL_ID)
        } else {
            NotificationCompat.Builder(context)
        }
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(intent.getStringExtra(NOTIF_TITLE))
            .setContentText(intent.getStringExtra(NOTIF_TEXT))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val notifManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notifManager.notify(NOTIF_ID, notification)
    }
}