package com.yeolsimee.moneysaving.domain.calendar

import java.util.*

data class CalendarDay(
    val year: Int,
    val month: Int,
    val day: Int,
    val today: Boolean
) {
    fun isToday(calendar: Calendar): Boolean {
        return year == calendar.get(Calendar.YEAR) &&
                month == calendar.get(Calendar.MONTH) + 1 &&
                day == calendar.get(Calendar.DAY_OF_MONTH)
    }
}