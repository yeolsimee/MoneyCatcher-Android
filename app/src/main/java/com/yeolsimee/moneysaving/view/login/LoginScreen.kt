package com.yeolsimee.moneysaving.view.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yeolsimee.moneysaving.R
import com.yeolsimee.moneysaving.ui.PrText
import com.yeolsimee.moneysaving.ui.theme.GoogleGray
import com.yeolsimee.moneysaving.ui.theme.Gray66
import com.yeolsimee.moneysaving.ui.theme.NaverGreen
import com.yeolsimee.moneysaving.ui.theme.RoumoTheme
import com.yeolsimee.moneysaving.utils.getReactiveHeight

@Composable
fun LoginScreen(
    onNaverLogin: () -> Unit = {},
    onGoogleLogin: () -> Unit = {},
    onAppleLogin: () -> Unit = {},
    onEmailButtonClick: () -> Unit = {}
) {
    RoumoTheme(navigationBarColor = Color.White) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.login_image),
                    contentDescription = "ROUMO",
                    modifier = Modifier.padding(horizontal = 25.dp)
                )

                Spacer(
                    modifier = Modifier.height(getReactiveHeight(85))
                )

                LoginButtonBox(
                    backgroundColor = NaverGreen,
                    text = "네이버로 로그인",
                    onClick = onNaverLogin
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.naver_logo),
                        contentDescription = "네이버",
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                LoginButtonBox(
                    borderColor = GoogleGray,
                    borderWidth = 1.dp,
                    text = "구글로 로그인",
                    textColor = Color.Black,
                    onClick = onGoogleLogin,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.google_icon),
                        contentDescription = "구글",
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                LoginButtonBox(
                    backgroundColor = Color.Black,
                    text = "Apple로 로그인",
                    onClick = onAppleLogin
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.apple_icon),
                        contentDescription = "Apple",
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                EmailLoginButton(onClick = onEmailButtonClick)

                Spacer(modifier = Modifier.height(getReactiveHeight(60)))
            }
        }
    }
}

@Composable
private fun EmailLoginButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .widthIn(min = 144.dp, max = 319.dp)
            .fillMaxWidth()
            .height(48.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
    ) {
        PrText(
            text = "그 외 이메일로 로그인",
            color = Gray66,
            fontWeight = FontWeight.W500,
            fontSize = 14.sp,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun LoginButtonBox(
    borderColor: Color = Color.Transparent,
    borderWidth: Dp = 0.dp,
    backgroundColor: Color = Color.White,
    text: String = "",
    textColor: Color = Color.White,
    onClick: () -> Unit = {},
    LoginImage: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 28.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(backgroundColor)
            .border(
                width = borderWidth,
                color = borderColor,
                shape = RoundedCornerShape(4.dp)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 20.dp)
        ) {
            LoginImage()
        }
        Box(modifier = Modifier.align(Alignment.Center)) {
            PrText(
                text = text,
                color = textColor,
                fontWeight = FontWeight.W600,
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 1.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}