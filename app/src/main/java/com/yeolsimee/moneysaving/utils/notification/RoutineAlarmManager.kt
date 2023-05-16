package com.yeolsimee.moneysaving.utils.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.ComponentActivity
import com.yeolsimee.moneysaving.App
import com.yeolsimee.moneysaving.data.entity.AlarmEntity
import com.yeolsimee.moneysaving.domain.entity.routine.RoutineResponse
import com.yeolsimee.moneysaving.utils.getIntDayOfWeekFromEnglish
import java.text.SimpleDateFormat
import java.util.Calendar

class RoutineAlarmManager {
    companion object {
        fun setRoutine(
            context: Context,
            dayOfWeeks: Array<String>,
            alarmTime: String,
            routineId: Int,
            routineName: String,
            onAlarmAdded: (alarmId: Int, dayOfWeek: Int) -> Unit = { _, _ -> }
        ) {
            val hour = alarmTime.substring(0, 2).toInt()
            val minute = alarmTime.substring(2, 4).toInt()

            // 요일 별로 알람이 추가되어야 한다.
            for (i in dayOfWeeks.indices) {
                val dayOfWeek = getIntDayOfWeekFromEnglish(dayOfWeeks[i])
                val triggerTime = getTriggerTime(
                    dayOfWeek = dayOfWeek,
                    hour = hour,
                    minute = minute,
                )

                val intent = getRoutineAlarmIntent(context)
                intent.putExtra("routineName", routineName)
                intent.putExtra("alarmTime", alarmTime)

                val alarmId = getAlarmId(routineId, dayOfWeek)
                val pIntent = makeAlarmPendingIntent(context, alarmId, intent)
                val alarmManager = getAlarmManager(context)

                Log.i(
                    App.TAG,
                    "Setting Routine Alarm: ${
                        SimpleDateFormat.getInstance().format(triggerTime.time)
                    }"
                )

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
            alarm: AlarmEntity,
            onAlarmAdded: (alarmId: Int, dayOfWeek: Int) -> Unit = { _, _ -> }
        ) {
            val hour = alarm.alarmTime.substring(0, 2).toInt()
            val minute = alarm.alarmTime.substring(2, 4).toInt()

            val triggerTime = getTriggerTime(
                dayOfWeek = alarm.dayOfWeek,
                hour = hour,
                minute = minute
            )

            val intent = getRoutineAlarmIntent(context)

            intent.putExtra("routineName", alarm.routineName)
            intent.putExtra("alarmTime", alarm.alarmTime)
            val pIntent = makeAlarmPendingIntent(context, alarm.alarmId, intent)
            val alarmManager = getAlarmManager(context)

            Log.i(
                App.TAG,
                "Setting Routine Alarm: ${
                    SimpleDateFormat.getInstance().format(triggerTime.time)
                }"
            )

            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                triggerTime.timeInMillis,
                AlarmManager.INTERVAL_DAY * 7,
                pIntent
            )

            onAlarmAdded(alarm.alarmId, alarm.dayOfWeek)
        }

        fun setDailyNotification(context: Context) {
            val intent = getRoutineAlarmIntent(context)
            intent.putExtra("routineName", "오늘 루틴 체크는 다 완료 하셨나요?")

            val alarmManager = getAlarmManager(context)
            val triggerTime = Calendar.getInstance()
            triggerTime.set(Calendar.HOUR_OF_DAY, 23)
            triggerTime.set(Calendar.MINUTE, 0)
            triggerTime.set(Calendar.SECOND, 0)
            if (triggerTime.timeInMillis < System.currentTimeMillis()) {
                triggerTime.add(Calendar.DAY_OF_YEAR, 1)
            }

            Log.i(App.TAG, "time: ${SimpleDateFormat.getInstance().format(triggerTime.time)}")
            val pIntent = makeAlarmPendingIntent(context, 11111111, intent)
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                triggerTime.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pIntent
            )
        }

        fun setToday(
            context: Context,
            alarmTime: String,
            routineId: Int,
            routineName: String
        ): Boolean {
            val hour = alarmTime.substring(0, 2).toInt()
            val minute = alarmTime.substring(2, 4).toInt()

            // 요일 별로 알람이 추가되어야 한다.

            val triggerTime = Calendar.getInstance()
            triggerTime.set(Calendar.HOUR_OF_DAY, hour)
            triggerTime.set(Calendar.MINUTE, minute)
            if (triggerTime.timeInMillis < System.currentTimeMillis()) {
                return false
            }

            val intent = getRoutineAlarmIntent(context)
            intent.putExtra("routineName", routineName)
            intent.putExtra("alarmTime", alarmTime)
            val pIntent = makeAlarmPendingIntent(context, routineId, intent)
            val alarmManager = getAlarmManager(context)

            Log.i(
                App.TAG,
                "Setting Routine Alarm: ${
                    SimpleDateFormat.getInstance().format(triggerTime.time)
                }"
            )

            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerTime.timeInMillis,
                pIntent
            )
            return true
        }


        fun cancelDailyNotification(context: Context) {
            val intent = getRoutineAlarmIntent(context)
            val pIntent = makeAlarmPendingIntent(context, 11111111, intent)
            val alarmManager = getAlarmManager(context)
            alarmManager.cancel(pIntent)
        }

        private fun getTriggerTime(
            dayOfWeek: Int = -1,
            hour: Int,
            minute: Int
        ): Calendar {
            val triggerTime = Calendar.getInstance()
            if (dayOfWeek != -1) triggerTime.set(Calendar.DAY_OF_WEEK, dayOfWeek)
            triggerTime.set(Calendar.HOUR_OF_DAY, hour)
            triggerTime.set(Calendar.MINUTE, minute)
            triggerTime.set(Calendar.SECOND, 0)

            if (triggerTime.timeInMillis < System.currentTimeMillis()) {
                triggerTime.add(Calendar.DAY_OF_YEAR, 7)
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

        fun cancelAll(context: Context, alarmList: List<RoutineResponse>) {
            val alarmManager = getAlarmManager(context)
            for (alarm in alarmList) {
                for (dayOfWeek in alarm.weekTypes) {
                    val alarmId = getAlarmId(alarm.routineId, getIntDayOfWeekFromEnglish(dayOfWeek))
                    alarmManager.cancel(
                        makeAlarmPendingIntent(context, alarmId, getRoutineAlarmIntent(context))
                    )
                }
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

        fun addAll(
            context: Context,
            alarmList: List<RoutineResponse>,
            onAlarmAdded: (alarmId: Int, dayOfWeek: Int, alarmTime: String, routineName: String) -> Unit
        ) {
            for (alarm in alarmList) {
                if (alarm.alarmStatus == "ON") {
                    setRoutine(
                        context = context,
                        dayOfWeeks = alarm.weekTypes,
                        alarmTime = alarm.alarmTime,
                        routineId = alarm.routineId,
                        routineName = alarm.routineName,
                        onAlarmAdded = { alarmId, dayOfWeek ->
                            onAlarmAdded(alarmId, dayOfWeek, alarm.alarmTime, alarm.routineName)
                        }
                    )
                }
            }
        }
    }
}