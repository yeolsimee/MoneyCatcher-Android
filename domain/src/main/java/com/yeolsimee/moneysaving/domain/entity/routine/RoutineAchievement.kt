package com.yeolsimee.moneysaving.domain.entity.routine

import com.yeolsimee.moneysaving.domain.calendar.DateIconState
import com.yeolsimee.moneysaving.domain.calendar.isFuture
import com.yeolsimee.moneysaving.domain.calendar.isToday

data class RoutineAchievement(
    val day: String,
    val routineAchievement: String
) {
    fun convertToDateIconState(selectedMonth: Int): DateIconState {
        val (year, month, day) = getYearMonthDay()
        val isToday = isToday(year, month, day)
        val isFuture = isFuture(year, month, day)

        // 과거 루틴은 기록이 있으니까 표시하고
        val iconState = if (selectedMonth > month) {
            if (routineAchievement != "NONE" && !isFuture) {
                DateIconState.OtherMonth
            } else {
                DateIconState.EmptyOtherMonth
            }
        } else if (selectedMonth < month) {  // 미래는 무조건 안보이게
            DateIconState.EmptyOtherMonth
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
