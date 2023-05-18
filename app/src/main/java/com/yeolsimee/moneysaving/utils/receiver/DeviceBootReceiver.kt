package com.yeolsimee.moneysaving.utils.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.yeolsimee.moneysaving.App
import com.yeolsimee.moneysaving.data.db.AlarmDao
import com.yeolsimee.moneysaving.utils.notification.RoutineAlarmManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DeviceBootReceiver : BroadcastReceiver() {

    @Inject
    lateinit var dao: AlarmDao

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null && context != null && intent.action != null) {

            CoroutineScope(Dispatchers.IO).launch {
                // 저장된 알람들 불러와서
                val alarms = dao.getAll()

                // 알람 재설정
                for (alarm in alarms) {
                    Log.i(App.TAG, "DeviceBootReceiver reset: ${alarm.alarmId} ${alarm.alarmTime}")
                    RoutineAlarmManager.setOneDay(
                        context,
                        alarm
                    )
                }
                RoutineAlarmManager.setDailyNotification(context)
            }
        }
    }
}