package com.yeolsimee.moneysaving.data.api

import com.google.gson.internal.LinkedTreeMap
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TestApiService {

    @GET("/common.do")
    suspend fun getTestValues(@Query("drwNo") roundNumber: Int, @Query("method") method: String? = "getLottoNumber"): Response<LinkedTreeMap<String, *>>
}