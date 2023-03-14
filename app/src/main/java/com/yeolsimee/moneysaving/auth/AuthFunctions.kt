package com.yeolsimee.moneysaving.auth

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.functions.FirebaseFunctions
import com.yeolsimee.moneysaving.App

object AuthFunctions {

    private fun getAuthResult(resultTask: Task<Map<String, String>>) {
        resultTask.addOnCompleteListener {
            if (it.isSuccessful) {
                val token = it.result["firebase_token"]
                Log.i(App.TAG, "firebase 성공: $token")

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

    fun getAuthResult(resultTask: Task<AuthResult>) {
        resultTask.addOnCompleteListener {
            if (it.isSuccessful) {
                val token = it.result.user?.getIdToken(false)?.result?.token
                Log.i(App.TAG, "firebase 성공: $token")
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

    fun customAuthByFirebaseFunctions(
        functions: FirebaseFunctions,
        accessToken: String?,
        type: String
    ) {
        if (accessToken != null) {
            val resultTask = functions.getHttpsCallable(type)
                .call(accessToken)
                .continueWith { task ->
                    @Suppress("UNCHECKED_CAST")
                    val result = task.result?.data as Map<String, String>
                    result
                }
            getAuthResult(resultTask)
        }
    }
}