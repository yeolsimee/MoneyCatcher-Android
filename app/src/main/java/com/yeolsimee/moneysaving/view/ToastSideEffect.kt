package com.yeolsimee.moneysaving.view

import androidx.annotation.Keep

@Keep
sealed class ToastSideEffect {
    @Keep
    data class Toast(val test: String): ToastSideEffect()
}

interface ISideEffect {
    fun showSideEffect(message: String?)
}