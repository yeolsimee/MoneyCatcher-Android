package com.yeolsimee.moneysaving.view.routine

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
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
    })
}

private fun setTitle(routineType: RoutineModifyOption?): String {
    val title = when (routineType) {
        RoutineModifyOption.add -> {
            "루틴 추가하기"
        }

        RoutineModifyOption.update -> {
            "루틴 수정하기"
        }

        else -> {
            ""
        }
    }
    return title
}