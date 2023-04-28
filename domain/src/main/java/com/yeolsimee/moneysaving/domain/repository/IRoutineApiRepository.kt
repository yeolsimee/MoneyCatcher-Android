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

    suspend fun updateRoutine(routineRequest: RoutineRequest): Result<RoutineResponse>

    suspend fun routineCheck(routineCheckRequest: RoutineCheckRequest): Result<Any>
}