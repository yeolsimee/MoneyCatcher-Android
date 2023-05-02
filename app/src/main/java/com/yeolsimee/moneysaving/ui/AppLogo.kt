package com.yeolsimee.moneysaving.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun AppLogoImage() {
    Box(
        modifier = Modifier
            .background(Color.Red)
            .width(98.dp)
            .height(30.dp)
    ) {
        PrText(text = "앱 로고 영역", color = Color.White)
    }
}

@Preview(showBackground = true)
@Composable
fun AppLogoPreview() {
    AppLogoImage()
}