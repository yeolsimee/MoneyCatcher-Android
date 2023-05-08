package com.yeolsimee.moneysaving.data.api

import com.yeolsimee.moneysaving.domain.entity.ApiResponse
import com.yeolsimee.moneysaving.domain.entity.LoginResult
import com.yeolsimee.moneysaving.domain.entity.user.ApiUser
import com.yeolsimee.moneysaving.domain.entity.user.NewUserChecker
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApiService {

    @POST("login")
    suspend fun login(): Response<ApiResponse<LoginResult>>

    @POST("isnewuser/update")
    suspend fun signUp(@Body userChecker: NewUserChecker = NewUserChecker("N")):
            Response<ApiResponse<ApiUser>>

    @POST("withdraw")
    suspend fun withdraw(): Response<ApiResponse<Any>>
}