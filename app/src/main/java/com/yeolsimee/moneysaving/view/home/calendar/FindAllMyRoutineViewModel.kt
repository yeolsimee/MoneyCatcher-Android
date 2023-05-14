package com.yeolsimee.moneysaving.view.home.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yeolsimee.moneysaving.domain.calendar.CalendarDay
import com.yeolsimee.moneysaving.domain.usecase.RoutineUseCase
import com.yeolsimee.moneysaving.ui.side_effect.IToastSideEffect
import com.yeolsimee.moneysaving.ui.side_effect.ToastSideEffect
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
    ContainerHost<MutableList<CalendarDay>, ToastSideEffect>, IToastSideEffect, ViewModel() {

    override val container = container<MutableList<CalendarDay>, ToastSideEffect>(mutableListOf())

    private var startDate: CalendarDay? = null
    private var endDate: CalendarDay? = null
    private var month: Int = -1
    private var days: MutableList<CalendarDay> = mutableListOf()

    override fun showSideEffect(message: String?) {
        intent {
            reduce { mutableListOf() }
            postSideEffect(ToastSideEffect.Toast(message ?: "Unknown Error Message"))
        }
    }

    fun find(
        dateRange: Pair<CalendarDay, CalendarDay>?,
        selectedMonth: Int,
        dayList: MutableList<CalendarDay>
    ) = intent {
        if (dateRange == null) {
            reduce { mutableListOf() }
            return@intent
        }
        startDate = dateRange.first
        endDate = dateRange.second
        month = selectedMonth
        days = dayList

        viewModelScope.launch {
            val result =
                routineUseCase.findAllMyRoutineDays(startDate.toString(), endDate.toString(), month)
            result.onSuccess {
                reduce {
                    CalendarDay.getDayList(dayList, it)
                }
            }.onFailure {
                showSideEffect(it.message)
            }
        }
    }

    fun refresh(callback: () -> Unit) = intent {
        if (startDate != null && endDate != null && month != -1) {
            viewModelScope.launch {
                reduce { mutableListOf() }  // 리스트를 한번 비웠다가 다시 추가한다.

                val result = routineUseCase.findAllMyRoutineDays(
                    startDate.toString(), endDate.toString(),
                    month
                )
                result.onSuccess {
                    reduce {
                        CalendarDay.getDayList(days, it)
                    }
                    callback()
                }.onFailure {
                    showSideEffect(it.message)
                }
            }
        }
    }
}