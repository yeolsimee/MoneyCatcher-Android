package com.yeolsimee.moneysaving.domain.entity.routine

import com.yeolsimee.moneysaving.domain.entity.category.CategoryWithRoutines

data class RoutinesOfDay(
    private val routineDay: String? = "",
    val categoryDatas: Array<CategoryWithRoutines> = arrayOf()
) {
    fun isNotLoading(): Boolean {
        return routineDay != "loading"
    }

    fun isEmpty(): Boolean {
        return routineDay != "loading" && categoryDatas.isEmpty()
    }
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
}

