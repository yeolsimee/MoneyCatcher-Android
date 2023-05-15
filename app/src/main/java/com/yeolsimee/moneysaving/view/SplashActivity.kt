@file:OptIn(
    ExperimentalLayoutApi::class,
    ExperimentalMaterial3Api::class
)

package com.yeolsimee.moneysaving.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.yeolsimee.moneysaving.view.login.EmailLoginViewModel
import com.yeolsimee.moneysaving.view.login.LoginActivity
import com.yeolsimee.moneysaving.view.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : ComponentActivity() {

    private val viewModel: LoginViewModel by viewModels()
    private val emailLoginViewModel: EmailLoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val splashScreen = installSplashScreen()
            splashScreen.setKeepOnScreenCondition { true }
        }

        super.onCreate(savedInstanceState)

        viewModel.autoLogin(
            signedUserCallback = { moveToMainActivity() },
            newUserCallback = { moveToAgreementActivity() },
            notLoggedInCallback = { moveToLoginActivity() }
        )
    }

    private fun moveToMainActivity() {
        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        finish()
    }

    private fun moveToAgreementActivity() {
        val intent = Intent(this@SplashActivity, LoginActivity::class.java)
        intent.putExtra("agreement", true)
        startActivity(intent)
        finish()
    }

    private fun moveToLoginActivity() {
        startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
        finish()
    }

    override fun onResume() {
        super.onResume()
        emailLoginViewModel.receiveEmailResult(
            intent = intent,
            activity = this@SplashActivity,
            signedUserCallback = { moveToMainActivity() },
            newUserCallback = { moveToAgreementActivity() }
        )
    }
}