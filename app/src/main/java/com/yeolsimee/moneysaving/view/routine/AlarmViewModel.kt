package com.yeolsimee.moneysaving.view.routine

import androidx.lifecycle.ViewModel
import com.yeolsimee.moneysaving.data.db.AlarmDao
import com.yeolsimee.moneysaving.data.entity.AlarmEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor(private val alarmDao: AlarmDao) : ViewModel() {

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

}