package com.yeolsimee.moneysaving.auth

import android.app.Activity
import android.util.Log
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.yeolsimee.moneysaving.App

object Apple {
    fun login(activity: Activity) {
        val provider = OAuthProvider.newBuilder("apple.com")
        provider.addCustomParameter("locale", "ko_KR")
        Firebase.auth.startActivityForSignInWithProvider(activity, provider.build())
            .addOnSuccessListener { authResult ->
                if (authResult.credential != null) {
                    val task = Firebase.auth.signInWithCredential(authResult.credential!!)
                    AuthFunctions.getAuthResult(task)
                } else {
                    Log.e(App.TAG, "로그인 정보 못 가져옴")
                }
            }
    }
}