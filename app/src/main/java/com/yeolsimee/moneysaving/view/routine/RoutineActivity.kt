@file:OptIn(ExperimentalMaterial3Api::class)

package com.yeolsimee.moneysaving.view.routine

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yeolsimee.moneysaving.ui.theme.RoumoTheme

@ExperimentalMaterial3Api
class RoutineActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val routineType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("routineType", RoutineModifyOption::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("routineType") as RoutineModifyOption
        }

        setContent {
            RoumoTheme {
                Scaffold(
                    topBar = {
                        RoutineTopAppBar(routineType) { finish() }
                    }
                ) {
                    Box(
                        Modifier
                            .padding(it)
                            .padding(horizontal = 28.dp)
                    ) {
                        val routineName = remember { mutableStateOf("") }
                        InputRoutineName(routineName)
                    }
                }
            }
        }
    }
}