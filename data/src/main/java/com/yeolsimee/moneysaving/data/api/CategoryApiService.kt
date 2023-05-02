package com.yeolsimee.moneysaving.data.api

import com.yeolsimee.moneysaving.domain.entity.ApiResponse
import com.yeolsimee.moneysaving.domain.entity.category.CategoryNameRequest
import com.yeolsimee.moneysaving.domain.entity.category.TextItem
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface CategoryApiService {
    @GET("category")
    suspend fun getCategoryList(
    ): Response<ApiResponse<List<TextItem>>>

    @POST("category/insert")
    suspend fun addCategory(
        @Body body: CategoryNameRequest
    ): Response<ApiResponse<TextItem>>
}