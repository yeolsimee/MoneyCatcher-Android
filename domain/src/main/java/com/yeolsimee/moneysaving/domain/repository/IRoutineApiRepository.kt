package com.yeolsimee.moneysaving.domain.repository

import com.yeolsimee.moneysaving.domain.entity.routine.RoutineDays

interface IRoutineApiRepository {
    suspend fun findAllMyRoutineDays(startDate: String, endDate: String): Result<RoutineDays>
}