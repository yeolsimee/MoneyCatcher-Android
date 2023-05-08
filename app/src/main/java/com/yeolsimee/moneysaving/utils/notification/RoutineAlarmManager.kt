package com.yeolsimee.moneysaving.utils.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.ComponentActivity
import com.yeolsimee.moneysaving.App
import com.yeolsimee.moneysaving.domain.entity.routine.RoutineResponse
import com.yeolsimee.moneysaving.utils.getIntDayOfWeekFromEnglish
import java.util.Calendar

class RoutineAlarmManager {
    companion object {
        fun setRoutine(
            context: Context,
            dayOfWeeks: Array<String>,
            alarmTime: String,
            routineId: Int,
            onAlarmAdded: (alarmId: Int, dayOfWeek: Int) -> Unit = { _, _ -> }
        ) {
            val hour = alarmTime.substring(0, 2).toInt()
            val minute = alarmTime.substring(2, 4).toInt()

            // 요일 별로 알람이 추가되어야 한다.
            for (i in dayOfWeeks.indices) {
                val dayOfWeek = getIntDayOfWeekFromEnglish(dayOfWeeks[i])
                val triggerTime = setTriggerTime(dayOfWeek, hour, minute)

                val intent = getRoutineAlarmIntent(context)

                // TODO 루틴알림에 필요한 정보 입력하기
                intent.putExtra("time", "${hour}시 ${minute}분")
                val alarmId = getAlarmId(routineId, dayOfWeek)
                val pIntent = makeAlarmPendingIntent(context, alarmId, intent)
                val alarmManager = getAlarmManager(context)

                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime.timeInMillis,
                    AlarmManager.INTERVAL_DAY * 7,
                    pIntent
                )

                onAlarmAdded(alarmId, dayOfWeek)
            }
        }

        fun setOneDay(
            context: Context,
            dayOfWeek: Int,
            alarmTime: String,
            alarmId: Int,
            onAlarmAdded: (alarmId: Int, dayOfWeek: Int) -> Unit = { _, _ -> }
        ) {
            val hour = alarmTime.substring(0, 2).toInt()
            val minute = alarmTime.substring(2, 4).toInt()

            val triggerTime = setTriggerTime(dayOfWeek, hour, minute)

            val intent = getRoutineAlarmIntent(context)

            // TODO 루틴알림에 필요한 정보 입력하기
            intent.putExtra("time", "${hour}시 ${minute}분")

            val pIntent = makeAlarmPendingIntent(context, alarmId, intent)
            val alarmManager = getAlarmManager(context)

            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                triggerTime.timeInMillis,
                AlarmManager.INTERVAL_DAY * 7,
                pIntent
            )

            onAlarmAdded(alarmId, dayOfWeek)
        }

        private fun setTriggerTime(
            dayOfWeek: Int,
            hour: Int,
            minute: Int
        ): Calendar {
            val triggerTime = Calendar.getInstance()
            triggerTime.set(Calendar.DAY_OF_WEEK, dayOfWeek)
            triggerTime.set(Calendar.HOUR_OF_DAY, hour)
            triggerTime.set(Calendar.MINUTE, minute)
            triggerTime.set(Calendar.SECOND, 0)

            if (triggerTime.timeInMillis < System.currentTimeMillis()) {
                triggerTime.add(Calendar.DAY_OF_YEAR, 7)
            } else {
                val mills = System.currentTimeMillis() - triggerTime.timeInMillis
                Log.i(App.TAG, "alarm gap: $mills")
            }
            return triggerTime
        }

        fun delete(context: Context, res: RoutineResponse, onDelete: (Int) -> Unit = {}) {
            val alarmManager = getAlarmManager(context)

            val routineId = res.routineId
            val weekTypes = res.weekTypes
            for (dayOfWeek in weekTypes) {
                val alarmId = getAlarmId(routineId, getIntDayOfWeekFromEnglish(dayOfWeek))
                alarmManager.cancel(
                    makeAlarmPendingIntent(context, alarmId, getRoutineAlarmIntent(context))
                )
                onDelete(alarmId)
            }

        }

        fun deleteAll(context: Context, alarmIdList: List<Int>) {
            val alarmManager = getAlarmManager(context)
            for (alarmId in alarmIdList) {
                alarmManager.cancel(
                    makeAlarmPendingIntent(context, alarmId, getRoutineAlarmIntent(context))
                )
            }
        }

        private fun makeAlarmPendingIntent(
            context: Context,
            alarmId: Int,
            intent: Intent
        ): PendingIntent = PendingIntent.getBroadcast(
            context,
            alarmId,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        private fun getRoutineAlarmIntent(context: Context): Intent {
            val intent = Intent("com.yeolsimee.moneysaving.ROUTINE_ALARM")
            intent.setPackage(context.packageName)
            return intent
        }

        private fun getAlarmManager(context: Context) =
            context.getSystemService(ComponentActivity.ALARM_SERVICE) as AlarmManager

        private fun getAlarmId(routineId: Int, dayOfWeek: Int): Int {
            return routineId * 100 + dayOfWeek
        }
    }
}