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
import com.yeolsimee.moneysaving.ui.AlwaysElevenPMAlarmId
import com.yeolsimee.moneysaving.ui.AlwaysElevenPMAlarmText
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
                    hour = hour,
                    minute = minute,
                )

                val alarmId = getAlarmId(routineId, dayOfWeek)

                val intent = getRoutineAlarmIntent(context)
                intent.putExtra("routineName", routineName)
                intent.putExtra("alarmTime", alarmTime)
                intent.putExtra("alarmId", alarmId)

                val pIntent = makeAlarmPendingIntent(context, alarmId, intent)
                val alarmManager = getAlarmManager(context)

                Log.d(
                    App.TAG,
                    "Setting Routine Alarm: ${
                        SimpleDateFormat.getInstance().format(triggerTime.time)
                    }"
                )

                alarmManager.setAlarmClock(
                    AlarmManager.AlarmClockInfo(
                        triggerTime.timeInMillis,
                        pIntent
                    ), pIntent
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
                hour = hour,
                minute = minute
            )

            val intent = getRoutineAlarmIntent(context)

            intent.putExtra("routineName", alarm.routineName)
            intent.putExtra("alarmTime", alarm.alarmTime)
            intent.putExtra("alarmId", alarm.alarmId)

            val pIntent = makeAlarmPendingIntent(context, alarm.alarmId, intent)
            val alarmManager = getAlarmManager(context)

            Log.d(
                App.TAG,
                "Setting Routine Alarm: ${
                    SimpleDateFormat.getInstance().format(triggerTime.time)
                }"
            )

            alarmManager.setAlarmClock(
                AlarmManager.AlarmClockInfo(
                    triggerTime.timeInMillis,
                    pIntent
                ), pIntent
            )

            onAlarmAdded(alarm.alarmId, alarm.dayOfWeek)
        }

        fun setDailyNotification(context: Context) {
            val intent = getRoutineAlarmIntent(context)
            intent.putExtra("routineName", AlwaysElevenPMAlarmText)
            intent.putExtra("alarmId", AlwaysElevenPMAlarmId)

            val alarmManager = getAlarmManager(context)
            val triggerTime = Calendar.getInstance()
            triggerTime.set(Calendar.HOUR_OF_DAY, 23)
            triggerTime.set(Calendar.MINUTE, 0)
            triggerTime.set(Calendar.SECOND, 0)

            Log.d(App.TAG, "time: ${SimpleDateFormat.getInstance().format(triggerTime.time)}")
            val pIntent = makeAlarmPendingIntent(context, AlwaysElevenPMAlarmId, intent)

            alarmManager.setAlarmClock(
                AlarmManager.AlarmClockInfo(
                    triggerTime.timeInMillis,
                    pIntent
                ), pIntent
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

            val triggerTime = Calendar.getInstance()
            triggerTime.set(Calendar.HOUR_OF_DAY, hour)
            triggerTime.set(Calendar.MINUTE, minute)
            triggerTime.set(Calendar.SECOND, 0)
            if (triggerTime.timeInMillis < System.currentTimeMillis()) {
                return false
            }

            val intent = getRoutineAlarmIntent(context)
            intent.putExtra("routineName", routineName)
            intent.putExtra("alarmTime", alarmTime)

            val pIntent = makeAlarmPendingIntent(context, routineId, intent)
            val alarmManager = getAlarmManager(context)

            Log.d(
                App.TAG,
                "Setting Routine Alarm: ${
                    SimpleDateFormat.getInstance().format(triggerTime.time)
                }"
            )

            alarmManager.setAlarmClock(
                AlarmManager.AlarmClockInfo(
                    triggerTime.timeInMillis,
                    pIntent
                ), pIntent
            )
            return true
        }


        private fun getTriggerTime(
            hour: Int,
            minute: Int
        ): Calendar {
            val triggerTime = Calendar.getInstance()
            triggerTime.set(Calendar.HOUR_OF_DAY, hour)
            triggerTime.set(Calendar.MINUTE, minute)
            triggerTime.set(Calendar.SECOND, 0)

            if (triggerTime.timeInMillis < System.currentTimeMillis()) {
                triggerTime.add(Calendar.DAY_OF_YEAR, 7)
            }
            return triggerTime
        }

        private fun getNextWeekTime(time: Calendar): Calendar {
            time.set(Calendar.SECOND, 0)
            time.add(Calendar.WEEK_OF_YEAR, 1)
            return time
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

        fun delete(context: Context, routineId: Int) {
            val alarmManager = getAlarmManager(context)

            for (dayOfWeek in 1..7) {
                val alarmId = getAlarmId(routineId, dayOfWeek)
                alarmManager.cancel(
                    makeAlarmPendingIntent(context, alarmId, getRoutineAlarmIntent(context))
                )
            }
            // 하루만 설정된 알람이 있을 경우
            alarmManager.cancel(
                makeAlarmPendingIntent(context, routineId, getRoutineAlarmIntent(context))
            )
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

        fun setNextWeek(
            context: Context,
            time: Calendar,
            routineName: String,
            alarmTime: String,
            alarmId: Int
        ) {
            val triggerTime = getNextWeekTime(time)

            val intent = getRoutineAlarmIntent(context)

            intent.putExtra("routineName", routineName)
            intent.putExtra("alarmTime", alarmTime)
            intent.putExtra("alarmId", alarmId)

            val pIntent = makeAlarmPendingIntent(context, alarmId, intent)
            val alarmManager = getAlarmManager(context)

            Log.d(
                App.TAG,
                "Setting Routine Alarm: ${
                    SimpleDateFormat.getInstance().format(triggerTime.time)
                }"
            )

            alarmManager.setAlarmClock(
                AlarmManager.AlarmClockInfo(
                    triggerTime.timeInMillis,
                    pIntent
                ), pIntent
            )
        }
    }
}