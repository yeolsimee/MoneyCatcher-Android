package com.yeolsimee.moneysaving.view.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.actionCodeSettings
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.yeolsimee.moneysaving.App
import com.yeolsimee.moneysaving.R
import com.yeolsimee.moneysaving.ui.theme.MoneySavingTheme

@ExperimentalMaterial3Api
class LoginActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var functions: FirebaseFunctions
    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var googleLoginLauncher: ActivityResultLauncher<Intent>
    private lateinit var naverLoginLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        functions = Firebase.functions(regionOrCustomDomain = "asia-northeast1")

        initGoogleLogin()
        initNaverLogin()

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
                                googleLoginLauncher.launch(googleSignInClient.signInIntent)
                            }) {
                                Text(text = "Google Login")
                            }

                            Button(onClick = {
                                auth.signOut()
                                googleSignInClient.signOut()
                            }) {
                                Text(text = "Google Logout")
                            }
                        }

                        Row(modifier = Modifier) {
                            Button(onClick = {
                                NaverIdLoginSDK.authenticate(applicationContext, naverLoginLauncher)
                            }) {
                                Text(text = "Naver Login")
                            }

                            Button(onClick = {
                                NaverIdLoginSDK.logout()
                            }) {
                                Text(text = "Naver Logout")
                            }
                        }

                        Row(modifier = Modifier) {
                            Button(onClick = {

                                // 로그인 조합 예제

                                // 카카오계정으로 로그인 공통 callback 구성
                                // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
                                val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                                    processKakaoLoginResult(error, token)
                                }

                                // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
                                if (UserApiClient.instance.isKakaoTalkLoginAvailable(this@LoginActivity)) {
                                    UserApiClient.instance.loginWithKakaoTalk(this@LoginActivity) { token, error ->
                                        if (error != null) {
                                            Log.e(App.TAG, "카카오톡으로 로그인 실패", error)

                                            // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                                            // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                                            if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                                                return@loginWithKakaoTalk
                                            }

                                            // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                                            UserApiClient.instance.loginWithKakaoAccount(
                                                this@LoginActivity,
                                                callback = callback
                                            )
                                        } else if (token != null) {
                                            Log.i(App.TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
                                        }
                                    }
                                } else {
                                    UserApiClient.instance.loginWithKakaoAccount(
                                        this@LoginActivity,
                                        callback = callback
                                    )
                                }
                            }) {
                                Text(text = "Kakao Login")
                            }

                            Button(onClick = {
                                UserApiClient.instance.logout { error ->
                                    Log.e(
                                        App.TAG,
                                        "카카오 로그아웃 에러: $error"
                                    )
                                }
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
                            auth.sendSignInLinkToEmail(email, actionCodeSettings {
                                url = "https://moneysaving.page.link/Tbeh?email=$email"
                                handleCodeInApp = true
//                                setIOSBundleId("com.yeolsimee.moneysave")
                                setAndroidPackageName(packageName, true, "3")
                                dynamicLinkDomain = "moneysaving.page.link"
                            })
                                .addOnCompleteListener {
                                    Toast.makeText(
                                        applicationContext,
                                        "이메일 전송됨",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } //
                                .addOnFailureListener { Log.e(App.TAG, "$it") }
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

    override fun onResume() {
        super.onResume()
        Firebase.dynamicLinks.getDynamicLink(intent)
            .addOnSuccessListener(this@LoginActivity) {
                val deepLink: Uri? = it?.link
                if (deepLink != null) {
                    val isValid = auth.isSignInWithEmailLink(deepLink.toString())

                    if (isValid) {
                        val continueUrl = deepLink.getQueryParameter("continueUrl") ?: ""
                        val email = Uri.parse(continueUrl).getQueryParameter("email") ?: ""
                        auth.signInWithEmailLink(email, deepLink.toString())
                            .addOnSuccessListener { result ->
                                result.user?.getIdToken(true)?.addOnSuccessListener {authResult ->
                                    Log.i(App.TAG, "email login firebase token: ${authResult.token}")
                                    Toast.makeText(applicationContext, authResult.token, Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                }

            }
            .addOnFailureListener(this@LoginActivity) { e ->
                Log.e(App.TAG, "getDynamicLink:onFailure: $e")
            }
    }

    private fun processKakaoLoginResult(
        error: Throwable?,
        token: OAuthToken?
    ) {
        if (error != null) {
            Log.e(App.TAG, "로그인 실패", error)
        } else if (token != null) {
            Log.i(App.TAG, "로그인 성공 ${token.accessToken}")
            Toast.makeText(
                applicationContext,
                "로그인 성공",
                Toast.LENGTH_SHORT
            )
                .show()
            firebaseAuthWithKakao(token.accessToken)
        }
    }

    private fun initNaverLogin() {
        naverLoginLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        // 네이버 로그인 인증이 성공했을 때 수행할 코드 추가
                        val accessToken = NaverIdLoginSDK.getAccessToken()
                        Toast.makeText(
                            applicationContext,
                            "naver 로그인 성공: $accessToken",
                            Toast.LENGTH_SHORT
                        ).show()
                        firebaseAuthWithNaver(accessToken)
                    }
                    RESULT_CANCELED -> {
                        // 실패 or 에러
                        val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                        val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                        Toast.makeText(
                            applicationContext,
                            "errorCode:$errorCode, errorDesc:$errorDescription",
                            Toast.LENGTH_SHORT
                        ).show()
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

    private fun firebaseAuthWithKakao(kakaoToken: String?) {
        if (kakaoToken != null) {
            val resultTask = customAuthByFirebaseFunctions(kakaoToken, "kakaoCustomAuth")
            getAuthResult(resultTask)
        }
    }

    private fun customAuthByFirebaseFunctions(
        token: String,
        type: String
    ): Task<Map<String, String>> {
        return functions.getHttpsCallable(type)
            .call(token)
            .continueWith { task ->
                @Suppress("UNCHECKED_CAST")
                val result = task.result?.data as Map<String, String>?
                result
            }
    }

    private fun firebaseAuthWithNaver(naverToken: String?) {
        if (naverToken != null) {
            val resultTask = customAuthByFirebaseFunctions(naverToken, "naverCustomAuth")
            getAuthResult(resultTask)
        }
    }

    private fun getAuthResult(resultTask: Task<Map<String, String>>) {
        resultTask.addOnCompleteListener {
            if (it.isSuccessful) {
                val token = it.result["firebase_token"]
                Toast.makeText(
                    applicationContext,
                    "firebase 성공: $token",
                    Toast.LENGTH_SHORT
                )
                    .show()

            } else {
                Toast.makeText(applicationContext, "firebase 실패", Toast.LENGTH_SHORT).show()
            }
        }
            .addOnCanceledListener {
                Toast.makeText(applicationContext, "firebase 취소", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                it.printStackTrace()
                Toast.makeText(applicationContext, "firebase 에러", Toast.LENGTH_SHORT).show()
            }
    }
}