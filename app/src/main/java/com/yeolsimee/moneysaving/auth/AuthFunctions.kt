package com.yeolsimee.moneysaving.auth

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.functions.FirebaseFunctions

object AuthFunctions {

    private fun getAuthMapResult(
        map: Map<String, String>,
        tokenCallback: ((String) -> Unit)
    ) {
        val token = map["firebase_token"]
        if (token != null) {
            tokenCallback(token)
        }
    }

    fun getAuthResult(
        resultTask: Task<AuthResult>,
        onResult: (Result<String>) -> Unit
    ) {
        resultTask.addOnCompleteListener {
            if (it.isSuccessful) {
                val token = it.result.user?.getIdToken(false)?.result?.token
                if (token != null) {
                    onResult(Result.success(token))
                } else {
                    onResult(Result.failure(Exception("토큰 null")))
                }
            } else {
                onResult(Result.failure(Exception("firebase 실패")))
            }
        }
            .addOnCanceledListener {
                onResult(Result.failure(Exception("firebase 취소")))
            }
            .addOnFailureListener {
                onResult(Result.failure(it))
            }
    }

    fun customAuthByFirebaseFunctions(
        functions: FirebaseFunctions,
        accessToken: String?,
        type: String,
        tokenCallback: ((String) -> Unit)
    ) {
        if (accessToken != null) {
            functions.getHttpsCallable(type)
                .call(accessToken)
                .addOnSuccessListener {
                    @Suppress("UNCHECKED_CAST")
                    val result = it.data as Map<String, String>
                    getAuthMapResult(result, tokenCallback)
                }
        }
    }
}