package com.yeolsimee.moneysaving.view.routine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yeolsimee.moneysaving.domain.entity.routine.RoutineResponse
import com.yeolsimee.moneysaving.domain.usecase.RoutineUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetRoutineViewModel @Inject constructor(private val routineUseCase: RoutineUseCase) :
    ViewModel() {

    fun getRoutine(
        routineId: String,
        onResult: (RoutineResponse) -> Unit
    ) {
        viewModelScope.launch {
            val result = routineUseCase.getRoutine(routineId)
            result.onSuccess {
                onResult(it)
            }.onFailure {
                onResult(RoutineResponse())
            }
        }
    }
}