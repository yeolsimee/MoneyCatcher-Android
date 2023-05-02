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

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initAuth()

        setContent {
            LoginScreen(
                onNaverLogin = {
                    viewModel.naverLogin(applicationContext, naverLoginLauncher)
                },
                onGoogleLogin = {
                    viewModel.googleLogin(googleLoginLauncher)
                },
                onAppleLogin = {
                    viewModel.appleLogin(this@LoginActivity) { moveToMainActivity() }
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
//        viewModel.logout()
        viewModel.autoLogin {
            moveToMainActivity()
        }
    }

    private fun moveToMainActivity() {
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        finish()
    }

    override fun onResume() {
        super.onResume()
        viewModel.receiveEmailResult(intent, this@LoginActivity) {
            moveToMainActivity()
        }
    }

    private fun initNaverLogin() {
        naverLoginLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                viewModel.naverInit(result) { moveToMainActivity() }
            }
    }

    private fun initGoogleLogin() {
        viewModel.init(this@LoginActivity) { moveToMainActivity() }
        googleLoginLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                viewModel.googleInit(it)
            }
    }
}