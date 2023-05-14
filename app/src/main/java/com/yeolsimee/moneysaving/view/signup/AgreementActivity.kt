@file:OptIn(ExperimentalLayoutApi::class)

package com.yeolsimee.moneysaving.view.signup

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import com.yeolsimee.moneysaving.view.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AgreementActivity: ComponentActivity() {

    private val viewModel: AgreementViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AgreementScreen(onFinish = { finish() }) {
                viewModel.signUp(onSuccess = {
                    val intent = Intent(this@AgreementActivity, MainActivity::class.java)
                    startActivity(intent)
                }, onFailure = {})
            }
        }
    }
}