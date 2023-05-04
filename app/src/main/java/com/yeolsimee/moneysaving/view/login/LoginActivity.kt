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
import com.yeolsimee.moneysaving.view.signup.AgreementActivity
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
                    loginViewModel.appleLogin(this@LoginActivity, { moveToMainActivity() }, { moveToAgreementActivity() })
                },
                onEmailButtonClick = {
                    val intent = Intent(this@LoginActivity, EmailLoginActivity::class.java)
                    startActivity(intent)
                }
            )
        }
    }

    private fun moveToAgreementActivity() {
        startActivity(Intent(this@LoginActivity, AgreementActivity::class.java))
        finish()
    }

    private fun initAuth() {
        initGoogleLogin()
        initNaverLogin()
        loginViewModel.autoLogin({
            moveToMainActivity()
        }, { moveToAgreementActivity() })
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
            signedUserCallback = { moveToMainActivity() },
            newUserCallback = { moveToAgreementActivity() }
        )
    }

    private fun initNaverLogin() {
        naverLoginLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                loginViewModel.naverInit(result, { moveToMainActivity() }, { moveToAgreementActivity() })
            }
    }

    private fun initGoogleLogin() {
        loginViewModel.init(this@LoginActivity, { moveToMainActivity() }, { moveToAgreementActivity() })
        googleLoginLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                loginViewModel.googleInit(it)
            }
    }
}