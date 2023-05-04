package com.yeolsimee.moneysaving.view.signup

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yeolsimee.moneysaving.R
import com.yeolsimee.moneysaving.ui.PrText
import com.yeolsimee.moneysaving.ui.appbar.BottomButtonAppBar
import com.yeolsimee.moneysaving.ui.appbar.TopBackButtonTitleAppBar
import com.yeolsimee.moneysaving.ui.theme.Black33
import com.yeolsimee.moneysaving.ui.theme.Gray66
import com.yeolsimee.moneysaving.ui.theme.GrayF0
import com.yeolsimee.moneysaving.ui.theme.RoumoTheme

@Composable
fun AgreementScreen(onClick: () -> Unit = {}) {
    RoumoTheme {
        val buttonState = remember { mutableStateOf(false) }
        Scaffold(
            topBar = {
                TopBackButtonTitleAppBar {}
            },
            bottomBar = {
                BottomButtonAppBar(
                    buttonState = buttonState,
                    buttonText = "동의하고 계속하기",
                    onClick = onClick)
            })
        {

            val firstCheck = remember { mutableStateOf(false) }
            val secondCheck = remember { mutableStateOf(false) }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(28.dp)
            ) {
                Column {
                    PrText(
                        text = "이용약관에 동의해주세요",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.W800,
                        color = Color.Black
                    )
                    Spacer(Modifier.height(44.dp))
                    Row(
                        Modifier.padding(vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(
                                id = if (firstCheck.value && secondCheck.value) R.drawable.icon_check
                                else R.drawable.icon_nonecheck
                            ), contentDescription = "전체동의 체크"
                        )
                        Spacer(Modifier.width(8.dp))
                        PrText(
                            text = "전체동의",
                            color = Color.Black,
                            fontWeight = FontWeight.W700,
                            fontSize = 16.sp
                        )
                    }
                    Divider(thickness = 1.5.dp, color = GrayF0)
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp, bottom = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row {
                            Image(
                                painter = painterResource(
                                    id = if (firstCheck.value) R.drawable.icon_check
                                    else R.drawable.icon_nonecheck
                                ), contentDescription = "서비스 이용 약관 체크"
                            )
                            Spacer(Modifier.width(8.dp))
                            PrText(
                                text = "서비스 이용 약관",
                                color = if (firstCheck.value) Black33 else Gray66,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.W500
                            )
                        }
                        Image(
                            painter = painterResource(id = R.drawable.icon_arrow_open),
                            contentDescription = "열어보기 화살표"
                        )
                    }
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, bottom = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row {
                            Image(
                                painter = painterResource(
                                    id = if (secondCheck.value) R.drawable.icon_check
                                    else R.drawable.icon_nonecheck
                                ), contentDescription = "개인 정보 처리 방침 체크"
                            )
                            Spacer(Modifier.width(8.dp))
                            PrText(
                                text = "개인 정보 처리 방침",
                                color = if (secondCheck.value) Black33 else Gray66,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.W500
                            )
                        }
                        Image(
                            painter = painterResource(id = R.drawable.icon_arrow_open),
                            contentDescription = "열어보기 화살표"
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AgreementScreenPreview() {
    AgreementScreen()
}