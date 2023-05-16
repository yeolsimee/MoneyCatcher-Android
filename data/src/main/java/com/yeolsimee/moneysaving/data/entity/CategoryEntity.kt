package com.yeolsimee.moneysaving.data.entity

import com.yeolsimee.moneysaving.domain.entity.category.TextItem

data class CategoryEntity(
    val categoryId: String,
    val categoryName: String
) {
    fun toTextItem(): TextItem {
        return TextItem(
            id = categoryId,
            name = categoryName
        )
    }

    companion object {
        fun fromTextItem(textItem: TextItem): CategoryEntity {
            return CategoryEntity(textItem.id, textItem.name)
        }
    }
}