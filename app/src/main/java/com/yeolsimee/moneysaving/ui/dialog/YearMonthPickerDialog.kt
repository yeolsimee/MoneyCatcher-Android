package com.yeolsimee.moneysaving.ui.dialog

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.NumberPicker
import androidx.fragment.app.DialogFragment
import com.yeolsimee.moneysaving.R

class YearMonthPickerDialog(
    private var activity: Activity,
    private var listener: DatePickerYearMonthListener,
    private var firstYear: Int,
    private var firstMonth: Int
) : DialogFragment() {

    interface DatePickerYearMonthListener {
        fun onDateSet(year: Int, month: Int)
    }

    companion object {
        private const val MAX_YEAR = 2099
        private const val MIN_YEAR = 2023
    }

    private var btnConfirm: Button? = null
    private var btnCancel: Button? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        val dialog: View = inflater.inflate(R.layout.year_month_picker, null).also {
            btnConfirm = it.findViewById(R.id.btn_confirm)
            btnCancel = it.findViewById(R.id.btn_cancel)
        }

        val monthPicker =
            dialog.findViewById<View>(R.id.picker_month) as NumberPicker
        val yearPicker =
            dialog.findViewById<View>(R.id.picker_year) as NumberPicker

        monthPicker.minValue = 1
        monthPicker.maxValue = 12
        monthPicker.value = firstMonth

        yearPicker.minValue = MIN_YEAR
        yearPicker.maxValue = MAX_YEAR
        yearPicker.value = firstYear
        builder.setView(dialog)

        btnConfirm?.setOnClickListener {
            listener.onDateSet(yearPicker.value, monthPicker.value)
            dismiss()
        }
        btnCancel?.setOnClickListener {
            dismiss()
        }

        return builder.create()
    }

}