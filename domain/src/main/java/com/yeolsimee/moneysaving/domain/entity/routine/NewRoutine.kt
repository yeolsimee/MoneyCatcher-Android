package com.yeolsimee.moneysaving.domain.entity.routine

data class NewRoutine(
    val alarmStatus: String,
    val routineType: String = "PRIVATE",
    val alarmTime: String,
    val routineName: String,
    val categoryId: Int,
    val weekTypes: List<String>,
    val routineTimeZone: String
)
