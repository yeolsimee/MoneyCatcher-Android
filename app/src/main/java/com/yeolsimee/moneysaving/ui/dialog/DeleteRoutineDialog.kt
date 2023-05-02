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

@Composable
fun DeleteRoutineDialog(
    dialogState: MutableState<Boolean>,
    onConfirmClick: () -> Unit
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
                        text = "해당 아이템을 삭제하시겠습니까?",
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
fun DeleteRoutineDialogPreview() {
    DeleteRoutineDialog(dialogState = remember { mutableStateOf(true) }, onConfirmClick = {})
}