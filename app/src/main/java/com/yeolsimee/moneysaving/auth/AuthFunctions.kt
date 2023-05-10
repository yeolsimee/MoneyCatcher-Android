package com.yeolsimee.moneysaving.auth

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.functions.FirebaseFunctions
import com.yeolsimee.moneysaving.App

object AuthFunctions {

    private fun getAuthMapResult(
        resultTask: Task<Map<String, String>>,
        tokenCallback: ((String) -> Unit)
    ) {
        resultTask.addOnCompleteListener {
            if (it.isSuccessful) {
                val token = it.result["firebase_token"]
                if (token != null) {
                    tokenCallback(token)
                }
            } else {
                Log.i(App.TAG, "firebase 실패")
            }
        }
            .addOnCanceledListener {
                Log.i(App.TAG, "firebase 취소")
            }
            .addOnFailureListener {
                it.printStackTrace()
                Log.i(App.TAG, "firebase 에러")
            }
    }

    fun getAuthResult(
        resultTask: Task<AuthResult>,
        tokenCallback: ((String) -> Unit)? = null,
        errorCallback: (() -> Unit)? = null
    ) {
        resultTask.addOnCompleteListener {
            if (it.isSuccessful) {
                val token = it.result.user?.getIdToken(false)?.result?.token
                if (token != null) {
                    tokenCallback?.invoke(token)
                } else {
                    errorCallback?.invoke()
                }
            } else {
                Log.i(App.TAG, "firebase 실패")
                errorCallback?.invoke()
            }
        }
            .addOnCanceledListener {
                Log.i(App.TAG, "firebase 취소")
                errorCallback?.invoke()
            }
            .addOnFailureListener {
                it.printStackTrace()
                Log.i(App.TAG, "firebase 에러")
                errorCallback?.invoke()
            }
    }

    fun customAuthByFirebaseFunctions(
        functions: FirebaseFunctions,
        accessToken: String?,
        type: String,
        tokenCallback: ((String) -> Unit)
    ) {
        if (accessToken != null) {
            val resultTask = functions.getHttpsCallable(type)
                .call(accessToken)
                .continueWith { task ->
                    @Suppress("UNCHECKED_CAST")
                    val result = task.result?.data as Map<String, String>
                    result
                }
            getAuthMapResult(resultTask, tokenCallback)
        }
    }
}