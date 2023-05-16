package com.yeolsimee.moneysaving.view.routine

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yeolsimee.moneysaving.data.db.AlarmDao
import com.yeolsimee.moneysaving.data.entity.AlarmEntity
import com.yeolsimee.moneysaving.data.repository.SettingsRepository
import com.yeolsimee.moneysaving.utils.notification.RoutineAlarmManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor(private val alarmDao: AlarmDao, private val settingsRepository: SettingsRepository) : ViewModel() {

    fun addAlarm(alarmId: Int, dayOfWeek: Int, alarmTime: String, routineName: String) {
        alarmDao.insertAll(
            AlarmEntity(
                alarmId = alarmId,
                dayOfWeek = dayOfWeek,
                alarmTime = alarmTime,
                routineName = routineName
            )
        )
    }

    fun deleteAlarm(alarmId: Int) {
        alarmDao.delete(alarmId)
    }

    fun setAlarmOn() {
        viewModelScope.launch {
            settingsRepository.setAlarmOn().collect()
        }
    }

    fun setDailyAlarm(context: Context) {
        viewModelScope.launch {
            settingsRepository.toggleAlarmState().collect { changedAlarmState ->
                if (changedAlarmState) {
                    RoutineAlarmManager.setDailyNotification(context)
                } else {
                    RoutineAlarmManager.cancelDailyNotification(context)
                }
            }
        }
    }
}