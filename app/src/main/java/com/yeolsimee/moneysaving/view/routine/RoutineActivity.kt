@file:OptIn(ExperimentalMaterial3Api::class)

package com.yeolsimee.moneysaving.view.routine

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.yeolsimee.moneysaving.domain.entity.routine.RoutineRequest
import com.yeolsimee.moneysaving.domain.entity.routine.RoutineResponse
import com.yeolsimee.moneysaving.ui.MyPageRouteCode
import com.yeolsimee.moneysaving.utils.collectAsStateWithLifecycleRemember
import com.yeolsimee.moneysaving.utils.hasNotificationPermission
import com.yeolsimee.moneysaving.utils.notification.RoutineAlarmManager
import com.yeolsimee.moneysaving.view.category.CategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@ExperimentalLayoutApi
@ExperimentalMaterial3Api
@AndroidEntryPoint
class RoutineActivity : ComponentActivity() {

    private val categoryViewModel: CategoryViewModel by viewModels()
    private val routineViewModel: RoutineModifyViewModel by viewModels()
    private val alarmViewModel: AlarmViewModel by viewModels()
    private val getRoutineViewModel: GetRoutineViewModel by viewModels()

    private val hasNotificationPermission = MutableStateFlow<Boolean?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val routineType = getRoutineType()
        val routineId = intent.getIntExtra("routineId", 0)
        val categoryId = intent.getStringExtra("categoryId") ?: ""

        if (routineType == RoutineModifyOption.Update) {
            getRoutineViewModel.getRoutine(routineId)
        }

        setContent {
            val selectedCategoryId = remember { mutableStateOf(categoryId) }
            val routine =
                getRoutineViewModel.container.stateFlow.collectAsStateWithLifecycleRemember(
                    initial = RoutineResponse()
                ).value
            val notificationCheckDialogState = remember { mutableStateOf(false) }

            if (routine.isEmpty() && routineType == RoutineModifyOption.Update) {
                RoutineScreen()
            } else {
                RoutineScreen(
                    routine = routine,
                    routineType = routineType,
                    categoryList = categoryViewModel.container.stateFlow.collectAsState().value,
                    selectedCategoryId = selectedCategoryId,
                    notificationCheckDialogState = notificationCheckDialogState,
                    closeCallback = {
                        finish()
                    },
                    onCompleteCallback = { req, hasWeekTypes ->
                        if (routineType == RoutineModifyOption.Add) {
                            routineViewModel.addRoutine(
                                routineRequest = req,
                                onSetAlarmCallback = { id ->
                                    setRoutineAlarm(hasWeekTypes, req, id)
                                },
                                onFinishCallback = {
                                    sendResultAndFinish(RESULT_OK)
                                }
                            )
                        } else {
                            routineViewModel.updateRoutine(
                                routine = routine,
                                routineRequest = req,
                                onSetAlarmCallback = { routine ->
                                    alarmViewModel.updateCheckedRoutine(
                                        routine.alarmTime,
                                        req.alarmTime
                                    )
                                    setRoutineAlarm(hasWeekTypes, req, routine.routineId)
                                },
                                onDeleteAlarmCallback = { res ->
                                    RoutineAlarmManager.delete(this@RoutineActivity, res) {
                                        alarmViewModel.deleteAlarm(it)
                                    }
                                },
                                onFinishCallback = {
                                    sendResultAndFinish(RESULT_OK)
                                }
                            )
                        }
                    },
                    toggleRoutineAlarm = { alarmState ->
                        setAlarmOnIfHasPermission(alarmState, notificationCheckDialogState)
                    },
                    onCategoryAdded = {
                        categoryViewModel.addCategory(it)
                    },
                    onCheckNotificationSetting = {
                        sendResultAndFinish(MyPageRouteCode)
                    }
                )
            }
        }
    }

    private fun setRoutineAlarm(
        hasWeekTypes: Boolean,
        req: RoutineRequest,
        id: Int,
    ) {
        if (hasWeekTypes) {
            RoutineAlarmManager.setRoutine(
                context = this@RoutineActivity,
                dayOfWeeks = req.weekTypes,
                alarmTime = req.alarmTime,
                routineId = id,
                routineName = req.routineName
            ) { alarmId, dayOfWeek ->
                alarmViewModel.addAlarm(
                    alarmId,
                    dayOfWeek,
                    req.alarmTime,
                    req.routineName
                )
            }
        } else {
            RoutineAlarmManager.setToday(
                context = this@RoutineActivity,
                alarmTime = req.alarmTime,
                routineId = id,
                routineName = req.routineName
            )
        }
    }

    private fun setAlarmOnIfHasPermission(
        alarmState: MutableState<Boolean>,
        notificationCheckDialogState: MutableState<Boolean>,
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            hasNotificationPermission.collect {
                if (hasNotificationPermission(requestPermissionLauncher)) {
                    alarmViewModel.getAlarmState {
                        if (it) {
                            alarmState.value = !alarmState.value
                            if (alarmState.value) alarmViewModel.setAlarmOn()
                        } else {
                            notificationCheckDialogState.value = true
                        }
                    }
                }
            }
        }
    }

    private fun sendResultAndFinish(code: Int) {
        setResult(code)
        finish()
    }

    private fun getRoutineType() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        intent.getSerializableExtra("routineType", RoutineModifyOption::class.java)
    } else {
        @Suppress("DEPRECATION")
        intent.getSerializableExtra("routineType") as RoutineModifyOption
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        hasNotificationPermission.value = isGranted
    }

}