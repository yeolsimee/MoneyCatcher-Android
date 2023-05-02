package com.yeolsimee.moneysaving.auth

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.ktx.actionCodeSettings
import com.google.firebase.auth.ktx.auth
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import com.yeolsimee.moneysaving.App
import java.lang.Exception

object Email {
    fun send(email: String, onComplete: () -> Unit = {}, onFailure: () -> Unit = {}, onError: () -> Unit = {}) {
        try {
            Firebase.auth.sendSignInLinkToEmail(email, actionCodeSettings {
                url = "https://moneysaving.page.link/Tbeh?email=$email"
                handleCodeInApp = true
                setIOSBundleId("com.yeolsimee.moneysave")
                setAndroidPackageName("com.yeolsimee.moneysaving", true, "3")
                dynamicLinkDomain = "moneysaving.page.link"
            }).addOnCompleteListener { onComplete() }.addOnFailureListener { onFailure() }
        } catch (e: Exception) {
            Log.e(App.TAG, e.message ?: "")
            onError()
        }
    }

    fun receive(intent: Intent?, activity: Activity, onFailure: () -> Unit, onComplete: (AuthResult) -> Unit) {
        Firebase.dynamicLinks.getDynamicLink(intent).addOnSuccessListener(activity) {
                val deepLink: Uri? = it?.link
                if (deepLink != null) {
                    val isValid = Firebase.auth.isSignInWithEmailLink(deepLink.toString())

                    if (isValid) {
                        val continueUrl = deepLink.getQueryParameter("continueUrl") ?: ""
                        val email = Uri.parse(continueUrl).getQueryParameter("email") ?: ""
                        Firebase.auth.signInWithEmailLink(email, deepLink.toString()).addOnCompleteListener { taskResult ->
                            if (taskResult.isSuccessful) {
                                onComplete(taskResult.result)
                            } else {
                                Log.e(App.TAG, taskResult.exception?.message ?: "")
                                onFailure()
                            }
                        }
                    }
                }
            }.addOnFailureListener(activity) { e ->
                Log.e(App.TAG, "getDynamicLink:onFailure: $e")
            }
    }
}