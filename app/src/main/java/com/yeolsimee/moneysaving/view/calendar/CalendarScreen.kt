package com.yeolsimee.moneysaving.view.calendar

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.widget.Button
import android.widget.NumberPicker
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import com.yeolsimee.moneysaving.R
import com.yeolsimee.moneysaving.ui.PrText
import com.yeolsimee.moneysaving.ui.calendar.DayOfMonthIcon

@Composable
fun CalendarScreen(viewModel: CalendarViewModel) {

    Column(Modifier.fillMaxSize()) {
        val dialogState = remember { mutableStateOf(false) }
        YearMonthDialog(dialogState, viewModel)

        AppLogoImage()
        Spacer(Modifier.height(16.dp))
        YearMonthSelectBox(dialogState, viewModel)
        ComposeCalendar(viewModel)
    }
}

@Composable
private fun AppLogoImage() {
    Box(
        modifier = Modifier
            .background(Color.Red)
            .width(98.dp)
            .height(30.dp)
    ) {
        PrText(text = "앱 로고 영역", color = Color.White)
    }
}

@Composable
private fun YearMonthSelectBox(
    dialogState: MutableState<Boolean>,
    viewModel: CalendarViewModel
) {
    Row(
        modifier = Modifier.clickable { dialogState.value = !dialogState.value },
        verticalAlignment = Alignment.Bottom
    ) {
        Image(
            painter = painterResource(id = R.drawable.icon_calendar),
            contentDescription = "연/월 선택"
        )
        Spacer(Modifier.width(2.dp))
        PrText(
            text = viewModel.date.observeAsState().value ?: "",
            style = TextStyle(
                fontWeight = FontWeight.W700,
                fontSize = 12.sp
            )
        )
        Image(
            painter = painterResource(id = R.drawable.icon_arrow_open),
            contentDescription = "연/월 선택"
        )
    }
}


@Composable
private fun ComposeCalendar(viewModel: CalendarViewModel) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(10.dp))
        val days = viewModel.dayList.observeAsState().value!!

        // 처음에 선택된 날은 오늘
        var selected = remember { mutableStateOf(viewModel.today) }

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

        Spacer(Modifier.height(8.dp))

        LazyVerticalGrid(columns = GridCells.Fixed(7), contentPadding = PaddingValues(0.dp)) {
            items(days) { date ->
                // TODO date 값 안에 API 연동해서 진행상황 받고 그에 따라 상태값 받음
                DayOfMonthIcon(date, selected) {
                    selected.value = it
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.Center) {
            Button(onClick = {
                viewModel.moveToBeforeMonth()
            }) {
                PrText(text = "Before")
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(onClick = {
                viewModel.moveToNextMonth()
            }) {
                PrText(text = "Next")
            }
        }
    }
}

@Composable
fun DayOfWeekText(text: String) {
    PrText(
        text = text,
        modifier = Modifier
            .width(38.dp)
            .height(12.dp),
        style = TextStyle(
            fontWeight = FontWeight.W700,
            fontSize = 10.sp,
            textAlign = TextAlign.Center
        )
    )
}


@SuppressLint("InflateParams")
@Composable
private fun YearMonthDialog(dialogState: MutableState<Boolean>, viewModel: CalendarViewModel) {
    if (dialogState.value) {
        Dialog(onDismissRequest = {
            dialogState.value = false
        }) {
            AndroidView(factory = { context ->
                val view =
                    LayoutInflater.from(context).inflate(R.layout.year_month_picker, null)

                val yearPicker = view.findViewById<NumberPicker>(R.id.picker_year)
                val monthPicker = view.findViewById<NumberPicker>(R.id.picker_month)
                val cancelButton = view.findViewById<Button>(R.id.btn_cancel)
                val confirmButton = view.findViewById<Button>(R.id.btn_confirm)

                yearPicker.minValue = 2023
                yearPicker.maxValue = 2099
                yearPicker.value = viewModel.year()

                monthPicker.minValue = 1
                monthPicker.maxValue = 12
                monthPicker.value = viewModel.month()

                cancelButton.setOnClickListener {
                    dialogState.value = false
                }

                confirmButton.setOnClickListener {
                    viewModel.setDate(yearPicker.value, monthPicker.value - 1)
                    dialogState.value = false
                }

                view
            })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppLogoPreview() {
    AppLogoImage()
}

@Preview(showBackground = true)
@Composable
fun YearMonthSelectBoxPreview() {
    YearMonthSelectBox(
        dialogState = remember { mutableStateOf(false) },
        viewModel = CalendarViewModel()
    )
}

@Preview(showBackground = true)
@Composable
fun YearMonthDialogPreview() {
    YearMonthDialog(
        dialogState = remember { mutableStateOf(true) },
        viewModel = CalendarViewModel()
    )
}