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
            "3" -> "아침"
            "4" -> "점심"
            "5" -> "저녁"
            "6" -> "밤"
            "7" -> "취침직전"
            "8" -> "기상직후"
            "9" -> "오전"
            "10" -> "오후"
            else -> routineTimeZone
        }
    }

    fun getAlarmText(): String {
        var hour = alarmTimeHour.toInt()
        val prefix = if (12 < hour) "오후" else "오전"
        hour = if (12 < hour) hour - 12 else hour
        return "$prefix $hour:$alarmTimeMinute"
    }

    fun isEmpty(): Boolean {
        return routineId.isEmpty()
    }
}
