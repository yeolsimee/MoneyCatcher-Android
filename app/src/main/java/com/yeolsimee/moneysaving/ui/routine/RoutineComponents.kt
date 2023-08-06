package com.yeolsimee.moneysaving.ui.routine

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.yeolsimee.moneysaving.R
import com.yeolsimee.moneysaving.domain.entity.routine.Routine
import com.yeolsimee.moneysaving.ui.PrText
import com.yeolsimee.moneysaving.ui.theme.Gray99

@Composable
fun RoutineTimeZone(routine: Routine) {
    Image(painter = painterResource(id = R.drawable.icon_time), contentDescription = "루틴시간")
    PrText(
        text = routine.getTimeZoneText(),
        fontSize = 11.sp,
        fontWeight = FontWeight.W500,
        color = Gray99
    )
}

@Composable
fun AlarmIconAndText(routine: Routine) {
    if (routine.alarmTimeHour.isNotEmpty()) {
        Image(painter = painterResource(id = R.drawable.icon_alram), contentDescription = "알림시간")
        PrText(
            text = routine.getAlarmText(),
            fontSize = 11.sp,
            fontWeight = FontWeight.W500,
            color = Gray99
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRoutineTimeZone() {
    RoutineTimeZone(Routine())
}

@Preview(showBackground = true)
@Composable
fun PreviewAlarmIconAndText() {
    AlarmIconAndText(Routine())
}