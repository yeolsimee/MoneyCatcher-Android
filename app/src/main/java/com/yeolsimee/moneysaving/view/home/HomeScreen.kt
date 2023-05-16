package com.yeolsimee.moneysaving.view.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yeolsimee.moneysaving.R
import com.yeolsimee.moneysaving.domain.calendar.CalendarDay
import com.yeolsimee.moneysaving.domain.entity.routine.RoutinesOfDay
import com.yeolsimee.moneysaving.ui.AppLogoImage
import com.yeolsimee.moneysaving.ui.PrText
import com.yeolsimee.moneysaving.ui.dialog.YearMonthDialog
import com.yeolsimee.moneysaving.ui.routine.EmptyRoutine
import com.yeolsimee.moneysaving.ui.routine.RoutineItems
import com.yeolsimee.moneysaving.utils.collectAsStateWithLifecycleRemember
import com.yeolsimee.moneysaving.utils.getReactiveHeight
import com.yeolsimee.moneysaving.view.home.calendar.CalendarViewModel
import com.yeolsimee.moneysaving.view.home.calendar.ComposeCalendar
import com.yeolsimee.moneysaving.view.home.calendar.FindAllMyRoutineViewModel
import com.yeolsimee.moneysaving.view.home.calendar.SelectedDateViewModel

@Composable
fun HomeScreen(
    calendarViewModel: CalendarViewModel,
    selectedDateViewModel: SelectedDateViewModel,
    findAllMyRoutineViewModel: FindAllMyRoutineViewModel,
    routineCheckViewModel: RoutineCheckViewModel,
    routineDeleteViewModel: RoutineDeleteViewModel,
    floatingButtonVisible: MutableState<Boolean>,
    onItemClick: (String, String) -> Unit = { _, _ -> }
) {
    val year = calendarViewModel.year()
    val month = calendarViewModel.month()
    val today = calendarViewModel.today

    val scrollState = rememberScrollState()

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(start = 28.dp, end = 28.dp)
            .verticalScroll(scrollState)
    ) {
        val spread = remember { mutableStateOf(false) }
        val dialogState = remember { mutableStateOf(false) }
        val selected = remember { mutableStateOf(today) }
        val calendarMonth = remember { mutableStateOf(today.month) }

        floatingButtonVisible.value = selected.value.toString() == today.toString()

        val onConfirmClick: (Int, Int) -> Unit =
            setOnYearMonthConfirm(
                calendarViewModel,
                calendarMonth,
                findAllMyRoutineViewModel,
                dialogState
            )

        YearMonthDialog(
            dialogState,
            year,
            month,
            onConfirmClick
        )
        Spacer(Modifier.height(4.dp))

        AppLogoImage()

        Spacer(Modifier.height(5.dp))

        YearMonthSelectBox(dialogState, calendarViewModel.date.observeAsState().value ?: "", spread)

        Spacer(modifier = Modifier.height(10.dp))

        ComposeCalendar(
            days = findAllMyRoutineViewModel.container.stateFlow.collectAsState().value,
            selected = selected,
            spread = spread,
            year = year,
            month = month,
            calendarMonth = calendarMonth,
            restoreSelected = {
                val resultDayList =
                    calendarViewModel.setDate(selected.value.year, selected.value.month - 1)
                findAllMyRoutineViewModel.find(
                    calendarViewModel.getFirstAndLastDate(resultDayList),
                    selected.value.month,
                    resultDayList
                )
            },
            onItemSelected = {
                selectedDateViewModel.find(it)
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        val routinesOfDayState by selectedDateViewModel.container.stateFlow.collectAsStateWithLifecycleRemember(
            RoutinesOfDay("loading")
        )

        Box(modifier = Modifier) {
            Column {
                Spacer(Modifier.height(16.dp))
                DateText(selected)

                if (routinesOfDayState.isEmpty()) {
                    Spacer(Modifier.height(43.dp))
                    EmptyRoutine()
                    Spacer(Modifier.height(getReactiveHeight(200)))
                } else if (routinesOfDayState.isNotLoading()) {
                    RoutineItems(
                        todayState = selected.value.isToday(),
                        selectedDate = selected.value,
                        routinesOfDayState = routinesOfDayState,
                        onItemClick = onItemClick,
                        onRoutineCheck = { check ->
                            routineCheckViewModel.check(check) { routinesOfDay ->
                                selectedDateViewModel.refresh(
                                    routinesOfDay
                                )
                            }
                        },
                        onItemDelete = {
                            routineDeleteViewModel.delete(it.routineId) {
                                findAllMyRoutineViewModel.refresh {
                                    selectedDateViewModel.find(selected.value)
                                }
                            }
                        }
                    )
                }
            }

            if (routinesOfDayState.isNotLoadingAndNotEmpty()) {
                Image(
                    painter = painterResource(id = R.drawable.coins),
                    contentDescription = "동전",
                    modifier = Modifier.align(
                        Alignment.TopEnd
                    )
                )
            }
        }


    }
}

@Composable
private fun setOnYearMonthConfirm(
    calendarViewModel: CalendarViewModel,
    calendarMonth: MutableState<Int>,
    findAllMyRoutineViewModel: FindAllMyRoutineViewModel,
    dialogState: MutableState<Boolean>
): (Int, Int) -> Unit {
    val confirmButtonListener: (Int, Int) -> Unit = { selectedYear, selectedMonth ->
        val resultDayList = calendarViewModel.setDate(selectedYear, selectedMonth - 1)
        calendarMonth.value = selectedMonth

        findAllMyRoutineViewModel.find(
            dateRange = calendarViewModel.getFirstAndLastDate(resultDayList),
            selectedMonth = calendarMonth.value,
            dayList = resultDayList,
        )
        dialogState.value = false
    }
    return confirmButtonListener
}

@Composable
private fun DateText(selected: MutableState<CalendarDay>) {
    Box(modifier = Modifier.fillMaxWidth()) {
        PrText(
            text = "${selected.value.month}월 ${selected.value.day}일 ${selected.value.getDayOfWeek()}",
            color = Color.Black,
            fontWeight = FontWeight.W700,
            fontSize = 18.sp,
        )
    }
}


@Composable
private fun YearMonthSelectBox(
    dialogState: MutableState<Boolean>, dateText: String, spread: MutableState<Boolean>
) {
    Row(
        modifier = Modifier.clickable(
            interactionSource = remember {
                MutableInteractionSource()
            },
            indication = null,
            onClick = {
                if (spread.value) {
                    dialogState.value = !dialogState.value
                }
            }),
        verticalAlignment = Alignment.Bottom
    ) {
        Image(
            painter = painterResource(id = R.drawable.icon_calendar), contentDescription = "연/월 선택"
        )
        Spacer(Modifier.width(2.dp))

        PrText(
            text = dateText,
            fontWeight = FontWeight.W700,
            fontSize = 12.sp
        )
        if (spread.value) {
            Image(
                painter = painterResource(id = R.drawable.icon_year_month_open),
                contentDescription = "연/월 선택"
            )
        }
    }
}


@Composable
fun DayOfWeekText(text: String) {
    val config = LocalConfiguration.current
    val itemWidth = (config.screenWidthDp.dp - (28.dp * 2)) / 7
    PrText(
        text = text, modifier = Modifier
            .width(itemWidth)
            .height(12.dp),
        fontWeight = FontWeight.W700, fontSize = 10.sp, textAlign = TextAlign.Center
    )
}

@Preview(showBackground = true)
@Composable
fun YearMonthSelectBoxPreview() {
    val spread = remember { mutableStateOf(true) }
    YearMonthSelectBox(
        dialogState = remember { mutableStateOf(false) },
        dateText = "4월 12일 수요일",
        spread = spread
    )
}
