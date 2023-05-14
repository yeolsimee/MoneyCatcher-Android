package com.yeolsimee.moneysaving.auth

import android.app.Activity
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.yeolsimee.moneysaving.App

object Apple {
    fun login(activity: Activity, loadingState: MutableLiveData<Boolean>, onSuccess: (Result<String>) -> Unit) {
        val provider = OAuthProvider.newBuilder("apple.com")
        provider.addCustomParameter("locale", "ko_KR")
        Firebase.auth.startActivityForSignInWithProvider(activity, provider.build())
            .addOnSuccessListener { authResult ->
                if (authResult.credential != null) {
                    val task = Firebase.auth.signInWithCredential(authResult.credential!!)
                    AuthFunctions.getAuthResult(task) {
                        onSuccess(it)
                    }
                } else {
                    Log.e(App.TAG, "로그인 정보 못 가져옴")
                }
            }.addOnFailureListener {
                loadingState.value = false
                Log.e(App.TAG, "로그인 취소")
            }
    }
}