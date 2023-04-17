package com.yeolsimee.moneysaving.view.routine

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yeolsimee.moneysaving.R
import com.yeolsimee.moneysaving.ui.PrText
import com.yeolsimee.moneysaving.ui.theme.Grey99
import com.yeolsimee.moneysaving.ui.theme.RoumoTheme

@Composable
fun InputRoutineName(routineName: MutableState<String>) {
    Column {
        Row(modifier = Modifier) {
            Image(
                painter = painterResource(id = R.drawable.image_pencil),
                contentDescription = "루틴명 입력"
            )
            Spacer(Modifier.width(4.dp))
            PrText(
                text = "루틴명은 무엇인가요?",
                fontWeight = FontWeight.W700,
                fontSize = 15.sp
            )
        }
        Spacer(Modifier.height(4.dp))
        BasicTextField(
            value = routineName.value,
            textStyle = TextStyle(
                fontWeight = FontWeight.W600,
                fontSize = 14.sp,
                color = Color.Black
            ),
            onValueChange = { t ->
                routineName.value = t
            },
            singleLine = true,
            decorationBox = { innerTextField ->
                Box {
                    if (routineName.value.isEmpty()) {
                        PrText(
                            text = "루틴명을 입력해주세요.",
                            color = Grey99,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W400
                        )
                    }
                    innerTextField()
                }
            },
            modifier = Modifier
                .padding(top = 10.dp, bottom = 9.dp)
                .fillMaxWidth()
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.Black)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun InputRoutineNamePreview() {
    RoumoTheme {
        Column(modifier = Modifier) {
            Spacer(Modifier.height(8.dp))
            InputRoutineName(routineName = remember { mutableStateOf("") })
            Spacer(Modifier.height(8.dp))
            InputRoutineName(routineName = remember { mutableStateOf("루틴명") })
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
fun SelectCategory() {
    Row {
        Image(
            painter = painterResource(id = R.drawable.image_tag),
            contentDescription = "루틴 카테고리 설정"
        )
        Spacer(Modifier.width(4.dp))
        PrText(
            text = "루틴의 카테고리를 설정해주세요",
            fontWeight = FontWeight.W700,
            fontSize = 15.sp
        )
    }
}