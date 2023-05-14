package com.yeolsimee.moneysaving.view.home.calendar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    val selected = remember { mutableStateOf(CalendarDay(2023, 4, 12)) }
    val spread = remember { mutableStateOf(true) }
    val calendarMonth = remember { mutableStateOf(4) }
    ComposeCalendar(days, selected, spread, 2023, 4, calendarMonth, {}) {}
}