package com.yeolsimee.moneysaving.view.login

import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yeolsimee.moneysaving.R
import com.yeolsimee.moneysaving.ui.PrText
import com.yeolsimee.moneysaving.ui.appbar.BottomButtonAppBar
import com.yeolsimee.moneysaving.ui.appbar.TopBackButtonTitleAppBar
import com.yeolsimee.moneysaving.ui.dialog.OneButtonTwoTitleDialog
import com.yeolsimee.moneysaving.ui.emailNotice
import com.yeolsimee.moneysaving.ui.theme.DismissRed
import com.yeolsimee.moneysaving.ui.theme.Gray99
import com.yeolsimee.moneysaving.ui.theme.GrayF0
import com.yeolsimee.moneysaving.ui.theme.RoumoTheme
import com.yeolsimee.moneysaving.utils.addFocusCleaner

@Composable
fun EmailLoginScreen(
    onBackClick: () -> Unit = {},
    onConfirmClick: (String) -> Unit = {}
) {
    val buttonState = remember { mutableStateOf(false) }
    val emailState = remember { mutableStateOf("") }
    val hasWritten = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val focusRequester by remember { mutableStateOf(FocusRequester()) }

    val emailLoginViewModel: EmailLoginViewModel = hiltViewModel()

    RoumoTheme(navigationBarColor = if (buttonState.value) Color.Black else Gray99) {

        val completeDialogState = remember { mutableStateOf(false) }
        val failureDialogState = remember { mutableStateOf(false) }
        val errorDialogState = remember { mutableStateOf(false) }

        Scaffold(
            topBar = { TopBackButtonTitleAppBar { onBackClick() } },
            bottomBar = {
                BottomButtonAppBar(buttonState = buttonState.value, buttonText = "확인") {
                    onConfirmClick(emailState.value)

                    emailLoginViewModel.send(emailState.value,
                        onComplete = {
                            completeDialogState.value = true
                        },
                        onError = {
                            errorDialogState.value = true
                        },
                        onFailure = {
                            failureDialogState.value = true
                        }
                    )
                }
            }
        ) {
            Box(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(top = 28.dp)
                    .addFocusCleaner(focusManager)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 28.dp)
                ) {
                    PrText(
                        text = "로그인 할 이메일을 입력해주세요",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.W800,
                        color = Color.Black
                    )
                    Spacer(Modifier.height(60.dp))
                    BasicTextField(
                        value = emailState.value,
                        textStyle = TextStyle(
                            fontWeight = FontWeight.W600, fontSize = 14.sp, color = Color.Black
                        ),
                        onValueChange = { t ->
                            buttonState.value = Patterns.EMAIL_ADDRESS.matcher(t).matches()
                            emailState.value = t
                            hasWritten.value = true
                        },
                        singleLine = true,
                        decorationBox = { innerTextField ->
                            Box {
                                if (emailState.value.isEmpty()) {
                                    PrText(
                                        text = "이메일",
                                        color = Gray99,
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
                            .focusRequester(focusRequester)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color.Black)
                    )
                    if (!buttonState.value) {
                        if (emailState.value.isNotEmpty()) {
                            PrText(
                                text = "메일형식으로 입력해주세요",
                                color = DismissRed,
                                fontWeight = FontWeight.W500,
                                fontSize = 12.sp,
                                letterSpacing = (-0.5).sp,
                                modifier = Modifier.padding(top = 6.dp, bottom = 8.dp)
                            )
                        } else if (hasWritten.value) {
                            PrText(
                                text = "메일을 입력해주세요",
                                color = DismissRed,
                                fontWeight = FontWeight.W500,
                                fontSize = 12.sp,
                                letterSpacing = (-0.5).sp,
                                modifier = Modifier.padding(top = 6.dp, bottom = 8.dp)
                            )
                        } else {
                            Spacer(Modifier.height(28.dp))
                        }
                    } else Spacer(Modifier.height(28.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                GrayF0
                            )
                    ) {
                        Row(
                            Modifier.padding(
                                top = 16.dp, bottom = 16.dp, start = 14.dp, end = 20.dp
                            ), verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.icon_error),
                                contentDescription = "이메일 경고"
                            )
                            Spacer(Modifier.width(8.dp))
                            PrText(
                                text = emailNotice,
                                fontWeight = FontWeight.W500,
                                fontSize = 12.sp,
                                color = Gray99,
                                textAlign = TextAlign.Start,
                                letterSpacing = (-0.5).sp,
                                lineHeight = (14.3).sp
                            )
                        }
                    }
                }
            }
        }

        if (completeDialogState.value) {
            OneButtonTwoTitleDialog(
                dialogState = completeDialogState,
                title = "인증 메일 발송",
                subTitle = "메일주소로 인증 메일이 발송되었습니다\n" +
                        "이메일 링크로 로그인을 완료해주세요",
            )
        }
        if (failureDialogState.value) {
            OneButtonTwoTitleDialog(
                title = "인증 실패",
                subTitle = "다른 계정으로 시도 해보세요",
                dialogState = remember { mutableStateOf(true) }
            )
        }
        if (errorDialogState.value) {
            OneButtonTwoTitleDialog(
                title = "오류",
                subTitle = "서비스 오류로 인해 일반 가입이 안되고 있어요\n" +
                        "소셜로그인을 이용해 주세요",
                dialogState = remember { mutableStateOf(true) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EmailLoginScreenPreview() {
    EmailLoginScreen({}) {}
}