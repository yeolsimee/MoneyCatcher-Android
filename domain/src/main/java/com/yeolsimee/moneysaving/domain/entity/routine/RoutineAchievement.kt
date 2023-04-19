package com.yeolsimee.moneysaving.domain.entity.routine

import com.yeolsimee.moneysaving.domain.calendar.DateIconState
import com.yeolsimee.moneysaving.domain.calendar.isToday

data class RoutineAchievement(
    val day: String,
    val routineAchievement: String
) {
    fun convertToDateIconState(selectedMonth: Int): DateIconState {
        val (year, month, day) = getYearMonthDay()
        val isToday = isToday(year, month, day)

        val iconState = if (selectedMonth < month) {
            DateIconState.NextMonth
        } else if (selectedMonth > month) {
            DateIconState.PreviousMonth
        } else {
            if (isToday) {
                DateIconState.Today
            } else {
                DateIconState.getStateFromRoutineAchievement(routineAchievement)
            }
        }
        return iconState
    }

    private fun getYearMonthDay(): Triple<Int, Int, Int> {
        val year = day.substring(0, 4).toInt()
        val month = day.substring(4, 6).toInt()
        val day = day.substring(6, 8).toInt()
        return Triple(year, month, day)
    }
}
