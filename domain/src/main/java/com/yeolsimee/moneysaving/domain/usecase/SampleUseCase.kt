package com.yeolsimee.moneysaving.domain.usecase

import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import com.yeolsimee.moneysaving.data.api.TestApiService
import com.yeolsimee.moneysaving.data.api.model.SampleData
import javax.inject.Inject

class SampleUseCase @Inject constructor(private val testApiService: TestApiService) {
    suspend operator fun invoke(round: Int): SampleData {
        val bodyResult = testApiService.getTestValues(round).body()

        if (bodyResult is LinkedTreeMap<String, *>) {
            return Gson().fromJson(bodyResult.toString(), SampleData::class.java)
        }

        return SampleData()
    }
}