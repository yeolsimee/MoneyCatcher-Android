package com.yeolsimee.moneysaving.view.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.yeolsimee.moneysaving.auth.*
import com.yeolsimee.moneysaving.ui.theme.RoumoTheme
import com.yeolsimee.moneysaving.view.home.MainActivity
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
            RoumoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(onClick = {
                            viewModel.googleLogin(googleLoginLauncher)
                        }) {
                            Text(text = "Google Login")
                        }

                        Button(onClick = {
                            viewModel.naverLogin(applicationContext, naverLoginLauncher)
                        }) {
                            Text(text = "Naver Login")
                        }

                        val textState = remember { mutableStateOf("") }

                        TextField(value = textState.value, onValueChange = {
                            textState.value = it
                        })

                        Button(onClick = {
                            val email = textState.value
                            Toast.makeText(applicationContext, email, Toast.LENGTH_SHORT).show()
                            Email.send(email)
                        }) {
                            Text(text = "Email Login")
                        }

                        Button(onClick = {
                            viewModel.appleLogin(this@LoginActivity) { moveToMainActivity() }
                        }) {
                            Text(text = "Apple Login")
                        }
                        Button(onClick = {
                            viewModel.logout()
                        }) {
                            Text(text = "Logout")
                        }
                    }
                }
            }
        }
    }

    private fun initAuth() {
        initGoogleLogin()
        initNaverLogin()
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