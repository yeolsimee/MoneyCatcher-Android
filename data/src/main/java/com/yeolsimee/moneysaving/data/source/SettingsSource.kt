package com.yeolsimee.moneysaving.data.source

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import com.yeolsimee.moneysaving.domain.entity.preference.SettingPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsSource {

    @Inject
    lateinit var dataStore: DataStore<Preferences>

    private object PreferencesKeys {
        val ALARM_STATE = booleanPreferencesKey("alarm_state")
    }

    fun getAlarmState(): Flow<SettingPreferences> = flow {
        dataStore.data.map { preferences ->
            val alarmState = preferences[PreferencesKeys.ALARM_STATE] ?: false
            SettingPreferences(alarmState = alarmState)
        }
    }
}