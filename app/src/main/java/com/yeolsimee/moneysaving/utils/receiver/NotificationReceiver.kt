package com.yeolsimee.moneysaving.utils.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.yeolsimee.moneysaving.App
import com.yeolsimee.moneysaving.utils.notification.NotificationHelper
import java.text.SimpleDateFormat
import java.util.*


class NotificationReceiver : BroadcastReceiver() {

    //수신되는 인텐트 - The Intent being received.

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i(App.TAG, "Received: ${intent?.action}, ${intent?.dataString}")
        val notificationHelper = NotificationHelper(context)

        if (intent != null && context != null) {
            if (intent.action == "com.yeolsimee.moneysaving.ROUTINE_ALARM") {
                // TODO 앱 알림 여부 확인해서 ON일 때만 동작하도록 구현하기

                val time = intent.getStringExtra("time") ?: ""

                val nb = notificationHelper.getChannelNotification("일림 제목 테스트", time)
                notificationHelper.getManager().notify(createID(), nb.build())
            }
        }
    }

    private fun createID(): Int {
        val now = Date()
        return SimpleDateFormat("ddHHmmss", Locale.KOREA).format(now).toInt()
    }
}