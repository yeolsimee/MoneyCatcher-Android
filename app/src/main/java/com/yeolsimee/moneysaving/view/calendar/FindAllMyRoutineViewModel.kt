package com.yeolsimee.moneysaving.view.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yeolsimee.moneysaving.domain.calendar.CalendarDay
import com.yeolsimee.moneysaving.domain.calendar.DateIconState
import com.yeolsimee.moneysaving.domain.usecase.RoutineUseCase
import com.yeolsimee.moneysaving.view.ISideEffect
import com.yeolsimee.moneysaving.view.ToastSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class FindAllMyRoutineViewModel @Inject constructor(private val routineUseCase: RoutineUseCase) :
    ContainerHost<MutableList<DateIconState>, ToastSideEffect>, ISideEffect, ViewModel() {

    override val container = container<MutableList<DateIconState>, ToastSideEffect>(mutableListOf())

    override fun showSideEffect(message: String?) {
        intent {
            postSideEffect(ToastSideEffect.Toast(message ?: "Unknown Error Message"))
        }
    }

    fun find(dateRange: Pair<CalendarDay, CalendarDay>?, selectedMonth: Int) = intent {
        if (dateRange == null) {
            reduce { mutableListOf() }
            return@intent
        }
        val startDate = dateRange.first
        val endDate = dateRange.second
        viewModelScope.launch {
            val result = routineUseCase.findAllMyRoutineDays(startDate.toString(), endDate.toString(), selectedMonth)
            result.onSuccess {
                reduce { it }
            }.onFailure {
                reduce { mutableListOf() }
                showSideEffect(it.message)
            }
        }
    }
}