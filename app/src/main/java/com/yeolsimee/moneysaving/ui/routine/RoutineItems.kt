@file:OptIn(ExperimentalMaterial3Api::class)

package com.yeolsimee.moneysaving.ui.routine

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yeolsimee.moneysaving.R
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
import com.yeolsimee.moneysaving.utils.DialogState
import com.yeolsimee.moneysaving.utils.onClick
import kotlinx.coroutines.launch
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

@Composable
fun RoutineItems(
    routinesOfDayState: RoutinesOfDay,
    categoryModifyDialogState: MutableState<DialogState<CategoryWithRoutines>> = remember {
        mutableStateOf(
            DialogState(false, null)
        )
    },
    onItemClick: (Int, String) -> Unit = { _, _ -> },
    onRoutineCheck: (RoutineCheckRequest, Routine) -> Unit = { _, _ -> },
    onItemDelete: (Routine) -> Unit = {},
) {
    val isToday = routinesOfDayState.isToday()
    val isNotPast = routinesOfDayState.isNotPast()
    val categoriesState = remember { mutableStateOf(routinesOfDayState.categoryDatas) }
    val date = routinesOfDayState.getDate()
    val cantEditDialogState = remember { mutableStateOf(false) }
    val state = rememberReorderableLazyListState(onMove = { from, to ->
        categoriesState.value = categoriesState.value.toMutableList().apply {
            add(to.index, removeAt(from.index))
        }
    })


    LazyColumn(
        state = state.listState,
        modifier = Modifier
            .reorderable(state)
            .detectReorderAfterLongPress(state)
    ) {
        items(categoriesState.value, { it }) { category ->
            ReorderableItem(state, key = category) { isDragging ->
                val elevation = animateDpAsState(if (isDragging) 16.dp else 0.dp, label = "")
                Column(
                    modifier = Modifier
                        .shadow(
                            elevation.value,
                            RoundedCornerShape(16.dp),
                            ambientColor = Color.Black,
                            spotColor = Color.Black
                        )
                        .background(Color.White)
                        .padding(horizontal = 28.dp)
                ) {
                    Spacer(Modifier.height(10.dp))
                    PrText(
                        text = category.categoryName,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.onClick {
                            categoryModifyDialogState.value = DialogState(true, category)
                        }
                    )
                    Spacer(Modifier.height(8.dp))

                    for (routine in category.routineDatas) {
                        val checked = routine.routineCheckYN == "Y"

                        val deleteDialogState = remember { mutableStateOf(false) }

                        val swipeState =
                            setRoutineSwipeState(isNotPast, routine, cantEditDialogState) {
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
                                                if (isNotPast) {
                                                    onItemClick(
                                                        routine.routineId,
                                                        category.categoryId
                                                    )
                                                } else {
                                                    cantEditDialogState.value = true
                                                }
                                            }
                                        )
                                ) {
                                    Column(
                                        Modifier
                                            .padding(start = 20.dp, end = 60.dp)
                                            .align(Alignment.CenterStart)
                                    ) {
                                        PrText(
                                            text = routine.routineName,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 15.sp,
                                            color = Color.Black,
                                            textAlign = TextAlign.Start,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            textDecoration = if (checked) TextDecoration.LineThrough else null,
                                        )
                                        Spacer(Modifier.height(8.dp))
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            RoutineTimeZone(routine)
                                            Spacer(Modifier.width(8.dp))
                                            AlarmIconAndText(routine)
                                        }
                                    }

                                    if (isToday) {
                                        Box(modifier = Modifier
                                            .width(60.dp)
                                            .align(Alignment.CenterEnd)
                                            .onClick {
                                                onRoutineCheck(
                                                    RoutineCheckRequest(
                                                        routineCheckYN = if (checked) "N" else "Y",
                                                        routineId = routine.routineId,
                                                        routineDay = date
                                                    ),
                                                    routine
                                                )
                                            }
                                        ) {
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
                        )
                        Spacer(Modifier.height(10.dp))

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
                }
            }
        }
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
    isNotPast: Boolean,
    routine: Routine,
    cantEditDialogState: MutableState<Boolean>,
    onItemDelete: (Routine) -> Unit,
) = rememberDismissState(
    confirmValueChange = { dismissValue ->
        if (dismissValue == DismissValue.DismissedToStart) {
            if (isNotPast) {
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
                routinesOfDayState = RoutinesOfDay(
                    routineDay = "20230519",
                    categoryDatas = mutableListOf(
                        CategoryWithRoutines(
                            categoryId = "1",
                            categoryName = "테스트 카테고리명",
                            remainingRoutineNum = "",
                            routineDatas = arrayOf(
                                Routine(
                                    routineId = 1,
                                    routineName = "루틴명 테스트",
                                    routineCheckYN = "Y",
                                    routineTimeZone = "1",
                                    alarmTimeHour = "11",
                                    alarmTimeMinute = "00"
                                ),
                                Routine(
                                    routineId = 2,
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
                                    routineId = 3,
                                    routineName = "루틴명 테스트",
                                    routineCheckYN = "N",
                                    routineTimeZone = "3",
                                    alarmTimeHour = "14",
                                    alarmTimeMinute = "00"
                                )
                            )
                        ),
                        CategoryWithRoutines(
                            categoryId = "3",
                            categoryName = "테스트 카테고리명",
                            remainingRoutineNum = "",
                            routineDatas = arrayOf(
                                Routine(
                                    routineId = 3,
                                    routineName = "루틴명 테스트 루틴명 테스트 루틴명 테스트 루틴명 테스트 루틴명 테스트",
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