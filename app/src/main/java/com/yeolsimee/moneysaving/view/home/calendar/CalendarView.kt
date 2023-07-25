@file:OptIn(ExperimentalFoundationApi::class)

package com.yeolsimee.moneysaving.view.home.calendar

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yeolsimee.moneysaving.R
import com.yeolsimee.moneysaving.domain.calendar.CalendarDay
import com.yeolsimee.moneysaving.domain.calendar.getWeekDays
import com.yeolsimee.moneysaving.ui.calendar.DayOfMonthIcon
import com.yeolsimee.moneysaving.view.home.DayOfWeekText
import java.util.Calendar

@Composable
fun ComposeCalendar(
    days: MutableList<CalendarDay>,
    selected: MutableState<CalendarDay>,
    spread: MutableState<Boolean>,
    year: Int,
    month: Int,
    calendarMonth: MutableState<Int>,
    restoreSelected: (Int) -> Unit,
    onItemSelected: (CalendarDay) -> Unit,
    onPageChanged: (PageChangedState) -> Unit,
    modifier: Modifier,
) {
    Box(modifier = modifier.background(Color.Transparent)) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DayOfWeekIndicator(modifier = modifier)
            Box(modifier = modifier.height(6.dp))

            CalendarGrid(
                modifier,
                days,
                spread,
                selected,
                month,
                calendarMonth,
                onItemSelected,
                onPageChanged
            )
            Box(modifier = modifier.height(13.5.dp))

            CalendarSpreadButton(
                modifier,
                spread,
                selected,
                year,
                month,
                calendarMonth,
                restoreSelected
            )
        }
    }
}


@Composable
private fun DayOfWeekIndicator(modifier: Modifier) {
    LazyHorizontalGrid(
        modifier = modifier
            .fillMaxWidth()
            .height(14.32.dp),
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
    modifier: Modifier,
    days: MutableList<CalendarDay>,
    spread: MutableState<Boolean>,
    selected: MutableState<CalendarDay>,
    month: Int,
    calendarMonth: MutableState<Int>,
    onItemSelected: (CalendarDay) -> Unit,
    onPageChanged: (PageChangedState) -> Unit,
) {
    val pageState = rememberPagerState(pageCount = { 1000 }, initialPage = 500)
    val currentPage = remember { mutableIntStateOf(500) }

    LaunchedEffect(pageState) {
        snapshotFlow { pageState.currentPage }.collect { page ->
            if (spread.value) {
                if (currentPage.intValue < page) {
                    currentPage.intValue = page
                    onPageChanged(PageChangedState.NEXT)
                } else if (currentPage.intValue > page) {
                    currentPage.intValue = page
                    onPageChanged(PageChangedState.PREV)
                }
                Log.i("ComposeCalendar", "page: ${page}, currentPage: ${pageState.currentPage}")
            }
        }
    }

    HorizontalPager(state = pageState, pageSpacing = 56.dp, verticalAlignment = Alignment.Top) { page ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier.heightIn(min = 68.dp, max = (68 * 7).dp)
        ) {
            val dayList = if (page < currentPage.intValue) getWeekDays(days.first().getPreviousCalendar())
            else if (page == currentPage.intValue) days
            else getWeekDays(days.last().getNextCalendar())

            Log.i("ComposeCalendar", "page: ${page}, currentPage: ${currentPage.intValue}")

            items(dayList) { date ->
                AnimatedVisibility(visible = spread.value) {
                    DayOfMonthIcon(date, selected, modifier = modifier) {
                        selected.value = it
                        calendarMonth.value = month
                        onItemSelected(it)
                    }
                }
                AnimatedVisibility(visible = !spread.value) {
                    if (date.isSameWeek(selected.value)) {
                        DayOfMonthIcon(date, selected, modifier = modifier) {
                            selected.value = it
                            calendarMonth.value = month
                            onItemSelected(it)
                        }
                    }
                }
            }
        }
    }
}

enum class PageChangedState {
    PREV, NEXT
}

@Composable
private fun CalendarSpreadButton(
    modifier: Modifier,
    spread: MutableState<Boolean>,
    selected: MutableState<CalendarDay>,
    year: Int,
    month: Int,
    calendarMonth: MutableState<Int>,
    restoreSelected: (Int) -> Unit,
) {
    Image(
        modifier = modifier.clickable(
            interactionSource = remember {
                MutableInteractionSource()
            },
            indication = null,
            onClick = {
                if (spread.value && (selected.value.year != year || calendarMonth.value - 1 != month)) {
                    restoreSelected(calendarMonth.value - 1)
                }
                spread.value = !spread.value
            }
        ),
        painter = if (spread.value) painterResource(id = R.drawable.icon_bigarrow_close)
        else painterResource(id = R.drawable.icon_bigarrow_open),
        contentDescription = "닫기"
    )
}

@Preview(showBackground = true)
@Composable
fun ComposeCalendarPreview() {
    val days = getWeekDays(Calendar.getInstance())
    val selected = remember { mutableStateOf(CalendarDay(2023, 6, 29)) }
    val spread = remember { mutableStateOf(true) }
    val calendarMonth = remember { mutableIntStateOf(4) }
    ComposeCalendar(
        days, selected, spread, 2023, 4, calendarMonth, {}, {},
        onPageChanged = {},
        Modifier.draggable(
            orientation = Orientation.Vertical,
            state = rememberDraggableState { dy ->
                spread.value = dy > 0
            }
        ),
    )
}