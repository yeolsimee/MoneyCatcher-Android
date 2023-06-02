package com.yeolsimee.moneysaving.utils.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.yeolsimee.moneysaving.App
import com.yeolsimee.moneysaving.data.db.AlarmDao
import com.yeolsimee.moneysaving.data.repository.SettingsRepository
import com.yeolsimee.moneysaving.ui.AlwaysElevenPMAlarmId
import com.yeolsimee.moneysaving.utils.notification.NotificationHelper
import com.yeolsimee.moneysaving.utils.notification.RoutineAlarmManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject


@AndroidEntryPoint
class NotificationReceiver : BroadcastReceiver() {

    @Inject
    lateinit var settingsRepository: SettingsRepository

    @Inject
    lateinit var dao: AlarmDao

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i(App.TAG, "Received: ${intent?.action}, ${intent?.dataString}")
        val notificationHelper = NotificationHelper(context)

        val current = Calendar.getInstance()

        if (intent != null && context != null && intent.action == "com.yeolsimee.moneysaving.ROUTINE_ALARM") {
            CoroutineScope(Dispatchers.IO).launch {
                val alarmTime = intent.getStringExtra("alarmTime") ?: ""
                val alarmId = intent.getIntExtra("alarmId", 0)
                val routineName = intent.getStringExtra("routineName") ?: ""

                if (alarmId == AlwaysElevenPMAlarmId) {
                    RoutineAlarmManager.setNextWeek(
                        context,
                        current,
                        routineName,
                        alarmTime,
                        alarmId
                    )
                } else {
                    dao.get(alarmId).let {
                        if (it.isNotEmpty()) {
                            // 일주일 후에 다시 울리도록 설정
                            val alarm = it.last()
                            RoutineAlarmManager.setNextWeek(
                                context,
                                current,
                                alarm.routineName,
                                alarm.alarmTime,
                                alarm.alarmId
                            )
                        }
                    }
                }

                // 설정에서 알람이 꺼져있으면 알림을 보이지 않음
                settingsRepository.getAlarmState {
                    if (!it) return@getAlarmState

                    // 체크하지 않은 루틴일 때만 알림을 보여줌
                    settingsRepository.getUnCheckedRoutine(alarmTime) { unchecked ->
                        if (unchecked) {
                            Log.i(App.TAG, "alarm not checked: $alarmTime")

                            val nb = notificationHelper.getChannelNotification(
                                "ROUMO",
                                routineName,
                                context
                            )
                            notificationHelper.getManager().notify(createID(), nb.build())
                        }
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