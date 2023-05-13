package com.yeolsimee.moneysaving.view.category

import androidx.annotation.Keep

@Keep
sealed class CategoryViewSideEffect {
    @Keep
    object Loading: CategoryViewSideEffect()

    @Keep
    object Empty : CategoryViewSideEffect()
}

interface ICategoryViewSideEffect {
    fun showLoading()

    fun showEmpty()
}