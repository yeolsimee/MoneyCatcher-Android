package com.yeolsimee.moneysaving.view.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.yeolsimee.moneysaving.ui.theme.MoneyCatcherTheme
import java.util.*

class TestActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MoneyCatcherTheme {
                Scaffold {
                    Column(
                        Modifier
                            .padding(it)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(onClick = {
                            setUp2()
                        }) {
                            Text(text = "알림 시작")
                        }
                        Button(onClick = {
                            turnOff()
                        }) {
                            Text(text = "알림 취소")
                        }
                    }
                }
            }
        }
    }

    private fun setUp2() {

        val triggerTime = Calendar.getInstance()
        val hour = 22
        val min = 30
        triggerTime.set(Calendar.HOUR_OF_DAY, hour)
        triggerTime.set(Calendar.MINUTE, min)
        triggerTime.set(Calendar.SECOND, 0)

        val intent = Intent("com.yeolsimee.moneysaving.ALARM_TEST")
        intent.setPackage(packageName)
        intent.putExtra("time", "${hour}시 ${min}분")
        val pIntent =
            PendingIntent.getBroadcast(this@TestActivity, 2, intent, PendingIntent.FLAG_IMMUTABLE)
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            triggerTime.timeInMillis,
            60 * 1000,
            pIntent
        )
    }

    private fun turnOff() {
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent("com.yeolsimee.moneysaving.ALARM_TEST")
        intent.setPackage(packageName)
        val pIntent =
            PendingIntent.getBroadcast(this@TestActivity, 2, intent, PendingIntent.FLAG_IMMUTABLE)
        alarmManager.cancel(pIntent)
    }
}