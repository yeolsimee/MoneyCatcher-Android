package com.yeolsimee.moneysaving.data.api

import com.yeolsimee.moneysaving.domain.entity.ApiResponse
import com.yeolsimee.moneysaving.domain.entity.routine.RoutineCheckRequest
import com.yeolsimee.moneysaving.domain.entity.routine.RoutineDays
import com.yeolsimee.moneysaving.domain.entity.routine.RoutineRequest
import com.yeolsimee.moneysaving.domain.entity.routine.RoutineResponse
import com.yeolsimee.moneysaving.domain.entity.routine.RoutinesOfDay
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface RoutineApiService {
    @GET("routinedays")
    suspend fun findAllMyRoutineDays(
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): Response<ApiResponse<RoutineDays>>

    @GET("routineday")
    suspend fun findRoutineDay(
        @Query("date") date: String,
        @Query("checkedRoutineShow") checkedRoutineShow: String = "Y",
    ): Response<ApiResponse<RoutinesOfDay?>>

    @POST("routine")
    suspend fun createRoutine(
        @Body body: RoutineRequest
    ): Response<ApiResponse<RoutineResponse>>

    @PUT("routine/{id}")
    suspend fun updateRoutine(
        @Body body: RoutineRequest
    ): Response<ApiResponse<RoutineResponse>>

    @POST("routinecheck")
    suspend fun routineCheck(
        @Body body: RoutineCheckRequest
    ): Response<ApiResponse<Any>>
}