package com.yeolsimee.moneysaving.domain.entity.routine

import java.io.Serializable

data class RoutineResponse(
    val routineId: Int = -1,
    val routineName: String = "",
    val categoryId: Int = -1,
    val categoryName: String = "",
    val routineType: String = "",
    val alarmStatus: String = "",
    val alarmTime: String = "",
    val routineTimeZone: String = "1",
    val weekTypes: Array<String> = arrayOf()
): Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RoutineResponse

        if (routineId != other.routineId) return false
        if (routineName != other.routineName) return false
        if (categoryId != other.categoryId) return false
        if (categoryName != other.categoryName) return false
        if (routineType != other.routineType) return false
        if (alarmStatus != other.alarmStatus) return false
        if (alarmTime != other.alarmTime) return false
        if (routineTimeZone != other.routineTimeZone) return false
        if (!weekTypes.contentEquals(other.weekTypes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = routineId
        result = 31 * result + routineName.hashCode()
        result = 31 * result + categoryId
        result = 31 * result + categoryName.hashCode()
        result = 31 * result + routineType.hashCode()
        result = 31 * result + alarmStatus.hashCode()
        result = 31 * result + alarmTime.hashCode()
        result = 31 * result + routineTimeZone.hashCode()
        result = 31 * result + weekTypes.contentHashCode()
        return result
    }

    fun isEmpty() = routineId == -1
}