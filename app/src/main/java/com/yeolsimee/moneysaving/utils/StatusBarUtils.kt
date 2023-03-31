package com.yeolsimee.moneysaving.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun SetStatusBarColor(color: Color) {
    val systemUiController = rememberSystemUiController()
    systemUiController.setNavigationBarColor(Color.White)
    systemUiController.setStatusBarColor(color)
}