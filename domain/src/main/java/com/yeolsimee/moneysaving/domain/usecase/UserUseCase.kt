package com.yeolsimee.moneysaving.domain.usecase

import com.yeolsimee.moneysaving.domain.entity.LoginResult
import com.yeolsimee.moneysaving.domain.entity.user.ApiUser
import com.yeolsimee.moneysaving.domain.repository.IUserApiRepository

class UserUseCase(private val userApiRepository: IUserApiRepository) {
    suspend fun login(): Result<LoginResult> {
        return userApiRepository.login()
    }

    suspend fun signUp(): Result<ApiUser> {
        return userApiRepository.signUp()
    }
}