package com.yeolsimee.moneysaving.view.routine

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yeolsimee.moneysaving.ui.PrText

@ExperimentalMaterial3Api
@Composable
fun RoutineTopAppBar(routineType: RoutineModifyOption?, onClick: () -> Unit) {
    val title = setTitle(routineType)

    TopAppBar(title = {
        PrText(
            text = title,
            fontWeight = FontWeight.W700,
            fontSize = 18.sp,
            letterSpacing = (-0.1).sp
        )
    }, navigationIcon = {
        IconButton(onClick = onClick) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "뒤로가기"
            )
        }
    }, colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.White))
}

private fun setTitle(routineType: RoutineModifyOption?): String {
    return if (routineType == RoutineModifyOption.add) "루틴 추가하기" else "루틴 수정하기"
}

@Composable
fun RoutineBottomAppBar(routineType: RoutineModifyOption?, onClick: () -> Unit) {
    val buttonText = setButtonText(routineType)
    BottomAppBar(Modifier.height(60.dp), contentPadding = PaddingValues(0.dp)) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { onClick() }
            )) {
            PrText(
                text = buttonText,
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

@Composable
private fun setButtonText(routineType: RoutineModifyOption?) =
    if (routineType == RoutineModifyOption.add) "저장하기" else "수정하기"