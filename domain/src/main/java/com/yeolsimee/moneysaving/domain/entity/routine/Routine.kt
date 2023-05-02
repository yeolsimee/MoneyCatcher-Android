package com.yeolsimee.moneysaving.domain.entity.routine

import java.io.Serializable

data class Routine(
    val routineId: String = "",
    val routineName: String = "",
    val routineCheckYN: String = "",
    val routineTimeZone: String = "",
    val alarmTimeHour: String = "",
    val alarmTimeMinute: String = ""
): Serializable {
    fun getTimeZoneText(): String {
        return when (routineTimeZone) {
            "1" -> "하루종일"
            "2" -> "아무때나"
            "3" -> "기상직후"
            "4" -> "아침"
            "5" -> "오전"
            "6" -> "점심"
            "7" -> "오후"
            "8" -> "저녁"
            "9" -> "밤"
            "10" -> "취침직전"
            else -> routineTimeZone
        }
    }

    fun getAlarmText(): String {
        var hour = alarmTimeHour.toInt()
        val prefix = if (12 < hour) "오후" else "오전"
        hour = if (12 < hour) hour - 12 else hour
        return "$prefix $hour:$alarmTimeMinute"
    }
}
