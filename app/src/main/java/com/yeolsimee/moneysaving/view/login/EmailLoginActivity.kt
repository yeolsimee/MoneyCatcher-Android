@file:OptIn(ExperimentalLayoutApi::class, ExperimentalLayoutApi::class,
    ExperimentalLayoutApi::class
)

package com.yeolsimee.moneysaving.view.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.yeolsimee.moneysaving.ui.dialog.OneButtonTwoTitleDialog
import com.yeolsimee.moneysaving.view.MainActivity
import com.yeolsimee.moneysaving.view.signup.AgreementActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EmailLoginActivity : ComponentActivity() {

    private val viewModel: EmailLoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val completeDialogState = remember { mutableStateOf(false) }
            val failureDialogState = remember { mutableStateOf(false) }
            val errorDialogState = remember { mutableStateOf(false) }

            EmailLoginScreen(
                onBackClick = { finish() },
                onConfirmClick = { email ->
                    viewModel.send(email, onComplete = {
                        completeDialogState.value = true
                    }, onFailure = {
                        failureDialogState.value = true
                    }, onError = {
                        errorDialogState.value = true
                    })
                }
            )

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

    override fun onResume() {
        super.onResume()
        viewModel.receiveEmailResult(
            intent,
            activity = this@EmailLoginActivity,
            signedUserCallback = {
                val intent = Intent(this@EmailLoginActivity, MainActivity::class.java)
                startActivity(intent)
                finishAffinity()
            },
            newUserCallback = {
                val intent = Intent(this@EmailLoginActivity, AgreementActivity::class.java)
                startActivity(intent)
            }
        ) {
            setContent {
                OneButtonTwoTitleDialog(
                    title = "오류",
                    subTitle = "서비스 오류로 인해 일반 가입이 안되고 있어요\n" +
                            "소셜로그인을 이용해 주세요",
                    dialogState = remember { mutableStateOf(true) }
                )
            }
        }
    }
}