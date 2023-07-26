@file:OptIn(ExperimentalMaterial3Api::class)

package com.yeolsimee.moneysaving.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yeolsimee.moneysaving.R
import com.yeolsimee.moneysaving.ui.PrText
import com.yeolsimee.moneysaving.ui.theme.DeleteRed
import com.yeolsimee.moneysaving.utils.onClick

@Composable
fun CategoryModifyDialog(state: MutableState<Boolean>) {

    if (state.value) {
        ModalBottomSheet(
            shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp),
            onDismissRequest = { state.value = false },
            dragHandle = { },
            containerColor = Color.Transparent
        ) {
            Column {
                Column(
                    modifier = Modifier
                        .background(Color.White)
                        .padding(vertical = 20.dp, horizontal = 28.dp)
                ) {
                    Box(
                        Modifier
                            .padding(vertical = 14.dp)
                            .fillMaxWidth()
                            .onClick { }) {
                        PrText(
                            text = stringResource(R.string.modify_category),
                            fontWeight = FontWeight.W600,
                            fontSize = 15.sp,
                            color = Color.Black
                        )
                    }
                    Box(
                        Modifier
                            .padding(vertical = 14.dp)
                            .fillMaxWidth()
                            .onClick { }) {
                        PrText(
                            text = stringResource(R.string.delete_category),
                            fontWeight = FontWeight.W600,
                            fontSize = 15.sp,
                            color = DeleteRed
                        )
                    }
                }
                Spacer(Modifier.navigationBarsPadding().height(66.dp))
            }
        }
    }
}

@Preview
@Composable
fun CategoryModifyDialogPreview() {
    CategoryModifyDialog(state = remember { mutableStateOf(true) })
}