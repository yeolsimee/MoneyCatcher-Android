package com.yeolsimee.moneysaving.domain.usecase

import com.yeolsimee.moneysaving.domain.entity.SampleData
import com.yeolsimee.moneysaving.domain.repository.ITestApiRepository

class SampleUseCase(private val iTestApiRepository: ITestApiRepository) {
    suspend operator fun invoke(round: Int): SampleData {
        return iTestApiRepository.getTestValues(round) ?: SampleData()
    }
}