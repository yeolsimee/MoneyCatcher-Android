package com.yeolsimee.moneysaving.data.repository

import com.yeolsimee.moneysaving.data.source.RoutineSource
import com.yeolsimee.moneysaving.domain.calendar.DateIconState
import com.yeolsimee.moneysaving.domain.entity.routine.RoutineCheckRequest
import com.yeolsimee.moneysaving.domain.entity.routine.RoutineRequest
import com.yeolsimee.moneysaving.domain.entity.routine.RoutineResponse
import com.yeolsimee.moneysaving.domain.entity.routine.RoutinesOfDay
import com.yeolsimee.moneysaving.domain.exception.ApiException
import com.yeolsimee.moneysaving.domain.repository.IRoutineApiRepository
import kotlinx.coroutines.flow.last

class RoutineApiRepository(private val source: RoutineSource): IRoutineApiRepository{
    override suspend fun findAllMyRoutineDays(
        startDate: String,
        endDate: String,
        selectedMonth: Int
    ): Result<MutableList<DateIconState>> {

        val response = source.findAllMyRoutineDays(startDate, endDate).last()
        val result = response.body()
        return if (result != null && result.hasData()) {
            Result.success(result.data.convertToDateIconStateList(selectedMonth))
        } else {
            Result.failure(ApiException(response.code(), result?.message))
        }
    }

    override suspend fun findRoutineDay(date: String): Result<RoutinesOfDay> {
        val response = source.findRoutineDay(date).last()
        val result = response.body()
        return if (result != null && result.hasData()) {
            Result.success(result.data!!)
        } else {
            Result.failure(ApiException(response.code(), result?.message))
        }
    }

    override suspend fun createRoutine(routineRequest: RoutineRequest): Result<RoutineResponse> {
        val response = source.createRoutine(routineRequest).last()
        val result = response.body()
        return if (result != null && result.hasData()) {
            Result.success(result.data)
        } else {
            Result.failure(ApiException(response.code(), result?.message))
        }
    }

    override suspend fun updateRoutine(routineId: Int, routineRequest: RoutineRequest): Result<RoutineResponse> {
        val response = source.updateRoutine(routineId, routineRequest).last()
        val result = response.body()
        return if (result != null && result.hasData()) {
            Result.success(result.data)
        } else {
            Result.failure(ApiException(response.code(), result?.message))
        }
    }

    override suspend fun routineCheck(routineCheckRequest: RoutineCheckRequest): Result<RoutinesOfDay> {
        val response = source.routineCheck(routineCheckRequest).last()
        val result = response.body()
        return if (result != null && result.hasData()) {
            Result.success(result.data)
        } else {
            Result.failure(ApiException(response.code(), result?.message))
        }
    }

    override suspend fun deleteRoutine(routineId: Int): Result<Boolean> {
        val response = source.deleteRoutine(routineId).last()
        val result = response.body()
        return if (result != null && result.code == 0) {
            Result.success(true)
        } else {
            Result.failure(ApiException(response.code(), result?.message))
        }
    }

    override suspend fun getRoutine(routineId: Int): Result<RoutineResponse> {
        val response = source.getRoutine(routineId).last()
        val result = response.body()
        return if (result != null && result.hasData()) {
            Result.success(result.data)
        } else {
            Result.failure(ApiException(response.code(), result?.message))
        }
    }

    override suspend fun getActivatedAlarmRoutine(): Result<List<RoutineResponse>> {
        val response = source.getActivatedAlarmRoutine().last()
        val result = response.body()
        return if (result != null && result.hasData()) {
            Result.success(result.data)
        } else {
            Result.failure(ApiException(response.code(), result?.message))
        }
    }


}