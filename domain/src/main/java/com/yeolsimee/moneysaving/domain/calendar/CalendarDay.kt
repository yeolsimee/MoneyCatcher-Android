package com.yeolsimee.moneysaving.domain.calendar

import java.util.*

data class CalendarDay(
    val year: Int,
    val month: Int,
    val day: Int,
    val today: Boolean = false,
    var iconState: DateIconState = DateIconState.Empty,
) {

    fun getNextDay(): CalendarDay {
        val calendar = getCalendar()
        calendar.add(Calendar.DAY_OF_MONTH, 1)

        return CalendarDay(
            year = calendar.get(Calendar.YEAR),
            month = calendar.get(Calendar.MONTH) - 1,
            day = calendar.get(Calendar.DAY_OF_MONTH),
            today = calendar.isToday()
        )
    }

    fun getDayOfWeek(): String {
        val calendar = getCalendar()
        return when(calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> "월요일"
            Calendar.TUESDAY -> "화요일"
            Calendar.WEDNESDAY -> "수요일"
            Calendar.THURSDAY -> "목요일"
            Calendar.FRIDAY -> "금요일"
            Calendar.SATURDAY -> "토요일"
            Calendar.SUNDAY -> "일요일"
            else -> ""
        }
    }

    private fun getCalendar(): Calendar {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month + 1)
        calendar.set(Calendar.DAY_OF_MONTH, day)
        return calendar
    }

    fun getWeekOfMonth(): Int {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month + 1)
        calendar.set(Calendar.DAY_OF_MONTH, day + 1)
        return calendar.get(Calendar.WEEK_OF_MONTH)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CalendarDay

        if (year != other.year) return false
        if (month != other.month) return false
        if (day != other.day) return false

        return true
    }

    override fun hashCode(): Int {
        var result = year
        result = 31 * result + month
        result = 31 * result + day
        return result
    }
}