package com.yeolsimee.moneysaving.domain.calendar

import android.util.Log
import java.util.Calendar
import kotlin.math.abs

data class CalendarDay(
    val year: Int,
    val month: Int,
    val day: Int,
    val today: Boolean = false,
    var iconState: DateIconState = DateIconState.Empty,
) {

    companion object {
        fun getDayList(
            initialDayList: MutableList<CalendarDay>,
            iconStateList: MutableList<DateIconState>
        ): MutableList<CalendarDay> {
            Log.i("CalendarDay", "${iconStateList.size} is ${initialDayList.size}")
            if (iconStateList.isNotEmpty() && iconStateList.size == initialDayList.size) {
                for (i in iconStateList.indices) {
                    initialDayList[i].iconState = iconStateList[i]
                }
            }
            return initialDayList
        }
    }

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
        calendar.set(Calendar.MONTH, month - 1)
        calendar.set(Calendar.DAY_OF_MONTH, day)
        return calendar
    }

    fun isSameWeek(selectedDay: CalendarDay): Boolean {
        val thisCalendar = getCalendar()
        val selected = getCalendarFromCalendarDay(selectedDay)

        val diff = abs(thisCalendar.time.time - selected.time.time)
        val days = diff / (1000 * 60 * 60 * 24)
        val isNear = days in 0 .. 6
        if (!isNear) return false
        val selectedDayOfWeek = selected.get(Calendar.DAY_OF_WEEK)
        val thisDayOfWeek = thisCalendar.get(Calendar.DAY_OF_WEEK)

        if (thisCalendar > selected) {
            if (thisDayOfWeek > 1) {
                return selectedDayOfWeek in 2 until thisDayOfWeek
            }
            return true // 나중인 날짜가 일요일이면 무조건 같은 주
        } else if (thisCalendar == selected) {
            return true
        } else {
            if (selectedDayOfWeek > 1) {
                return thisDayOfWeek in 2 until  selectedDayOfWeek
            }
            return true
        }
    }

    fun isToday(): Boolean {
        return getCalendar().isToday()
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

    override fun toString(): String {
        val y = year.toString().padStart(4, '0')
        val m = month.toString().padStart(2, '0')
        val d = day.toString().padStart(2, '0')
        return y + m + d
    }
}