package com.yeolsimee.moneysaving.domain.entity.routine

data class RoutineRequest(
    val alarmStatus: String,
    val routineType: String = "PRIVATE",
    val alarmTime: String,
    val routineName: String,
    val categoryId: String,
    val weekTypes: Array<String>,
    val routineTimeZone: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RoutineRequest

        if (alarmStatus != other.alarmStatus) return false
        if (routineType != other.routineType) return false
        if (alarmTime != other.alarmTime) return false
        if (routineName != other.routineName) return false
        if (categoryId != other.categoryId) return false
        if (!weekTypes.contentEquals(other.weekTypes)) return false
        if (routineTimeZone != other.routineTimeZone) return false

        return true
    }

    override fun hashCode(): Int {
        var result = alarmStatus.hashCode()
        result = 31 * result + routineType.hashCode()
        result = 31 * result + alarmTime.hashCode()
        result = 31 * result + routineName.hashCode()
        result = 31 * result + categoryId.hashCode()
        result = 31 * result + weekTypes.contentHashCode()
        result = 31 * result + routineTimeZone.hashCode()
        return result
    }
}