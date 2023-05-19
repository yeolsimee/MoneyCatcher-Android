package com.yeolsimee.moneysaving.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.yeolsimee.moneysaving.ui.theme.Gray66

@Composable
fun TwoButtonOneTitleDialog(
    dialogState: MutableState<Boolean>,
    text: String = "",
    confirmText: String = "확인",
    onConfirmClick: () -> Unit = {},
    onCancelClick: () -> Unit = {}
) {
    if (dialogState.value) {
        Dialog(onDismissRequest = {
            dialogState.value = false
            onCancelClick()
        }) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.White)
                    .padding(top = 28.dp, bottom = 8.dp, start = 20.dp, end = 4.dp)
            ) {
                Column {
                    PrText(
                        text = text,
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
                            onCancelClick()
                        }) {
                            PrText(
                                text = "취소",
                                color = Gray66,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W400
                            )
                        }
                        TextButton(onClick = {
                            dialogState.value = false
                            onConfirmClick()
                        }) {
                            PrText(
                                text = confirmText,
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
fun TwoButtonOneTitleDialogPreview() {
//    TwoButtonOneTitleDialog(
//        dialogState = remember { mutableStateOf(true) },
//        text = "해당 아이템을 삭제하시겠습니까?",
//        onConfirmClick = {}
//    )
    TwoButtonOneTitleDialog(
        dialogState = remember { mutableStateOf(true) },
        text = "알림 설정에서 \n" +
                "알림 전체 OFF를 한 경우 \n" +
                "알림을 받을 수 없습니다.",
        confirmText = "알림설정으로 이동하기",
        onConfirmClick = {  }
    )
}