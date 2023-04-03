package com.yeolsimee.moneysaving.data.api

import com.google.gson.Gson
import com.yeolsimee.moneysaving.data.sample.SampleSource
import com.yeolsimee.moneysaving.domain.entity.SampleData
import com.yeolsimee.moneysaving.domain.repository.ITestApiRepository
import kotlinx.coroutines.flow.first

class TestApiRepository(private val source: SampleSource): ITestApiRepository {
    override suspend fun getTestValues(roundNumber: Int): SampleData? {
        val body = source.getTestValues(roundNumber).first()

        return Gson().fromJson(body.toString(), SampleData::class.java)
    }
}