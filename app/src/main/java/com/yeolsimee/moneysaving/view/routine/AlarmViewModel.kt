package com.yeolsimee.moneysaving.view.routine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yeolsimee.moneysaving.data.db.AlarmDao
import com.yeolsimee.moneysaving.data.entity.AlarmEntity
import com.yeolsimee.moneysaving.data.repository.SettingsRepository
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

    fun getAlarmState(callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            settingsRepository.getAlarmState {
                callback(it)
            }
        }
    }

    fun setAlarmOn() {
        viewModelScope.launch {
            settingsRepository.setAlarmOn().collect()
        }
    }

    fun updateCheckedRoutine(beforeAlarmTime: String, afterAlarmTime: String) {
        viewModelScope.launch {
            settingsRepository.updateCheckRoutineAlarm(beforeAlarmTime, afterAlarmTime)
        }
    }
}