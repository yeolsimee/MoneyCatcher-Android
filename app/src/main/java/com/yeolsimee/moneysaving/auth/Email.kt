package com.yeolsimee.moneysaving.auth

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.ktx.actionCodeSettings
import com.google.firebase.auth.ktx.auth
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import com.yeolsimee.moneysaving.App

object Email {
    fun send(email: String) {
        Firebase.auth.sendSignInLinkToEmail(email, actionCodeSettings {
            url = "https://moneysaving.page.link/Tbeh?email=$email"
            handleCodeInApp = true
            setIOSBundleId("com.yeolsimee.moneysave")
            setAndroidPackageName("com.yeolsimee.moneysaving", true, "3")
            dynamicLinkDomain = "moneysaving.page.link"
        })
            .addOnCompleteListener {
                Log.i(App.TAG, "이메일 전송됨")
            }
            .addOnFailureListener { Log.e(App.TAG, "$it") }
    }

    fun receive(intent: Intent?, activity: Activity) {
        Firebase.dynamicLinks.getDynamicLink(intent)
            .addOnSuccessListener(activity) {
                val deepLink: Uri? = it?.link
                if (deepLink != null) {
                    val isValid = Firebase.auth.isSignInWithEmailLink(deepLink.toString())

                    if (isValid) {
                        val continueUrl = deepLink.getQueryParameter("continueUrl") ?: ""
                        val email = Uri.parse(continueUrl).getQueryParameter("email") ?: ""
                        Firebase.auth.signInWithEmailLink(email, deepLink.toString())
                            .addOnSuccessListener { result ->
                                result.user?.getIdToken(true)?.addOnSuccessListener { authResult ->
                                    Log.i(
                                        App.TAG,
                                        "email login firebase token: ${authResult.token}"
                                    )
                                    Toast.makeText(
                                        activity,
                                        authResult.token,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    }
                }

            }
            .addOnFailureListener(activity) { e ->
                Log.e(App.TAG, "getDynamicLink:onFailure: $e")
            }
    }
}