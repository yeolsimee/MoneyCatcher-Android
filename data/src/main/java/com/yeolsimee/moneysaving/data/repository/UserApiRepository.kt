package com.yeolsimee.moneysaving.data.repository

import com.yeolsimee.moneysaving.data.source.UserSource
import com.yeolsimee.moneysaving.domain.entity.LoginResult
import com.yeolsimee.moneysaving.domain.repository.IUserApiRepository
import kotlinx.coroutines.flow.last

class UserApiRepository(private val source: UserSource): IUserApiRepository {
    override suspend fun login(token: String): Result<LoginResult> {

        val result = source.login(token).last().body()
        return if (result != null && result.success) {
            Result.success(result.data)
        } else {
            Result.failure(Exception(result?.message))
        }
    }
}