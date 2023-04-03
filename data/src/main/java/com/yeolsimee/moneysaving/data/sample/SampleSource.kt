package com.yeolsimee.moneysaving.data.sample

import com.google.gson.internal.LinkedTreeMap
import com.yeolsimee.moneysaving.data.api.TestApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class SampleSource(
    private val api: TestApiService
) {
    fun getTestValues(round: Int): Flow<Response<LinkedTreeMap<String, *>>> {
        return flow {
            emit(api.getTestValues(round))
        }
    }
}