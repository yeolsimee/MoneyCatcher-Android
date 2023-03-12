package com.yeolsimee.moneysaving

import android.app.Application
import com.google.firebase.FirebaseApp
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application() {

    companion object {
        const val TAG = "MoneySavingApp"
    }
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(applicationContext)
        KakaoSdk.init(this, BuildConfig.KAKAO_API_KEY)
    }
}