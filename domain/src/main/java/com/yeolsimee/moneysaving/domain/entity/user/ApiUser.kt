package com.yeolsimee.moneysaving.domain.entity.user

data class ApiUser(
    val name: String = "",
    val userName: String = "",
    val nickName: String? = "",
    val gender: String? = "",
    val phoneNumber: String? = "",
    val isNewUser: String = ""
)
