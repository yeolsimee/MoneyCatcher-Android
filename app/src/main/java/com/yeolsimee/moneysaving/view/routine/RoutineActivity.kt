package com.yeolsimee.moneysaving.view.routine

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.yeolsimee.moneysaving.ui.PrText
import com.yeolsimee.moneysaving.ui.theme.RoumoTheme

@ExperimentalMaterial3Api
class RoutineActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val title = intent.getStringExtra("titleString") ?: ""

        setContent {
            RoumoTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(title = {
                            PrText(
                                text = title,
                                fontWeight = FontWeight.W700,
                                fontSize = 18.sp,
                                letterSpacing = (-0.1).sp
                            )
                        }, navigationIcon = {
                            IconButton(onClick = { finish() }) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "뒤로가기"
                                )
                            }
                        })
                    }
                ) {
                    Box(Modifier.padding(it)) {

                    }
                }
            }
        }
    }
}