package com.yeolsimee.moneysaving.domain.entity

import androidx.annotation.Keep

@Keep
data class LoginResult(
    val name: String?,
    val isNewUser: String?
)