package com.yeolsimee.moneysaving.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.yeolsimee.moneysaving.BottomNavItem
import com.yeolsimee.moneysaving.R
import com.yeolsimee.moneysaving.Routes
import com.yeolsimee.moneysaving.domain.calendar.CalendarDay
import com.yeolsimee.moneysaving.domain.entity.category.CategoryWithRoutines
import com.yeolsimee.moneysaving.domain.entity.category.TextItem
import com.yeolsimee.moneysaving.domain.entity.routine.RoutineRequest
import com.yeolsimee.moneysaving.ui.PrText
import com.yeolsimee.moneysaving.ui.dialog.CategoryModifyDialog
import com.yeolsimee.moneysaving.ui.dialog.CategoryUpdateDialog
import com.yeolsimee.moneysaving.ui.dialog.TwoButtonOneTitleDialog
import com.yeolsimee.moneysaving.ui.snackbar.CustomSnackBarHost
import com.yeolsimee.moneysaving.ui.theme.Gray99
import com.yeolsimee.moneysaving.ui.theme.RoumoTheme
import com.yeolsimee.moneysaving.utils.DialogState
import com.yeolsimee.moneysaving.utils.notification.RoutineAlarmManager
import com.yeolsimee.moneysaving.view.category.CategoryViewModel
import com.yeolsimee.moneysaving.view.home.HomeScreen
import com.yeolsimee.moneysaving.view.home.calendar.CalendarViewModel
import com.yeolsimee.moneysaving.view.home.calendar.FindAllMyRoutineViewModel
import com.yeolsimee.moneysaving.view.home.calendar.SelectedDateViewModel
import com.yeolsimee.moneysaving.view.mypage.MyPageScreen
import com.yeolsimee.moneysaving.view.mypage.MyPageViewModel
import com.yeolsimee.moneysaving.view.recommend.RecommendScreen
import com.yeolsimee.moneysaving.view.routine.AlarmViewModel
import com.yeolsimee.moneysaving.view.routine.GetRoutineViewModel
import com.yeolsimee.moneysaving.view.routine.RoutineModifyViewModel
import com.yeolsimee.moneysaving.view.routine.RoutineScreen
import com.yeolsimee.moneysaving.view.routine.RoutineType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun MainScreen(
    alarmState: State<Boolean>,
    navigator: Navigator = Navigator(),
    snackbarState: SnackbarHostState = remember { SnackbarHostState() },
    onRouteToLoginScreen: () -> Unit = {},
    onChangeAlarmState: (Boolean) -> Unit = {},
    findAllMyRoutineViewModel: FindAllMyRoutineViewModel = hiltViewModel(),
    calendarViewModel: CalendarViewModel = hiltViewModel(),
    selectedDateViewModel: SelectedDateViewModel = hiltViewModel(),
    setRoutineAlarm: (hasWeekTypes: Boolean, req: RoutineRequest, id: Int) -> Unit = { _, _, _ -> },
    setAlarmOnIfHasPermission: (alarmState: MutableState<Boolean>, notificationCheckDialogState: MutableState<Boolean>) -> Unit = { _, _ -> },
) {
    val activity = LocalContext.current as Activity

    val navController = rememberNavController()
    val floatingButtonVisible = remember { mutableStateOf(false) }
    val categoryModifyDialogState: MutableState<DialogState<CategoryWithRoutines>> =
        remember { mutableStateOf(DialogState(false, null)) }

    val today = calendarViewModel.today
    val dayList = calendarViewModel.dayList.collectAsState().value

    val selectedDateState = remember { mutableStateOf(calendarViewModel.today) }

    val destination by navigator.destination.collectAsState()
    LaunchedEffect(destination) {
        if (navController.currentDestination?.route != destination.screenRoute) {
            navigateTo(navController, destination)
        }
    }

    LaunchedEffect(Unit) {
        findAllMyRoutineViewModel.find(
            calendarViewModel.getFirstAndLastDate(dayList), today.month, dayList
        )

        selectedDateViewModel.find(today)
    }

    RoumoTheme(navigationBarColor = Color.Black) {
        Scaffold(
            floatingActionButton = {
                val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route
                if (floatingButtonVisible.value &&
                    currentDestination == BottomNavItem.Home.screenRoute) {
                    FloatingActionButton(
                        onClick = {
                            navController.navigate(Routes.routineAdd)
                        },
                        containerColor = Color.Black,
                        shape = CircleShape,
                        elevation = FloatingActionButtonDefaults.elevation(0.dp),
                        modifier = Modifier
                            .padding(end = 12.dp, bottom = 24.dp)
                            .size(50.dp)
                    ) {
                        Image(
                            modifier = Modifier.size(20.dp),
                            painter = painterResource(id = R.drawable.icon_plus),
                            contentDescription = "루틴 추가"
                        )
                    }
                }
            },
            bottomBar = {
                val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route
                if (currentDestination == BottomNavItem.Home.screenRoute
                    || currentDestination == BottomNavItem.MyPage.screenRoute
                    || currentDestination == BottomNavItem.Recommend.screenRoute) {
                    MainBottomNavigation(navController, categoryModifyDialogState, selectedDateState)
                }
            }
        ) {
            Box(Modifier.padding(it)) {
                NavHost(
                    navController = navController,
                    startDestination = BottomNavItem.Home.screenRoute
                ) {
                    composable(BottomNavItem.Home.screenRoute) {
                        HomeScreen(
                            calendarViewModel = calendarViewModel,
                            selectedDateViewModel = selectedDateViewModel,
                            findAllMyRoutineViewModel = findAllMyRoutineViewModel,
                            categoryModifyDialogState = categoryModifyDialogState,
                            selectedState = selectedDateState,
                            floatingButtonVisible = floatingButtonVisible,
                            onItemClick = { routineId, categoryId ->
                                navController.navigate("${Routes.routineUpdate}/$routineId/$categoryId") {
                                    launchSingleTop = false
                                    restoreState = true
                                }
                            },
                            onDelete = { routineId ->
                                RoutineAlarmManager.delete(activity, routineId)
                            },
                        )
                        CustomSnackBarHost(snackbarState)
                    }
                    composable(BottomNavItem.Recommend.screenRoute) {
                        RecommendScreen()
                    }
                    composable(BottomNavItem.MyPage.screenRoute) {
                        val myPageViewModel: MyPageViewModel = hiltViewModel()

                        MyPageScreen(
                            alarmState = alarmState,
                            onChangeAlarmState = {
                                onChangeAlarmState(it)
                            },
                            onLogout = {
                                myPageViewModel.logoutAndCancelAlarms(activity) {
                                    CoroutineScope(Dispatchers.Main).launch {
                                        onRouteToLoginScreen()
                                    }
                                }
                            }, onWithdraw = {
                                myPageViewModel.withdraw(activity) {
                                    CoroutineScope(Dispatchers.Main).launch {
                                        onRouteToLoginScreen()
                                    }
                                }
                            }, openInternetBrowser = { url ->
                                activity.startActivity(
                                    Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                )
                            },
                        )

                        CustomSnackBarHost(snackbarState)
                    }


                    composable(Routes.routineAdd) {
                        val routineViewModel: RoutineModifyViewModel = hiltViewModel()

                        RoutineScreen(
                            routineType = RoutineType.Add,
                            closeCallback = {
                                navController.popBackStack()
                            },
                            onCompleteCallback = { req, hasWeekTypes, _ ->
                                routineViewModel.addRoutine(
                                    routineRequest = req,
                                    onSetAlarmCallback = { id ->
                                        setRoutineAlarm(hasWeekTypes, req, id)
                                    },
                                    onFinishCallback = {
                                        CoroutineScope(Dispatchers.Main).launch {
                                            navController.popBackStack()
                                            selectedDateViewModel.find(calendarViewModel.today)
                                        }
                                    }
                                )
                            },
                            toggleRoutineAlarm = { alarmState, notificationCheckDialogState ->
                                setAlarmOnIfHasPermission(
                                    alarmState,
                                    notificationCheckDialogState
                                )
                            },
                            onCheckNotificationSetting = {
                                navController.popBackStack()
                                // TODO MyPage로 이동
                            },

                            )
                    }
                    composable("${Routes.routineUpdate}/{routineId}/{categoryId}") { entry ->
                        val routineId = (entry.arguments?.getString("routineId") ?: "0").toInt()
                        val categoryId = entry.arguments?.getString("categoryId") ?: ""
                        val routineViewModel: RoutineModifyViewModel = hiltViewModel()
                        val alarmViewModel: AlarmViewModel = hiltViewModel()

                        val getRoutineViewModel: GetRoutineViewModel = hiltViewModel()

                        LaunchedEffect(getRoutineViewModel) {
                            getRoutineViewModel.getRoutine(routineId)
                        }

                        val routine by getRoutineViewModel.collectAsState()

                        if (!routine.isEmpty()) {
                            RoutineScreen(
                                routine = routine,
                                routineType = RoutineType.Update,
                                selectedCategoryId = remember { mutableStateOf(categoryId) },
                                closeCallback = {
                                    navController.popBackStack()
                                },
                                onCompleteCallback = { req, hasWeekTypes, completedRoutine ->
                                    routineViewModel.updateRoutine(
                                        routine = completedRoutine,
                                        routineRequest = req,
                                        onSetAlarmCallback = { changedRoutine ->
                                            alarmViewModel.updateCheckedRoutine(
                                                changedRoutine.alarmTime,
                                                req.alarmTime
                                            )
                                            setRoutineAlarm(
                                                hasWeekTypes,
                                                req,
                                                changedRoutine.routineId
                                            )
                                        },
                                        onDeleteAlarmCallback = { res ->
                                            RoutineAlarmManager.delete(activity, res) { alarmId ->
                                                alarmViewModel.deleteAlarm(alarmId)
                                            }
                                        },
                                        onFinishCallback = {
                                            navController.popBackStack()
                                            selectedDateViewModel.find(calendarViewModel.today)
                                        }
                                    )
                                },
                                toggleRoutineAlarm = { alarmState, notificationCheckDialogState ->
                                    setAlarmOnIfHasPermission(
                                        alarmState,
                                        notificationCheckDialogState
                                    )
                                },
                                onCheckNotificationSetting = {
                                    navController.popBackStack()
                                    // MyPage로 이동
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MainBottomNavigation(
    navController: NavHostController,
    categoryModifyDialogState: MutableState<DialogState<CategoryWithRoutines>>,
    selectedDateState: MutableState<CalendarDay>,
) {
    val selectedDateViewModel: SelectedDateViewModel = hiltViewModel()

    Column(modifier = Modifier) {
        val categoryUpdateDialogState: MutableState<Boolean> =
            remember { mutableStateOf(false) }
        val categoryDeleteDialogState: MutableState<Boolean> =
            remember { mutableStateOf(false) }

        val dialogState = categoryModifyDialogState.value
        if (dialogState.isShowing) {
            CategoryModifyDialog(
                state = categoryModifyDialogState,
                categoryUpdateDialogState,
                categoryDeleteDialogState
            )
        }
        if (categoryUpdateDialogState.value) {
            val categoryViewModel: CategoryViewModel = hiltViewModel()
            val category = dialogState.data!!.getTextItem()
            categoryModifyDialogState.value = dialogState.copy(isShowing = false)
            CategoryUpdateDialog(dialogState = categoryUpdateDialogState,
                categoryName = remember { mutableStateOf(category.name) },
                onConfirmClick = { categoryName ->
                    CoroutineScope(Dispatchers.Main).launch {
                        val result =
                            categoryViewModel.update(TextItem(category.id, categoryName))
                        if (result.isSuccess) {
                            selectedDateViewModel.find(selectedDateState.value)
                        }
                    }
                })
        }

        if (categoryDeleteDialogState.value) {
            val categoryViewModel: CategoryViewModel = hiltViewModel()
            val category = dialogState.data!!.getTextItem()
            categoryModifyDialogState.value = dialogState.copy(isShowing = false)
            TwoButtonOneTitleDialog(text = "카테고리 삭제시 해당 루틴들도 함께 사라져요.\n" + "해당 카테고리를 정말 삭제하시겠어요?",
                dialogState = categoryDeleteDialogState,
                onConfirmClick = {
                    CoroutineScope(Dispatchers.Main).launch {
                        categoryViewModel.delete(category).onSuccess {
                            selectedDateViewModel.find(selectedDateState.value)
                        }
                    }
                },
                onCancelClick = {})
        }

        BottomNavigator(
            navController, listOf(
                BottomNavItem.Home, BottomNavItem.Recommend, BottomNavItem.MyPage
            )
        )
    }
}

@Composable
fun BottomNavigator(
    navController: NavHostController,
    items: List<BottomNavItem>,
) {
    NavigationBar(
        contentColor = Color.Black,
        containerColor = Color.Black,
        modifier = Modifier.height(66.dp),
        tonalElevation = 0.dp
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->

            val isSelected = currentRoute == item.screenRoute
            val resId = if (isSelected) item.pressedResId else item.normalResId
            val fontWeight = if (isSelected) FontWeight.Bold else FontWeight.W400
            val labelColor = if (isSelected) Color.White else Gray99

            NavigationBarItem(icon = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    Box(
                        Modifier
                            .height(3.dp)
                            .width(50.dp)
                            .clip(RoundedCornerShape(size = 2.5.dp))
                            .background(color = if (isSelected) Color.White else Color.Black)
                    )
                    Spacer(Modifier.height(9.dp))
                    Icon(
                        painter = painterResource(id = resId),
                        contentDescription = item.title,
                        modifier = Modifier
                            .width(20.dp)
                            .height(20.dp)
                            .padding(0.dp)
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    PrText(
                        item.title,
                        fontSize = 10.sp,
                        fontWeight = fontWeight,
                        textAlign = TextAlign.Center,
                        letterSpacing = (-0.1).sp,
                        color = labelColor,
                        softWrap = false,
                    )
                }
            }, selected = isSelected, onClick = {
                navigateTo(navController, item)
            }, colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White, indicatorColor = Color.Transparent
            ), modifier = Modifier.padding(0.dp)
            )
        }
    }
}

private fun navigateTo(navController: NavHostController, item: BottomNavItem) {
    try {
        navController.navigate(item.screenRoute) {
            navController.graph.startDestinationRoute?.let {
                popUpTo(it) { saveState = true }
            }
            launchSingleTop = true
            restoreState = true
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}