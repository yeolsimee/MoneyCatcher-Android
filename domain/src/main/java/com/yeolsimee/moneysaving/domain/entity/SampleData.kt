package com.yeolsimee.moneysaving.domain.entity

import androidx.annotation.Keep

@Keep
data class SampleData(
    val totSellamnt: Double? = null,
    val returnValue: String? = null,
    val drwNoDate: String? = null,
    val firstWinamnt: Double? = null,
    val drwNo: Double? = null,
    val drwtNo1: Double? = null,
    val drwtNo2: Double? = null,
    val drwtNo3: Double? = null,
    val drwtNo4: Double? = null,
    val drwtNo5: Double? = null,
    val drwtNo6: Double? = null,
    val firstPrzwnerCo: Long? = null,
    val bnusNo: Double? = null
) {
    fun isNullOrBlank(): Boolean {
        return returnValue.isNullOrBlank()
    }

}