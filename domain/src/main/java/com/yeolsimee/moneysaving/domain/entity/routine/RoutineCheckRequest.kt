package com.yeolsimee.moneysaving.domain.entity.routine

data class RoutineCheckRequest(
    val routineCheckYN: String,
    val routineId: Int,
    val routineDay: String
)
