package com.yeolsimee.moneysaving.data.repository

import com.yeolsimee.moneysaving.data.source.SettingsSource
import com.yeolsimee.moneysaving.domain.entity.preference.SettingPreferences
import kotlinx.coroutines.flow.Flow

class SettingsRepository(
    private val settingsSource: SettingsSource
) {
    fun getAlarmState(): Flow<SettingPreferences> = settingsSource.getAlarmState()
}