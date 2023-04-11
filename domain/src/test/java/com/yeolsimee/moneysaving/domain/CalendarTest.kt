package com.yeolsimee.moneysaving.domain

import com.yeolsimee.moneysaving.domain.calendar.CalendarDay
import com.yeolsimee.moneysaving.domain.calendar.isToday
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Calendar

class CalendarTest {
    @Test
    fun CalendarDay에서_2023년_3월_31일_다음_날짜는_2023년_4월_1일이다() {
        val target = CalendarDay(2023, 3, 31, false)
        val result = target.getNextDay()
        val expectedDay = CalendarDay(2023, 4, 1, false)
        assertEquals(expectedDay, result)
    }

    @Test
    fun CalendarDay에서_2024년_2월_28일_다음_날짜는_2024년_2월_29일이다() {
        val target = CalendarDay(2024, 2, 28, false)
        val result = target.getNextDay()
        val expectedDay = CalendarDay(2024, 2, 29, false)
        assertEquals(expectedDay, result)
    }

    @Test
    fun CalendarExtensionTodayCheckTest() {
        val today = Calendar.getInstance()
        assertTrue(today.isToday())
    }
}