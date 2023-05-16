package com.yeolsimee.moneysaving.domain.repository

import com.yeolsimee.moneysaving.domain.entity.category.TextItem

interface ICategoryApiRepository {
    suspend fun getCategoryList(): Result<MutableList<TextItem>>

    suspend fun addCategory(name: String): Result<TextItem>

    suspend fun delete(category: TextItem): Result<Any>
    suspend fun update(category: TextItem): Result<Any>
}