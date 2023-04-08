package com.yeolsimee.moneysaving.data.api

import com.yeolsimee.moneysaving.domain.entity.ApiResponse
import com.yeolsimee.moneysaving.domain.entity.LoginResult
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.POST

interface UserApiService {

    @POST("login")
    suspend fun login(@Header("x-auth") token: String): Response<ApiResponse<LoginResult>>
}