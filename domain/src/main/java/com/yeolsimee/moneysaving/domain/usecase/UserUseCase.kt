package com.yeolsimee.moneysaving.domain.usecase

import com.yeolsimee.moneysaving.domain.entity.LoginResult
import com.yeolsimee.moneysaving.domain.entity.routine.RoutineResponse
import com.yeolsimee.moneysaving.domain.entity.user.ApiUser
import com.yeolsimee.moneysaving.domain.repository.IUserApiRepository

class UserUseCase(
    private val userApiRepository: IUserApiRepository,
    private val routineUseCase: RoutineUseCase
) {
    suspend fun login(): Result<LoginResult> {
        return userApiRepository.login()
    }

    suspend fun signUp(): Result<ApiUser> {
        return userApiRepository.signUp()
    }

    suspend fun withdraw(): Result<Boolean> {
        return userApiRepository.withdraw()
    }

    suspend fun getAlarmList(): Result<List<RoutineResponse>> {
        return routineUseCase.getActivatedAlarmRoutine()
    }
}