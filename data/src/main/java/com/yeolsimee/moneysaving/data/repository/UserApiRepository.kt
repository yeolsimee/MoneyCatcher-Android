package com.yeolsimee.moneysaving.data.repository

import com.yeolsimee.moneysaving.data.source.UserSource
import com.yeolsimee.moneysaving.domain.entity.LoginResult
import com.yeolsimee.moneysaving.domain.entity.user.ApiUser
import com.yeolsimee.moneysaving.domain.exception.ApiException
import com.yeolsimee.moneysaving.domain.repository.IUserApiRepository
import kotlinx.coroutines.flow.last

class UserApiRepository(private val source: UserSource): IUserApiRepository {
    override suspend fun login(): Result<LoginResult> {

        val response = source.login().last()
        val result = response.body()
        return if (result != null && result.hasData()) {
            Result.success(result.data)
        } else {
            Result.failure(ApiException(response.code(), result?.message))
        }
    }

    override suspend fun signUp(): Result<ApiUser> {
        val response = source.signUp().last()
        val result = response.body()
        return if (result != null && result.hasData()) {
            Result.success(result.data)
        } else {
            Result.failure(ApiException(response.code(), result?.message))
        }
    }

    override suspend fun withdraw(): Result<Boolean> {
        val response = source.withdraw().last()
        val result = response.body()
        return if (result != null && result.hasData()) {
            Result.success(true)
        } else {
            Result.failure(ApiException(response.code(), result?.message))
        }
    }
}