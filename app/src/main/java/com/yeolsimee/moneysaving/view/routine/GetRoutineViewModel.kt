package com.yeolsimee.moneysaving.view.routine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yeolsimee.moneysaving.domain.entity.routine.RoutineResponse
import com.yeolsimee.moneysaving.domain.usecase.RoutineUseCase
import com.yeolsimee.moneysaving.ui.side_effect.ApiCallSideEffect
import com.yeolsimee.moneysaving.ui.side_effect.IApiCallSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class GetRoutineViewModel @Inject constructor(private val routineUseCase: RoutineUseCase) :
    ContainerHost<RoutineResponse, ApiCallSideEffect>, IApiCallSideEffect, ViewModel() {

    override val container = container<RoutineResponse, ApiCallSideEffect>(RoutineResponse())

    fun getRoutine(
        routineId: Int
    ) = intent {
        viewModelScope.launch {
            showLoading()
            routineUseCase.getRoutine(routineId).onSuccess {
                reduce { it }
            }.onFailure {
                showEmpty()
            }
        }
    }

    override fun showLoading() {
        intent {
            postSideEffect(ApiCallSideEffect.Loading)
        }
    }

    override fun showEmpty() {
        intent {
            postSideEffect(ApiCallSideEffect.Empty)
        }
    }
}