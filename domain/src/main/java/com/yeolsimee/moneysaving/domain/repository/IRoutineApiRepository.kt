package com.yeolsimee.moneysaving.domain.repository

import com.yeolsimee.moneysaving.domain.entity.routine.RoutineDays
import com.yeolsimee.moneysaving.domain.entity.routine.RoutinesOfDay

interface IRoutineApiRepository {
    suspend fun findAllMyRoutineDays(startDate: String, endDate: String): Result<RoutineDays>

    suspend fun findRoutineDay(date: String): Result<RoutinesOfDay>
}