package com.yeolsimee.moneysaving.auth

import android.app.Activity
import android.util.Log
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase

import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.yeolsimee.moneysaving.App

object Kakao {
    fun login(activity: Activity) {
        val functions = Firebase.functions(regionOrCustomDomain = "asia-northeast1")

        // 카카오계정으로 로그인 공통 callback 구성
        // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            processKakaoLoginResult(functions, error, token)
        }

        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(activity)) {
            UserApiClient.instance.loginWithKakaoTalk(activity) { token, error ->
                if (error != null) {
                    Log.e(App.TAG, "카카오톡으로 로그인 실패", error)

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(
                        activity,
                        callback = callback
                    )
                } else if (token != null) {
                    Log.i(App.TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
                    processKakaoLoginResult(functions, null, token)
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(
                activity,
                callback = callback
            )
        }
    }

    private fun processKakaoLoginResult(
        functions: FirebaseFunctions,
        error: Throwable?,
        token: OAuthToken?
    ) {
        if (error != null) {
            Log.e(App.TAG, "로그인 실패", error)
        } else if (token != null) {
            Log.i(App.TAG, "로그인 성공 ${token.accessToken}")
            AuthFunctions.customAuthByFirebaseFunctions(functions, token.accessToken, "kakaoCustomAuth")
        }
    }

    fun logout() {
        UserApiClient.instance.logout { error ->
            Log.e(
                App.TAG,
                "카카오 로그아웃 에러: $error"
            )
        }
    }
}