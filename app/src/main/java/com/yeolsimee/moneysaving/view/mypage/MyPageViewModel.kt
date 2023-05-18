package com.yeolsimee.moneysaving.view.mypage

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.yeolsimee.moneysaving.auth.Google
import com.yeolsimee.moneysaving.auth.Naver
import com.yeolsimee.moneysaving.data.db.AlarmDao
import com.yeolsimee.moneysaving.data.repository.SettingsRepository
import com.yeolsimee.moneysaving.domain.usecase.UserUseCase
import com.yeolsimee.moneysaving.utils.notification.RoutineAlarmManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    private val dao: AlarmDao,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val alarmState = MutableLiveData(false)

    init {
        getSettings()
    }

    private fun getSettings(changedAlarmState: Boolean? = null) {
        viewModelScope.launch {
            if (changedAlarmState != null) {
                alarmState.value = changedAlarmState
            } else {
                settingsRepository.getAlarmState {
                    alarmState.value = it
                }
            }
        }
    }

    fun changeAlarmState() {
        viewModelScope.launch {
            settingsRepository.toggleAlarmState().collect { changedAlarmState ->
                getSettings(changedAlarmState = changedAlarmState)
            }
        }
    }

    fun logoutAndCancelAlarms(activity: Activity, onSuccess: () -> Unit) {
        cancelAlarms(activity) {
            // 모든 API 통신 완료 후
            logout(activity)
            onSuccess()
        }
    }

    private fun logout(activity: Activity) {
        Firebase.auth.signOut()
        Naver.logout()
        Google(activity).logout()
    }

    fun withdraw(activity: Activity, onSuccess: () -> Unit) {
        cancelAlarms(activity) {
            userUseCase.withdraw().onSuccess { withdrawResult ->  // 3. 회원 탈퇴
                if (withdrawResult) {
                    logout(activity)  // 회원 탈퇴 후 로그아웃 과정까지 함께 수행한다.
                    onSuccess()
                }
            }
        }
    }

    private fun cancelAlarms(activity: Activity, onDelete: suspend CoroutineScope.() -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.deleteAll() // 로그아웃 시 알람 정보를 디바이스에서 지운다.
            // 등록되어 있는 해당 유저의 모든 알람 목록을 조회하고,
            userUseCase.getAlarmList().onSuccess { alarmList ->
                // 알람 서비스에서 해제한다.
                RoutineAlarmManager.cancelAll(activity, alarmList)
                onDelete()
            }
        }
    }
}