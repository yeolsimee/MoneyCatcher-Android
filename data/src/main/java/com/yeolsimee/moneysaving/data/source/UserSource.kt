package com.yeolsimee.moneysaving.data.source

import com.yeolsimee.moneysaving.data.api.UserApiService
import com.yeolsimee.moneysaving.domain.entity.ApiResponse
import com.yeolsimee.moneysaving.domain.entity.LoginResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class UserSource(
    private val api: UserApiService
) {
    fun login(token: String): Flow<Response<ApiResponse<LoginResult>>> =
        flow { emit(api.login(token)) }
}