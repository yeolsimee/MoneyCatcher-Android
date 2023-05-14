package com.yeolsimee.moneysaving.utils.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.yeolsimee.moneysaving.App
import com.yeolsimee.moneysaving.data.repository.SettingsRepository
import com.yeolsimee.moneysaving.utils.notification.NotificationHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject


@AndroidEntryPoint
class NotificationReceiver : BroadcastReceiver() {

    @Inject
    lateinit var settingsRepository: SettingsRepository

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i(App.TAG, "Received: ${intent?.action}, ${intent?.dataString}")
        val notificationHelper = NotificationHelper(context)

        if (intent != null && context != null && intent.action == "com.yeolsimee.moneysaving.ROUTINE_ALARM") {
            CoroutineScope(Dispatchers.IO).launch {
                settingsRepository.getAlarmState().collect {
                    if (it.alarmState) {
                        val routineName = intent.getStringExtra("routineName") ?: ""
                        val nb = notificationHelper.getChannelNotification("ROUMO", routineName, context)
                        notificationHelper.getManager().notify(createID(), nb.build())
                    }
                }
            }
        }
    }

    private fun createID(): Int {
        val now = Date()
        return SimpleDateFormat("ddHHmmss", Locale.KOREA).format(now).toInt()
    }
}