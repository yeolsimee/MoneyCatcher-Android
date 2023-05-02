package com.yeolsimee.moneysaving.auth

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.yeolsimee.moneysaving.App
import com.yeolsimee.moneysaving.R

class Google(activity: Activity, private val tokenCallback: (String) -> Unit = {}) {

    private var googleSignInClient: GoogleSignInClient

    init {
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity.getString(R.string.web_client_id))
            .build()
        googleSignInClient = GoogleSignIn.getClient(activity, options)
    }

    fun init(result: ActivityResult) {

        if (result.resultCode == ComponentActivity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleSignInResult(task)
        } else {
            Log.e(App.TAG, "firebase 로그인 결과가 다름: ${result.resultCode}")
        }
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        val account = task.getResult(ApiException::class.java)

        if (account != null && account.idToken != null) {
            firebaseAuthWithGoogle(account.idToken!!)
        }
    }

    private fun firebaseAuthWithGoogle(googleToken: String) {
        val credential = GoogleAuthProvider.getCredential(googleToken, null)
        val task = Firebase.auth.signInWithCredential(credential)
        AuthFunctions.getAuthResult(task, tokenCallback = { token ->
            tokenCallback(token)
        })
    }

    fun login(googleLoginLauncher: ActivityResultLauncher<Intent>) {
        googleLoginLauncher.launch(googleSignInClient.signInIntent)
    }

    fun logout() {
        googleSignInClient.signOut()
    }
}