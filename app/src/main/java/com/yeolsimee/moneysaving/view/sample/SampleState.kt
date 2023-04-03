package com.yeolsimee.moneysaving.view.sample

import com.yeolsimee.moneysaving.domain.entity.SampleData

data class SampleState(
    val number1: Int? = null,
    val number2: Int? = null,
    val number3: Int? = null,
    val number4: Int? = null,
    val number5: Int? = null,
    val number6: Int? = null,
    val bonus: Int? = null
)

fun generateSampleStateFromSampleData(data: SampleData): SampleState {
    return SampleState(
        number1 = data.drwtNo1!!.toInt(),
        number2 = data.drwtNo2!!.toInt(),
        number3 = data.drwtNo3!!.toInt(),
        number4 = data.drwtNo4!!.toInt(),
        number5 = data.drwtNo5!!.toInt(),
        number6 = data.drwtNo6!!.toInt(),
        bonus = data.bnusNo!!.toInt()
    )
}