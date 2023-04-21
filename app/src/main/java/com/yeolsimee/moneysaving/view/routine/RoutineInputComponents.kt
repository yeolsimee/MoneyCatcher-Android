@file:OptIn(
    ExperimentalLayoutApi::class, ExperimentalLayoutApi::class,
    ExperimentalLayoutApi::class
)

package com.yeolsimee.moneysaving.view.routine

import android.app.TimePickerDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yeolsimee.moneysaving.R
import com.yeolsimee.moneysaving.domain.entity.category.TextItem
import com.yeolsimee.moneysaving.ui.PrText
import com.yeolsimee.moneysaving.ui.calendar.DayOfWeekIcon
import com.yeolsimee.moneysaving.ui.dialog.AddCategoryDialog
import com.yeolsimee.moneysaving.ui.list_item.SelectedItem
import com.yeolsimee.moneysaving.ui.list_item.UnSelectedItem
import com.yeolsimee.moneysaving.ui.theme.Gray99
import com.yeolsimee.moneysaving.ui.theme.GrayF0
import com.yeolsimee.moneysaving.ui.theme.RoumoTheme
import com.yeolsimee.moneysaving.utils.getTwoDigitsHour
import com.yeolsimee.moneysaving.utils.getTwoDigitsMinute

@Composable
fun InputRoutineName(routineName: MutableState<String>, focusRequester: FocusRequester) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.image_pencil),
                contentDescription = "루틴명 입력"
            )
            Spacer(Modifier.width(4.dp))
            PrText(
                text = "루틴명은 무엇인가요?", fontWeight = FontWeight.W700, fontSize = 15.sp
            )
        }
        Spacer(Modifier.height(4.dp))
        BasicTextField(
            value = routineName.value,
            textStyle = TextStyle(
                fontWeight = FontWeight.W600,
                fontSize = 14.sp,
                color = Color.Black
            ), onValueChange = { t ->
                if (t.length <= 50) {
                    routineName.value = t
                }
            }, singleLine = true, decorationBox = { innerTextField ->
                Box {
                    if (routineName.value.isEmpty()) {
                        PrText(
                            text = "루틴명을 입력해주세요.",
                            color = Gray99,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W400
                        )
                    }
                    innerTextField()
                }
            }, modifier = Modifier
                .padding(top = 10.dp, bottom = 9.dp)
                .fillMaxWidth()
                .focusRequester(focusRequester)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.Black)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun InputRoutineNamePreview() {
    val focusRequester by remember { mutableStateOf(FocusRequester()) }
    RoumoTheme {
        Column(modifier = Modifier) {
            Spacer(Modifier.height(8.dp))
            InputRoutineName(
                routineName = remember { mutableStateOf("") },
                focusRequester = focusRequester
            )
            Spacer(Modifier.height(8.dp))
            InputRoutineName(
                routineName = remember { mutableStateOf("루틴명") },
                focusRequester = focusRequester
            )
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
fun SelectCategory(
    categories: MutableList<TextItem>,
    selectedId: MutableState<String>,
    addCategoryState: MutableState<Boolean>,
    selectCallback: (String) -> Unit,
    addCallback: (String) -> Unit = {},
) {
    Column(modifier = Modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.image_tag),
                contentDescription = "루틴 카테고리 설정"
            )
            Spacer(Modifier.width(4.dp))
            PrText(
                text = "루틴의 카테고리를 설정해주세요", fontWeight = FontWeight.W700, fontSize = 15.sp
            )
        }
        Spacer(Modifier.height(11.dp))
        FlowRow {
            categories.forEach {
                if (selectedId.value == it.id) {
                    SelectedItem(it, selectCallback)
                } else {
                    UnSelectedItem(it, selectCallback)
                }
            }
            if (categories.size < 10) {
                Box(Modifier.padding(top = 6.dp)) {
                    Box(
                        Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.Black)
                            .clickable(interactionSource = remember {
                                MutableInteractionSource()
                            }, indication = null, onClick = {
                                addCategoryState.value = true
                            })
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "카테고리 추가",
                            tint = Color.White,
                            modifier = Modifier
                                .width(16.dp)
                                .height(16.dp)
                                .align(Alignment.Center)
                        )
                    }
                }
                AddCategoryDialog(
                    dialogState = addCategoryState,
                    confirmButtonListener = { addCallback(it) })
            }
        }
    }
}


@Preview(showBackground = true, widthDp = 400)
@Composable
fun SelectCategoryPreview() {
    RoumoTheme {
        val selectedCategoryId = remember { mutableStateOf("1") }
        val addCategoryState = remember { mutableStateOf(false) }
        SelectCategory(
            mutableListOf(
                TextItem("1", "💰아껴쓰기"),
                TextItem("2", "주린이 성장일기"),
                TextItem("3", "임티는 사용자 자유"),
                TextItem("4", "열네글자까지들어가요일이삼사")
            ),
            selectedId = selectedCategoryId,
            addCategoryState = addCategoryState,
            selectCallback = {
                selectedCategoryId.value = it
            },
            addCallback = {}
        )
    }
}

@Composable
fun SelectRoutineRepeat(selectedList: List<Boolean>, onClick: (Int) -> Unit) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.image_refresh),
                contentDescription = "루틴 반복 설정"
            )
            Spacer(Modifier.width(4.dp))
            PrText(text = "루틴을 언제 반복할까요?", fontWeight = FontWeight.W700, fontSize = 15.sp)
        }
        Spacer(Modifier.height(12.dp))
        Row {
            for (i in 0 until 7) {
                if (i != 0) {
                    Spacer(Modifier.width(10.dp))
                }
                DayOfWeekIcon(
                    dayOfWeek = i,
                    initialSelected = selectedList[i],
                    onClick = { onClick(i) })
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SelectRoutineRepeatPreview() {
    RoumoTheme {
        SelectRoutineRepeat(
            mutableListOf(false, false, false, true, false, false, false)
        ) {}
    }
}

@Composable
fun SelectRoutineTimeZone(
    selectedId: MutableState<String>,
    selectCallback: (String) -> Unit
) {
    val timeZoneList = remember {
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
    }
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.image_time),
                contentDescription = "루틴 수행 시간대"
            )
            Spacer(Modifier.width(4.dp))
            PrText(
                text = "루틴을 수행할 시간대를 설정해주세요",
                fontWeight = FontWeight.W700,
                fontSize = 15.sp,
            )
        }
        Spacer(Modifier.height(11.dp))
        FlowRow {
            timeZoneList.forEach {
                if (selectedId.value == it.id) {
                    SelectedItem(it, selectCallback)
                } else {
                    UnSelectedItem(it, selectCallback)
                }
            }
        }
    }
}


@Preview(showBackground = true, widthDp = 319)
@Composable
fun SelectRoutineTimeZonePreview() {
    RoumoTheme {
        SelectRoutineTimeZone(
            selectedId = remember {
                mutableStateOf("1")
            }) {}
    }
}

@Composable
fun SettingAlarmTime(
    alarmState: MutableState<Boolean>,
    hourState: MutableState<Int>,
    minuteState: MutableState<Int>,
    hasNotificationPermission: () -> Boolean = { false },
) {
    val timeText = hourState.value.getTwoDigitsHour() + ":" +
            minuteState.value.getTwoDigitsMinute()

    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.image_alram),
                    contentDescription = "알람"
                )
                Spacer(Modifier.width(4.dp))
                PrText(text = "알람이 필요하세요?", fontWeight = FontWeight.W700, fontSize = 15.sp)
            }
            if (alarmState.value) {
                Image(
                    painter = painterResource(id = R.drawable.toggle_on),
                    contentDescription = "켜짐",
                    Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            alarmState.value = !alarmState.value
                        }
                    )
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.toggle_off),
                    contentDescription = "꺼짐",
                    Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            // TODO 알람 권한 확인
                            if (hasNotificationPermission()) {
                                alarmState.value = !alarmState.value
                            }
                        }
                    )
                )
            }
        }
        if (alarmState.value) {
            Spacer(Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.arrow_forward),
                        contentDescription = "몇 시에 알려드릴까요?",
                        modifier = Modifier.padding(start = 3.dp, end = 5.5.dp)
                    )
                    PrText(text = "몇 시에 알려드릴까요?", fontWeight = FontWeight.W600, fontSize = 13.sp)
                }
                val localContext = LocalContext.current
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.White)
                        .border(width = 1.5.dp, color = GrayF0, shape = RoundedCornerShape(4.dp))
                        .padding(vertical = 8.dp, horizontal = 10.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {
                                val dialog = TimePickerDialog(localContext, { _, h, m ->
                                    hourState.value = h
                                    minuteState.value = m
                                }, hourState.value, minuteState.value, false)
                                dialog.show()
                            }
                        )
                ) {
                    PrText(text = timeText, fontWeight = FontWeight.W500, fontSize = 12.sp)
                }
            }
        }
    }
}


@Preview(showBackground = true, widthDp = 319)
@Composable
fun SettingAlarmTimePreview() {

    val alarmState1 = remember { mutableStateOf(true) }
    val alarmState2 = remember { mutableStateOf(false) }
    val hourState = remember { mutableStateOf(11) }
    val minuteState = remember { mutableStateOf(0) }

    RoumoTheme {
        Column {
            SettingAlarmTime(alarmState = alarmState1, hourState, minuteState)
            Spacer(modifier = Modifier.height(8.dp))
            SettingAlarmTime(alarmState = alarmState2, hourState, minuteState)
        }
    }
}
