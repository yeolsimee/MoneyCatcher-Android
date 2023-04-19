package com.yeolsimee.moneysaving.domain.calendar

enum class DateIconState {
    Gold, Silver, Bronze, Today, PreviousMonth, NextMonth, Empty;

    companion object {
        fun getStateFromRoutineAchievement(routineAchievement: String): DateIconState {
            return when (routineAchievement) {
                "GOLD" -> {
                    Gold
                }
                "SILVER" -> {
                    Silver
                }
                "BRONZE" -> {
                    Bronze
                }
                else -> {
                    Empty
                }
            }
        }
    }
}
