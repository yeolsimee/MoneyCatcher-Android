package com.yeolsimee.moneysaving.view.mypage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yeolsimee.moneysaving.R
import com.yeolsimee.moneysaving.ui.PrText
import com.yeolsimee.moneysaving.ui.theme.DismissRed
import com.yeolsimee.moneysaving.ui.theme.GrayF0
import com.yeolsimee.moneysaving.utils.onClick

@Composable
fun MyPageScreen(onLogout: () -> Unit = {}, onWithdraw: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        val pushState = remember { mutableStateOf(false) }
        Column {
            MoveListItem("카테고리 수정") {}
            Divider(thickness = 1.5.dp, color = GrayF0)
            AlarmItem(pushState)
            Divider(thickness = 3.dp, color = GrayF0)
            MoveListItem("이용 약관") {}
            Divider(thickness = 1.5.dp, color = GrayF0)
            MoveListItem("개인정보 처리방침") {}
            Divider(thickness = 3.dp, color = GrayF0)
            MoveListItem("로그아웃", iconVisible = false) {
                onLogout()
            }
            Divider(thickness = 1.5.dp, color = GrayF0)
            MoveListItem("회원탈퇴", color = DismissRed, iconVisible = false) {
                onWithdraw()
            }
            Divider(thickness = 1.5.dp, color = GrayF0)
        }

        Text(
            text = "내 정보",
            style = MaterialTheme.typography.displayLarge,
            textAlign = TextAlign.Center,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.Center)
                .clickable {
                    onLogout()
                }
        )
    }
}

@Composable
private fun AlarmItem(pushState: MutableState<Boolean>) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(52.dp)
            .padding(vertical = 14.dp, horizontal = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PrText(
            text = "푸쉬 알람",
            fontWeight = FontWeight.W500,
            fontSize = 15.sp,
            color = Color.Black
        )
        Image(
            painter = painterResource(id = if (pushState.value) R.drawable.toggle_on else R.drawable.toggle_off),
            contentDescription = "푸쉬 토글 버튼"
        )
    }
}

@Composable
private fun MoveListItem(title: String = "", color: Color = Color.Black, iconVisible: Boolean = true, onClick: () -> Unit = {}) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(52.dp)
            .onClick {
                onClick()
            }
            .padding(vertical = 14.dp, horizontal = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PrText(
            text = title,
            fontWeight = FontWeight.W500,
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
fun MyPageScreenPreview() {
    MyPageScreen()
}