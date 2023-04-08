package com.yeolsimee.moneysaving.domain.entity

data class ApiResponse<T>(
    val success:Boolean,
    val code: Int,
    val message: String,
    val data: T
)
