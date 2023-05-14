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
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.MutableLiveData
import com.yeolsimee.moneysaving.ui.loading.LoadingScreen
import com.yeolsimee.moneysaving.view.MainActivity
import com.yeolsimee.moneysaving.view.signup.AgreementActivity
import dagger.hilt.android.AndroidEntryPoint


@ExperimentalLayoutApi
@AndroidEntryPoint
@ExperimentalMaterial3Api
class LoginActivity : ComponentActivity() {


    private lateinit var googleLoginLauncher: ActivityResultLauncher<Intent>
    private lateinit var naverLoginLauncher: ActivityResultLauncher<Intent>
    private val agreementActivityLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            loadingState.value = false
            loginViewModel.logout()
        }

    private val loginViewModel: LoginViewModel by viewModels()
    private val emailLoginViewModel: EmailLoginViewModel by viewModels()

    private val loadingState = MutableLiveData(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initAuth()

        if (intent.getBooleanExtra("agreement", false)) {
            moveToAgreementActivity()
        }

        setContent {
            LoginScreen(
                onNaverLogin = {
                    loadingState.value = true
                    loginViewModel.naverLogin(applicationContext, naverLoginLauncher)
                },
                onGoogleLogin = {
                    loginViewModel.googleLogin(googleLoginLauncher)
                },
                onAppleLogin = {
                    loadingState.value = true
                    loginViewModel.appleLogin(
                        this@LoginActivity,
                        loadingState,
                        { moveToMainActivity() },
                        { moveToAgreementActivity() })
                },
                onEmailButtonClick = {
                    val intent = Intent(this@LoginActivity, EmailLoginActivity::class.java)
                    startActivity(intent)
                }
            )
            if (loadingState.observeAsState().value == true) {
                LoadingScreen()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (loadingState.value == false) {
            emailLoginViewModel.receiveEmailResult(
                intent = intent,
                activity = this@LoginActivity,
                loadingState = loadingState,
                signedUserCallback = { moveToMainActivity() },
                newUserCallback = { moveToAgreementActivity() }
            )
        }
    }

    private fun moveToAgreementActivity() {
        val intent = Intent(this@LoginActivity, AgreementActivity::class.java)
        agreementActivityLauncher.launch(intent)
    }

    private fun initAuth() {
        initGoogleLogin()
        initNaverLogin()
    }

    // 이미 가입한 유저라면 루틴 알림 목록을 다시 불러와 등록해야 한다.
    private fun moveToMainActivity() {
        loginViewModel.updateRoutineAlarms(this@LoginActivity) {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }
    }

    private fun initNaverLogin() {
        naverLoginLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                    loginViewModel.naverInit(
                        result = it,
                        { moveToMainActivity() }
                    ) { moveToAgreementActivity() }
                } else {
                    loadingState.value = false
                }
            }
    }

    private fun initGoogleLogin() {
        loginViewModel.init(
            this@LoginActivity,
            { moveToMainActivity() },
            { moveToAgreementActivity() })
        googleLoginLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                    loadingState.value = true
                    loginViewModel.googleInit(it)
                }
            }
    }
}