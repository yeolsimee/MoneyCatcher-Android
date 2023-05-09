package com.yeolsimee.moneysaving.domain.repository

import com.yeolsimee.moneysaving.domain.entity.LoginResult
import com.yeolsimee.moneysaving.domain.entity.user.ApiUser

interface IUserApiRepository {
    suspend fun login(): Result<LoginResult>
    suspend fun signUp(): Result<ApiUser>
    suspend fun withdraw(): Result<Boolean>
}