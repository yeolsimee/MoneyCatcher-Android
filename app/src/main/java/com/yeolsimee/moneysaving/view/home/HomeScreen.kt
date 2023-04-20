package com.yeolsimee.moneysaving.view.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yeolsimee.moneysaving.R
import com.yeolsimee.moneysaving.domain.calendar.CalendarDay
import com.yeolsimee.moneysaving.domain.calendar.getWeekDays
import com.yeolsimee.moneysaving.domain.entity.routine.RoutinesOfDay
import com.yeolsimee.moneysaving.ui.AppLogoImage
import com.yeolsimee.moneysaving.ui.PrText
import com.yeolsimee.moneysaving.ui.calendar.DayOfMonthIcon
import com.yeolsimee.moneysaving.ui.dialog.YearMonthDialog
import com.yeolsimee.moneysaving.ui.routine.AlarmIconAndText
import com.yeolsimee.moneysaving.ui.routine.RoutineTimeZone
import com.yeolsimee.moneysaving.ui.theme.GrayF0
import com.yeolsimee.moneysaving.utils.collectAsStateWithLifecycleRemember
import com.yeolsimee.moneysaving.view.calendar.CalendarViewModel
import com.yeolsimee.moneysaving.view.calendar.FindAllMyRoutineViewModel
import com.yeolsimee.moneysaving.view.calendar.SelectedDateViewModel
import java.util.Calendar

@Composable
fun HomeScreen(
    viewModel: CalendarViewModel,
    selectedDateViewModel: SelectedDateViewModel,
    findAllMyRoutineViewModel: FindAllMyRoutineViewModel
) {
    val year = viewModel.year()
    val month = viewModel.month()
    val today = viewModel.today

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
        val calendarMonth = remember { mutableStateOf(selected.value.month - 1) }

        val cancelButtonListener = {
            dialogState.value = false
        }

        val confirmButtonListener: (Int, Int) -> Unit =
            { selectedYear, selectedMonth ->
                viewModel.setDate(selectedYear, selectedMonth - 1)
                findAllMyRoutineViewModel.find(viewModel.getFirstAndLastDate(), selectedMonth)
                dialogState.value = false
            }

        YearMonthDialog(
            dialogState,
            year,
            month,
            cancelButtonListener,
            confirmButtonListener
        )

        AppLogoImage()

        Spacer(Modifier.height(16.dp))

        YearMonthSelectBox(dialogState, viewModel.date.observeAsState().value ?: "", spread)

        Spacer(modifier = Modifier.height(10.dp))

        val iconStateMutableList by findAllMyRoutineViewModel.container.stateFlow.collectAsStateWithLifecycleRemember(
            mutableListOf()
        )

        viewModel.load(iconStateMutableList)

        ComposeCalendar(
            viewModel.dayList.observeAsState().value!!,
            selected,
            spread,
            year,
            month,
            calendarMonth,
            restoreSelected = {
                viewModel.setDate(selected.value.year, it + 1)
                findAllMyRoutineViewModel.find(viewModel.getFirstAndLastDate(), it + 2)
            },
            onItemSelected = {
                selectedDateViewModel.find(it)
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        DateText(selected)

        val routinesOfDayState: RoutinesOfDay by selectedDateViewModel.container.stateFlow.collectAsStateWithLifecycleRemember(
            RoutinesOfDay()
        )

        if (routinesOfDayState.getRoutineDay().isEmpty()) {
            EmptyRoutine()
        } else {
            ShowRoutines(routinesOfDayState)
        }
    }
}

@Composable
private fun ShowRoutines(routinesOfDayState: RoutinesOfDay) {
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
            }
            Spacer(Modifier.height(20.dp))
        }
        Spacer(Modifier.height(56.dp))
    }
}

@Composable
private fun EmptyRoutine() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(45.dp))
        Image(
            painter = painterResource(id = R.drawable.empty_routine),
            contentDescription = "루틴이 비어 있어요!"
        )
        PrText(
            text = stringResource(R.string.routine_is_empty),
            fontSize = 20.sp,
            fontWeight = FontWeight.W800,
        )
        Spacer(Modifier.height(10.dp))
        PrText(
            text = stringResource(R.string.please_add_routine_button),
            fontSize = 14.sp,
            fontWeight = FontWeight.W700,
        )
        Spacer(Modifier.height(200.dp))
    }
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
                painter = painterResource(id = R.drawable.icon_arrow_open),
                contentDescription = "연/월 선택"
            )
        }
    }
}

@Composable
private fun ComposeCalendar(
    days: MutableList<CalendarDay>,
    selected: MutableState<CalendarDay>,
    spread: MutableState<Boolean>,
    year: Int,
    month: Int,
    calendarMonth: MutableState<Int>,
    restoreSelected: (Int) -> Unit,
    onItemSelected: (CalendarDay) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DayOfWeekIndicator()
        Spacer(Modifier.height(8.dp))

        CalendarGrid(days, spread, selected, month, calendarMonth, onItemSelected)
        Spacer(modifier = Modifier.height(12.dp))

        CalendarSpreadButton(spread, selected, year, month, calendarMonth, restoreSelected)
    }
}

@Composable
private fun DayOfWeekIndicator() {
    LazyHorizontalGrid(
        modifier = Modifier
            .fillMaxWidth()
            .height(12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        rows = GridCells.Fixed(1),
    ) {
        items(arrayOf("월", "화", "수", "목", "금", "토", "일")) {
            DayOfWeekText(it)
        }
    }
}

@Composable
private fun CalendarGrid(
    days: MutableList<CalendarDay>,
    spread: MutableState<Boolean>,
    selected: MutableState<CalendarDay>,
    month: Int,
    calendarMonth: MutableState<Int>,
    onItemSelected: (CalendarDay) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        contentPadding = PaddingValues(0.dp),
        modifier = Modifier.heightIn(min = 54.dp, max = (54 * 7).dp)
    ) {
        items(days) { date ->
            AnimatedVisibility(visible = spread.value) {
                DayOfMonthIcon(date, selected) {
                    selected.value = it
                    calendarMonth.value = month
                    onItemSelected(it)
                }
            }
            AnimatedVisibility(visible = !spread.value) {
                if (date.isSameWeek(selected.value)) {
                    DayOfMonthIcon(date, selected) {
                        selected.value = it
                        calendarMonth.value = month
                        onItemSelected(it)
                    }
                }
            }
        }
    }
}

@Composable
private fun CalendarSpreadButton(
    spread: MutableState<Boolean>,
    selected: MutableState<CalendarDay>,
    year: Int,
    month: Int,
    calendarMonth: MutableState<Int>,
    restoreSelected: (Int) -> Unit
) {
    Image(
        modifier = Modifier.clickable(
            interactionSource = remember {
                MutableInteractionSource()
            },
            indication = null,
            onClick = {
                if (spread.value && (selected.value.year != year || selected.value.month != month)) {
                    restoreSelected(calendarMonth.value - 1)
                }
                spread.value = !spread.value
            }
        ),
        painter = if (spread.value) painterResource(id = R.drawable.icon_bigarrow_end)
        else painterResource(id = R.drawable.icon_bigarrow_open),
        contentDescription = "닫기"
    )
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
fun ComposeCalendarPreview() {
    val days = getWeekDays(Calendar.getInstance())
    val selected = remember { mutableStateOf(CalendarDay(2023, 4, 12)) }
    val spread = remember { mutableStateOf(false) }
    val calendarMonth = remember { mutableStateOf(4) }
    ComposeCalendar(days, selected, spread, 2023, 4, calendarMonth, {}) {}
}

@Preview(showBackground = true)
@Composable
fun YearMonthSelectBoxPreview() {
    val spread = remember { mutableStateOf(false) }
    YearMonthSelectBox(
        dialogState = remember { mutableStateOf(false) },
        dateText = "4월 12일 수요일",
        spread = spread
    )
}
