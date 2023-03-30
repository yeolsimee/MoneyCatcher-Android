package com.yeolsimee.moneysaving.view.calendar

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.widget.Button
import android.widget.NumberPicker
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import com.yeolsimee.moneysaving.R

@Composable
fun CalendarScreen(viewModel: CalendarViewModel) {

    val dialogState = remember { mutableStateOf(false) }
    YearMonthDialog(dialogState, viewModel)
    Button(onClick = {
        dialogState.value = !dialogState.value
    }) {
        Text(text = "현재: ${viewModel.date.observeAsState().value}")
    }
    Spacer(modifier = Modifier.height(12.dp))

    ComposeCalendar(viewModel)
}


@Composable
private fun ComposeCalendar(viewModel: CalendarViewModel) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(10.dp))
        val days = viewModel.dayList.observeAsState().value!!

        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "일", Modifier.padding(12.dp), textAlign = TextAlign.Center)
            Text(text = "월", Modifier.padding(12.dp), textAlign = TextAlign.Center)
            Text(text = "화", Modifier.padding(12.dp), textAlign = TextAlign.Center)
            Text(text = "수", Modifier.padding(12.dp), textAlign = TextAlign.Center)
            Text(text = "목", Modifier.padding(12.dp), textAlign = TextAlign.Center)
            Text(text = "금", Modifier.padding(12.dp), textAlign = TextAlign.Center)
            Text(text = "토", Modifier.padding(12.dp), textAlign = TextAlign.Center)
        }

        LazyVerticalGrid(columns = GridCells.Fixed(7)) {
            items(days) { date ->
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .padding(8.dp)
                ) {
                    Text(
                        text = "${date.day}",
                        Modifier
                            .fillMaxSize()
                            .background(if (date.today) Color.Green else Color.LightGray)
                            .padding(8.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.Center) {
            Button(onClick = {
                viewModel.moveToBeforeMonth()
            }) {
                Text(text = "Before")
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(onClick = {
                viewModel.moveToNextMonth()
            }) {
                Text(text = "Next")
            }
        }
    }
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