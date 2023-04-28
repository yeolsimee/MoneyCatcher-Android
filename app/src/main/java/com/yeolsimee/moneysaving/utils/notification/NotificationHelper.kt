package com.yeolsimee.moneysaving.utils.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.yeolsimee.moneysaving.R

class NotificationHelper(context: Context?): ContextWrapper(context)  {

    private val channelId: String = "channelId"
    private val channelName: String = "channelName"

    init {
        createChannel()
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            channel.enableLights(true)
            channel.enableVibration(true)
            channel.lightColor = Color.RED
            channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE

            getManager().createNotificationChannel(channel)
        }
    }

    fun getManager(): NotificationManager {
        return getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }

    fun getChannelNotification(title: String, message: String): NotificationCompat.Builder {
        return NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_launcher_background)

    }

}