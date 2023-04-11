package com.yeolsimee.moneysaving.view.calendar

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yeolsimee.moneysaving.domain.calendar.CalendarDay
import com.yeolsimee.moneysaving.domain.calendar.getWeekDays
import com.yeolsimee.moneysaving.domain.calendar.setNextDay
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor() : ViewModel() {

    private var lastDayOfMonth: Int

    private var _today: CalendarDay

    val today: CalendarDay get() = _today

    private val calendar = Calendar.getInstance()

    init {
        val todayCalendar = Calendar.getInstance()
        val now = Date()
        todayCalendar.time = now

        _today = CalendarDay(
            todayCalendar.get(Calendar.YEAR),
            todayCalendar.get(Calendar.MONTH) + 1,
            todayCalendar.get(Calendar.DATE),
            true
        )

        lastDayOfMonth = todayCalendar.getMaximum(Calendar.DAY_OF_MONTH)
        Log.i("TAG", "현재 월: ${calendar.get(Calendar.MONTH) + 1}")
    }

    fun year(): Int {
        return calendar.get(Calendar.YEAR)
    }

    fun month(): Int {
        return calendar.get(Calendar.MONTH) + 1
    }

    private val _date: MutableLiveData<String> =
        MutableLiveData("${calendar.get(Calendar.YEAR)}년 ${calendar.get(Calendar.MONTH) + 1}월")

    val date: MutableLiveData<String>
        get() = _date

    fun setDate(year: Int, month: Int) {
        calendar.setNextDay(year, month)
        _date.value = "${year}년 ${month + 1}월"
        _dayList.value = getWeekDays(calendar)
    }

    fun moveToNextMonth() {
        calendar.add(Calendar.MONTH, 1)
        _dayList.value = getWeekDays(calendar)
        _date.value =
            "${calendar.get(Calendar.YEAR)}년 ${calendar.get(Calendar.MONTH) + 1}월"
    }

    fun moveToBeforeMonth() {
        calendar.add(Calendar.MONTH, -1)
        _dayList.value = getWeekDays(calendar)
        _date.value =
            "${calendar.get(Calendar.YEAR)}년 ${calendar.get(Calendar.MONTH) + 1}월"
    }

    private val _dayList: MutableLiveData<MutableList<CalendarDay>> =
        MutableLiveData(getWeekDays(calendar))

    val dayList: LiveData<MutableList<CalendarDay>>
        get() = _dayList
}