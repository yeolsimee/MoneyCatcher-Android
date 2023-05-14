package com.yeolsimee.moneysaving.data.entity

import com.yeolsimee.moneysaving.domain.entity.category.TextItem

data class CategoryEntity(
    val id: Int,
    val name: String
) {
    fun toTextItem(): TextItem {
        return TextItem(
            id = this.id.toString(),
            name = name
        )
    }

    companion object {
        fun fromTextItem(textItem: TextItem): CategoryEntity {
            return CategoryEntity(textItem.id.toInt(), textItem.name)
        }
    }
}