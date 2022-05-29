package com.example.apparchsample.services

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.apparchsample.MainActivity
import com.example.apparchsample.R


class ScreenTimeService : Service() {

    private var handler: Handler? = null
    private var runnable: Runnable? = null
    private val channelId = "timer_channel"
    private var counter = 0
    private var foregroundId = 101
    private var threeMinute = 300

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun updateNotification(): Notification {
        counter++
        val info = "$counter seconds"
        val context: Context = applicationContext
        val action = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getActivity(
                context,
                0, Intent(context, MainActivity::class.java),
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        } else {
            PendingIntent.getActivity(
                context,
                0, Intent(context, MainActivity::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        val builder = NotificationCompat.Builder(this, getChannelId())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }
        return builder.setContentIntent(action)
            .setContentTitle("Your Screen Time")
            .setContentText(info)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(action)
            .setAutoCancel(true)
            .setOngoing(false).build()

    }

    private fun getChannelId(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
            channelId
        } else {
            channelId
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val important = NotificationManager.IMPORTANCE_LOW
        val channel = NotificationChannel(channelId, "Hands-Up", important)
        channel.description = "TimerChannel"
        channel.lightColor = Color.BLUE
        channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(channel)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action?.contains("start") == true) {
            handler = Handler()
            runnable = Runnable {
                if (counter < threeMinute) {
                    startForeground(foregroundId, updateNotification())
                    handler?.postDelayed(runnable!!, 1000)
                }
            }
            handler?.post(runnable!!)
        } else {
            handler?.removeCallbacks(runnable!!)
            stopForeground(true)
            stopSelf()
        }
        return START_STICKY
    }
}