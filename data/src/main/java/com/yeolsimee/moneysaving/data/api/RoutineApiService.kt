package com.yeolsimee.moneysaving.data.api

import com.yeolsimee.moneysaving.domain.entity.ApiResponse
import com.yeolsimee.moneysaving.domain.entity.routine.RoutineDays
import com.yeolsimee.moneysaving.domain.entity.routine.RoutinesOfDay
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface RoutineApiService {
    @GET("routinedays/{startDate}/{endDate}")
    suspend fun findAllMyRoutineDays(
        @Path("startDate") startDate: String,
        @Path("endDate") endDate: String
    ): Response<ApiResponse<RoutineDays>>

    @GET("routineday/{date}")
    suspend fun findRoutineDay(
        @Path("date") date: String
    ): Response<ApiResponse<RoutinesOfDay>>
}