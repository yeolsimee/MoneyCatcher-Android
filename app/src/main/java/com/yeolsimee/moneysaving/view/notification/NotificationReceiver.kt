package com.yeolsimee.moneysaving.view.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.yeolsimee.moneysaving.App


class NotificationReceiver : BroadcastReceiver() {

    //수신되는 인텐트 - The Intent being received.
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i(App.TAG, "Received: ${intent?.action}, ${intent?.dataString}")

        if (intent != null) {
            if (intent.action == "com.yeolsimee.moneysaving.ALARM_TEST") {
                Toast.makeText(context, "notification received", Toast.LENGTH_SHORT).show()
            }
        }
    }
}