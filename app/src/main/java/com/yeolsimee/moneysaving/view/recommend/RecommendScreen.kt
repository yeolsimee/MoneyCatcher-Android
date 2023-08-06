package com.yeolsimee.moneysaving.view.recommend

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
        Image(
            painter = painterResource(id = R.drawable.app_logo),
            contentDescription = "ROUMO",
            modifier = Modifier.padding(start = 28.dp, top = 16.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 18.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ready_image),
                contentDescription = "준비중 아이콘",
                modifier = Modifier.padding(horizontal = 28.dp)
            )
            Spacer(Modifier.height(13.dp))
            PrText(
                text = "업데이트 예정이에요",
                fontWeight = FontWeight.W800,
                fontSize = 20.sp,
                color = Color.Black
            )
            Spacer(Modifier.height(5.dp))
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