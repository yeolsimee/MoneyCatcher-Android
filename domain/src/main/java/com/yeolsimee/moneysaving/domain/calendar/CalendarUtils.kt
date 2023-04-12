package com.yeolsimee.moneysaving.domain.calendar

import java.util.*

fun Calendar.isToday(): Boolean {
    val todayCalendar = Calendar.getInstance()
    return todayCalendar.get(Calendar.YEAR) == this.get(Calendar.YEAR)
            && todayCalendar.get(Calendar.MONTH) == this.get(Calendar.MONTH)
            && todayCalendar.get(Calendar.DAY_OF_MONTH) == this.get(Calendar.DAY_OF_MONTH)
}

fun Calendar.setNextDay(year: Int, month: Int) {
    this.set(Calendar.YEAR, year)
    this.set(Calendar.MONTH, month)
    this.set(Calendar.DAY_OF_MONTH, 1)
}

fun Calendar.isPreviousMonth(): Boolean {
    val todayCalendar = Calendar.getInstance()
    return todayCalendar.get(Calendar.MONTH) == this.get(Calendar.MONTH) + 1
}

fun Calendar.isNextMonth(): Boolean {
    val todayCalendar = Calendar.getInstance()
    return todayCalendar.get(Calendar.MONTH) + 1 == this.get(Calendar.MONTH)
}

fun getWeekDays(calendar: Calendar): MutableList<CalendarDay> {
    val tempDayList = mutableListOf<CalendarDay>()

    val month = calendar.get(Calendar.MONTH)
    val tempCal = calendar.clone() as Calendar

    tempCal.set(Calendar.DAY_OF_MONTH, 1)
    val amount = 1 - tempCal.get(Calendar.DAY_OF_WEEK)
    if (amount < 0) {
        tempCal.add(Calendar.DAY_OF_MONTH, amount + 1)
    }
    // Before 1 Month
    while (tempCal.get(Calendar.MONTH) != month) {
        addDate(tempCal, tempDayList)
        tempCal.add(Calendar.DAY_OF_MONTH, 1)
    }
    // This Month
    while (tempCal.get(Calendar.MONTH) == month) {
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
    // TODO Temporary Start
    val random = Random()
    val number = random.nextInt(5)
    var state = DateIconState.Empty

    if (number == 1) {
        state = DateIconState.Gold
    } else if (number == 2) {
        state = DateIconState.Silver
    } else if (number == 3) {
        state = DateIconState.Bronze
    } else if (number == 4) {
        state = DateIconState.Silver
    }
    // TODO Temporary End

    if (calendar.isToday()) {
        state = DateIconState.Today
    }
    if (calendar.isPreviousMonth()) {
        state = DateIconState.PreviousMonth
    }
    if (calendar.isNextMonth()) {
        state = DateIconState.NextMonth
    }

    tempDayList.add(
        CalendarDay(
            year = calendar.get(Calendar.YEAR),
            month = calendar.get(Calendar.MONTH) + 1,
            day = calendar.get(Calendar.DAY_OF_MONTH),
            today = calendar.isToday(),
            iconState = state
        )
    )
}