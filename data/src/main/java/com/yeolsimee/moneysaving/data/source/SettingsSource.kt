package com.yeolsimee.moneysaving.data.source

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.yeolsimee.moneysaving.data.data_store.DataStoreService
import com.yeolsimee.moneysaving.domain.entity.preference.SettingPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class SettingsSource(private val dataStoreService: DataStoreService) {

    private object PreferencesKeys {
        val ALARM_STATE = booleanPreferencesKey("alarm_state")
    }

    fun getAlarmState() = dataStoreService.getDataStore().data.map { preferences ->
        val alarmState = preferences[PreferencesKeys.ALARM_STATE] ?: false
        SettingPreferences(alarmState = alarmState)
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
}