package com.yeolsimee.moneysaving.data.api

import com.yeolsimee.moneysaving.domain.entity.ApiResponse
import com.yeolsimee.moneysaving.domain.entity.routine.RoutineDays
import com.yeolsimee.moneysaving.domain.entity.routine.RoutinesOfDay
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RoutineApiService {
    @GET("routinedays")
    suspend fun findAllMyRoutineDays(
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): Response<ApiResponse<RoutineDays>>

    @GET("routineday/{date}")
    suspend fun findRoutineDay(
        @Path("date") date: String
    ): Response<ApiResponse<RoutinesOfDay?>>
}