@file:OptIn(
    ExperimentalLayoutApi::class,
    ExperimentalMaterial3Api::class
)

package com.yeolsimee.moneysaving.view

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yeolsimee.moneysaving.Routes
import com.yeolsimee.moneysaving.domain.entity.routine.RoutineRequest
import com.yeolsimee.moneysaving.ui.loading.LoadingScreen
import com.yeolsimee.moneysaving.ui.theme.RoumoTheme
import com.yeolsimee.moneysaving.utils.executeForTimeMillis
import com.yeolsimee.moneysaving.utils.hasNotificationPermission
import com.yeolsimee.moneysaving.utils.notification.RoutineAlarmManager
import com.yeolsimee.moneysaving.view.login.EmailLoginScreen
import com.yeolsimee.moneysaving.view.login.EmailLoginViewModel
import com.yeolsimee.moneysaving.view.login.LoginScreen
import com.yeolsimee.moneysaving.view.login.LoginViewModel
import com.yeolsimee.moneysaving.view.mypage.MyPageViewModel
import com.yeolsimee.moneysaving.view.routine.AlarmViewModel
import com.yeolsimee.moneysaving.view.signup.AgreementScreen
import com.yeolsimee.moneysaving.view.signup.AgreementViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var googleLoginLauncher: ActivityResultLauncher<Intent>
    private lateinit var naverLoginLauncher: ActivityResultLauncher<Intent>
    private val loadingState = MutableStateFlow(false)
    private val loginViewModel: LoginViewModel by viewModels()

    private val emailLoginViewModel: EmailLoginViewModel by viewModels()
    private val myPageViewModel: MyPageViewModel by viewModels()
    private val alarmViewModel: AlarmViewModel by viewModels()

    private lateinit var alarmState: State<Boolean>
    private lateinit var snackbarState: SnackbarHostState

    private val hasNotificationPermission = MutableStateFlow<Boolean?>(null)
    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        setSplashScreenOn()

        super.onCreate(savedInstanceState)

        initAuth()

        setContent {
            navController = rememberNavController()
            snackbarState = remember { SnackbarHostState() }

            BackHandler {

            }

            Log.i("main", "setContent")
            RoumoTheme(navigationBarColor = Color.White) {
                Log.i("main", "RoumoTheme")
                NavHost(navController = navController, startDestination = Routes.splash) {
                    composable(Routes.splash) {}
                    composable(Routes.login) {
                        LoginScreen(
                            onNaverLogin = {
                                loadingState.value = true
                                val loginResult =
                                    loginViewModel.naverLogin(
                                        applicationContext,
                                        naverLoginLauncher
                                    )
                                if (!loginResult) loadingState.value = false
                            },
                            onGoogleLogin = {
                                loginViewModel.googleLogin(googleLoginLauncher)
                            },
                            onAppleLogin = {
                                loadingState.value = true
                                loginViewModel.appleLogin(
                                    this@MainActivity,
                                    loadingState = loadingState,
                                    signedUserCallback = {
                                        navController.navigate(Routes.main)
                                    },
                                    newUserCallback = { navController.navigate(Routes.agreement) },
                                    loginFailCallback = { loadingState.value = false },
                                )
                            },
                            onEmailButtonClick = {
                                navController.navigate(Routes.emailLogin)
                            }
                        )
                        if (loadingState.collectAsState().value) {
                            LoadingScreen()
                        }
                    }
                    composable(Routes.agreement) {
                        val viewModel: AgreementViewModel by viewModels()
                        AgreementScreen(
                            onFinish = { navController.popBackStack() },
                            onClick = {
                                viewModel.signUp(onSuccess = {
                                    navController.navigate(Routes.main)
                                },
                                    onFailure = {}
                                )
                            },
                            onBrowserOpen = { url ->
                                startActivity(
                                    Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                )
                            }
                        )
                    }
                    composable(Routes.main) {

                        LaunchedEffect(Unit) {
                            myPageViewModel.getSettings(hasPermission = hasNotificationPermission(postNotificationPermissionLauncher))
                        }

                        alarmState = myPageViewModel.alarmState.collectAsState()

                        MainScreen(
                            alarmState = alarmState,
                            snackbarState = snackbarState,
                            onRouteToLoginScreen = {
                                loadingState.value = false
                                navController.navigate(Routes.login)
                            },
                            onChangeAlarmState = {
                                if (hasNotificationPermission(postNotificationPermissionLauncher)) {
                                    myPageViewModel.changeAlarmState()
                                    executeForTimeMillis(CoroutineScope(Dispatchers.Main), 1000) {
                                        snackbarState.showSnackbar(
                                            message = if (it) "알림이 설정되었어요!" else "알림이 해제되었어요!",
                                            duration = SnackbarDuration.Short
                                        )
                                    }
                                }
                            },
                            setRoutineAlarm = { hasWeekTypes, req, id ->
                                setRoutineAlarm(hasWeekTypes, req, id)
                            },
                        ) { alarmState, notificationCheckDialogState ->
                            setAlarmOnIfHasPermission(alarmState, notificationCheckDialogState)
                        }
                    }
                    composable(Routes.emailLogin) {
                        EmailLoginScreen(
                            onBackClick = {
                                navController.popBackStack()
                            }
                        )
                    }

                }

                loginViewModel.autoLogin(
                    signedUserCallback = {
                        setSplashScreenOff()
                        loadingState.value = false
                        navController.navigate(Routes.main)
                    },
                    newUserCallback = {
                        loadingState.value = false
                        loginViewModel.logout()
                        setSplashScreenOff()
                        navController.navigate(Routes.agreement)
                    },
                    notLoggedInCallback = {
                        setSplashScreenOff()
                        loadingState.value = false
                        navController.navigate(Routes.login)
                    }
                )
            }
        }
    }

    private fun setSplashScreenOn() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            val splashScreen = installSplashScreen()
//            splashScreen.setKeepOnScreenCondition { true }
//            Log.i("SplashActivity", "SplashScreen is on")
//        }
    }

    private fun setSplashScreenOff() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            val splashScreen = installSplashScreen()
//            splashScreen.setKeepOnScreenCondition { false }
//            Log.i("SplashActivity", "SplashScreen is off")
//        }
    }

    private fun setRoutineAlarm(
        hasWeekTypes: Boolean,
        req: RoutineRequest,
        id: Int,
    ) {
        if (hasWeekTypes) {
            RoutineAlarmManager.setRoutine(
                context = this@MainActivity,
                dayOfWeeks = req.weekTypes,
                alarmTime = req.alarmTime,
                routineId = id,
                routineName = req.routineName
            ) { alarmId, dayOfWeek ->
                alarmViewModel.addAlarm(
                    alarmId,
                    dayOfWeek,
                    req.alarmTime,
                    req.routineName
                )
            }
        } else {
            RoutineAlarmManager.setToday(
                context = this@MainActivity,
                alarmTime = req.alarmTime,
                routineId = id,
                routineName = req.routineName
            )
        }
    }

    private fun setAlarmOnIfHasPermission(
        alarmState: MutableState<Boolean>,
        notificationCheckDialogState: MutableState<Boolean>,
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            hasNotificationPermission.collect {
                if (hasNotificationPermission(postNotificationPermissionLauncher)) {
                    alarmViewModel.getAlarmState {
                        if (it) {
                            alarmState.value = !alarmState.value
                            if (alarmState.value) alarmViewModel.setAlarmOn()
                        } else {
                            notificationCheckDialogState.value = true
                        }
                    }
                }
            }
        }
    }


    private fun initAuth() {
        initGoogleLogin()
        initNaverLogin()
    }

    override fun onResume() {
        super.onResume()
        emailLoginViewModel.receiveEmailResult(
            intent = intent,
            activity = this@MainActivity,
            signedUserCallback = {
                setSplashScreenOff()
                navController.navigate(Routes.main)
            },
            newUserCallback = {
                loadingState.value = false
                setSplashScreenOff()
                navController.navigate(Routes.agreement)
            }
        )
    }

    private fun initNaverLogin() {
        naverLoginLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                    loginViewModel.naverInit(
                        result = it,
                        signedUserCallback = {
                            setSplashScreenOff()
                            navController.navigate(Routes.main)
                        },
                        newUserCallback = {
                            setSplashScreenOff()
                            navController.navigate(Routes.agreement)
                        },
                        loginFailCallback = { loadingState.value = false },
                    )
                } else {
                    loadingState.value = false
                }
            }
    }

    private fun initGoogleLogin() {
        loginViewModel.init(
            this@MainActivity,
            signedUserCallback = {
                setSplashScreenOff()
                navController.navigate(Routes.main)
            },
            newUserCallback = {
                setSplashScreenOff()
                navController.navigate(Routes.agreement)
            },
            loginFailCallback = { loadingState.value = false },
        )
        googleLoginLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                    loadingState.value = true
                    loginViewModel.googleInit(it)
                }
            }
    }

    private val postNotificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        hasNotificationPermission.value = isGranted
        if (isGranted) {
            myPageViewModel.changeAlarmState()
            executeForTimeMillis(CoroutineScope(Dispatchers.Main), 1000) {
                snackbarState.showSnackbar(
                    message = if (alarmState.value) "알림이 해제되었어요!" else "알림이 설정되었어요!",
                    duration = SnackbarDuration.Short
                )
            }
        }
    }
}