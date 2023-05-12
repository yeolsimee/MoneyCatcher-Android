package com.yeolsimee.moneysaving.view.mypage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
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
import com.yeolsimee.moneysaving.ui.snackbar.CustomSnackBarHost
import com.yeolsimee.moneysaving.ui.theme.DismissRed
import com.yeolsimee.moneysaving.ui.theme.GrayF0
import com.yeolsimee.moneysaving.utils.onClick
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun MyPageScreen(
    myPageViewModel: MyPageViewModel,
    onMoveToCategoryUpdateScreen: () -> Unit = {},
    onLogout: () -> Unit = {},
    onWithdraw: () -> Unit = {},
    openInternetBrowser: (String) -> Unit = {}
) {
    val alarmState = myPageViewModel.alarmState.observeAsState(false)
    val logoutDialogState = remember { mutableStateOf(false) }
    val withdrawDialogState = remember { mutableStateOf(false) }
    val snackbarState = remember { SnackbarHostState() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column {
            MoveListItem("카테고리 수정") { onMoveToCategoryUpdateScreen() }
            Divider(thickness = 1.5.dp, color = GrayF0)
            AlarmItem(alarmState, snackbarState) {
                myPageViewModel.changeAlarmState()
            }
            Divider(thickness = 3.dp, color = GrayF0)

            val agreementUrl = stringResource(R.string.agreement_url)
            val policyUrl = stringResource(R.string.policy_url)

            MoveListItem("이용 약관") {
                openInternetBrowser(agreementUrl)
            }
            Divider(thickness = 1.5.dp, color = GrayF0)
            MoveListItem("개인정보 처리방침") {
                openInternetBrowser(policyUrl)
            }
            Divider(thickness = 3.dp, color = GrayF0)
            MoveListItem("로그아웃", iconVisible = false) {
                logoutDialogState.value = true
            }
            Divider(thickness = 1.5.dp, color = GrayF0)
            MoveListItem("회원탈퇴", color = DismissRed, iconVisible = false) {
                withdrawDialogState.value = true
            }
            Divider(thickness = 1.5.dp, color = GrayF0)
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

        CustomSnackBarHost(snackbarState)
    }
}

@Composable
private fun AlarmItem(
    alarmState: State<Boolean>,
    snackbarState: SnackbarHostState,
    onChange: () -> Unit = {}
) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(52.dp)
            .padding(vertical = 13.dp, horizontal = 24.dp),
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
            modifier = Modifier.onClick {
                CoroutineScope(Dispatchers.Main).launch {
                    onChange()
                    val text = if (alarmState.value) "알람이 해제되었어요!" else "알람이 설정되었어요!"
                    snackbarState.showSnackbar(text)
                }
            }
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
            .padding(vertical = 13.dp, horizontal = 24.dp),
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
                painter = painterResource(id = R.drawable.icon_bigarrow_end),
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

    val snackbarState = remember { SnackbarHostState() }

    Column {
        AlarmItem(alarmState = remember {
            mutableStateOf(false)
        }, snackbarState = snackbarState)
        AlarmItem(alarmState = remember {
            mutableStateOf(true)
        }, snackbarState = snackbarState)
    }
}