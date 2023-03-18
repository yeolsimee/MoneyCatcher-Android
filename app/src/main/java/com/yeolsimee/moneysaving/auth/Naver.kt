package com.yeolsimee.moneysaving.auth

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.navercorp.nid.NaverIdLoginSDK
import com.yeolsimee.moneysaving.App

object Naver {
    fun init(result: ActivityResult) {
        val functions = Firebase.functions(regionOrCustomDomain = "asia-northeast1")
        when (result.resultCode) {
            ComponentActivity.RESULT_OK -> {
                // 네이버 로그인 인증이 성공했을 때 수행할 코드 추가
                val accessToken = NaverIdLoginSDK.getAccessToken()
                AuthFunctions.customAuthByFirebaseFunctions(
                    functions,
                    accessToken,
                    "naverCustomAuth"
                )
            }
            ComponentActivity.RESULT_CANCELED -> {
                // 실패 or 에러
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Log.e(App.TAG, "errorCode:$errorCode, errorDesc:$errorDescription")
            }
        }
    }

    fun login(context: Context, launcher: ActivityResultLauncher<Intent>) {
        NaverIdLoginSDK.authenticate(context, launcher)
    }

    fun logout() {
        NaverIdLoginSDK.logout()
    }
}