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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
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
import com.yeolsimee.moneysaving.domain.calendar.getWeekDays
import com.yeolsimee.moneysaving.ui.PrText
import com.yeolsimee.moneysaving.ui.calendar.DayOfMonthIcon
import com.yeolsimee.moneysaving.utils.getMonthFromPage
import com.yeolsimee.moneysaving.utils.getMonthsPassedSince2023

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
            CalendarGrid(
                modifier,
                days,
                spread,
                selected,
                year,
                month,
                calendarMonth,
                onItemSelected,
                onPageChanged
            )
            Box(modifier = modifier.height(4.dp))

            CalendarSpreadButton(
                modifier,
                spread,
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
fun DayOfWeekText(text: String) {
    val config = LocalConfiguration.current
    val itemWidth = (config.screenWidthDp.dp - (28.dp * 2)) / 7
    PrText(
        text = text,
        modifier = Modifier
            .width(itemWidth),
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        lineHeight = 14.32.sp,
        textAlign = TextAlign.Center
    )
}


@Composable
private fun CalendarGrid(
    modifier: Modifier,
    days: MutableList<CalendarDay>,
    spread: MutableState<Boolean>,
    selected: MutableState<CalendarDay>,
    year: Int,
    month: Int,
    calendarMonth: MutableState<Int>,
    onItemSelected: (CalendarDay) -> Unit,
    onPageChanged: (PageChangedState) -> Unit,
) {
    val initialPage = getMonthsPassedSince2023(year, month)
    val pageState = rememberPagerState(initialPage = initialPage)
    val currentPage = remember { mutableStateOf(initialPage) }

    LaunchedEffect(pageState) {
        snapshotFlow { pageState.currentPage }.collect { page ->
            if (spread.value) {
                if (currentPage.value < page) {
                    onPageChanged(PageChangedState.NEXT)
                } else if (currentPage.value > page) {
                    onPageChanged(PageChangedState.PREV)
                }
                Log.i("ComposeCalendar", "page: ${page}, currentPage: ${pageState.currentPage}")
                currentPage.value = page
            }
        }
    }

    // year와 month가 변경될 때마다 실행되는 LaunchedEffect
    LaunchedEffect(year, month) {
        val newInitialPage = getMonthsPassedSince2023(year, month)
        currentPage.value = newInitialPage
        pageState.scrollToPage(newInitialPage)
    }

    HorizontalPager(
        pageCount = 924,
        state = pageState,
        pageSpacing = 56.dp,
        verticalAlignment = Alignment.Top,
        userScrollEnabled = spread.value
    ) { page ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier.heightIn(min = 68.dp, max = (68 * 7).dp)
        ) {
            if (days.isNotEmpty()) {
                val currentMonth = getMonthFromPage(page)
                Log.i("ComposeCalendar", "page: ${page}, currentPage1: ${pageState.currentPage}, currentPage2: ${currentPage.value}, days[15].month: ${days[15].month}, currentMonth: $currentMonth")
                val dayList = if (page < currentPage.value) getWeekDays(days.first().getPreviousCalendar())
                else if (page == currentPage.value) days
                else getWeekDays(days.last().getNextCalendar())

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
}

enum class PageChangedState {
    PREV, NEXT
}

@Composable
private fun CalendarSpreadButton(
    modifier: Modifier = Modifier,
    spread: MutableState<Boolean> = mutableStateOf(true),
    calendarMonth: MutableState<Int> = mutableStateOf(4),
    restoreSelected: (Int) -> Unit = {},
) {
    Image(
        modifier = modifier.clickable(
            interactionSource = remember {
                MutableInteractionSource()
            },
            indication = null,
            onClick = {
                restoreSelected(calendarMonth.value - 1)
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
    val calendarMonth = remember { mutableStateOf(4) }
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