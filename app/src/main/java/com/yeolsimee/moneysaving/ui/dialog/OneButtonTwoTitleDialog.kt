package com.yeolsimee.moneysaving.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.yeolsimee.moneysaving.ui.PrText

@Composable
fun OneButtonTwoTitleDialog(
    dialogState: MutableState<Boolean>,
    title: String = "",
    subTitle: String = "",
    onConfirmClick: () -> Unit = {}
) {
    if (dialogState.value) {
        Dialog(onDismissRequest = { dialogState.value = false }) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.White)
                    .padding(top = 28.dp, bottom = 8.dp, start = 20.dp, end = 4.dp)
            ) {
                Column {
                    PrText(
                        text = title,
                        fontWeight = FontWeight.W600,
                        fontSize = 16.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Start
                    )
                    Spacer(Modifier.height(24.dp))
                    PrText(
                        text = subTitle,
                        fontWeight = FontWeight.W500,
                        fontSize = 14.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Start
                    )
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, end = 8.dp)
                    ) {
                        TextButton(onClick = {
                            dialogState.value = false
                            onConfirmClick()
                        }) {
                            PrText(
                                text = "확인",
                                color = Color.Black,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W500
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OneButtonTwoTitleDialogPreview1() {
    OneButtonTwoTitleDialog(
        title = "인증 메일 발송",
        subTitle = "메일주소로 인증 메일이 발송되었습니다\n" +
                "이메일 링크로 로그인을 완료해주세요",
        dialogState = remember { mutableStateOf(true) }
    )
}

@Preview(showBackground = true)
@Composable
fun OneButtonTwoTitleDialogPreview2() {
    OneButtonTwoTitleDialog(
        title = "인증 실패",
        subTitle = "다른 계정으로 시도 해보세요",
        dialogState = remember { mutableStateOf(true) }
    )
}

@Preview(showBackground = true)
@Composable
fun OneButtonTwoTitleDialogPreview3() {
    OneButtonTwoTitleDialog(
        title = "오류",
        subTitle = "서비스 오류로 인해 일반 가입이 안되고 있어요\n" +
                "소셜로그인을 이용해 주세요",
        dialogState = remember { mutableStateOf(true) }
    )
}