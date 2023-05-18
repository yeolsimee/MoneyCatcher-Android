package com.yeolsimee.moneysaving

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import com.navercorp.nid.NaverIdLoginSDK
import com.yeolsimee.moneysaving.data.repository.SettingsRepository
import com.yeolsimee.moneysaving.utils.notification.RoutineAlarmManager
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    companion object {
        const val TAG = "RoumoApp"
    }

    @Inject
    lateinit var settingsRepository: SettingsRepository

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
        CoroutineScope(Dispatchers.Main).launch {
            settingsRepository.isFirstInstall {
                if (it) {
                    Log.d(TAG, "First Install Setting Daily Notification")
                    RoutineAlarmManager.setDailyNotification(this@App)
                }
            }
        }
    }
}