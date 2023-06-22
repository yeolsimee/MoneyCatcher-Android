@file:OptIn(
    ExperimentalLayoutApi::class
)

package com.yeolsimee.moneysaving.view.routine

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yeolsimee.moneysaving.R
import com.yeolsimee.moneysaving.domain.entity.category.TextItem
import com.yeolsimee.moneysaving.ui.PrText
import com.yeolsimee.moneysaving.ui.calendar.DayOfWeekIcon
import com.yeolsimee.moneysaving.ui.list_item.SelectedItem
import com.yeolsimee.moneysaving.ui.list_item.UnSelectedItem
import com.yeolsimee.moneysaving.ui.theme.Gray99
import com.yeolsimee.moneysaving.ui.theme.GrayF0
import com.yeolsimee.moneysaving.ui.theme.RoumoTheme
import com.yeolsimee.moneysaving.ui.theme.pretendard
import com.yeolsimee.moneysaving.utils.getTwoDigits
import com.yeolsimee.moneysaving.utils.getTwoDigitsHour

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
                lineHeight = 17.sp,
                color = Color.Black,
                fontFamily = pretendard,
                platformStyle = PlatformTextStyle(includeFontPadding = false),
                letterSpacing = (-0.1).sp,
            ), onValueChange = { t ->
                if (t.length <= 50) {
                    routineName.value = t
                }
            }, singleLine = true,
            decorationBox = { innerTextField ->
                Box {
                    if (routineName.value.isEmpty()) {
                        PrText(
                            text = "루틴명을 입력해주세요.",
                            color = Gray99,
                            fontSize = 14.sp,
                            lineHeight = 17.sp,
                            fontWeight = FontWeight.W600
                        )
                    }
                    innerTextField()
                }
            }, modifier = Modifier
                .padding(top = 10.dp, start = 2.dp, bottom = 7.dp)
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

@OptIn(ExperimentalLayoutApi::class)
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
    toggleRoutineAlarm: (MutableState<Boolean>) -> Unit = {},
    timePickerDialogState: MutableState<Boolean>,
) {
    val timeText = hourState.value.getTwoDigitsHour() + ":" +
            minuteState.value.getTwoDigits()

    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.image_alram),
                    contentDescription = "알림"
                )
                Spacer(Modifier.width(4.dp))
                PrText(text = "알림이 필요하세요?", fontWeight = FontWeight.W700, fontSize = 15.sp)
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
                            toggleRoutineAlarm(alarmState)
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
                                timePickerDialogState.value = true
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
    val hourState = remember { mutableIntStateOf(11) }
    val minuteState = remember { mutableIntStateOf(0) }
    val timePickerDialogState = remember { mutableStateOf(false) }

    RoumoTheme {
        Column {
            SettingAlarmTime(
                alarmState = alarmState1,
                hourState,
                minuteState,
                timePickerDialogState = timePickerDialogState
            )
            Spacer(modifier = Modifier.height(8.dp))
            SettingAlarmTime(
                alarmState = alarmState2,
                hourState,
                minuteState,
                timePickerDialogState = timePickerDialogState
            )
        }
    }
}
