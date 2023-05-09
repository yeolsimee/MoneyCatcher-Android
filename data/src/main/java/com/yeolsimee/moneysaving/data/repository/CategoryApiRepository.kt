package com.yeolsimee.moneysaving.data.repository

import com.yeolsimee.moneysaving.data.api.CategoryApiService
import com.yeolsimee.moneysaving.domain.entity.category.CategoryNameRequest
import com.yeolsimee.moneysaving.domain.entity.category.TextItem
import com.yeolsimee.moneysaving.domain.exception.ApiException
import com.yeolsimee.moneysaving.domain.repository.ICategoryApiRepository
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last

class CategoryApiRepository(private val api: CategoryApiService): ICategoryApiRepository {
    override suspend fun getCategoryList(): Result<MutableList<TextItem>> {
        val response = flow { emit(api.getCategoryList()) }.last()
        val result = response.body()

        return if (result != null && result.success) {
            Result.success(result.data.toMutableList())
        } else {
            Result.failure(ApiException(response.code(), result?.message))
        }
    }

    override suspend fun addCategory(name: String): Result<TextItem> {
        val response = flow { emit(api.addCategory(CategoryNameRequest(name))) }.last()
        val result = response.body()

        return if (result != null && result.success) {
            Result.success(result.data)
        } else {
            Result.failure(ApiException(response.code(), result?.message))
        }
    }
}