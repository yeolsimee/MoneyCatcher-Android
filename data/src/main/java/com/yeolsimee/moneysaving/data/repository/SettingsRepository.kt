package com.yeolsimee.moneysaving.data.repository

import com.yeolsimee.moneysaving.data.source.SettingsSource
import com.yeolsimee.moneysaving.domain.entity.routine.Routine

class SettingsRepository(
    private val settingsSource: SettingsSource
) {
    suspend fun getAlarmState(callback: (Boolean) -> Unit) = settingsSource.getAlarmState(callback)
    fun toggleAlarmState() = settingsSource.toggleAlarmState()
    fun setAlarmOn() = settingsSource.setAlarmOn()
    suspend fun setAlarmOff(callback: (changedState: Boolean) -> Unit) =
        settingsSource.setAlarmOff(callback)

    fun setAlarmOffOnce(routine: Routine) = settingsSource.setAlarmOffOnce(routine)
    fun setAlarmOn(routine: Routine) = settingsSource.setAlarmOn(routine)

    fun getUnCheckedRoutine(alarmTime: String, callback: (Boolean) -> Unit) =
        settingsSource.getUnCheckedRoutine(alarmTime, callback)

    fun updateCheckRoutineAlarm(beforeAlarmTime: String, afterAlarmTime: String) =
        settingsSource.updateCheckRoutineAlarm(beforeAlarmTime, afterAlarmTime)

    suspend fun isFirstInstall(callback: (Boolean) -> Unit) =
        settingsSource.isFirstInstall(callback)
}