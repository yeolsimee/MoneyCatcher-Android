package com.yeolsimee.moneysaving.domain.usecase

import com.yeolsimee.moneysaving.domain.entity.LoginResult
import com.yeolsimee.moneysaving.domain.repository.IUserApiRepository

class UserUseCase(private val userApiRepository: IUserApiRepository) {
    suspend fun login(token: String): LoginResult? {
        return userApiRepository.login(token)
    }
}