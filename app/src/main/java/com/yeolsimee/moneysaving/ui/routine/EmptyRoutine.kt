package com.yeolsimee.moneysaving.ui.routine

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yeolsimee.moneysaving.R
import com.yeolsimee.moneysaving.ui.PrText
import com.yeolsimee.moneysaving.ui.theme.Gray66

@Composable
fun EmptyRoutine(modifier: Modifier = Modifier, hasSubText: Boolean = true) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.empty_icon),
            contentDescription = "루틴이 비어 있어요!"
        )
        Spacer(Modifier.height(13.dp))
        PrText(
            text = stringResource(R.string.routine_is_empty),
            fontSize = 20.sp,
            fontWeight = FontWeight.W800,
        )
        if (hasSubText) {
            Spacer(Modifier.height(5.dp))
            PrText(
                text = stringResource(R.string.please_add_routine_button),
                fontSize = 14.sp,
                fontWeight = FontWeight.W700,
                color = Gray66
            )
        }
    }
}