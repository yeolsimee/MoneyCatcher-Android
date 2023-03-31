package com.yeolsimee.moneysaving.view.calendar

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yeolsimee.moneysaving.domain.calendar.CalendarDay
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor() : ViewModel() {

    private var lastDayOfMonth: Int

    private var today: CalendarDay
    private val calendar = Calendar.getInstance()
    private val monthCalendar = Calendar.getInstance()

    init {
        val todayCalendar = Calendar.getInstance()
        val now = Date()
        todayCalendar.time = now
        monthCalendar.time = now

        today = CalendarDay(
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
        return calendar.get(Calendar.MONTH)
    }

    private val _date: MutableLiveData<String> =
        MutableLiveData("${monthCalendar.get(Calendar.YEAR)}년 ${monthCalendar.get(Calendar.MONTH) + 1}월")

    val date: MutableLiveData<String>
        get() = _date

    fun setDate(year: Int, month: Int) {
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        monthCalendar.set(Calendar.YEAR, year)
        monthCalendar.set(Calendar.MONTH, month)
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1)
        _date.value = "${year}년 ${month+1}월"
        _dayList.value = getWeekDays()
    }

    fun moveToNextMonth() {
        calendar.add(Calendar.MONTH, 0)
        monthCalendar.add(Calendar.MONTH, 1)
        _dayList.value = getWeekDays()
        _date.value =
            "${monthCalendar.get(Calendar.YEAR)}년 ${monthCalendar.get(Calendar.MONTH) + 1}월"
    }

    fun moveToBeforeMonth() {
        calendar.add(Calendar.MONTH, -2)
        monthCalendar.add(Calendar.MONTH, -1)
        _dayList.value = getWeekDays()
        _date.value =
            "${monthCalendar.get(Calendar.YEAR)}년 ${monthCalendar.get(Calendar.MONTH) + 1}월"
    }

    private val _dayList: MutableLiveData<MutableList<CalendarDay>> = MutableLiveData(getWeekDays())

    val dayList: LiveData<MutableList<CalendarDay>>
        get() = _dayList

    private fun getWeekDays(): MutableList<CalendarDay> {
        val tempDayList = mutableListOf<CalendarDay>()

        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val month = calendar.get(Calendar.MONTH) + 1
        for (i in 0..5) {
            for (j in 0..6) {
                calendar.add(Calendar.DAY_OF_MONTH, (1 - calendar.get(Calendar.DAY_OF_WEEK)) + j)

                tempDayList.add(
                    CalendarDay(
                        year = calendar.get(Calendar.YEAR),
                        month = calendar.get(Calendar.MONTH) + 1,
                        day = calendar.get(Calendar.DAY_OF_MONTH),
                        today = today.isToday(calendar)
                    )
                )
            }

            val lastDay = tempDayList.last()
            Log.i("check", "${lastDay.month}월 ${lastDay.day}일")
            if (lastDay.month != month) {
                break
            }
            calendar.add(Calendar.WEEK_OF_MONTH, 1)
        }
        return tempDayList
    }
}