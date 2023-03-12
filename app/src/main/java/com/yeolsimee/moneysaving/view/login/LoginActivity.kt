package com.yeolsimee.moneysaving.view.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.kakao.sdk.user.UserApiClient
import com.yeolsimee.moneysaving.App
import com.yeolsimee.moneysaving.R
import com.yeolsimee.moneysaving.ui.theme.MoneySavingTheme

class LoginActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var googleLoginLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        initGoogleLogin()

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
                        Button(onClick = {
                            googleLoginLauncher.launch(googleSignInClient.signInIntent)
                        }) {
                            Text(text = "Google Login")
                        }

                        Button(onClick = { /*TODO*/ }) {
                            Text(text = "Naver Login")
                        }

                        Button(onClick = {
                            UserApiClient.instance.loginWithKakaoTalk(this@LoginActivity) { token, error ->
                                if (error != null) {
                                    Log.e(App.TAG, "로그인 실패", error)
                                } else if (token != null) {
                                    Log.i(App.TAG, "로그인 성공 ${token.accessToken}")
                                    Toast.makeText(applicationContext, "로그인 성공", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                        }) {
                            Text(text = "Kakao Login")
                        }

                        Button(onClick = { /*TODO*/ }) {
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

    private fun initGoogleLogin() {
        googleLoginLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                    handleSignInResult(task)
                } else {
                    Toast.makeText(
                        applicationContext,
                        "firebase 로그인 결과가 다름: ${it.resultCode}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .build()
        googleSignInClient = GoogleSignIn.getClient(this@LoginActivity, options)
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        val account = task.getResult(ApiException::class.java)

        if (account != null && account.idToken != null) {
            firebaseAuthWithGoogle(account.idToken!!)
            Toast.makeText(applicationContext, account.displayName, Toast.LENGTH_SHORT).show()
        }
    }

    private fun firebaseAuthWithGoogle(googleToken: String) {
        val credential = GoogleAuthProvider.getCredential(googleToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val token = it.result.user?.getIdToken(false)?.result?.token
                    Toast.makeText(applicationContext, "firebase 성공: $token", Toast.LENGTH_SHORT)
                        .show()

                } else {
                    Toast.makeText(applicationContext, "firebase 실패", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnCanceledListener {
                Toast.makeText(applicationContext, "firebase 취소", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(applicationContext, "firebase 에러", Toast.LENGTH_SHORT).show()
            }
    }
}