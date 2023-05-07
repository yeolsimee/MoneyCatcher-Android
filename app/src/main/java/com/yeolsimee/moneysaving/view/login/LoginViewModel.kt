package com.yeolsimee.moneysaving.view.login

import android.app.Activity
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
import com.yeolsimee.moneysaving.auth.Apple
import com.yeolsimee.moneysaving.auth.Google
import com.yeolsimee.moneysaving.auth.Naver
import com.yeolsimee.moneysaving.domain.usecase.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalLayoutApi
@ExperimentalMaterial3Api
@HiltViewModel
class LoginViewModel @Inject constructor(private val userUseCase: UserUseCase) : ViewModel() {
    private lateinit var google: Google

    fun init(activity: Activity, signedUserCallback: () -> Unit, newUserCallback: () -> Unit) {
        google = Google(activity) {
            loginUsingToken(signedUserCallback, newUserCallback)
        }
    }

    private fun showLoginFailed(message: String?) {
        Log.e(App.TAG, "login failed: $message")
    }

    fun naverInit(
        result: ActivityResult,
        signedUserCallback: () -> Unit,
        newUserCallback: () -> Unit
    ) {
        Naver.init(result, tokenCallback = {
            loginUsingToken(signedUserCallback, newUserCallback)
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

    fun appleLogin(
        loginActivity: LoginActivity,
        signedUserCallback: () -> Unit,
        newUserCallback: () -> Unit
    ) {
        Apple.login(loginActivity) {
            loginUsingToken(signedUserCallback, newUserCallback)
        }
    }

    private fun loginUsingToken(signedUserCallback: () -> Unit, newUserCallback: () -> Unit) {
        viewModelScope.launch {
            userUseCase.login().onSuccess {
                if (it.isNewUser == "Y") {
                    newUserCallback()
                } else {
                    signedUserCallback()
                }
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

    fun logout(activity: Activity) {
        Firebase.auth.signOut()
        Naver.logout()
        Google(activity).logout()
    }

    fun autoLogin(signedUserCallback: () -> Unit, newUserCallback: () -> Unit) {
        val user = Firebase.auth.currentUser
        if (user != null && user.uid.isNotEmpty()) {
            user.getIdToken(false).addOnSuccessListener {
                val token = it.token
                if (token != null) {
                    loginUsingToken(signedUserCallback, newUserCallback)
                }
            }
        }
    }

    fun withdraw(activity: Activity, onSuccess: () -> Unit) {

    }
}