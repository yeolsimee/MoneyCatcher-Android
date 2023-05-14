package com.yeolsimee.moneysaving.domain.usecase

import com.yeolsimee.moneysaving.domain.calendar.DateIconState
import com.yeolsimee.moneysaving.domain.entity.routine.RoutineCheckRequest
import com.yeolsimee.moneysaving.domain.entity.routine.RoutineRequest
import com.yeolsimee.moneysaving.domain.entity.routine.RoutineResponse
import com.yeolsimee.moneysaving.domain.entity.routine.RoutinesOfDay
import com.yeolsimee.moneysaving.domain.repository.IRoutineApiRepository

class RoutineUseCase(private val repository: IRoutineApiRepository) {
    suspend fun findAllMyRoutineDays(startDate: String, endDate: String, selectedMonth: Int): Result<MutableList<DateIconState>> {
        return repository.findAllMyRoutineDays(startDate, endDate, selectedMonth)
    }

    suspend fun findRoutineDay(date: String): Result<RoutinesOfDay> {
        return repository.findRoutineDay(date)
    }

    suspend fun createRoutine(routineRequest: RoutineRequest): Result<RoutineResponse> {
        return repository.createRoutine(routineRequest)
    }

    suspend fun updateRoutine(routineId: String?, routineRequest: RoutineRequest): Result<RoutineResponse> {
        return repository.updateRoutine(routineId ?: "", routineRequest)
    }

    suspend fun routineCheck(routineCheckRequest: RoutineCheckRequest): Result<RoutinesOfDay> {
        return repository.routineCheck(routineCheckRequest)
    }

    suspend fun deleteRoutine(routineId: String): Result<Boolean> {
        return repository.deleteRoutine(routineId)
    }

    suspend fun getRoutine(routineId: String): Result<RoutineResponse> {
        return repository.getRoutine(routineId)
    }

    suspend fun getActivatedAlarmRoutine(): Result<List<RoutineResponse>> {
        return repository.getActivatedAlarmRoutine()
    }
}