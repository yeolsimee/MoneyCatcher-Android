package com.yeolsimee.moneysaving.domain.calendar

import android.util.Log
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

fun getWeekDays(calendar: Calendar): MutableList<CalendarDay> {
    val tempDayList = mutableListOf<CalendarDay>()

    calendar.set(Calendar.DAY_OF_MONTH, 1)
    val month = calendar.get(Calendar.MONTH) + 1
    for (i in 0..5) {
        for (j in 0..6) {
            addDate(j, calendar, tempDayList)
        }

        val lastDay = tempDayList.last().getAfterTwoDay()
        Log.i("check", "${lastDay.month}월 ${lastDay.day}일")
        if (lastDay.month != month) {
            break
        }
        calendar.add(Calendar.WEEK_OF_MONTH, 1)
    }

    return tempDayList
}

private fun addDate(
    dayOfWeek: Int,
    calendar: Calendar,
    tempDayList: MutableList<CalendarDay>
) {
    val amount = (1 - calendar.get(Calendar.DAY_OF_WEEK)) + dayOfWeek
    calendar.add(Calendar.DAY_OF_MONTH, amount)

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
    val today = calendar.isToday()
    if (today) {
        state = DateIconState.Today
    }
    tempDayList.add(
        CalendarDay(
            year = calendar.get(Calendar.YEAR),
            month = calendar.get(Calendar.MONTH) + 1,
            day = calendar.get(Calendar.DAY_OF_MONTH),
            today = today,
            iconState = state
        )
    )
}