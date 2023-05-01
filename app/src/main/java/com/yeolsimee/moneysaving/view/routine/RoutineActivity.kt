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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.yeolsimee.moneysaving.App
import com.yeolsimee.moneysaving.domain.entity.routine.Routine
import com.yeolsimee.moneysaving.utils.notification.RoutineAlarmManager
import com.yeolsimee.moneysaving.view.category.CategoryViewModel
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalLayoutApi
@ExperimentalMaterial3Api
@AndroidEntryPoint
class RoutineActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val routineType = getRoutineType()
        val routine = getRoutine()
        val categoryId = intent.getStringExtra("categoryId") ?: ""

        setContent {
            val categoryViewModel: CategoryViewModel = hiltViewModel()
            val routineViewModel: RoutineModifyViewModel = hiltViewModel()

            val categoryList = categoryViewModel.container.stateFlow.collectAsState()

            val selectedCategoryId = remember { mutableStateOf(categoryId) }
            RoutineScreen(
                initialData = routine,
                routineType = routineType,
                categoryList = categoryList.value,
                selectedCategoryId = selectedCategoryId,
                closeCallback = {
                    finish()
                },
                onCompleteCallback = { req ->
                    if (routineType == RoutineModifyOption.add) {
                        routineViewModel.addRoutine(
                            routineRequest = req,
                            onSetAlarmCallback = { id ->
                                RoutineAlarmManager.set(this@RoutineActivity, req, id)
                            },
                            onFinishCallback = {
                                sendResultAndFinish()
                            }
                        )
                    } else {
                        routineViewModel.updateRoutine(
                            routineId = routine?.routineId,
                            routineRequest = req,
                            onSetAlarmCallback = { id ->
                                RoutineAlarmManager.set(this@RoutineActivity, req, id)
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

    private fun getRoutine() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        intent.getSerializableExtra("routine", Routine::class.java)
    } else {
        @Suppress("DEPRECATION")
        intent.getSerializableExtra("routine") as Routine?
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