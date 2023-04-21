package com.yeolsimee.moneysaving.utils

import java.util.Calendar

fun getTextFromDayOfWeek(dayOfWeek: Int): String {
    when (dayOfWeek) {
        0 -> {
            return "월"
        }

        1 -> {
            return "화"
        }

        2 -> {
            return "수"
        }

        3 -> {
            return "목"
        }

        4 -> {
            return "금"
        }

        5 -> {
            return "토"
        }

        6 -> {
            return "일"
        }

        else -> return ""
    }
}

fun Int.getTwoDigitsHour(): String {
    val prefix = if (this > 12) "오후" else "오전"
    val hour = if (this > 12) this - 12 else this
    return String.format("$prefix %02d", hour)
}

fun Int.getTwoDigitsMinute(): String {
    return String.format("%02d", this)
}

fun getWeekTypes(checkList: List<Boolean>): List<String> {
    val result = mutableListOf<String>()

    for (dayOfWeek in checkList.indices) {
        if (checkList[dayOfWeek]) {
            val temp = getEnglishDayOfWeekByInt(dayOfWeek)
            result.add(temp)
        }
    }
    return result
}

private fun getEnglishDayOfWeekByInt(dayOfWeek: Int) = when (dayOfWeek) {
    0 -> "MONDAY"
    1 -> "TUESDAY"
    2 -> "WEDNESDAY"
    3 -> "THURSDAY"
    4 -> "FRIDAY"
    5 -> "SATURDAY"
    6 -> "SUNDAY"
    else -> ""
}

fun getIntDayOfWeekFromEnglish(dayOfWeek: String) = when (dayOfWeek) {
    "MONDAY" -> Calendar.MONDAY
    "TUESDAY" -> Calendar.TUESDAY
    "WEDNESDAY" -> Calendar.WEDNESDAY
    "THURSDAY" -> Calendar.THURSDAY
    "FRIDAY" -> Calendar.FRIDAY
    "SATURDAY" -> Calendar.SATURDAY
    "SUNDAY" -> Calendar.SUNDAY
    else -> -1
}