package com.yeolsimee.moneysaving.utils

data class DialogState<T>(
    val isShowing: Boolean = false,
    val data: T? = null
)
