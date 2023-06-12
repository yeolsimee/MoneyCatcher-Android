package com.yeolsimee.moneysaving.view.mypage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yeolsimee.moneysaving.R
import com.yeolsimee.moneysaving.ui.PrText
import com.yeolsimee.moneysaving.ui.dialog.TwoButtonTwoTitleDialog
import com.yeolsimee.moneysaving.ui.theme.DismissRed
import com.yeolsimee.moneysaving.utils.onClick

@Composable
fun MyPageScreen(
    alarmState: State<Boolean>,
    onChangeAlarmState: () -> Unit = {},
    onLogout: () -> Unit = {},
    onWithdraw: () -> Unit = {},
    openInternetBrowser: (String) -> Unit = {}
) {
    val logoutDialogState = remember { mutableStateOf(false) }
    val withdrawDialogState = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column {
            Image(
                painter = painterResource(id = R.drawable.app_logo),
                contentDescription = "ROUMO",
                modifier = Modifier.padding(start = 28.dp, top = 16.dp)
            )
            Spacer(Modifier.height(17.dp))
            AlarmItem(alarmState) {
                onChangeAlarmState()
            }
            val agreementUrl = stringResource(R.string.agreement_url)
            val policyUrl = stringResource(R.string.policy_url)

            MoveListItem("서비스 이용 약관") {
                openInternetBrowser(agreementUrl)
            }
            MoveListItem("개인정보 처리방침") {
                openInternetBrowser(policyUrl)
            }
            MoveListItem("로그아웃", iconVisible = false) {
                logoutDialogState.value = true
            }
            MoveListItem("회원탈퇴", color = DismissRed, iconVisible = false) {
                withdrawDialogState.value = true
            }
        }

        TwoButtonTwoTitleDialog(
            dialogState = remember { logoutDialogState },
            title = "로그아웃",
            content = "계정을 로그아웃 하시겠어요?",
            leftButtonText = "취소",
            rightButtonText = "로그아웃",
            onRightClick = { onLogout() }
        )

        TwoButtonTwoTitleDialog(
            dialogState = remember { withdrawDialogState },
            title = "회원탈퇴",
            content = "지금 탈퇴하시면 모든 루틴들이 사라져요",
            leftButtonText = "회원탈퇴",
            rightButtonText = "역시 그만둘래요",
            onLeftClick = { onWithdraw() }
        )
    }
}

@Composable
private fun AlarmItem(
    alarmState: State<Boolean>,
    onChange: () -> Unit = {}
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp, horizontal = 28.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PrText(
            text = "알림",
            fontWeight = FontWeight.W600,
            fontSize = 15.sp,
            color = Color.Black
        )
        Image(
            painter = painterResource(id = if (alarmState.value) R.drawable.toggle_on else R.drawable.toggle_off),
            contentDescription = "푸쉬 토글 버튼",
            modifier = Modifier.onClick { onChange() }
        )
    }
}

@Composable
private fun MoveListItem(
    title: String = "",
    color: Color = Color.Black,
    iconVisible: Boolean = true,
    onClick: () -> Unit = {}
) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(52.dp)
            .onClick {
                onClick()
            }
            .padding(vertical = 14.dp, horizontal = 28.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PrText(
            text = title,
            fontWeight = FontWeight.W600,
            fontSize = 15.sp,
            color = color
        )
        if (iconVisible)
            Image(
                painter = painterResource(id = R.drawable.icon_extend),
                contentDescription = "화살표"
            )
    }
}

@Preview(showBackground = true)
@Composable
fun MoveListItemPreview() {
    MoveListItem(title = "테스트")
}

@Preview(showBackground = true)
@Composable
fun AlarmItemPreview() {
    Column {
        AlarmItem(alarmState = remember {
            mutableStateOf(false)
        })
        AlarmItem(alarmState = remember {
            mutableStateOf(true)
        })
    }
}

@Preview(showBackground = true)
@Composable
fun MyPageScreenPreview() {
    MyPageScreen(
        alarmState = remember { mutableStateOf(true) }
    )
}