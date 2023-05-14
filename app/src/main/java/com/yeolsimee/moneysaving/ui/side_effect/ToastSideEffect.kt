package com.yeolsimee.moneysaving.ui.side_effect

import androidx.annotation.Keep

@Keep
sealed class ToastSideEffect {
    @Keep
    data class Toast(val test: String): ToastSideEffect()
}

interface IToastSideEffect {
    fun showSideEffect(message: String?)
}