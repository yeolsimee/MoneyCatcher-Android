package com.yeolsimee.moneysaving.data.repository

import com.yeolsimee.moneysaving.data.source.SettingsSource

class SettingsRepository(
    private val settingsSource: SettingsSource
) {
    fun getAlarmState() = settingsSource.getAlarmState()
    fun toggleAlarmState() = settingsSource.toggleAlarmState()
    fun setAlarmOn() = settingsSource.setAlarmOn()
}