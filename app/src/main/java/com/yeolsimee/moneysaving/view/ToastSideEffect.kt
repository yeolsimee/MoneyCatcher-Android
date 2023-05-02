package com.yeolsimee.moneysaving.view

sealed class ToastSideEffect {
    data class Toast(val test: String): ToastSideEffect()
}

interface ISideEffect {
    fun showSideEffect(message: String?)
}