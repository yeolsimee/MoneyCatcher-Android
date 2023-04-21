package com.yeolsimee.moneysaving.utils.notification

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import androidx.activity.ComponentActivity
import com.yeolsimee.moneysaving.domain.entity.routine.NewRoutine
import com.yeolsimee.moneysaving.utils.getIntDayOfWeekFromEnglish
import java.util.Calendar

class RoutineAlarmManager {
    companion object {
        // 1000(mill) * 60(초) * 60(분) * 24(시간) * 7(일)
        private const val oneWeekMillis: Long = 7 * 24 * 60 * 60 * 1000

        fun set(
            activity: Activity,
            newRoutine: NewRoutine,
            routineId: Int,
        ) {
            val dayOfWeeks = newRoutine.weekTypes
            val hour = newRoutine.alarmTime.substring(0, 2).toInt()
            val minute = newRoutine.alarmTime.substring(2, 4).toInt()

            // 요일 별로 알람이 추가되어야 한다.
            for (i in dayOfWeeks.indices) {
                val dayOfWeek = dayOfWeeks[i]
                val triggerTime = Calendar.getInstance()
                triggerTime.set(Calendar.DAY_OF_WEEK, getIntDayOfWeekFromEnglish(dayOfWeek))
                triggerTime.set(Calendar.HOUR_OF_DAY, hour)
                triggerTime.set(Calendar.MINUTE, minute)
                triggerTime.set(Calendar.SECOND, 0)
                val intent = Intent("com.yeolsimee.moneysaving.ALARM_TEST")
                intent.setPackage(activity.packageName)
                intent.putExtra("time", "${hour}시 ${minute}분")
                val pIntent =
                    PendingIntent.getBroadcast(activity, routineId * 100 + i, intent, PendingIntent.FLAG_IMMUTABLE)
                val alarmManager = activity.getSystemService(ComponentActivity.ALARM_SERVICE) as AlarmManager

                alarmManager.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime.timeInMillis,
                    oneWeekMillis,
                    pIntent
                )
            }
        }
    }
}