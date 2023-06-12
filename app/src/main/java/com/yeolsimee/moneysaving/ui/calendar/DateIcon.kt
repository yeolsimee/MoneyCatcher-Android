package com.yeolsimee.moneysaving.ui.calendar

import androidx.annotation.IntRange
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yeolsimee.moneysaving.R
import com.yeolsimee.moneysaving.domain.calendar.CalendarDay
import com.yeolsimee.moneysaving.domain.calendar.DateIconState
import com.yeolsimee.moneysaving.ui.PrText
import com.yeolsimee.moneysaving.ui.theme.Black17
import com.yeolsimee.moneysaving.ui.theme.Gold
import com.yeolsimee.moneysaving.ui.theme.Gray17
import com.yeolsimee.moneysaving.ui.theme.Gray66
import com.yeolsimee.moneysaving.ui.theme.GrayF0
import com.yeolsimee.moneysaving.ui.theme.RoumoTheme
import com.yeolsimee.moneysaving.ui.theme.Silver
import com.yeolsimee.moneysaving.utils.getTextFromDayOfWeek

@Composable
fun GoldIcon() {
    RoundMoneyIcon(color = Gold)
}

@Composable
fun SilverIcon() {
    RoundMoneyIcon(color = Silver)
}

@Composable
fun BronzeIcon() {
    RoundMoneyIcon(color = Color.White)
}

@Composable
fun RoundMoneyIcon(color: Color) {
    Box(
        Modifier
            .size(28.dp)
            .border(width = 2.dp, color = Black17, shape = CircleShape)
            .clip(CircleShape)
            .background(color)
    ) {
        PrText(
            text = "₩",
            style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Black),
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun EmptyMoneyIcon() {
    Box(
        Modifier
            .size(28.dp)
            .border(width = 1.5.dp, color = Black17, shape = CircleShape)
            .clip(CircleShape)
            .background(Color.White)
    )
}

@Composable
fun TodayIcon() {
    Image(
        painter = painterResource(id = R.drawable.image_today),
        contentDescription = "오늘",
        modifier = Modifier.size(28.dp)
    )
}


@Composable
fun OtherMonthIcon() {
    Box(
        Modifier
            .size(28.dp)
            .border(width = 1.5.dp, color = Gray17, shape = CircleShape)
            .clip(CircleShape)
    ) {
        PrText(
            text = "₩",
            fontSize = 15.sp, color = Gray17, fontWeight = FontWeight.W800,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}


@Composable
fun EmptyOtherMonthIcon() {
    Box(
        Modifier
            .size(28.dp)
            .border(width = 1.5.dp, color = Gray17, shape = CircleShape)
            .clip(CircleShape)
    )
}

@Composable
fun DayOfWeekIcon(
    @IntRange(from = 0, to = 6) dayOfWeek: Int,
    initialSelected: Boolean,
    onClick: (Int) -> Unit
) {
    val text = getTextFromDayOfWeek(dayOfWeek)
    val selected = remember { mutableStateOf(initialSelected) }

    if (selected.value) {
        SelectedDayOfWeek(dayOfWeek, text) {
            onClick(dayOfWeek)
            selected.value = !selected.value
        }
    } else {
        UnSelectedDayOfWeek(dayOfWeek, text) {
            onClick(dayOfWeek)
            selected.value = !selected.value
        }
    }
}

@Composable
private fun SelectedDayOfWeek(
    dayOfWeek: Int,
    text: String,
    onClick: (Int) -> Unit,
) {
    Box(
        Modifier
            .size(32.dp)
            .border(width = 1.5.dp, color = Color.Black, shape = CircleShape)
            .clip(CircleShape)
            .background(color = GrayF0)
            .clickable(
                interactionSource = remember {
                    MutableInteractionSource()
                },
                indication = null,
                onClick = { onClick(dayOfWeek) })
    ) {
        PrText(
            text = text,
            fontWeight = FontWeight.W600,
            fontSize = 13.sp,
            color = Color.Black,
            modifier = Modifier.align(
                Alignment.Center
            )
        )
    }
}

@Composable
private fun UnSelectedDayOfWeek(
    dayOfWeek: Int,
    text: String,
    onClick: (Int) -> Unit,
) {
    Box(
        Modifier
            .size(32.dp)
            .border(width = 1.5.dp, color = GrayF0, shape = CircleShape)
            .clip(CircleShape)
            .clickable(
                interactionSource = remember {
                    MutableInteractionSource()
                },
                indication = null,
                onClick = { onClick(dayOfWeek) })
    ) {
        PrText(
            text = text,
            fontWeight = FontWeight.W500,
            fontSize = 13.sp,
            color = Gray66,
            modifier = Modifier.align(
                Alignment.Center
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DayOfWeekIconPreview() {
    RoumoTheme {
        Row(modifier = Modifier) {
            DayOfWeekIcon(0, false) {}
            DayOfWeekIcon(6, true) {}
        }
    }
}

@Composable
fun DayOfMonthIcon(
    date: CalendarDay,
    selectedDay: MutableState<CalendarDay>,
    modifier: Modifier = Modifier,
    onClick: (CalendarDay) -> Unit
) {
    val day = date.day
    val iconState = date.iconState
    val selected = date == selectedDay.value

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .width(38.dp)
            .wrapContentHeight()
            .clickable(interactionSource = remember {
                MutableInteractionSource()
            }, indication = null, onClick = { onClick(date) })
    ) {
        PrText(
            text = "$day",
            fontWeight = if (selected) FontWeight.W800 else FontWeight.W500,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(6.dp))
        DateIconBuilder(iconState)
        if (selected) {
            Spacer(Modifier.height(6.dp))
            Divider()
            Spacer(Modifier.height(4.dp))
        } else {
            Spacer(Modifier.height(10.dp))
        }
    }
}

@Composable
private fun Divider() {
    Box(
        Modifier
            .height(2.5.dp)
            .width(24.dp)
            .clip(RoundedCornerShape(size = 4.dp))
            .background(color = Color.Black)
    )
}

@Composable
fun DateIconBuilder(iconState: DateIconState) {
    when (iconState) {
        DateIconState.Gold -> GoldIcon()
        DateIconState.Silver -> SilverIcon()
        DateIconState.Bronze -> BronzeIcon()
        DateIconState.Today -> TodayIcon()
        DateIconState.Empty -> EmptyMoneyIcon()
        DateIconState.OtherMonth -> OtherMonthIcon()
        DateIconState.EmptyOtherMonth -> EmptyOtherMonthIcon()
    }
}

@Preview(showBackground = true)
@Composable
fun IconPreviews() {
    Row {
        GoldIcon()
        SilverIcon()
        BronzeIcon()
        TodayIcon()
        EmptyMoneyIcon()
    }
}

@Preview(showBackground = true)
@Composable
fun DayOfMonthIconPreview() {
    val selectedDay = remember { mutableStateOf(CalendarDay(2023, 4, 16, false)) }
    Row {
        DayOfMonthIcon(
            CalendarDay(2023, 4, 11, iconState = DateIconState.Gold, today = false),
            selectedDay
        ) {}
        DayOfMonthIcon(
            CalendarDay(2023, 4, 12, iconState = DateIconState.Silver, today = false),
            selectedDay
        ) {}
        DayOfMonthIcon(
            CalendarDay(2023, 4, 13, iconState = DateIconState.Bronze, today = false),
            selectedDay
        ) {}
        DayOfMonthIcon(
            CalendarDay(2023, 4, 14, iconState = DateIconState.Today, today = true),
            selectedDay
        ) {}
        DayOfMonthIcon(
            CalendarDay(2023, 4, 15, iconState = DateIconState.Empty, today = false),
            selectedDay
        ) {}
        DayOfMonthIcon(
            CalendarDay(2023, 4, 16, iconState = DateIconState.OtherMonth, today = false),
            selectedDay
        ) {}
        DayOfMonthIcon(
            CalendarDay(2023, 4, 17, iconState = DateIconState.EmptyOtherMonth, today = false),
            selectedDay
        ) {}
    }
}