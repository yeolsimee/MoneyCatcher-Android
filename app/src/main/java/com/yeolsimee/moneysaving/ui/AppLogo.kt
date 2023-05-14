package com.yeolsimee.moneysaving.ui

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.yeolsimee.moneysaving.R

@Composable
fun AppLogoImage() {
    Image(painter = painterResource(id = R.drawable.app_logo), contentDescription = "앱 로고")
}

@Preview(showBackground = true)
@Composable
fun AppLogoPreview() {
    AppLogoImage()
}