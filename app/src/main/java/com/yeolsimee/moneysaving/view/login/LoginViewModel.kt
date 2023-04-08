package com.yeolsimee.moneysaving.view.login

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.yeolsimee.moneysaving.App
import com.yeolsimee.moneysaving.auth.*
import com.yeolsimee.moneysaving.domain.entity.LoginResult
import com.yeolsimee.moneysaving.domain.usecase.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalMaterial3Api
@HiltViewModel
class LoginViewModel @Inject constructor(private val userUseCase: UserUseCase) : ViewModel() {
    private lateinit var google: Google

    fun init(activity: LoginActivity) {
        google = Google(activity) { token ->
            loginUsingToken(token)
        }
    }

    private fun showLoginFailed(message: String?) {
        Log.e(App.TAG, "login failed: $message")
    }

    fun naverInit(result: ActivityResult) {
        Naver.init(result, tokenCallback = { token ->
            loginUsingToken(token)
        }, failedCallback = {
            showLoginFailed(it)
        })
    }

    fun googleInit(it: ActivityResult) {
        google.init(it)
    }

    fun googleLogin(launcher: ActivityResultLauncher<Intent>) {
        google.login(launcher)
    }

    fun googleLogout() {
        Firebase.auth.signOut()
        google.logout()
    }

    fun appleLogin(loginActivity: LoginActivity) {
        Apple.login(loginActivity) { token ->
            loginUsingToken(token)
        }
    }

    private fun loginUsingToken(token: String) {
        viewModelScope.launch {
            userUseCase.login(token).onSuccess {
                showLoginSuccess(it)
            }.onFailure {
                showLoginFailed(it.message)
            }
        }
    }

    fun naverLogin(
        applicationContext: Context,
        naverLoginLauncher: ActivityResultLauncher<Intent>
    ) {
        Naver.login(applicationContext, naverLoginLauncher)
    }

    fun naverLogout() {
        Naver.logout()
        Firebase.auth.signOut()
    }

    fun kakaoLogin(loginActivity: LoginActivity) {
        Kakao.login(loginActivity) { token ->
            loginUsingToken(token)
        }
    }

    fun kakaoLogout() {
        Kakao.logout()
        Firebase.auth.signOut()
    }

    fun receiveEmailResult(intent: Intent, activity: LoginActivity) {
        Email.receive(intent, activity)
        Firebase.auth.pendingAuthResult?.addOnSuccessListener { authResult ->
            if (authResult.credential != null) {
                val task = Firebase.auth.signInWithCredential(authResult.credential!!)
                AuthFunctions.getAuthResult(task)
            }
        }
    }

    private fun showLoginSuccess(result: LoginResult) {
        Log.i(App.TAG, result.toString())
    }
}