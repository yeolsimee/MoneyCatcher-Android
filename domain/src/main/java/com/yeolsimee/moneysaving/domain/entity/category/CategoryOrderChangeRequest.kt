package com.yeolsimee.moneysaving.domain.entity.category

data class CategoryOrderChangeRequest(
    val categoryId: String,
    val targetCategoryId: String
)