package com.yeolsimee.moneysaving.domain.entity.user

data class ApiUser(
    val name: String = "",
    val username: String = "",
    val nickName: String? = "",
    val gender: String? = "",
    val phoneNumber: String? = "",
    val isNewUser: String = ""
)
