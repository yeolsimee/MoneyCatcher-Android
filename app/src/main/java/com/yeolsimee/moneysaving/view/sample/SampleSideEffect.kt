package com.yeolsimee.moneysaving.view.sample

sealed class SampleSideEffect {
    data class Toast(val test: String): SampleSideEffect()
}