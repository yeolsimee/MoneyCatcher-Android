package com.yeolsimee.moneysaving.domain.calendar

import android.util.Log
import java.util.Calendar

fun Calendar.isToday(): Boolean {
    val todayCalendar = Calendar.getInstance()
    return todayCalendar.get(Calendar.YEAR) == this.get(Calendar.YEAR)
            && todayCalendar.get(Calendar.MONTH) == this.get(Calendar.MONTH)
            && todayCalendar.get(Calendar.DAY_OF_MONTH) == this.get(Calendar.DAY_OF_MONTH)
}

fun Calendar.isNotPast(): Boolean {
    return Calendar.getInstance() <= this
}

fun isToday(year: Int, month: Int, day: Int): Boolean {
    val todayCalendar = Calendar.getInstance()
    return todayCalendar.get(Calendar.YEAR) == year
            && todayCalendar.get(Calendar.MONTH) == month - 1
            && todayCalendar.get(Calendar.DAY_OF_MONTH) == day
}

fun isTodayFromString(date: String?): Boolean {
    if (date == null) return false
    val year = date.substring(0, 4).toInt()
    val month = date.substring(4, 6).toInt()
    val day = date.substring(6, 8).toInt()

    val todayCalendar = Calendar.getInstance()
    return todayCalendar.get(Calendar.YEAR) == year
            && todayCalendar.get(Calendar.MONTH) == month - 1
            && todayCalendar.get(Calendar.DAY_OF_MONTH) == day
}

fun isNotPastFromString(date: String?): Boolean {
    if (date == null) return false
    val year = date.substring(0, 4).toInt()
    val month = date.substring(4, 6).toInt()
    val day = date.substring(6, 8).toInt()

    val today = Calendar.getInstance()
    val todayYear = today.get(Calendar.YEAR)
    val todayMonth = today.get(Calendar.MONTH) + 1
    val todayDay = today.get(Calendar.DAY_OF_MONTH)

    return (todayYear == year && todayMonth == month && todayDay >= day) ||
            (todayYear == year && todayMonth >= month) ||
            (todayYear >= year)
}

fun Calendar.setNextDay(year: Int, month: Int) {
    this.set(Calendar.YEAR, year)
    this.set(Calendar.MONTH, month)
    this.set(Calendar.DAY_OF_MONTH, 1)
}

fun getWeekDays(calendar: Calendar): MutableList<CalendarDay> {
    val tempDayList = mutableListOf<CalendarDay>()

    val month = calendar.get(Calendar.MONTH) + 1
    val tempCal = calendar.clone() as Calendar

    tempCal.set(Calendar.DAY_OF_MONTH, 1)
    Log.i("CalendarUtil", "${tempCal.time}")
    val amount = 1 - tempCal.get(Calendar.DAY_OF_WEEK)
    if (amount < 0) {
        tempCal.add(Calendar.DAY_OF_MONTH, amount + 1)
    } else if (amount == 0) {
        tempCal.add(Calendar.DAY_OF_MONTH, -6)
    }
    // Before 1 Month
    while (tempCal.get(Calendar.MONTH) + 1 != month) {
        addDate(tempCal, tempDayList)
        tempCal.add(Calendar.DAY_OF_MONTH, 1)
    }
    // This Month
    while (tempCal.get(Calendar.MONTH) + 1 == month) {
        addDate(tempCal, tempDayList)
        tempCal.add(Calendar.DAY_OF_MONTH, 1)
    }
    val dayOfWeek = tempCal.get(Calendar.DAY_OF_WEEK)
    if (dayOfWeek == Calendar.MONDAY) {
        return tempDayList
    } else {
        while (tempCal.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            addDate(tempCal, tempDayList)
            tempCal.add(Calendar.DAY_OF_MONTH, 1)
        }
    }

    return tempDayList
}

private fun addDate(
    calendar: Calendar,
    tempDayList: MutableList<CalendarDay>
) {
    tempDayList.add(
        CalendarDay(
            year = calendar.get(Calendar.YEAR),
            month = calendar.get(Calendar.MONTH) + 1,
            day = calendar.get(Calendar.DAY_OF_MONTH),
            today = calendar.isToday()
        )
    )
}

fun getCalendarFromCalendarDay(calendarDay: CalendarDay): Calendar {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.YEAR, calendarDay.year)
    calendar.set(Calendar.MONTH, calendarDay.month - 1)
    calendar.set(Calendar.DAY_OF_MONTH, calendarDay.day)
    return calendar
}
