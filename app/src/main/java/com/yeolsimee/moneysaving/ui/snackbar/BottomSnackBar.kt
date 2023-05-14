package com.yeolsimee.moneysaving.ui.snackbar


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yeolsimee.moneysaving.ui.PrText


val CustomSnackBarHost: @Composable (SnackbarHostState) -> Unit = { state ->
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 30.dp, vertical = 20.dp)
    ) {
        SnackbarHost(modifier = Modifier.align(Alignment.BottomCenter), hostState = state) { data ->
            BottomSnackBar(text = data.visuals.message)
        }
    }
}

@Composable
private fun BottomSnackBar(text: String) {
    Snackbar(
        containerColor = Color.Black,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.height(38.dp)
    ) {
        PrText(
            text = text,
            color = Color.White,
            textAlign = TextAlign.Center,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )
    }
}


@Preview(showBackground = true)
@Composable
fun BottomSnackBarPreview() {
    Box(modifier = Modifier.padding(8.dp)) {
        BottomSnackBar(text = "카테고리명이 수정되었어요!")
    }
}
