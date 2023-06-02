package com.yeolsimee.moneysaving.ui.dialog

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.yeolsimee.moneysaving.App
import com.yeolsimee.moneysaving.ui.PrText
import com.yeolsimee.moneysaving.ui.theme.Gray66
import com.yeolsimee.moneysaving.ui.theme.Gray99

@Composable
fun YearMonthDialog(
    dialogState: MutableState<Boolean>,
    year: Int,
    month: Int,
    onConfirmClick: (Int, Int) -> Unit
) {
    val yearState = remember { mutableIntStateOf(year) }
    val monthState = remember { mutableIntStateOf(month) }
    if (dialogState.value) {
        Dialog(
            onDismissRequest = {
                dialogState.value = false
            }) {
            Box(
                modifier = Modifier
                    .width(230.dp)
                    .wrapContentHeight()
                    .clip(shape = RoundedCornerShape(4.dp))
                    .background(Color.White)
                    .padding(0.dp)
            ) {
                Column {
                    PrText(
                        text = "${year}년 ${month}월",
                        fontWeight = FontWeight.W600,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(start = 20.dp, top = 20.dp, bottom = 20.dp)
                    )
                    Row(Modifier.fillMaxWidth()) {
                        YearPicker(initialYear = year, yearState)
                        MonthPicker(initialMonth = month, monthState)
                    }
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, end = 8.dp, bottom = 12.dp)
                    ) {
                        TextButton(onClick = {
                            dialogState.value = false
                        }) {
                            PrText(
                                text = "취소",
                                color = Gray66,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W400
                            )
                        }
                        TextButton(onClick = {
                            dialogState.value = false
                            onConfirmClick(yearState.intValue, monthState.intValue)
                        }) {
                            PrText(
                                text = "확인",
                                color = Color.Black,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W500
                            )
                        }
                    }
                }
            }
        }
    }
}

@SuppressLint("FrequentlyChangedStateReadInComposition")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun YearPicker(
    initialYear: Int,
    yearState: MutableState<Int>
) {
    val length = 2099 - 2023 + 1
    val height = 78.dp
    val cellSize = height / 3

    val expandedSize = length * 10_000_000
    val initialListPoint = expandedSize / 2
    val targetIndex = initialListPoint + initialYear - 2023

    val scrollState = rememberLazyListState(targetIndex)

    if (!scrollState.isScrollInProgress) {
        Log.e(App.TAG, "scroll error")
    }

    LaunchedEffect(Unit) {
        scrollState.scrollToItem(targetIndex - 1)
    }

    Box(
        modifier = Modifier
            .width(115.dp)
            .height(height)
    ) {
        LazyColumn(
            state = scrollState,
            flingBehavior = rememberSnapFlingBehavior(lazyListState = scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            items(expandedSize) {
                val num = (it % length) + 1
                val firstItem = scrollState.firstVisibleItemIndex % length + 1
                val isCurrent = if (firstItem == 2099 - 2022) 1 == num else firstItem + 1 == num
                val year = num + 2022
                if (isCurrent) {
                    yearState.value = year
                }
                Box(
                    modifier = Modifier
                        .height(cellSize),
                    contentAlignment = Alignment.Center
                ) {
                    PrText(
                        text = "$year",
                        color = if (isCurrent) Color.Black else Gray99,
                        fontWeight = if (isCurrent) FontWeight.W600 else FontWeight.W500,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@SuppressLint("FrequentlyChangedStateReadInComposition")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MonthPicker(
    initialMonth: Int,
    monthState: MutableState<Int>
) {
    val length = 12
    val height = 78.dp
    val cellSize = height / 3

    val expandedSize = length * 10_000_000
    val initialListPoint = expandedSize / 2
    val targetIndex = initialListPoint + initialMonth - 1

    val scrollState = rememberLazyListState(targetIndex)

    if (!scrollState.isScrollInProgress) {
        Log.e(App.TAG, "scroll error")
    }

    LaunchedEffect(Unit) {
        scrollState.scrollToItem(targetIndex - 1)
    }

    Box(
        modifier = Modifier
            .width(115.dp)
            .height(height)
    ) {
        LazyColumn(
            state = scrollState,
            flingBehavior = rememberSnapFlingBehavior(lazyListState = scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            items(expandedSize) {
                val num = (it % length) + 1
                val firstItem = scrollState.firstVisibleItemIndex % length + 1
                val isCurrent = if (firstItem == 12) 1 == num else firstItem + 1 == num
                if (isCurrent) {
                    monthState.value = num
                }
                Box(
                    modifier = Modifier
                        .height(cellSize),
                    contentAlignment = Alignment.Center
                ) {
                    PrText(
                        text = "${num}월",
                        color = if (isCurrent) Color.Black else Gray99,
                        fontWeight = if (isCurrent) FontWeight.W600 else FontWeight.W500,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun YearMonthDialogPreview() {
    val context = LocalContext.current
    YearMonthDialog(
        dialogState = remember { mutableStateOf(true) },
        year = 2023,
        month = 1,
        onConfirmClick = { year, month ->
            Toast.makeText(context, "${year}년 ${month}월", Toast.LENGTH_SHORT).show()
        }
    )
}
