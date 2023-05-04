@file:OptIn(ExperimentalLayoutApi::class)

package com.yeolsimee.moneysaving.view.signup

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import com.yeolsimee.moneysaving.view.MainActivity

class AgreementActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AgreementScreen {
                // TODO API 연동
                val intent = Intent(this@AgreementActivity, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
}