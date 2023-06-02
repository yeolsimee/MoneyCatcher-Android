package com.yeolsimee.moneysaving.data.source

import com.yeolsimee.moneysaving.data.api.RoutineApiService
import com.yeolsimee.moneysaving.domain.entity.routine.RoutineCheckRequest
import com.yeolsimee.moneysaving.domain.entity.routine.RoutineRequest
import kotlinx.coroutines.flow.flow

class RoutineSource(
    private val api: RoutineApiService
) {
    fun findAllMyRoutineDays(
        startDate: String, endDate: String
    ) = flow {
        emit(api.findAllMyRoutineDays(startDate, endDate))
    }

    fun findRoutineDay(date: String) = flow {
        emit(api.findRoutineDay(date))
    }

    fun createRoutine(routineRequest: RoutineRequest) = flow {
        emit(api.createRoutine(routineRequest))
    }

    fun updateRoutine(
        routineId: Int, routineRequest: RoutineRequest
    ) = flow {
        emit(api.updateRoutine(routineId, routineRequest))
    }

    fun routineCheck(routineCheckRequest: RoutineCheckRequest) = flow {
        emit(api.routineCheck(routineCheckRequest))
    }

    fun deleteRoutine(routineId: Int) = flow {
        emit(api.deleteRoutine(routineId))
    }

    fun getRoutine(routineId: Int) = flow {
        emit(api.getRoutine(routineId))
    }

    fun getActivatedAlarmRoutine() = flow {
        emit(api.getActivatedAlarmRoutine())
    }
}