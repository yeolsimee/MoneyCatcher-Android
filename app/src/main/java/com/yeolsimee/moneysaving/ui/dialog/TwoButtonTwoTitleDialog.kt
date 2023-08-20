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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.yeolsimee.moneysaving.ui.PrText
import com.yeolsimee.moneysaving.ui.theme.Gray66
import com.yeolsimee.moneysaving.utils.VerticalSpacer

@Composable
fun TwoButtonTwoTitleDialog(
    dialogState: MutableState<Boolean>,
    title: String = "",
    content: String = "",
    leftButtonText: String = "",
    rightButtonText: String = "",
    onLeftClick: () -> Unit = {},
    onRightClick: () -> Unit = {}
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
                        color = Color.Black
                    )
                    24.VerticalSpacer()
                    PrText(
                        text = content,
                        fontWeight = FontWeight.W500,
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, end = 8.dp)
                    ) {
                        TextButton(onClick = {
                            dialogState.value = false
                            onLeftClick()
                        }) {
                            PrText(
                                text = leftButtonText,
                                color = Gray66,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W400
                            )
                        }
                        TextButton(onClick = {
                            dialogState.value = false
                            onRightClick()
                        }) {
                            PrText(
                                text = rightButtonText,
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
fun WithdrawDialogPreview() {
    TwoButtonTwoTitleDialog(
        dialogState = remember { mutableStateOf(true) },
        title = "회원탈퇴",
        content = "지금 탈퇴하시면 모든 루틴들이 사라져요",
        leftButtonText = "회원탈퇴",
        rightButtonText = "역시 그만둘래요"
    )
}

@Preview(showBackground = true)
@Composable
fun LogoutDialogPreview() {
    TwoButtonTwoTitleDialog(
        dialogState = remember { mutableStateOf(true) },
        title = "로그아웃",
        content = "계정을 로그아웃 하시겠어요?",
        leftButtonText = "취소",
        rightButtonText = "로그아웃"
    )
}