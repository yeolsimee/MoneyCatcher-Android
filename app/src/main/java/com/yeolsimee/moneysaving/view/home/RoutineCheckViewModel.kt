package com.yeolsimee.moneysaving.view.home

import androidx.lifecycle.ViewModel
import com.yeolsimee.moneysaving.domain.entity.routine.RoutineCheckRequest
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
class RoutineCheckViewModel @Inject constructor(private val routineUseCase: RoutineUseCase) :
    ContainerHost<Any, ToastSideEffect>, ISideEffect, ViewModel() {

    override val container = container<Any, ToastSideEffect>("")

    fun check(routineCheckRequest: RoutineCheckRequest) = intent {
        val result = routineUseCase.routineCheck(routineCheckRequest)
        result.onSuccess {
            reduce { it }
        }.onFailure { showSideEffect(it.message) }
    }

    override fun showSideEffect(message: String?) {
        intent {
            reduce { "" }
            postSideEffect(ToastSideEffect.Toast(message ?: "Unknown Error Message"))
        }
    }
}