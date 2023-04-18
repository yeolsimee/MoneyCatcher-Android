package com.yeolsimee.moneysaving.utils

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