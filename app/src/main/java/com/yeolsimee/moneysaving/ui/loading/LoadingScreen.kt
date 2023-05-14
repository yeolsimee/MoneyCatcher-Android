package com.yeolsimee.moneysaving.ui.loading

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.yeolsimee.moneysaving.R
import com.yeolsimee.moneysaving.ui.theme.RoumoTheme

@Composable
fun LoadingScreen() {

    RoumoTheme(navigationBarColor = Color.White) {
        Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
            Image(
                painter = painterResource(id = R.drawable.loading_image),
                contentDescription = "로그인중",
                modifier = Modifier.align(
                    Alignment.Center
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingScreenPreview() {
    LoadingScreen()
}