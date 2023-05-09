package com.yeolsimee.moneysaving.view.recommend

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yeolsimee.moneysaving.R
import com.yeolsimee.moneysaving.ui.PrText
import com.yeolsimee.moneysaving.ui.theme.Gray66

@Composable
fun RecommendScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.middle_empty_icon),
                contentDescription = "앱 아이콘"
            )
            Spacer(Modifier.height(11.dp))
            PrText(
                text = "업데이트 예정이에요",
                fontWeight = FontWeight.W800,
                fontSize = 20.sp,
                color = Color.Black
            )
            Spacer(Modifier.height(4.dp))
            PrText(
                text = "추후 업데이트 예정이니 조금만 기다려주세요",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Gray66
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecommendScreenPreview() {
    RecommendScreen()
}