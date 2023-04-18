package com.yeolsimee.moneysaving.view.login

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.ExperimentalLayoutApi
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

@ExperimentalLayoutApi
@ExperimentalMaterial3Api
@HiltViewModel
class LoginViewModel @Inject constructor(private val userUseCase: UserUseCase) : ViewModel() {
    private lateinit var google: Google

    fun init(activity: LoginActivity, callback: () -> Unit) {
        google = Google(activity) { token ->
            loginUsingToken(callback)
        }
    }

    private fun showLoginFailed(message: String?) {
        Log.e(App.TAG, "login failed: $message")
    }

    fun naverInit(result: ActivityResult, callback: () -> Unit) {
        Naver.init(result, tokenCallback = { token ->
            loginUsingToken(callback)
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

    fun appleLogin(loginActivity: LoginActivity, callback: () -> Unit) {
        Apple.login(loginActivity) { token ->
            loginUsingToken(callback)
        }
    }

    private fun loginUsingToken(callback: () -> Unit) {
        viewModelScope.launch {
            userUseCase.login().onSuccess {
                showLoginSuccess(it)
                callback()
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

    fun receiveEmailResult(intent: Intent, activity: LoginActivity, callback: () -> Unit) {
        // 1. Email SignIn
        Email.receive(intent, activity)

        // 2. 1번 결과 처리
        Firebase.auth.pendingAuthResult?.addOnSuccessListener { authResult ->
            if (authResult.credential != null) {
                val task = Firebase.auth.signInWithCredential(authResult.credential!!)
                AuthFunctions.getAuthResult(task, tokenCallback = { token ->
                    viewModelScope.launch {
                        userUseCase.login().onSuccess {
                            showLoginSuccess(it)
                            callback()
                        }.onFailure {
                            showLoginFailed(it.message)
                        }
                    }
                })
            }
        }
    }

    private fun showLoginSuccess(result: LoginResult) {
        Log.i(App.TAG, "로그인 성공: ${result.name} 가입여부: ${result.isNewUser}")
    }

    fun logout() {
        Firebase.auth.signOut()
        Naver.logout()
        google.logout()
    }

    fun autoLogin(callback: () -> Unit) {
        val user = Firebase.auth.currentUser
        if (user != null && user.uid.isNotEmpty()) {
            user.getIdToken(false).addOnSuccessListener {
                val token = it.token
                if (token != null) {
                    loginUsingToken(callback)
                }
            }
        }
    }
}