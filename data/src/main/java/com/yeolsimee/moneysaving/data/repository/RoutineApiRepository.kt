package com.yeolsimee.moneysaving.data.repository

import com.yeolsimee.moneysaving.data.source.RoutineSource
import com.yeolsimee.moneysaving.domain.entity.routine.RoutineDays
import com.yeolsimee.moneysaving.domain.entity.routine.RoutinesOfDay
import com.yeolsimee.moneysaving.domain.exception.ApiException
import com.yeolsimee.moneysaving.domain.repository.IRoutineApiRepository
import kotlinx.coroutines.flow.last

class RoutineApiRepository(private val source: RoutineSource): IRoutineApiRepository{
    override suspend fun findAllMyRoutineDays(
        startDate: String,
        endDate: String
    ): Result<RoutineDays> {

        val response = source.findAllMyRoutineDays(startDate, endDate).last()
        val result = response.body()
        return if (result != null && result.success) {
            Result.success(result.data)
        } else {
            Result.failure(ApiException(response.code(), result?.message))
        }
    }

    override suspend fun findRoutineDay(date: String): Result<RoutinesOfDay> {
        val response = source.findRoutineDay(date).last()
        val result = response.body()
        return if (result != null && result.success && result.data != null) {
            Result.success(result.data!!)
        } else {
            Result.failure(ApiException(response.code(), result?.message))
        }
    }
}