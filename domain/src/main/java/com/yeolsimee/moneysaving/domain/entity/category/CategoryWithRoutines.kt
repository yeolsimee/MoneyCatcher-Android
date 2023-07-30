package com.yeolsimee.moneysaving.domain.entity.category

import com.yeolsimee.moneysaving.domain.entity.routine.Routine

data class CategoryWithRoutines(
    val categoryId: String,
    val categoryName: String,
    val remainingRoutineNum: String,
    val routineDatas: Array<Routine>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CategoryWithRoutines

        if (categoryId != other.categoryId) return false
        if (categoryName != other.categoryName) return false
        if (remainingRoutineNum != other.remainingRoutineNum) return false
        if (!routineDatas.contentEquals(other.routineDatas)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = categoryId.hashCode()
        result = 31 * result + categoryName.hashCode()
        result = 31 * result + remainingRoutineNum.hashCode()
        result = 31 * result + routineDatas.contentHashCode()
        return result
    }

    fun getTextItem(): TextItem {
        return TextItem(categoryId, categoryName)
    }
}
