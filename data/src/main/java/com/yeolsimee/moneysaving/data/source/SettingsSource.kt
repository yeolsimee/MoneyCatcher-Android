package com.yeolsimee.moneysaving.data.source

import android.util.Log
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.yeolsimee.moneysaving.data.data_store.DataStoreService
import com.yeolsimee.moneysaving.domain.entity.routine.Routine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SettingsSource(private val dataStoreService: DataStoreService) {

    private object PreferencesKeys {
        val ALARM_STATE = booleanPreferencesKey("alarm_state")
    }

    fun getAlarmState(callback: (Boolean) -> Unit) {
        dataStoreService.getDataStore().data.map { preferences ->
            callback(preferences[PreferencesKeys.ALARM_STATE] ?: false)
        }
    }


    fun toggleAlarmState(): Flow<Boolean> = flow {
        dataStoreService.getDataStore().edit { preferences ->
            val alarmState = preferences[PreferencesKeys.ALARM_STATE] ?: false
            preferences[PreferencesKeys.ALARM_STATE] = !alarmState
            emit(!alarmState)
        }
    }

    fun setAlarmOn(): Flow<Boolean> = flow {
        dataStoreService.getDataStore().edit { preferences ->
            preferences[PreferencesKeys.ALARM_STATE] = true
            emit(true)
        }
    }

    fun setAlarmOffOnce(routine: Routine) = flow {
        val time = routine.alarmTimeHour + routine.alarmTimeMinute
        dataStoreService.getDataStore().edit { preferences ->
            val current = preferences[booleanPreferencesKey(time)]
            Log.d("Alarm", "current: $current")
            preferences[booleanPreferencesKey(time)] = true
            emit(true)
        }
    }

    fun setAlarmOn(routine: Routine): Flow<Boolean> = flow {
        val time = routine.alarmTimeHour + routine.alarmTimeMinute
        dataStoreService.getDataStore().edit { preferences ->
            val current = preferences[booleanPreferencesKey(time)]
            Log.d("Alarm", "current: $current")
            preferences[booleanPreferencesKey(time)] = false
            emit(true)
        }
    }

    fun getUnCheckedRoutine(time: String, callback: (Boolean) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            dataStoreService.getDataStore().edit { preferences ->
                val hasKey = preferences[booleanPreferencesKey(time)]
                if (hasKey == true) {   // 한번 체크 되어 있으면 해제하기
                    preferences[booleanPreferencesKey(time)] = false
                }
                callback(hasKey != true)    // alarm이 켜져있지 않으면 true
            }
        }
    }

    fun updateCheckRoutineAlarm(beforeAlarmTime: String, afterAlarmTime: String) = flow {
        dataStoreService.getDataStore().edit { preferences ->
            val hasKey = preferences[booleanPreferencesKey(beforeAlarmTime)]
            if (hasKey == true) {   // 한번 체크 되어 있으면 새로운 알람 시간으로 교체하기
                preferences[booleanPreferencesKey(beforeAlarmTime)] = false
                preferences[booleanPreferencesKey(afterAlarmTime)] = true
            }
            emit(true)
        }
    }
}