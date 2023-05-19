package com.yeolsimee.moneysaving.domain.calendar

enum class DateIconState {
    Gold, Silver, Bronze, Today, OtherMonth, EmptyOtherMonth, Empty;

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
                else -> {   // NONE, YET
                    Empty
                }
            }
        }
    }
}
