package com.yeolsimee.moneysaving.domain.entity.routine

import com.yeolsimee.moneysaving.domain.calendar.DateIconState

data class RoutineDays(
    val routineDays: Array<RoutineAchievement> = arrayOf()
) {
    fun convertToDateIconStateList(selectedMonth: Int): MutableList<DateIconState> {
        val list = mutableListOf<DateIconState>()
        routineDays.forEach { list.add(it.convertToDateIconState(selectedMonth)) }
        return list
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RoutineDays

        if (!routineDays.contentEquals(other.routineDays)) return false

        return true
    }

    override fun hashCode(): Int {
        return routineDays.contentHashCode()
    }
}
