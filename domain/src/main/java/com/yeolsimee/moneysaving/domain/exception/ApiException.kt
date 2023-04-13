package com.yeolsimee.moneysaving.domain.exception

class ApiException(
    private val code: Int,
    private val errorMessage: String? = "예상치 못한 에러"
) : Exception() {
    override val message: String
        get() = "code: $code, message: $errorMessage"
}