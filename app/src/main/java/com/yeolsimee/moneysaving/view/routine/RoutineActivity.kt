@file:OptIn(ExperimentalMaterial3Api::class)

package com.yeolsimee.moneysaving.view.routine

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.material3.ExperimentalMaterial3Api

@ExperimentalLayoutApi
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
            RoutineScreen(
                routineType = routineType,
                closeCallback = {
                    finish()
                }, onCompleteCallback = {

                }
            )
        }
    }
}