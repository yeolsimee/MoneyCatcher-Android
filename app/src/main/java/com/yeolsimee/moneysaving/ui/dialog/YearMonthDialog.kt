package com.yeolsimee.moneysaving.ui.dialog

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.widget.Button
import android.widget.NumberPicker
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import com.yeolsimee.moneysaving.R

@SuppressLint("InflateParams")
@Composable
fun YearMonthDialog(
    dialogState: MutableState<Boolean>,
    year: Int,
    month: Int,
    cancelButtonListener: () -> Unit,
    confirmButtonListener: (NumberPicker, NumberPicker) -> Unit
) {
    if (dialogState.value) {
        Dialog(onDismissRequest = {
            dialogState.value = false
        }) {
            AndroidView(factory = { context ->
                val view = LayoutInflater.from(context).inflate(R.layout.year_month_picker, null)

                val yearPicker = view.findViewById<NumberPicker>(R.id.picker_year)
                val monthPicker = view.findViewById<NumberPicker>(R.id.picker_month)
                val cancelButton = view.findViewById<Button>(R.id.btn_cancel)
                val confirmButton = view.findViewById<Button>(R.id.btn_confirm)

                yearPicker.minValue = 2023
                yearPicker.maxValue = 2099
                yearPicker.value = year

                monthPicker.minValue = 1
                monthPicker.maxValue = 12
                monthPicker.value = month

                cancelButton.setOnClickListener {
                    cancelButtonListener()
                }

                confirmButton.setOnClickListener {
                    confirmButtonListener(yearPicker, monthPicker)
                }

                view
            })
        }
    }
}


@Preview(showBackground = true)
@Composable
fun YearMonthDialogPreview() {
    YearMonthDialog(
        dialogState = remember { mutableStateOf(true) },
        year = 2023,
        month = 4,
        cancelButtonListener = {},
        confirmButtonListener = { _, _ -> }
    )
}