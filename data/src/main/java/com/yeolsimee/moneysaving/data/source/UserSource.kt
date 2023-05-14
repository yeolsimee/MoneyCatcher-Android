package com.yeolsimee.moneysaving.data.source

import com.yeolsimee.moneysaving.data.api.UserApiService
import com.yeolsimee.moneysaving.domain.entity.ApiResponse
import com.yeolsimee.moneysaving.domain.entity.LoginResult
import com.yeolsimee.moneysaving.domain.entity.user.ApiUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class UserSource(
    private val api: UserApiService
) {
    fun login(): Flow<Response<ApiResponse<LoginResult>>> = flow { emit(api.login()) }
    fun signUp(): Flow<Response<ApiResponse<ApiUser>>> = flow { emit(api.signUp()) }
    fun withdraw(): Flow<Response<ApiResponse<Any>>> = flow { emit(api.withdraw()) }
}