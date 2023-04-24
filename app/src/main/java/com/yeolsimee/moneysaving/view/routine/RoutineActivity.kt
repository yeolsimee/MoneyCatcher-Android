@file:OptIn(ExperimentalMaterial3Api::class)

package com.yeolsimee.moneysaving.view.routine

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.collectAsState
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.yeolsimee.moneysaving.App
import com.yeolsimee.moneysaving.utils.notification.RoutineAlarmManager
import com.yeolsimee.moneysaving.view.category.CategoryViewModel
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalLayoutApi
@ExperimentalMaterial3Api
@AndroidEntryPoint
class RoutineActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val routineType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("routineType", RoutineModifyOption::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("routineType") as RoutineModifyOption
        }

        setContent {
            val categoryViewModel: CategoryViewModel = hiltViewModel()

            val categoryList = categoryViewModel.container.stateFlow.collectAsState()
            RoutineScreen(
                routineType = routineType,
                categoryList = categoryList.value,
                closeCallback = {
                    finish()
                },
                onCompleteCallback = {
                    // 1. API 전송
                    val routineId = 1   // TODO 생성된 루틴의 결과에서 routineId 값을 가지고 와서 알람 ID 생성할 때 이용 하기
                    // 2. Alarm 추가
                    if (it.alarmStatus == "ON") {
                        RoutineAlarmManager.set(this@RoutineActivity, it, routineId)
                    }
                },
                hasNotificationPermission = {
                    checkNotificationPermission()
                },
                onCategoryAdded = {
                    categoryViewModel.addCategory(it)
                }
            )
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.i(App.TAG, "Notification Permission Granted")
        } else {
            Log.i(App.TAG, "Notification Permission Not Granted")
        }
    }

    private fun checkNotificationPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this, Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    return true
                }

                shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                    // Responds to click on the action
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val uri: Uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                }

                else -> {
                    // The registered ActivityResultCallback gets the result of this request
                    requestPermissionLauncher.launch(
                        Manifest.permission.POST_NOTIFICATIONS
                    )
                }
            }
            return false
        }
        return true
    }
}