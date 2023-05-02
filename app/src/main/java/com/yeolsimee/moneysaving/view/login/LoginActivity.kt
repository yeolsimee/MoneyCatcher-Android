package com.yeolsimee.moneysaving.view.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.material3.ExperimentalMaterial3Api
import com.yeolsimee.moneysaving.view.MainActivity
import dagger.hilt.android.AndroidEntryPoint


@ExperimentalLayoutApi
@AndroidEntryPoint
@ExperimentalMaterial3Api
class LoginActivity : ComponentActivity() {


    private lateinit var googleLoginLauncher: ActivityResultLauncher<Intent>
    private lateinit var naverLoginLauncher: ActivityResultLauncher<Intent>

    private val loginViewModel: LoginViewModel by viewModels()
    private val emailLoginViewModel: EmailLoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initAuth()

        setContent {
            LoginScreen(
                onNaverLogin = {
                    loginViewModel.naverLogin(applicationContext, naverLoginLauncher)
                },
                onGoogleLogin = {
                    loginViewModel.googleLogin(googleLoginLauncher)
                },
                onAppleLogin = {
                    loginViewModel.appleLogin(this@LoginActivity) { moveToMainActivity() }
                },
                onEmailButtonClick = {
                    val intent = Intent(this@LoginActivity, EmailLoginActivity::class.java)
                    startActivity(intent)
                }
            )
        }
    }

    private fun initAuth() {
        initGoogleLogin()
        initNaverLogin()
        loginViewModel.autoLogin {
            moveToMainActivity()
        }
    }

    private fun moveToMainActivity() {
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        finish()
    }

    override fun onResume() {
        super.onResume()
        emailLoginViewModel.receiveEmailResult(
            intent,
            this@LoginActivity,
            onSuccess = { moveToMainActivity() })
    }

    private fun initNaverLogin() {
        naverLoginLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                loginViewModel.naverInit(result) { moveToMainActivity() }
            }
    }

    private fun initGoogleLogin() {
        loginViewModel.init(this@LoginActivity) { moveToMainActivity() }
        googleLoginLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                loginViewModel.googleInit(it)
            }
    }
}