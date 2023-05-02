@file:OptIn(ExperimentalMaterial3Api::class)

package com.yeolsimee.moneysaving.ui.appbar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yeolsimee.moneysaving.ui.PrText
import com.yeolsimee.moneysaving.ui.theme.Gray99

@Composable
fun TopBackButtonTitleAppBar(text: String? = "", onClick: () -> Unit) {
    TopAppBar(
        title = {
            PrText(
                text = text!!,
                fontWeight = FontWeight.W700,
                fontSize = 18.sp,
                letterSpacing = (-0.1).sp
            )
        },
        navigationIcon = {
            IconButton(onClick = onClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    tint = Color.Black,
                    contentDescription = "뒤로가기"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White
        )
    )
}


@Composable
fun BottomButtonAppBar(
    buttonText: String? = "",
    buttonState: MutableState<Boolean>,
    onClick: () -> Unit
) {
    BottomAppBar(Modifier
        .height(60.dp)
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            enabled = buttonState.value,
            onClick = { onClick() }
        ), contentPadding = PaddingValues(0.dp)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    if (buttonState.value) Color.Black
                    else Gray99
                )
        ) {
            PrText(
                text = buttonText!!,
                fontWeight = FontWeight.W800,
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier.align(
                    Alignment.Center
                )
            )
        }
    }
}