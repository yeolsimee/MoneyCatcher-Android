package com.yeolsimee.moneysaving.domain.usecase

import com.yeolsimee.moneysaving.domain.entity.LoginResult
import com.yeolsimee.moneysaving.domain.repository.IUserApiRepository

class UserUseCase(private val userApiRepository: IUserApiRepository) {
    suspend fun login(): Result<LoginResult> {
        return userApiRepository.login()
    }
}