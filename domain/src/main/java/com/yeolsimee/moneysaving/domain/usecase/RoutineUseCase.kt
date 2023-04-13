package com.yeolsimee.moneysaving.domain.usecase

import com.yeolsimee.moneysaving.domain.entity.routine.RoutineDays
import com.yeolsimee.moneysaving.domain.repository.IRoutineApiRepository

class RoutineUseCase(private val repository: IRoutineApiRepository) {
    suspend fun findAllMyRoutineDays(startDate: String, endDate: String): Result<RoutineDays> {
        return repository.findAllMyRoutineDays(startDate, endDate)
    }
}