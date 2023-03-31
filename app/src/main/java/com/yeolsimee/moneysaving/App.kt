package com.yeolsimee.moneysaving

import android.app.Application
import com.google.firebase.FirebaseApp
import com.kakao.sdk.common.KakaoSdk
import com.navercorp.nid.NaverIdLoginSDK
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    companion object {
        const val TAG = "MoneyCatcherApp"
    }

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(applicationContext)
        KakaoSdk.init(this, BuildConfig.KAKAO_API_KEY)
        NaverIdLoginSDK.initialize(
            applicationContext,
            BuildConfig.OAUTH_CLIENT_ID,
            BuildConfig.OAUTH_CLIENT_SECRET,
            BuildConfig.OAUTH_CLIENT_NAME
        )
    }
}