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
import com.yeolsimee.moneysaving.data.db.AlarmDao
import com.yeolsimee.moneysaving.domain.usecase.UserUseCase
import com.yeolsimee.moneysaving.utils.notification.RoutineAlarmManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalLayoutApi
@ExperimentalMaterial3Api
@HiltViewModel
class LoginViewModel @Inject constructor(private val userUseCase: UserUseCase, private val dao: AlarmDao) : ViewModel() {
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
        dao.deleteAll()
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
        viewModelScope.launch {
            // TODO API 알림 목록 조회
//            userUseCase.getAlarmList().onSuccess { alarmList ->
            // TODO 알림 리스트 가져와 등록된 알람들 삭제
            RoutineAlarmManager.deleteAll(activity, listOf())
            userUseCase.withdraw().onSuccess { withdrawResult ->  // 회원 탈퇴
                if (withdrawResult) {
                    logout(activity)    // firebase logout
                    onSuccess() // move to login view
                }
            }
//            }
        }
    }

    fun updateRoutineAlarms(callback: () -> Unit) {
        /* TODO
            1. API 루틴 알림 리스트 조회
            2. 리스트로 받은 루틴 알림 모두 등록 (forEach: RoutineAlarmManager.setRoutine()
            3. 홈화면으로 이동
         */
        // userUseCase.getAlarmList().onSuccess { alarmList ->
        //
        //    callback()
        // }
        callback()
    }
}