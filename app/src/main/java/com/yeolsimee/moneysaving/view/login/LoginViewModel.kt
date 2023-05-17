package com.yeolsimee.moneysaving.view.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.yeolsimee.moneysaving.auth.Apple
import com.yeolsimee.moneysaving.auth.Google
import com.yeolsimee.moneysaving.auth.Naver
import com.yeolsimee.moneysaving.data.db.AlarmDao
import com.yeolsimee.moneysaving.data.entity.AlarmEntity
import com.yeolsimee.moneysaving.domain.usecase.UserUseCase
import com.yeolsimee.moneysaving.utils.notification.RoutineAlarmManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalLayoutApi
@ExperimentalMaterial3Api
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    private val alarmDao: AlarmDao
) : ViewModel() {
    private lateinit var google: Google

    fun init(activity: Activity, signedUserCallback: () -> Unit, newUserCallback: () -> Unit, loginFailCallback: () -> Unit = {}) {
        google = Google(activity) {
            login(signedUserCallback, newUserCallback, loginFailCallback)
        }
    }

    private fun showLoginFailed() {
        logout()
    }

    fun naverInit(
        result: ActivityResult,
        signedUserCallback: () -> Unit,
        newUserCallback: () -> Unit,
        loginFailCallback: () -> Unit = {}
    ) {
        Naver.init(
            result = result,
            tokenCallback = {
                login(signedUserCallback, newUserCallback, loginFailCallback)
            }
        ) {
            showLoginFailed()
        }
    }

    fun googleInit(it: ActivityResult) {
        google.init(it)
    }

    fun googleLogin(launcher: ActivityResultLauncher<Intent>) {
        google.login(launcher)
    }

    fun appleLogin(
        loginActivity: LoginActivity,
        loadingState: MutableLiveData<Boolean>,
        signedUserCallback: () -> Unit,
        newUserCallback: () -> Unit,
        loginFailCallback: () -> Unit = {}
    ) {
        viewModelScope.launch {
            Apple.login(loginActivity, loadingState) {
                login(signedUserCallback, newUserCallback, loginFailCallback)
            }
        }
    }

    private fun login(signedUserCallback: () -> Unit, newUserCallback: () -> Unit, loginFailCallback: () -> Unit) {
        viewModelScope.launch {
            userUseCase.login().onSuccess {
                if (it.isNewUser == "Y") {
                    newUserCallback()
                } else {
                    signedUserCallback()
                }
            }.onFailure {
                logout()
                loginFailCallback()
            }
        }
    }

    fun naverLogin(
        applicationContext: Context,
        naverLoginLauncher: ActivityResultLauncher<Intent>
    ): Boolean {
        return Naver.login(applicationContext, naverLoginLauncher)
    }

    fun autoLogin(signedUserCallback: () -> Unit, newUserCallback: () -> Unit, notLoggedInCallback: () -> Unit) {
        val user = Firebase.auth.currentUser
        if (user != null && user.uid.isNotEmpty()) {
            user.getIdToken(false).addOnSuccessListener {
                val token = it.token
                if (token != null) {
                    login(signedUserCallback, newUserCallback, notLoggedInCallback)
                }
            }
        } else {
            notLoggedInCallback()
        }
    }

    fun updateRoutineAlarms(context: Context, callback: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            // 1. 활성화된 알람 목록 서버에서 불러오기
            userUseCase.getAlarmList().onSuccess { alarmList ->
                // 2. 알람 서비스 재등록
                RoutineAlarmManager.addAll(context, alarmList) { alarmId, dayOfWeek, alarmTime, routineName ->
                    // 3. 알람 정보 디바이스에 기록
                    alarmDao.insertAll(
                        AlarmEntity(
                            alarmId = alarmId,
                            dayOfWeek = dayOfWeek,
                            alarmTime = alarmTime,
                            routineName = routineName
                        )
                    )
                }
                callback()
            }
        }
    }

    fun logout() {
        Firebase.auth.signOut()
        Naver.logout()
        google.logout()
    }
}