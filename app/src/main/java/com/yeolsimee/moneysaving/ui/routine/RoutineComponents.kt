package com.yeolsimee.moneysaving.ui.routine

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.yeolsimee.moneysaving.R
import com.yeolsimee.moneysaving.domain.entity.routine.Routine
import com.yeolsimee.moneysaving.ui.PrText
import com.yeolsimee.moneysaving.ui.theme.Grey99

@Composable
fun RoutineTimeZone(routine: Routine) {
    Image(painter = painterResource(id = R.drawable.icon_time), contentDescription = "루틴시간")
    PrText(
        text = routine.getTimeZoneText(),
        fontSize = 11.sp,
        fontWeight = FontWeight.W500,
        color = Grey99
    )
}

@Composable
fun AlarmIconAndText(routine: Routine) {
    Image(painter = painterResource(id = R.drawable.icon_alram), contentDescription = "알림시간")
    PrText(
        text = routine.getAlarmText(),
        fontSize = 11.sp,
        fontWeight = FontWeight.W500,
        color = Grey99
    )
}