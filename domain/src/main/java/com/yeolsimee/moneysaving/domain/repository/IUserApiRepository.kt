package com.yeolsimee.moneysaving.domain.repository

import com.yeolsimee.moneysaving.domain.entity.LoginResult

interface IUserApiRepository {
    suspend fun login(): Result<LoginResult>
}