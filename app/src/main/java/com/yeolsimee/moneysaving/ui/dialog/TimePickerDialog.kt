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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
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
import com.yeolsimee.moneysaving.domain.calendar.AmPmTime
import com.yeolsimee.moneysaving.ui.PrText
import com.yeolsimee.moneysaving.ui.theme.Gray66
import com.yeolsimee.moneysaving.ui.theme.Gray99
import com.yeolsimee.moneysaving.ui.theme.GrayF0
import com.yeolsimee.moneysaving.utils.onClick

@Composable
fun TimePickerDialog(
    dialogState: MutableState<Boolean>,
    hour: Int,
    minute: Int,
    ampm: AmPmTime,
    onConfirmClick: (Int, Int, AmPmTime) -> Unit
) {
    val hourState = remember { mutableIntStateOf(hour) }
    val minuteState = remember { mutableIntStateOf(minute) }
    val ampmState = remember { mutableStateOf(ampm) }

    if (dialogState.value) {
        Dialog(
            onDismissRequest = {
                dialogState.value = false
            }
        ) {
            Box(
                modifier = Modifier
                    .width(231.dp)
                    .height(170.dp)
                    .clip(shape = RoundedCornerShape(4.dp))
                    .background(Color.White)
                    .padding(0.dp)
            ) {
                Column {
                    Box(modifier = Modifier.padding(top = 21.dp, start = 36.dp, end = 36.dp)) {
                        Row(Modifier.fillMaxWidth()) {
                            HourPicker(initialHour = hour, hourState)
                            MinutePicker(initialMinute = minute, minuteState = minuteState)
                            AmPmPicker(initialAmPm = ampm, ampmState = ampmState)
                        }
                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 32.dp),
                            color = GrayF0,
                            thickness = 1.dp
                        )
                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 61.dp),
                            color = GrayF0,
                            thickness = 1.dp
                        )
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .fillMaxWidth()
                        .padding(bottom = 9.dp, end = 14.dp)
                ) {
                    PrText(
                        modifier = Modifier
                            .onClick { dialogState.value = false }
                            .padding(12.dp),
                        text = "취소",
                        color = Gray66,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.W400,
                        lineHeight = 28.sp
                    )
                    PrText(
                        modifier = Modifier
                            .onClick {
                                dialogState.value = false
                                onConfirmClick(
                                    hourState.intValue,
                                    minuteState.intValue,
                                    ampmState.value
                                )
                            }
                            .padding(12.dp),
                        text = "확인",
                        color = Color.Black,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.W600,
                        lineHeight = 28.sp
                    )
                }
            }
        }
    }
}

@SuppressLint("FrequentlyChangedStateReadInComposition")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HourPicker(
    initialHour: Int,
    hourState: MutableState<Int>
) {
    val length = 12
    val height = 93.dp
    val cellSize = height / 3

    val expandedSize = length * 10_000_000
    val initialListPoint = expandedSize / 2
    val targetIndex = initialListPoint + initialHour - 1

    val scrollState = rememberLazyListState(targetIndex)

    if (!scrollState.isScrollInProgress) {
        Log.e(App.TAG, "scroll error")
    }

    LaunchedEffect(Unit) {
        scrollState.scrollToItem(targetIndex - 1)
    }

    Box(
        modifier = Modifier
            .width(53.dp)
            .height(height)
    ) {
        LazyColumn(
            state = scrollState,
            flingBehavior = rememberSnapFlingBehavior(lazyListState = scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            items(expandedSize) {
                val hour = (it % length) + 1
                val firstItem = scrollState.firstVisibleItemIndex % length + 1
                val isCurrent = if (firstItem == 12) 1 == hour else firstItem + 1 == hour
                if (isCurrent) {
                    hourState.value = hour
                }
                Box(
                    modifier = Modifier
                        .height(cellSize),
                    contentAlignment = Alignment.Center
                ) {
                    PrText(
                        text = "$hour",
                        color = if (isCurrent) Color.Black else Gray99,
                        fontWeight = if (isCurrent) FontWeight.SemiBold else FontWeight.Medium,
                        fontSize = if (isCurrent) 17.sp else 14.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = if (isCurrent) 20.sp else 17.sp
                    )
                }
            }
        }
    }
}

@SuppressLint("FrequentlyChangedStateReadInComposition")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MinutePicker(
    initialMinute: Int,
    minuteState: MutableState<Int>
) {
    val length = 60
    val height = 93.dp
    val cellSize = height / 3

    val expandedSize = length * 10_000_000
    val initialListPoint = expandedSize / 2
    val targetIndex = initialListPoint + initialMinute - 1

    val scrollState = rememberLazyListState(targetIndex)

    if (!scrollState.isScrollInProgress) {
        Log.e(App.TAG, "scroll error")
    }

    LaunchedEffect(Unit) {
        scrollState.scrollToItem(targetIndex - 1)
    }

    Box(
        modifier = Modifier
            .width(53.dp)
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
                val isCurrent = if (firstItem == 60) 1 == num else firstItem + 1 == num

                val minuteText = if (num == 60) 0 else num

                if (isCurrent) {
                    minuteState.value = minuteText
                }
                Box(
                    modifier = Modifier
                        .height(cellSize),
                    contentAlignment = Alignment.Center
                ) {
                    PrText(
                        text = String.format("%02d", minuteText),
                        color = if (isCurrent) Color.Black else Gray99,
                        fontWeight = if (isCurrent) FontWeight.SemiBold else FontWeight.Medium,
                        fontSize = if (isCurrent) 17.sp else 14.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

// make ampm picker
@SuppressLint("FrequentlyChangedStateReadInComposition")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AmPmPicker(
    initialAmPm: AmPmTime,
    ampmState: MutableState<AmPmTime>
) {
    val length = 4
    val height = 93.dp
    val cellSize = height / 3

    val targetIndex = if (initialAmPm == AmPmTime.AM) 0 else 1

    val scrollState = rememberLazyListState(targetIndex)

    if (!scrollState.isScrollInProgress) {
        Log.e(App.TAG, "scroll error")
    }

    LaunchedEffect(Unit) {
        scrollState.scrollToItem(targetIndex)
    }

    Box(
        modifier = Modifier
            .width(53.dp)
            .height(height)
    ) {
        LazyColumn(
            state = scrollState,
            flingBehavior = rememberSnapFlingBehavior(lazyListState = scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            items(length) {
                val ampm = if (it <= 1) AmPmTime.AM else AmPmTime.PM
                val firstItem = scrollState.firstVisibleItemIndex + 1
                val isCurrent = if (firstItem <= 1) AmPmTime.AM == ampm else AmPmTime.PM == ampm
                if (isCurrent) {
                    ampmState.value = ampm
                }
                Box(
                    modifier = Modifier
                        .height(cellSize),
                    contentAlignment = Alignment.Center
                ) {
                    val text = if (it == 1 || it == 2) ampm.name else ""
                    PrText(
                        text = text,
                        color = if (isCurrent) Color.Black else Gray99,
                        fontWeight = if (isCurrent) FontWeight.SemiBold else FontWeight.Medium,
                        fontSize = if (isCurrent) 17.sp else 14.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TimePickerDialogPreview() {
    val context = LocalContext.current
    TimePickerDialog(
        dialogState = remember { mutableStateOf(true) },
        hour = 4,
        minute = 0,
        ampm = AmPmTime.AM,
        onConfirmClick = { hour, minute, ampm ->
            Toast.makeText(
                context,
                "${hour}시 ${minute}분 ${if (ampm == AmPmTime.AM) "오전" else "오후"}",
                Toast.LENGTH_SHORT
            ).show()
        }
    )
}
