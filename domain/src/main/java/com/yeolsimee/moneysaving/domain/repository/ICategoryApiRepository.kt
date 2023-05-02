package com.yeolsimee.moneysaving.domain.repository

import com.yeolsimee.moneysaving.domain.entity.category.TextItem

interface ICategoryApiRepository {
    suspend fun getCategoryList(
    ): Result<MutableList<TextItem>>

    suspend fun addCategory(
        name: String
    ): Result<TextItem>
}