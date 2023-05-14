@file:OptIn(ExperimentalMaterial3Api::class)

package com.yeolsimee.moneysaving.ui.routine

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yeolsimee.moneysaving.R
import com.yeolsimee.moneysaving.domain.calendar.CalendarDay
import com.yeolsimee.moneysaving.domain.entity.category.CategoryWithRoutines
import com.yeolsimee.moneysaving.domain.entity.routine.Routine
import com.yeolsimee.moneysaving.domain.entity.routine.RoutineCheckRequest
import com.yeolsimee.moneysaving.domain.entity.routine.RoutinesOfDay
import com.yeolsimee.moneysaving.ui.PrText
import com.yeolsimee.moneysaving.ui.dialog.OneButtonOneTitleDialog
import com.yeolsimee.moneysaving.ui.dialog.TwoButtonOneTitleDialog
import com.yeolsimee.moneysaving.ui.theme.DismissRed
import com.yeolsimee.moneysaving.ui.theme.GrayF0
import com.yeolsimee.moneysaving.ui.theme.RoumoTheme
import kotlinx.coroutines.launch

@Composable
fun RoutineItems(
    selectedDate: CalendarDay,
    routinesOfDayState: RoutinesOfDay,
    onItemClick: (String, String) -> Unit = { _, _ -> },
    onRoutineCheck: (RoutineCheckRequest) -> Unit = {},
    onItemDelete: (Routine) -> Unit = {},
) {
    val categories = routinesOfDayState.categoryDatas
    val date = selectedDate.toString()
    val cantEditDialogState = remember { mutableStateOf(false) }

    Spacer(Modifier.height(18.dp))

    Column {
        for (category in categories) {
            PrText(
                text = category.categoryName,
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(Modifier.height(8.dp))

            for (routine in category.routineDatas) {
                val checked = routine.routineCheckYN == "Y"

                val deleteDialogState = remember { mutableStateOf(false) }

                val swipeState = setRoutineSwipeState(selectedDate, routine, cantEditDialogState) {
                    deleteDialogState.value = true
                }

                SwipeToDismiss(
                    state = swipeState,
                    background = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(DismissRed)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.icon_trash),
                                contentDescription = "루틴 지우기",
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .padding(end = 20.dp)
                            )
                        }
                    },
                    directions = setOf(DismissDirection.EndToStart),
                    dismissContent = {
                        Box(
                            modifier = Modifier
                                .border(
                                    width = 1.5.dp,
                                    shape = RoundedCornerShape(4.dp),
                                    color = if (checked) Color.Black else GrayF0
                                )
                                .background(color = if (checked) GrayF0 else Color.White)
                                .fillMaxWidth()
                                .height(70.dp)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = {
                                        if (selectedDate.isNotPast()) {
                                            onItemClick(routine.routineId, category.categoryId)
                                        } else {
                                            cantEditDialogState.value = true
                                        }
                                    }
                                )
                        ) {
                            Row(
                                Modifier
                                    .fillMaxSize()
                                    .padding(start = 20.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    PrText(
                                        text = routine.routineName,
                                        fontWeight = FontWeight.W800,
                                        fontSize = 15.sp,
                                        color = Color.Black,
                                        textDecoration = if (checked) TextDecoration.LineThrough else null,
                                    )
                                    Spacer(Modifier.height(8.dp))
                                    Row {
                                        RoutineTimeZone(routine)
                                        Spacer(Modifier.width(8.dp))
                                        AlarmIconAndText(routine)
                                    }
                                }
                                if (selectedDate.isToday()) {
                                    Box(modifier = Modifier
                                        .width(60.dp)
                                        .clickable(
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = null,
                                            onClick = {
                                                onRoutineCheck(
                                                    RoutineCheckRequest(
                                                        routineCheckYN = if (checked) "N" else "Y",
                                                        routineId = routine.routineId.toInt(),
                                                        routineDay = date
                                                    )
                                                )
                                            }
                                        )) {
                                        Image(
                                            painter = painterResource(
                                                id = if (checked) R.drawable.icon_check
                                                else R.drawable.icon_nonecheck
                                            ),
                                            contentDescription = "루틴 체크",
                                            modifier = Modifier.align(Alignment.Center)
                                        )
                                    }
                                }
                            }
                        }
                    }
                )
                Spacer(Modifier.height(8.dp))

                val scope = rememberCoroutineScope()
                TwoButtonOneTitleDialog(
                    dialogState = deleteDialogState,
                    text = "해당 아이템을 삭제하시겠습니까?",
                    onConfirmClick = {
                        onItemDelete(routine)
                    },
                    onCancelClick = {
                        scope.launch { swipeState.reset() }
                    }
                )
            }
            Spacer(Modifier.height(20.dp))
        }
        Spacer(Modifier.height(20.dp))
    }

    if (cantEditDialogState.value) {
        OneButtonOneTitleDialog(
            dialogState = cantEditDialogState,
            text = "현재 날짜에서만 루틴을 수정할 수 있습니다"
        )
    }
}

@Composable
private fun setRoutineSwipeState(
    selectedDate: CalendarDay,
    routine: Routine,
    cantEditDialogState: MutableState<Boolean>,
    onItemDelete: (Routine) -> Unit,
) = rememberDismissState(
    confirmValueChange = { dismissValue ->
        if (dismissValue == DismissValue.DismissedToStart) {
            if (selectedDate.isToday()) {
                onItemDelete(routine)
            } else {
                cantEditDialogState.value = true
            }
        }
        false
    },
)

@Preview(showBackground = true)
@Composable
fun RoutineItemPreview() {
    RoumoTheme {
        Box(modifier = Modifier.padding(10.dp)) {
            RoutineItems(
                selectedDate = CalendarDay(2023, 5, 2),
                routinesOfDayState = RoutinesOfDay(
                    routineDay = "20230424",
                    categoryDatas = arrayOf(
                        CategoryWithRoutines(
                            categoryId = "1",
                            categoryName = "테스트 카테고리명",
                            remainingRoutineNum = "",
                            routineDatas = arrayOf(
                                Routine(
                                    routineId = "1",
                                    routineName = "루틴명 테스트",
                                    routineCheckYN = "Y",
                                    routineTimeZone = "1",
                                    alarmTimeHour = "11",
                                    alarmTimeMinute = "00"
                                ),
                                Routine(
                                    routineId = "2",
                                    routineName = "루틴명 테스트",
                                    routineCheckYN = "Y",
                                    routineTimeZone = "2",
                                )
                            )
                        ),
                        CategoryWithRoutines(
                            categoryId = "2",
                            categoryName = "테스트 카테고리명",
                            remainingRoutineNum = "",
                            routineDatas = arrayOf(
                                Routine(
                                    routineId = "3",
                                    routineName = "루틴명 테스트",
                                    routineCheckYN = "N",
                                    routineTimeZone = "3",
                                    alarmTimeHour = "14",
                                    alarmTimeMinute = "00"
                                )
                            )
                        )
                    )
                )
            )
        }
    }
}