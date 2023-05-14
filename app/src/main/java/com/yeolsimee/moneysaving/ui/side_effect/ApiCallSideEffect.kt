package com.yeolsimee.moneysaving.ui.side_effect

import androidx.annotation.Keep

@Keep
sealed class ApiCallSideEffect {
    @Keep
    object Loading: ApiCallSideEffect()

    @Keep
    object Empty : ApiCallSideEffect()
}

interface IApiCallSideEffect {
    fun showLoading()

    fun showEmpty()
}