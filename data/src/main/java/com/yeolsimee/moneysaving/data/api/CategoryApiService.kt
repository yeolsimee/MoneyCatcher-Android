package com.yeolsimee.moneysaving.data.api

import com.yeolsimee.moneysaving.data.entity.CategoryEntity
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
    ): Response<ApiResponse<List<CategoryEntity>>>

    @POST("category/insert")
    suspend fun addCategory(
        @Body body: CategoryNameRequest
    ): Response<ApiResponse<CategoryEntity>>

    @POST("category/delete")
    suspend fun deleteCategory(
        @Body body: TextItem
    ): Response<ApiResponse<Any>>
}