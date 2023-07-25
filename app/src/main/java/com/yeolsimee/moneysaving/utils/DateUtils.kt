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
    if (this < 0 || 24 < this) throw NumberFormatException("시간 표현 범위 초과")
    val prefix = if (this > 12) "오후" else "오전"
    val hour = if (this > 12) this - 12 else this
    return String.format("$prefix %02d", hour)
}

fun Int.getTwoDigits(): String {
    return String.format("%02d", this)
}

fun getWeekTypes(checkList: List<Boolean>): Array<String> {
    val result = mutableListOf<String>()

    for (dayOfWeek in checkList.indices) {
        if (checkList[dayOfWeek]) {
            val temp = getEnglishDayOfWeekByInt(dayOfWeek)
            result.add(temp)
        }
    }
    return result.toTypedArray()
}

fun getWeekTypeCheckList(weekTypes: Array<String>): MutableList<Boolean> {
    val result = Array(7) { false }

    weekTypes.forEach {
        val index = when (it) {
            "MONDAY"  -> 0
            "TUESDAY"  -> 1
            "WEDNESDAY"  -> 2
            "THURSDAY"  -> 3
            "FRIDAY"  -> 4
            "SATURDAY"  -> 5
            "SUNDAY"  -> 6
            else -> -1
        }
        if (index != -1) {
            result[index] = true
        }
    }
    return result.toMutableList()
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

fun getMonthsPassedSince2023(year: Int, month: Int): Int {
    val startDate = Calendar.getInstance()
    startDate.set(2023, Calendar.JANUARY, 1)

    val currentDate = Calendar.getInstance()
    currentDate.set(Calendar.YEAR, year)
    currentDate.set(Calendar.MONTH, month - 1)

    var monthsPassed = 0
    while (startDate.before(currentDate)) {
        startDate.add(Calendar.MONTH, 1)
        monthsPassed++
    }

    return monthsPassed - 1 // To account for the initial month (January 2023)
}