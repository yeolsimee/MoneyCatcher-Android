package com.yeolsimee.moneysaving.view.routine

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yeolsimee.moneysaving.domain.calendar.AmPmTime
import com.yeolsimee.moneysaving.domain.calendar.getCurrentTime
import com.yeolsimee.moneysaving.domain.entity.routine.RoutineRequest
import com.yeolsimee.moneysaving.domain.entity.routine.RoutineResponse
import com.yeolsimee.moneysaving.ui.appbar.BottomButtonAppBar
import com.yeolsimee.moneysaving.ui.appbar.TopBackButtonTitleAppBar
import com.yeolsimee.moneysaving.ui.dialog.TimePickerDialog
import com.yeolsimee.moneysaving.ui.dialog.TwoButtonOneTitleDialog
import com.yeolsimee.moneysaving.ui.theme.Gray99
import com.yeolsimee.moneysaving.ui.theme.RoumoTheme
import com.yeolsimee.moneysaving.utils.VerticalSpacer
import com.yeolsimee.moneysaving.utils.addFocusCleaner
import com.yeolsimee.moneysaving.utils.getTwoDigits
import com.yeolsimee.moneysaving.utils.getWeekTypeCheckList
import com.yeolsimee.moneysaving.utils.getWeekTypes
import com.yeolsimee.moneysaving.view.category.CategoryGridView
import com.yeolsimee.moneysaving.view.category.CategoryViewModel

@Composable
fun RoutineScreen(
    routine: RoutineResponse = RoutineResponse(),
    routineType: RoutineType? = RoutineType.Update,
    categoryViewModel: CategoryViewModel = hiltViewModel(),
    selectedCategoryId: MutableState<String> = remember { mutableStateOf("") },
    closeCallback: () -> Unit = {},
    onCompleteCallback: (req: RoutineRequest, hasWeekTypes: Boolean, routine: RoutineResponse) -> Unit = { _, _, _ -> },
    toggleRoutineAlarm: (MutableState<Boolean>, MutableState<Boolean>) -> Unit = { _, _ -> },
    onCheckNotificationSetting: () -> Unit = {},
) {
    val notificationCheckDialogState = remember { mutableStateOf(false) }

    val buttonState = remember { mutableStateOf(false) }
    val timePickerDialogState = remember { mutableStateOf(false) }

    val categoryList = categoryViewModel.container.stateFlow.collectAsState().value

    RoumoTheme(navigationBarColor = if (buttonState.value) Color.Black else Gray99) {
        val focusRequester by remember { mutableStateOf(FocusRequester()) }
        val focusManager = LocalFocusManager.current
        val scrollState = rememberScrollState()

        val nameState = remember { mutableStateOf(routine.routineName) }

        if (categoryList.isNotEmpty() && selectedCategoryId.value.isEmpty()) {
            selectedCategoryId.value = categoryList.first().id
        }

        val repeatSelectList = getRoutineRepeatList(routine)
        val selectedRoutineTimeZoneId =
            remember { mutableStateOf(routine.routineTimeZone) }

        val alarmState = remember { mutableStateOf(routine.alarmStatus == "ON") }
        val now = getCurrentTime()

        val hourState: MutableState<Int> = remember {
            mutableStateOf(
                if (alarmState.value) routine.alarmTime.substring(0, 2).toInt() else now.first
            )
        }
        val minuteState: MutableState<Int> = remember {
            mutableStateOf(
                if (alarmState.value) routine.alarmTime.substring(2, 4).toInt() else now.second
            )
        }

        val addCategoryState = remember { mutableStateOf(false) }

        Scaffold(
            topBar = {
                TopBackButtonTitleAppBar(routineType?.title) { closeCallback() }
            },
            bottomBar = {
                buttonState.value = canSaveRoutine(nameState.value, selectedCategoryId.value)

                BottomButtonAppBar(routineType?.title, buttonState.value) {
                    val weekTypes = getWeekTypes(repeatSelectList)
                    onCompleteCallback(
                        RoutineRequest(
                            alarmStatus = if (alarmState.value) "ON" else "OFF",
                            alarmTime = getAlarmTime(
                                alarmState.value,
                                hourState.value,
                                minuteState.value
                            ),
                            routineName = nameState.value,
                            categoryId = selectedCategoryId.value,
                            routineTimeZone = selectedRoutineTimeZoneId.value,
                            weekTypes = weekTypes,
                        ),
                        weekTypes.isNotEmpty(),
                        routine
                    )
                }
            },
            containerColor = Color.White
        ) {
            Box(
                Modifier
                    .padding(it)
                    .padding(horizontal = 28.dp)
                    .background(Color.White)
                    .addFocusCleaner(focusManager)
            ) {
                Column(Modifier.verticalScroll(scrollState)) {
                    InputRoutineName(nameState, focusRequester)
                    20.VerticalSpacer()
                    CategoryGridView(
                        categoryList,
                        selectedId = selectedCategoryId.value,
                        addCategoryState = addCategoryState,
                        selectCallback = { id ->
                            selectedCategoryId.value = id
                        },
                        addCallback = { categoryName ->
                            categoryViewModel.addCategory(categoryName)
                        }
                    )
                    20.VerticalSpacer()
                    SelectRoutineRepeat(repeatSelectList.toList()) { i ->
                        repeatSelectList[i] = !repeatSelectList[i]
                    }
                    20.VerticalSpacer()
                    SelectRoutineTimeZone(
                        selectedId = selectedRoutineTimeZoneId.value
                    ) { id ->
                        selectedRoutineTimeZoneId.value = id
                    }
                    20.VerticalSpacer()
                    SettingAlarmTime(
                        alarmState,
                        hourState,
                        minuteState,
                        toggleRoutineAlarm,
                        timePickerDialogState,
                        notificationCheckDialogState
                    )
                }
            }
        }
        if (notificationCheckDialogState.value) {
            TwoButtonOneTitleDialog(
                dialogState = notificationCheckDialogState,
                text = "알림 설정에서 \n" +
                        "알림 전체 OFF를 한 경우 \n" +
                        "알림을 받을 수 없습니다.",
                onConfirmClick = { onCheckNotificationSetting() }
            )
        }

        if (timePickerDialogState.value) {
            TimePickerDialog(
                dialogState = timePickerDialogState,
                hour = now.first,
                minute = now.second,
                ampm = now.third,
                onConfirmClick = { hour, minute, ampm ->
                    hourState.value = hour + if (ampm == AmPmTime.PM) 12 else 0
                    minuteState.value = minute
                }
            )
        }
    }
}

@Composable
private fun getRoutineRepeatList(initialData: RoutineResponse?) =

    remember {
        if (initialData != null) getWeekTypeCheckList(initialData.weekTypes)
        else (1..7).map { false }.toMutableList()
    }

@Composable
private fun canSaveRoutine(
    routineName: String,
    categoryId: String
) = routineName.isNotEmpty() && categoryId.isNotEmpty()

private fun getAlarmTime(
    alarmState: Boolean,
    hourState: Int,
    minuteState: Int
): String {
    return if (alarmState) {
        val hour = hourState.getTwoDigits()
        val minute = minuteState.getTwoDigits()
        "${hour}${minute}"
    } else {
        ""
    }
}

@ExperimentalLayoutApi
@Preview(showBackground = true)
@Composable
fun RoutineAddScreenPreview() {
    RoutineScreen(
        routineType = RoutineType.Add,
    )
}