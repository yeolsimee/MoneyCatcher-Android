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
import androidx.activity.viewModels
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.content.ContextCompat
import com.yeolsimee.moneysaving.App
import com.yeolsimee.moneysaving.domain.entity.routine.RoutineResponse
import com.yeolsimee.moneysaving.utils.notification.RoutineAlarmManager
import com.yeolsimee.moneysaving.view.category.CategoryViewModel
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalLayoutApi
@ExperimentalMaterial3Api
@AndroidEntryPoint
class RoutineActivity : ComponentActivity() {

    private val categoryViewModel: CategoryViewModel by viewModels()
    private val routineViewModel: RoutineModifyViewModel by viewModels()
    private val alarmViewModel: AlarmViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val routineType = getRoutineType()
        val routineResponse = getRoutineResponse()
        val categoryId = intent.getStringExtra("categoryId") ?: ""

        setContent {
            val selectedCategoryId = remember { mutableStateOf(categoryId) }
            RoutineScreen(
                initialData = routineResponse,
                routineType = routineType,
                categoryList = categoryViewModel.container.stateFlow.collectAsState().value,
                selectedCategoryId = selectedCategoryId,
                closeCallback = {
                    finish()
                },
                onCompleteCallback = { req ->
                    if (routineType == RoutineModifyOption.Add) {
                        routineViewModel.addRoutine(
                            routineRequest = req,
                            onSetAlarmCallback = { id ->
                                RoutineAlarmManager.setRoutine(
                                    this@RoutineActivity,
                                    req.weekTypes,
                                    req.alarmTime,
                                    id
                                ) { alarmId, dayOfWeek ->
                                    alarmViewModel.addAlarm(alarmId, dayOfWeek, req.alarmTime)
                                }
                            },
                            onFinishCallback = {
                                sendResultAndFinish()
                            }
                        )
                    } else {
                        routineViewModel.updateRoutine(
                            routineId = routineResponse?.routineId,
                            routineRequest = req,
                            onSetAlarmCallback = { id ->
                                RoutineAlarmManager.setRoutine(
                                    this@RoutineActivity,
                                    req.weekTypes,
                                    req.alarmTime,
                                    id
                                )
                            },
                            onDeleteAlarmCallback = { res ->
                                RoutineAlarmManager.delete(this@RoutineActivity, res) {
                                    alarmViewModel.deleteAlarm(it)
                                }
                            },
                            onFinishCallback = {
                                sendResultAndFinish()
                            }
                        )
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

    private fun sendResultAndFinish() {
        setResult(RESULT_OK)
        finish()
    }

    private fun getRoutineResponse() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        intent.getSerializableExtra("routine", RoutineResponse::class.java)
    } else {
        @Suppress("DEPRECATION")
        intent.getSerializableExtra("routine") as RoutineResponse?
    }

    private fun getRoutineType() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        intent.getSerializableExtra("routineType", RoutineModifyOption::class.java)
    } else {
        @Suppress("DEPRECATION")
        intent.getSerializableExtra("routineType") as RoutineModifyOption
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