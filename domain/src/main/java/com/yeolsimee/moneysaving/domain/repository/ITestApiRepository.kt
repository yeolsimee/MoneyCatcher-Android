package com.yeolsimee.moneysaving.domain.repository

import com.yeolsimee.moneysaving.domain.entity.SampleData

interface ITestApiRepository {
    suspend fun getTestValues(roundNumber: Int): SampleData?
}