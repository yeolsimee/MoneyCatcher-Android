package com.yeolsimee.moneysaving.ui.calendar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
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
import com.yeolsimee.moneysaving.ui.theme.*

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
    RoundMoneyIcon(color = Bronze)
}

@Composable
fun RoundMoneyIcon(color: Color) {
    Box(
        Modifier
            .size(28.dp)
            .border(width = 1.5.dp, color = Black17, shape = CircleShape)
            .clip(CircleShape)
            .background(color)
    ) {
        PrText(
            text = "₩",
            style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.W800),
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
    Image(painter = painterResource(id = R.drawable.image_today), contentDescription = "선택됨")
}


@Composable
fun PreviousMonthIcon() {
    Box(
        Modifier
            .size(28.dp)
            .border(width = 1.5.dp, color = Grey17, shape = CircleShape)
            .clip(CircleShape)
    ) {
        PrText(
            text = "₩",
            style = TextStyle(fontSize = 15.sp, color = Grey17, fontWeight = FontWeight.W800),
            modifier = Modifier.align(Alignment.Center)
        )
    }
}


@Composable
fun NextMonthIcon() {
    Box(
        Modifier
            .size(28.dp)
            .border(width = 1.5.dp, color = Grey17, shape = CircleShape)
            .clip(CircleShape)
    )
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
            fontSize = 10.sp,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(8.dp))
        DateIconBuilder(iconState)
        if (selected) {
            Spacer(Modifier.height(4.dp))
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
            .height(2.dp)
            .width(20.dp)
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
        DateIconState.PreviousMonth -> PreviousMonthIcon()
        DateIconState.NextMonth -> NextMonthIcon()
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
            CalendarDay(2023, 4, 16, iconState = DateIconState.PreviousMonth, today = false),
            selectedDay
        ) {}
        DayOfMonthIcon(
            CalendarDay(2023, 4, 17, iconState = DateIconState.NextMonth, today = false),
            selectedDay
        ) {}
    }
}