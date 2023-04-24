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
import androidx.compose.runtime.mutableStateListOf
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
import com.yeolsimee.moneysaving.ui.theme.RoumoTheme
import com.yeolsimee.moneysaving.utils.addFocusCleaner
import com.yeolsimee.moneysaving.utils.getTwoDigits
import com.yeolsimee.moneysaving.utils.getWeekTypes
import com.yeolsimee.moneysaving.view.category.CategoryGridView

@ExperimentalMaterial3Api
@ExperimentalLayoutApi
@Composable
fun RoutineScreen(
    routineType: RoutineModifyOption?,
    categoryList: MutableList<TextItem>,
    selectedCategoryId: MutableState<String>,
    closeCallback: () -> Unit,
    onCompleteCallback: (RoutineRequest) -> Unit,
    hasNotificationPermission: () -> Boolean,
    onCategoryAdded: (String) -> Unit,
) {
    RoumoTheme(navigationBarColor = Color.Black) {
        val focusRequester by remember { mutableStateOf(FocusRequester()) }
        val focusManager = LocalFocusManager.current
        val scrollState = rememberScrollState()

        val routineName = remember { mutableStateOf("") }

        if (categoryList.isNotEmpty()) {
            selectedCategoryId.value = categoryList.last().id
        }

        val repeatSelectList =
            remember { mutableStateListOf(false, false, false, false, false, false, false) }
        val selectedRoutineTimeZoneId = remember { mutableStateOf("1") }
        val alarmState = remember { mutableStateOf(false) }
        val hourState = remember { mutableStateOf(13) }
        val minuteState = remember { mutableStateOf(0) }

        val addCategoryState = remember { mutableStateOf(false) }

        Scaffold(
            topBar = {
                RoutineTopAppBar(routineType) { closeCallback() }
            },
            bottomBar = {
                RoutineBottomAppBar(routineType) {
                    onCompleteCallback(
                        RoutineRequest(
                            alarmStatus = if (alarmState.value) "ON" else "OFF",
                            alarmTime = getAlarmTime(alarmState, hourState, minuteState),
                            routineName = routineName.value,
                            categoryId = selectedCategoryId.value,
                            routineTimeZone = selectedRoutineTimeZoneId.value,
                            weekTypes = getWeekTypes(repeatSelectList),
                        )
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
            ) {
                Column(
                    Modifier
                        .verticalScroll(scrollState)
                        .addFocusCleaner(focusManager)
                ) {
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
                            // 카테고리 추가 API 호출
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
                    SettingAlarmTime(alarmState, hourState, minuteState, hasNotificationPermission)
                }
            }
        }
    }
}

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
fun RoutineScreenPreview() {
    RoutineScreen(
        routineType = RoutineModifyOption.add,
        categoryList = remember {
            mutableListOf(
                TextItem("1", "💰아껴쓰기"),
                TextItem("2", "주린이 성장일기"),
                TextItem("3", "임티는 사용자 자유"),
                TextItem("4", "열네글자까지들어가요일이삼사")
            )
        },
        selectedCategoryId = remember { mutableStateOf("") },
        closeCallback = {},
        onCompleteCallback = {},
        hasNotificationPermission = {
            return@RoutineScreen true
        }
    ) {

    }
}