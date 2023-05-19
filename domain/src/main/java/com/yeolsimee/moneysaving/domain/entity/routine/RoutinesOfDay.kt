package com.yeolsimee.moneysaving.domain.entity.routine

import com.yeolsimee.moneysaving.domain.calendar.isNotPastFromString
import com.yeolsimee.moneysaving.domain.calendar.isTodayFromString
import com.yeolsimee.moneysaving.domain.entity.category.CategoryWithRoutines

data class RoutinesOfDay(
    val routineDay: String? = "",
    val categoryDatas: Array<CategoryWithRoutines> = arrayOf()
) {
    fun isNotLoadingAndNotEmpty() = routineDay != "loading" && categoryDatas.isNotEmpty()
    fun isNotLoading() = routineDay != "loading"
    fun isEmpty() = routineDay != "loading" && categoryDatas.isEmpty()
    fun isToday() = isTodayFromString(routineDay)
    fun isNotPast() = isNotPastFromString(routineDay)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RoutinesOfDay

        if (routineDay != other.routineDay) return false
        if (!categoryDatas.contentEquals(other.categoryDatas)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = routineDay.hashCode()
        result = 31 * result + categoryDatas.contentHashCode()
        return result
    }

    fun getDate(): String {
        val (year, month, day) = getYearMonthDay()
        val y = year.toString().padStart(4, '0')
        val m = month.toString().padStart(2, '0')
        val d = day.toString().padStart(2, '0')
        return y + m + d
    }

    fun getYearMonthDay(): Triple<Int, Int, Int> {
        if (routineDay?.isEmpty() != false) return Triple(0,0,0)
        val year = routineDay.substring(0, 4).toInt()
        val month = routineDay.substring(4, 6).toInt()
        val day = routineDay.substring(6, 8).toInt()
        return Triple(year, month, day)
    }
}

