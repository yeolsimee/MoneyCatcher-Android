package com.yeolsimee.moneysaving.view.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.yeolsimee.moneysaving.auth.Email
import com.yeolsimee.moneysaving.auth.Google
import com.yeolsimee.moneysaving.auth.Kakao
import com.yeolsimee.moneysaving.auth.Naver
import com.yeolsimee.moneysaving.ui.theme.MoneySavingTheme

@ExperimentalMaterial3Api
class LoginActivity : ComponentActivity() {

    private lateinit var google: Google

    private lateinit var googleLoginLauncher: ActivityResultLauncher<Intent>
    private lateinit var naverLoginLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initAuth()

        setContent {
            MoneySavingTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(modifier = Modifier) {
                            Button(onClick = {
                                google.login(googleLoginLauncher)
                            }) {
                                Text(text = "Google Login")
                            }

                            Button(onClick = {
                                Firebase.auth.signOut()
                                google.logout()
                            }) {
                                Text(text = "Google Logout")
                            }
                        }

                        Row(modifier = Modifier) {
                            Button(onClick = {
                                Naver.login(applicationContext, naverLoginLauncher)
                            }) {
                                Text(text = "Naver Login")
                            }

                            Button(onClick = {
                                Naver.logout()
                            }) {
                                Text(text = "Naver Logout")
                            }
                        }

                        Row(modifier = Modifier) {
                            Button(onClick = {
                                Kakao.login(this@LoginActivity)
                            }) {
                                Text(text = "Kakao Login")
                            }

                            Button(onClick = {
                                Kakao.logout()
                            }) {
                                Text(text = "Kakao Logout")
                            }
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

                        Button(onClick = { /*TODO*/ }) {
                            Text(text = "Apple Login")
                        }
                    }
                }
            }
        }
    }

    private fun initAuth() {
        initGoogleLogin()
        initNaverLogin()
    }

    override fun onResume() {
        super.onResume()
        Email.receive(intent, this@LoginActivity)
    }

    private fun initNaverLogin() {
        naverLoginLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                Naver.init(result)
            }
    }

    private fun initGoogleLogin() {
        google = Google(this@LoginActivity)
        googleLoginLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                google.init(it)
            }
    }
}