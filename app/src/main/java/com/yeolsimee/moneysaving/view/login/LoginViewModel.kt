package com.yeolsimee.moneysaving.view.login

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
import com.yeolsimee.moneysaving.auth.Apple
import com.yeolsimee.moneysaving.auth.Google
import com.yeolsimee.moneysaving.domain.usecase.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalMaterial3Api
@HiltViewModel
class LoginViewModel @Inject constructor(private val userUseCase: UserUseCase) : ViewModel() {
    private lateinit var google: Google

    @OptIn(ExperimentalMaterial3Api::class)
    fun init(activity: LoginActivity) {
        google = Google(activity) { token ->
            viewModelScope.launch {
                val result = userUseCase.login(token)
                Log.i(App.TAG, result.toString())
            }
        }
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
            viewModelScope.launch {
                val result = userUseCase.login(token)
                Log.i(App.TAG, result.toString())
            }
        }
    }
}