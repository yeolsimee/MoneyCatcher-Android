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
import com.yeolsimee.moneysaving.ui.theme.RoumoTheme
import com.yeolsimee.moneysaving.utils.addFocusCleaner

@ExperimentalMaterial3Api
@ExperimentalLayoutApi
@Composable
fun RoutineScreen(
    routineType: RoutineModifyOption?,
    closeCallback: () -> Unit,
    onCompleteCallback: () -> Unit
) {
    RoumoTheme(navigationBarColor = Color.Black) {
        Scaffold(
            topBar = {
                RoutineTopAppBar(routineType) { closeCallback() }
            },
            bottomBar = {
                RoutineBottomAppBar(routineType) { onCompleteCallback() }
            },
            containerColor = Color.White
        ) {
            Box(
                Modifier
                    .padding(it)
                    .padding(horizontal = 28.dp)
                    .background(Color.White)
            ) {
                val focusRequester by remember { mutableStateOf(FocusRequester()) }
                val focusManager = LocalFocusManager.current
                val routineName = remember { mutableStateOf("") }
                val selectedCategoryId = remember { mutableStateOf("1") }
                val scrollState = rememberScrollState()
                val repeatSelectList =
                    remember { mutableStateListOf(false, false, false, false, false, false, false) }
                val selectedRoutineTimeZoneId = remember { mutableStateOf("1") }
                val alarmState = remember { mutableStateOf(false) }
                val hourState = remember { mutableStateOf(13) }
                val minuteState = remember { mutableStateOf(0) }

                Column(
                    Modifier
                        .verticalScroll(scrollState)
                        .addFocusCleaner(focusManager)
                ) {
                    InputRoutineName(routineName, focusRequester)
                    Spacer(Modifier.height(20.dp))
                    SelectCategory(
                        mutableListOf(
                            TextItem("1", "💰아껴쓰기"),
                            TextItem("2", "주린이 성장일기"),
                            TextItem("3", "임티는 사용자 자유"),
                            TextItem("4", "열네글자까지들어가요일이삼사")
                        ),
                        selectedId = selectedCategoryId,
                        selectCallback = { id ->
                            selectedCategoryId.value = id
                        }
                    )
                    Spacer(Modifier.height(20.dp))
                    SelectRoutineRepeat(repeatSelectList.toList()) { i ->
                        repeatSelectList[i] = !repeatSelectList[i]
                    }
                    Spacer(Modifier.height(20.dp))
                    SelectRoutineTimeZone(
                        remember {
                            mutableListOf(
                                TextItem("1", "하루종일"),
                                TextItem("2", "아무때나"),
                                TextItem("3", "기상직후"),
                                TextItem("4", "아침"),
                                TextItem("5", "오전"),
                                TextItem("6", "점심"),
                                TextItem("7", "오후"),
                                TextItem("8", "저녁"),
                                TextItem("9", "밤"),
                                TextItem("10", "취침직전"),
                            )
                        },
                        selectedId = selectedRoutineTimeZoneId
                    ) { id ->
                        selectedRoutineTimeZoneId.value = id
                    }
                    Spacer(Modifier.height(20.dp))
                    SettingAlarmTime(alarmState, hourState, minuteState)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalLayoutApi
@Preview(showBackground = true)
@Composable
fun RoutineScreenPreview() {
    RoutineScreen(
        routineType = RoutineModifyOption.add,
        closeCallback = {},
        onCompleteCallback = {})
}