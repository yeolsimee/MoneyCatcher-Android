package com.yeolsimee.moneysaving.view.routine

import androidx.lifecycle.ViewModel
import com.yeolsimee.moneysaving.domain.entity.routine.RoutineRequest
import com.yeolsimee.moneysaving.domain.entity.routine.RoutineResponse
import com.yeolsimee.moneysaving.domain.usecase.RoutineUseCase
import com.yeolsimee.moneysaving.ui.side_effect.IToastSideEffect
import com.yeolsimee.moneysaving.ui.side_effect.ToastSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class RoutineModifyViewModel @Inject constructor(private val routineUseCase: RoutineUseCase) :
    ContainerHost<RoutineResponse, ToastSideEffect>, IToastSideEffect, ViewModel() {

    override val container = container<RoutineResponse, ToastSideEffect>(RoutineResponse())

    fun addRoutine(
        routineRequest: RoutineRequest,
        onSetAlarmCallback: (Int) -> Unit = {},
        onFinishCallback: (RoutineResponse) -> Unit = {}
    ) = intent {
        val result = routineUseCase.createRoutine(routineRequest)
        result.onSuccess {
            if (it.alarmStatus == "ON") {
                onSetAlarmCallback(it.routineId)
            }
            onFinishCallback(it)
        }.onFailure {
            showSideEffect(it.message)
        }
    }

    fun updateRoutine(
        routine: RoutineResponse,
        routineRequest: RoutineRequest,
        onSetAlarmCallback: (RoutineResponse) -> Unit = {},
        onDeleteAlarmCallback: (RoutineResponse) -> Unit = {},
        onFinishCallback: (RoutineResponse) -> Unit = {}
    ) = intent {
        val result = routineUseCase.updateRoutine(routine.routineId, routineRequest)
        result.onSuccess {
            if (it.alarmStatus == "ON") {
                onSetAlarmCallback(it)
            } else {
                onDeleteAlarmCallback(it)   // OFF 이면 무조건 삭제 동작
            }
            onFinishCallback(it)
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