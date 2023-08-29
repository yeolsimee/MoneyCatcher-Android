package com.yeolsimee.moneysaving.view.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yeolsimee.moneysaving.R
import com.yeolsimee.moneysaving.domain.calendar.CalendarDay
import com.yeolsimee.moneysaving.domain.entity.category.CategoryWithRoutines
import com.yeolsimee.moneysaving.domain.entity.routine.RoutinesOfDay
import com.yeolsimee.moneysaving.ui.AnimatedToggleButton
import com.yeolsimee.moneysaving.ui.AppLogoImage
import com.yeolsimee.moneysaving.ui.PrText
import com.yeolsimee.moneysaving.ui.dialog.YearMonthDialog
import com.yeolsimee.moneysaving.ui.routine.EmptyRoutine
import com.yeolsimee.moneysaving.ui.routine.RoutineItems
import com.yeolsimee.moneysaving.ui.routine.RoutineItemsWithCategories
import com.yeolsimee.moneysaving.ui.theme.RoumoTheme
import com.yeolsimee.moneysaving.utils.DialogState
import com.yeolsimee.moneysaving.utils.HorizontalSpacer
import com.yeolsimee.moneysaving.utils.VerticalSpacer
import com.yeolsimee.moneysaving.utils.getReactiveHeight
import com.yeolsimee.moneysaving.view.home.calendar.CalendarViewModel
import com.yeolsimee.moneysaving.view.home.calendar.ComposeCalendar
import com.yeolsimee.moneysaving.view.home.calendar.FindAllMyRoutineViewModel
import com.yeolsimee.moneysaving.view.home.calendar.PageChangedState
import com.yeolsimee.moneysaving.view.home.calendar.SelectedDateViewModel

@Composable
fun HomeScreen(
    calendarViewModel: CalendarViewModel,
    selectedDateViewModel: SelectedDateViewModel,
    findAllMyRoutineViewModel: FindAllMyRoutineViewModel,
    routineCheckViewModel: RoutineCheckViewModel = hiltViewModel(),
    routineDeleteViewModel: RoutineDeleteViewModel = hiltViewModel(),
    routineStateViewModel: RoutineStateViewModel = hiltViewModel(),
    floatingButtonVisible: MutableState<Boolean>,
    categoryModifyDialogState: MutableState<DialogState<CategoryWithRoutines>>,
    selectedState: MutableState<CalendarDay>,
    onItemClick: (Int, String) -> Unit = { _, _ -> },
    onDelete: (routineId: Int) -> Unit = {}
) {
    val year = calendarViewModel.year()
    val month = calendarViewModel.month()
    val today = calendarViewModel.today

    RoumoTheme(navigationBarColor = Color.Black) {
        Column(
            Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            val spread = remember { mutableStateOf(false) }
            val dialogState = remember { mutableStateOf(false) }
            val calendarMonth = remember { mutableStateOf(today.month) }
            val routineViewState = routineStateViewModel.getRoutineState().collectAsState(initial = false)
            val routinesOfDayState by selectedDateViewModel.container.stateFlow.collectAsState(
                RoutinesOfDay("loading")
            )

            floatingButtonVisible.value = selectedState.value.toString() == today.toString()

            val onConfirmClick: (Int, Int) -> Unit =
                setOnYearMonthConfirm(
                    calendarViewModel,
                    calendarMonth,
                    findAllMyRoutineViewModel,
                    dialogState
                )

            Column(Modifier.padding(horizontal = 28.dp)) {
                YearMonthDialog(
                    dialogState,
                    year,
                    month,
                    spread,
                    onConfirmClick
                )
                16.VerticalSpacer()

                AppLogoImage()

                20.VerticalSpacer()

                YearMonthSelectBox(dialogState, calendarViewModel.date.collectAsState().value)

                Spacer(modifier = Modifier.height(10.dp))

                ComposeCalendar(
                    modifier = Modifier.draggable(
                        orientation = Orientation.Vertical,
                        state = rememberDraggableState { dy ->
                            spread.value = dy > 0
                        }
                    ),
                    days = findAllMyRoutineViewModel.container.stateFlow.collectAsState().value,
                    selected = selectedState,
                    spread = spread,
                    year = year,
                    month = month,
                    calendarMonth = calendarMonth,
                    restoreSelected = {
                        val resultDayList =
                            calendarViewModel.setDate(selectedState.value.year, selectedState.value.month - 1)
                        findAllMyRoutineViewModel.find(
                            calendarViewModel.getFirstAndLastDate(resultDayList),
                            selectedState.value.month,
                            resultDayList
                        )
                    },
                    onItemSelected = {
                        selectedDateViewModel.find(it)
                    },
                    onPageChanged = { pageChangedState ->
                        if (pageChangedState == PageChangedState.PREV) {
                            setOnYearMonthConfirm(
                                calendarViewModel,
                                calendarMonth,
                                findAllMyRoutineViewModel,
                                dialogState
                            )(calendarViewModel.year(), calendarViewModel.month() - 1)
                        } else {
                            setOnYearMonthConfirm(
                                calendarViewModel,
                                calendarMonth,
                                findAllMyRoutineViewModel,
                                dialogState
                            )(calendarViewModel.year(), calendarViewModel.month() + 1)
                        }
                    }
                )

                22.VerticalSpacer()

                Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    DateText(selectedState)

                    if (!routinesOfDayState.isEmpty()) {
                        AnimatedToggleButton(
                            routineViewState = routineViewState.value,
                        )
                    }
                }
            }


            if (routinesOfDayState.isEmpty()) {
                50.VerticalSpacer()
                EmptyRoutine()
                Spacer(Modifier.height(getReactiveHeight(135)))
            } else if (routinesOfDayState.isNotLoading()) {
                12.VerticalSpacer()
                if (routineViewState.value) {
                    RoutineItems(
                        routinesOfDayState = routinesOfDayState,
                        onItemClick = onItemClick,
                        onRoutineCheck = { check, routine ->
                            routineCheckViewModel.check(check, routine) { routinesOfDay ->
                                selectedDateViewModel.refresh(
                                    routinesOfDay
                                )
                            }
                        }
                    ) {
                        routineDeleteViewModel.delete(it.routineId) {
                            onDelete(it.routineId)
                            findAllMyRoutineViewModel.refresh {
                                selectedDateViewModel.find(selectedState.value)
                            }
                        }
                    }
                } else {
                    RoutineItemsWithCategories(
                        routinesOfDayState = routinesOfDayState,
                        categoryModifyDialogState = categoryModifyDialogState,
                        onItemClick = onItemClick,
                        onRoutineCheck = { check, routine ->
                            routineCheckViewModel.check(check, routine) { routinesOfDay ->
                                selectedDateViewModel.refresh(
                                    routinesOfDay
                                )
                            }
                        },
                        onItemDelete = {
                            routineDeleteViewModel.delete(it.routineId) {
                                onDelete(it.routineId)
                                findAllMyRoutineViewModel.refresh {
                                    selectedDateViewModel.find(selectedState.value)
                                }
                            }
                        },
                        onItemOrderChanged = {
                            findAllMyRoutineViewModel.refresh {
                                selectedDateViewModel.find(selectedState.value)
                            }
                        }
                    )
                }
                20.VerticalSpacer()
            }
        }
    }
}

private fun setOnYearMonthConfirm(
    calendarViewModel: CalendarViewModel,
    calendarMonth: MutableState<Int>,
    findAllMyRoutineViewModel: FindAllMyRoutineViewModel,
    dialogState: MutableState<Boolean>
): (Int, Int) -> Unit {
    val confirmButtonListener: (Int, Int) -> Unit = { selectedYear, selectedMonth ->

        var year = selectedYear
        var month = selectedMonth
        if (selectedMonth == 0) {
            year -= 1
            month = 12
        } else if (selectedMonth == 13) {
            year += 1
            month = 1
        }

        val resultDayList = calendarViewModel.setDate(year, month - 1)
        calendarMonth.value = month

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
    Box {
        PrText(
            text = "${selected.value.month}월 ${selected.value.day}일 ${selected.value.getDayOfWeek()}",
            color = Color.Black,
            fontWeight = FontWeight.W700,
            fontSize = 17.sp,
        )
    }
}


@Composable
private fun YearMonthSelectBox(
    dialogState: MutableState<Boolean>, dateText: String
) {
    Row(
        modifier = Modifier.clickable(
            interactionSource = remember {
                MutableInteractionSource()
            },
            indication = null,
            onClick = {
                dialogState.value = !dialogState.value
            }),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.icon_calendar), contentDescription = "연/월 선택"
        )
        5.HorizontalSpacer()

        PrText(
            text = dateText,
            fontWeight = FontWeight.W700,
            fontSize = 15.sp
        )
        7.HorizontalSpacer()
        Image(
            painter = painterResource(id = R.drawable.icon_date_extend),
            contentDescription = "연/월 선택"
        )
    }
}


@Preview(showBackground = true)
@Composable
fun YearMonthSelectBoxPreview() {
    YearMonthSelectBox(
        dialogState = remember { mutableStateOf(false) },
        dateText = "4월 12일 수요일"
    )
}

@Preview(showBackground = true)
@Composable
fun DateTextPreview() {
    RoumoTheme {
        DateText(
            selected = remember {
                mutableStateOf(
                    CalendarDay(
                        year = 2021,
                        month = 4,
                        day = 12
                    )
                )
            }
        )
    }
}