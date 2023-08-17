package com.yeolsimee.moneysaving.view.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yeolsimee.moneysaving.data.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoutineStateViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
): ViewModel() {
    fun getRoutineState() = settingsRepository.getRoutineViewState()
    fun setRoutineState(state: Boolean) {
        viewModelScope.launch {
            settingsRepository.setRoutineViewState(state)
        }
    }
}