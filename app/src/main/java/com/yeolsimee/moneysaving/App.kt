package com.yeolsimee.moneysaving

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import com.navercorp.nid.NaverIdLoginSDK
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    companion object {
        const val TAG = "RoumoApp"
    }

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(applicationContext)
        try {
            NaverIdLoginSDK.initialize(
                applicationContext,
                BuildConfig.OAUTH_CLIENT_ID,
                BuildConfig.OAUTH_CLIENT_SECRET,
                BuildConfig.OAUTH_CLIENT_NAME
            )
        } catch (e: Exception) {
            Log.e(TAG, "네이버 로그인 초기화 오류")
            e.printStackTrace()
        }
    }
}