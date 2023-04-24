package com.yeolsimee.moneysaving.view.routine

import androidx.lifecycle.ViewModel
import com.yeolsimee.moneysaving.domain.entity.routine.RoutineRequest
import com.yeolsimee.moneysaving.domain.entity.routine.RoutineResponse
import com.yeolsimee.moneysaving.domain.usecase.RoutineUseCase
import com.yeolsimee.moneysaving.view.ISideEffect
import com.yeolsimee.moneysaving.view.ToastSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class RoutineModifyViewModel @Inject constructor(private val routineUseCase: RoutineUseCase) :
    ContainerHost<RoutineResponse, ToastSideEffect>, ISideEffect, ViewModel() {

    override val container = container<RoutineResponse, ToastSideEffect>(RoutineResponse())

    fun addRoutine(routineRequest: RoutineRequest, onSetAlarmCallback: (Int) -> Unit = {}, onFinishCallback: () -> Unit = {}) = intent {
        val result = routineUseCase.createRoutine(routineRequest)
        result.onSuccess {
            if (it.alarmStatus == "ON") {
                onSetAlarmCallback(it.routineId)
            }
            onFinishCallback()
            reduce { it }
        }.onFailure {
            showSideEffect(it.message)
        }
    }

    override fun showSideEffect(message: String?) {
        intent {
            reduce { RoutineResponse() }
            postSideEffect(ToastSideEffect.Toast(message ?: "Unknown Error Message"))
        }
    }
}