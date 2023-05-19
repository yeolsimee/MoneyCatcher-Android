@file:OptIn(ExperimentalMaterial3Api::class)

package com.yeolsimee.moneysaving.view.routine

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yeolsimee.moneysaving.domain.entity.category.TextItem
import com.yeolsimee.moneysaving.domain.entity.routine.RoutineRequest
import com.yeolsimee.moneysaving.domain.entity.routine.RoutineResponse
import com.yeolsimee.moneysaving.ui.appbar.BottomButtonAppBar
import com.yeolsimee.moneysaving.ui.appbar.TopBackButtonTitleAppBar
import com.yeolsimee.moneysaving.ui.dialog.TwoButtonOneTitleDialog
import com.yeolsimee.moneysaving.ui.theme.Gray99
import com.yeolsimee.moneysaving.ui.theme.RoumoTheme
import com.yeolsimee.moneysaving.utils.addFocusCleaner
import com.yeolsimee.moneysaving.utils.getTwoDigits
import com.yeolsimee.moneysaving.utils.getWeekTypeCheckList
import com.yeolsimee.moneysaving.utils.getWeekTypes
import com.yeolsimee.moneysaving.view.category.CategoryGridView

@ExperimentalMaterial3Api
@ExperimentalLayoutApi
@Composable
fun RoutineScreen(
    routine: RoutineResponse = RoutineResponse(),
    routineType: RoutineModifyOption? = RoutineModifyOption.Update,
    categoryList: MutableList<TextItem> = mutableListOf(),
    selectedCategoryId: MutableState<String> = remember { mutableStateOf("") },
    notificationCheckDialogState: MutableState<Boolean> = remember { mutableStateOf(false) },
    closeCallback: () -> Unit = {},
    onCompleteCallback: (req: RoutineRequest, hasWeekTypes: Boolean) -> Unit = { _, _ -> },
    toggleRoutineAlarm: (MutableState<Boolean>) -> Unit = {},
    onCategoryAdded: (String) -> Unit = {},
    onCheckNotificationSetting: () -> Unit = {}
) {
    val buttonState = remember { mutableStateOf(false) }

    RoumoTheme(navigationBarColor = if (buttonState.value) Color.Black else Gray99) {
        val focusRequester by remember { mutableStateOf(FocusRequester()) }
        val focusManager = LocalFocusManager.current
        val scrollState = rememberScrollState()

        val routineName = remember { mutableStateOf(routine.routineName) }

        if (categoryList.isNotEmpty() && selectedCategoryId.value.isEmpty()) {
            selectedCategoryId.value = categoryList.first().id
        }

        val repeatSelectList = getRoutineRepeatList(routine)
        val selectedRoutineTimeZoneId =
            remember { mutableStateOf(routine.routineTimeZone) }

        val alarmState = remember { mutableStateOf(routine.alarmStatus == "ON") }
        val hourState = remember {
            mutableStateOf(
                if (alarmState.value) routine.alarmTime.substring(0, 2).toInt() else 13
            )
        }
        val minuteState = remember {
            mutableStateOf(
                if (alarmState.value) routine.alarmTime.substring(2, 4).toInt() else 0
            )
        }
        val addCategoryState = remember { mutableStateOf(false) }

        Scaffold(
            topBar = {
                TopBackButtonTitleAppBar(routineType?.title) { closeCallback() }
            },
            bottomBar = {
                buttonState.value = canSaveRoutine(routineName.value, selectedCategoryId)

                BottomButtonAppBar(routineType?.title, buttonState.value) {
                    val weekTypes = getWeekTypes(repeatSelectList)
                    onCompleteCallback(
                        RoutineRequest(
                            alarmStatus = if (alarmState.value) "ON" else "OFF",
                            alarmTime = getAlarmTime(alarmState, hourState, minuteState),
                            routineName = routineName.value,
                            categoryId = selectedCategoryId.value,
                            routineTimeZone = selectedRoutineTimeZoneId.value,
                            weekTypes = weekTypes,
                        ),
                        weekTypes.isNotEmpty()
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
                    InputRoutineName(routineName, focusRequester)
                    Spacer(Modifier.height(20.dp))
                    CategoryGridView(
                        categoryList,
                        selectedId = selectedCategoryId,
                        addCategoryState = addCategoryState,
                        selectCallback = { id ->
                            selectedCategoryId.value = id
                        },
                        addCallback = { categoryName ->
                            onCategoryAdded(categoryName)
                        }
                    )
                    Spacer(Modifier.height(20.dp))
                    SelectRoutineRepeat(repeatSelectList.toList()) { i ->
                        repeatSelectList[i] = !repeatSelectList[i]
                    }
                    Spacer(Modifier.height(20.dp))
                    SelectRoutineTimeZone(
                        selectedId = selectedRoutineTimeZoneId
                    ) { id ->
                        selectedRoutineTimeZoneId.value = id
                    }
                    Spacer(Modifier.height(20.dp))
                    SettingAlarmTime(alarmState, hourState, minuteState, toggleRoutineAlarm)
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
    selectedCategoryId: MutableState<String>
) = routineName.isNotEmpty() && selectedCategoryId.value.isNotEmpty()

private fun getAlarmTime(
    alarmState: MutableState<Boolean>,
    hourState: MutableState<Int>,
    minuteState: MutableState<Int>
): String {
    return if (alarmState.value) {
        val hour = hourState.value.getTwoDigits()
        val minute = minuteState.value.getTwoDigits()
        "${hour}${minute}"
    } else {
        ""
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalLayoutApi
@Preview(showBackground = true)
@Composable
fun RoutineAddScreenPreview() {
    RoutineScreen(
        routineType = RoutineModifyOption.Add,
        categoryList = remember {
            mutableListOf(
                TextItem("1", "💰아껴쓰기"),
                TextItem("2", "주린이 성장일기"),
                TextItem("3", "임티는 사용자 자유"),
                TextItem("4", "열네글자까지들어가요일이삼사")
            )
        },
    )
}