package com.yeolsimee.moneysaving.view.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yeolsimee.moneysaving.domain.usecase.RoutineUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoutineDeleteViewModel @Inject constructor(private val routineUseCase: RoutineUseCase) : ViewModel() {

    fun delete(routineId: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            routineUseCase.deleteRoutine(routineId).onSuccess {
                onSuccess()
            }
        }
    }

}