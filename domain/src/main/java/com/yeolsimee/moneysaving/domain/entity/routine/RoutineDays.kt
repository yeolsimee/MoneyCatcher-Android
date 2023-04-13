package com.yeolsimee.moneysaving.domain.entity.routine

data class RoutineDays(
    val routineDays: Array<RoutineAchievement>
) {
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
