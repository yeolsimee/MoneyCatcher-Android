package com.yeolsimee.moneysaving.utils


import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Int.HorizontalSpacer() {
    Spacer(Modifier.width(this.dp))
}

@Composable
fun Double.HorizontalSpacer() {
    Spacer(Modifier.width(this.dp))
}

@Composable
fun Int.VerticalSpacer() {
    Spacer(Modifier.height(this.dp))
}