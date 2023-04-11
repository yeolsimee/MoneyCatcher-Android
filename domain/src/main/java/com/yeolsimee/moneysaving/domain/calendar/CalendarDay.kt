package com.yeolsimee.moneysaving.domain.calendar

import java.util.*

data class CalendarDay(
    val year: Int,
    val month: Int,
    val day: Int,
    val today: Boolean,
    var iconState: DateIconState = DateIconState.Empty,
) {
    fun isSame(calendar: Calendar): Boolean {
        return year == calendar.get(Calendar.YEAR) &&
                month == calendar.get(Calendar.MONTH) + 1 &&
                day == calendar.get(Calendar.DAY_OF_MONTH)
    }

    fun getNextDay(): CalendarDay {
        val calendar = GregorianCalendar()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month + 1)
        calendar.set(Calendar.DAY_OF_MONTH, day)
        calendar.add(Calendar.DAY_OF_MONTH, 1)

        return CalendarDay(
            year = calendar.get(Calendar.YEAR),
            month = calendar.get(Calendar.MONTH) - 1,
            day = calendar.get(Calendar.DAY_OF_MONTH),
            today = calendar.isToday()
        )
    }

    fun getAfterTwoDay(): CalendarDay {
        val calendar = GregorianCalendar()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month + 1)
        calendar.set(Calendar.DAY_OF_MONTH, day)
        calendar.add(Calendar.DAY_OF_MONTH, 2)

        return CalendarDay(
            year = calendar.get(Calendar.YEAR),
            month = calendar.get(Calendar.MONTH) - 1,
            day = calendar.get(Calendar.DAY_OF_MONTH),
            today = calendar.isToday()
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CalendarDay

        if (year != other.year) return false
        if (month != other.month) return false
        if (day != other.day) return false
        if (today != other.today) return false

        return true
    }

    override fun hashCode(): Int {
        var result = year
        result = 31 * result + month
        result = 31 * result + day
        result = 31 * result + today.hashCode()
        return result
    }


}