package com.yeolsimee.moneysaving.domain.usecase

import com.yeolsimee.moneysaving.domain.calendar.DateIconState
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
}