package com.yeolsimee.moneysaving.domain.repository

import com.yeolsimee.moneysaving.domain.calendar.DateIconState
import com.yeolsimee.moneysaving.domain.entity.routine.RoutineCheckRequest
import com.yeolsimee.moneysaving.domain.entity.routine.RoutineRequest
import com.yeolsimee.moneysaving.domain.entity.routine.RoutineResponse
import com.yeolsimee.moneysaving.domain.entity.routine.RoutinesOfDay

interface IRoutineApiRepository {
    suspend fun findAllMyRoutineDays(startDate: String, endDate: String, selectedMonth: Int): Result<MutableList<DateIconState>>
    suspend fun findRoutineDay(date: String): Result<RoutinesOfDay>
    suspend fun createRoutine(routineRequest: RoutineRequest): Result<RoutineResponse>
    suspend fun updateRoutine(routineId: String, routineRequest: RoutineRequest): Result<RoutineResponse>
    suspend fun routineCheck(routineCheckRequest: RoutineCheckRequest): Result<RoutinesOfDay>
    suspend fun deleteRoutine(routineId: String): Result<Boolean>
    suspend fun getRoutine(routineId: String): Result<RoutineResponse>
    suspend fun getActivatedAlarmRoutine(): Result<List<RoutineResponse>>
}