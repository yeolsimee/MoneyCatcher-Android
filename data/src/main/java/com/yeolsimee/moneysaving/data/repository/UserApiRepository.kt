package com.yeolsimee.moneysaving.data.repository

import android.util.Log
import com.yeolsimee.moneysaving.data.source.UserSource
import com.yeolsimee.moneysaving.domain.entity.LoginResult
import com.yeolsimee.moneysaving.domain.repository.IUserApiRepository
import kotlinx.coroutines.flow.last

class UserApiRepository(private val source: UserSource): IUserApiRepository {
    override suspend fun login(token: String): LoginResult? {

        val result = source.login(token).last().body()
        return if (result != null && result.success) {
            result.data
        } else {
            // TODO 오류코드에 따라 분기
            Log.e("UserApiRepository", result?.message ?: "unexpected error")
            null
        }
    }
}