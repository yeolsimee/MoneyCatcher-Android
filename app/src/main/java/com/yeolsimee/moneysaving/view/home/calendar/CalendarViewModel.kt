package com.yeolsimee.moneysaving.view.home.calendar

import androidx.lifecycle.ViewModel
import com.yeolsimee.moneysaving.domain.calendar.CalendarDay
import com.yeolsimee.moneysaving.domain.calendar.getWeekDays
import com.yeolsimee.moneysaving.domain.calendar.setNextDay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(): ViewModel() {

    private var lastDayOfMonth: Int
    private val calendar = Calendar.getInstance()

    private var _today: CalendarDay
    val today: CalendarDay
        get() = _today

    private val _dayList: MutableStateFlow<MutableList<CalendarDay>>
    val dayList: StateFlow<MutableList<CalendarDay>>
        get() = _dayList


    init {
        calendar.firstDayOfWeek = Calendar.MONDAY
        val todayCalendar = Calendar.getInstance()
        todayCalendar.time = Date()

        _today = CalendarDay(
            todayCalendar.get(Calendar.YEAR),
            todayCalendar.get(Calendar.MONTH) + 1,
            todayCalendar.get(Calendar.DATE),
            true
        )
        lastDayOfMonth = todayCalendar.getMaximum(Calendar.DAY_OF_MONTH)
        _dayList = MutableStateFlow(getWeekDays(calendar))
    }
    fun year(): Int {
        return calendar.get(Calendar.YEAR)
    }

    fun month(): Int {
        return calendar.get(Calendar.MONTH) + 1
    }

    private val _date =
        MutableStateFlow("${calendar.get(Calendar.YEAR)}년 ${calendar.get(Calendar.MONTH) + 1}월")

    val date: MutableStateFlow<String>
        get() = _date

    fun setDate(year: Int, month: Int): MutableList<CalendarDay> {
        calendar.setNextDay(year, month)
        _date.value = "${year}년 ${month + 1}월"
        return getWeekDays(calendar)
    }

    fun getFirstAndLastDate(resultDayList: MutableList<CalendarDay>): Pair<CalendarDay, CalendarDay>? {
        if (resultDayList.isEmpty()) return null
        return Pair(resultDayList.first(), resultDayList.last())
    }
}