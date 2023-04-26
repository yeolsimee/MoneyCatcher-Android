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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.yeolsimee.moneysaving.domain.entity.category.CategoryWithRoutines
import com.yeolsimee.moneysaving.domain.entity.routine.Routine
import com.yeolsimee.moneysaving.domain.entity.routine.RoutinesOfDay
import com.yeolsimee.moneysaving.ui.PrText
import com.yeolsimee.moneysaving.ui.theme.GrayF0
import com.yeolsimee.moneysaving.ui.theme.RoumoTheme

@Composable
fun RoutineItem(routinesOfDayState: RoutinesOfDay, onItemClick: (Routine, String) -> Unit = { _, _ -> }) {
    val categories = routinesOfDayState.categoryDatas

    Spacer(Modifier.height(18.dp))

    Column {
        for (category in categories) {
            PrText(
                text = category.categoryName,
                fontSize = 14.sp,
                fontWeight = FontWeight.W700
            )
            Spacer(Modifier.height(8.dp))

            for (routine in category.routineDatas) {
                val checked = routine.routineCheckYN == "Y"
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
                                onItemClick(routine, category.categoryId)
                            }
                        )
                ) {
                    Row(
                        Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp),
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
                        Image(
                            painter = painterResource(
                                id = if (checked) R.drawable.icon_check
                                else R.drawable.icon_nonecheck
                            ),
                            contentDescription = "루틴 체크"
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
            }
            Spacer(Modifier.height(20.dp))
        }
        Spacer(Modifier.height(20.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun RoutineItemPreview() {
    RoumoTheme {
        Box(modifier = Modifier.padding(10.dp)) {
            RoutineItem(
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