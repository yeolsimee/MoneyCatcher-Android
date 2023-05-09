package com.yeolsimee.moneysaving.view.mypage

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    private val dao: AlarmDao,
    settingsRepository: SettingsRepository
) : ViewModel() {

    val settings = settingsRepository.getAlarmState().asLiveData()

    fun logout(activity: Activity) {
        dao.deleteAll()
        Firebase.auth.signOut()
        Naver.logout()
        Google(activity).logout()
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
}