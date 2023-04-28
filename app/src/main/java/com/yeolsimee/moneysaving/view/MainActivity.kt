@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.yeolsimee.moneysaving.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.yeolsimee.moneysaving.ui.PrText
import com.yeolsimee.moneysaving.ui.theme.Gray99
import com.yeolsimee.moneysaving.ui.theme.RoumoTheme
import com.yeolsimee.moneysaving.view.home.calendar.CalendarViewModel
import com.yeolsimee.moneysaving.view.home.calendar.FindAllMyRoutineViewModel
import com.yeolsimee.moneysaving.view.home.calendar.SelectedDateViewModel
import com.yeolsimee.moneysaving.view.home.HomeScreen
import com.yeolsimee.moneysaving.view.home.RoutineCheckViewModel
import com.yeolsimee.moneysaving.view.mypage.MyPageScreen
import com.yeolsimee.moneysaving.view.recommend.RecommendScreen
import com.yeolsimee.moneysaving.view.routine.RoutineActivity
import com.yeolsimee.moneysaving.view.routine.RoutineModifyOption
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalLayoutApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var callback: OnBackPressedCallback
    private var pressedTime: Long = 0
    private val routineActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        result.data?.getEx
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RoumoTheme(navigationBarColor = Color.Black) {
                MainScreenView()
            }
        }

        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (pressedTime + 2000 > System.currentTimeMillis()) {
                    finishAffinity()
                } else {
                    Toast.makeText(applicationContext, "한번 더 누르면 종료", Toast.LENGTH_SHORT).show()
                }
                pressedTime = System.currentTimeMillis()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
    }

    @Composable
    fun MainScreenView() {
        val navController = rememberNavController()
        val floatingButtonVisible = remember { mutableStateOf(false) }

        val calendarViewModel: CalendarViewModel = hiltViewModel()
        val selectedDateViewModel: SelectedDateViewModel = hiltViewModel()
        val findAllMyRoutineViewModel: FindAllMyRoutineViewModel = hiltViewModel()
        val routineCheckViewModel: RoutineCheckViewModel = hiltViewModel()

        val today = calendarViewModel.today
        selectedDateViewModel.find(today)
        val dayList = calendarViewModel.dayList.value!!

        findAllMyRoutineViewModel.find(
            calendarViewModel.getFirstAndLastDate(dayList),
            today.month,
            dayList
        )

        Scaffold(
            content = {
                Box(
                    Modifier
                        .padding(it)
                        .background(Color.White)
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = BottomNavItem.Home.screenRoute
                    ) {
                        composable(BottomNavItem.Home.screenRoute) {

                            floatingButtonVisible.value = true

                            HomeScreen(
                                calendarViewModel = calendarViewModel,
                                selectedDateViewModel = selectedDateViewModel,
                                findAllMyRoutineViewModel = findAllMyRoutineViewModel,
                                routineCheckViewModel = routineCheckViewModel,
                                onItemClick = { routine, categoryId ->
                                    val intent = Intent(this@MainActivity, RoutineActivity::class.java)
                                    intent.putExtra("routine", routine)
                                    intent.putExtra("routineType", RoutineModifyOption.update)
                                    intent.putExtra("categoryId", categoryId)
                                    routineActivityLauncher.launch(intent)
                                }
                            )
                        }
                        composable(BottomNavItem.Recommend.screenRoute) {
                            floatingButtonVisible.value = false
                            RecommendScreen()
                        }
                        composable(BottomNavItem.MyPage.screenRoute) {
                            floatingButtonVisible.value = false
                            MyPageScreen()
                        }
                    }
                }
            },
            floatingActionButton = {
                if (floatingButtonVisible.value) {
                    FloatingActionButton(
                        onClick = {
                            val intent = Intent(this@MainActivity, RoutineActivity::class.java)
                            intent.putExtra("routineType", RoutineModifyOption.add)
                            routineActivityLauncher.launch(intent)
                        },
                        containerColor = Color.Black,
                        shape = CircleShape,
                        elevation = FloatingActionButtonDefaults.elevation(0.dp),
                        modifier = Modifier.padding(end = 12.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.icon_plus),
                            contentDescription = "루틴 추가"
                        )
                    }
                }
            },
            bottomBar = { MainBottomNavigation(navController) }
        )
    }

    @Composable
    fun MainBottomNavigation(navController: NavHostController) {
        val items = listOf(
            BottomNavItem.Home,
            BottomNavItem.Recommend,
            BottomNavItem.MyPage
        )

        NavigationBar(
            contentColor = Color.Black,
            containerColor = Color.Black,
            modifier = Modifier.height(49.dp)
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            items.forEach { item ->

                val isSelected = currentRoute == item.screenRoute
                val resId = if (isSelected) item.pressedResId else item.normalResId
                val fontWeight = if (isSelected) FontWeight.Bold else FontWeight.W400
                val labelColor = if (isSelected) Color.White else Gray99

                NavigationBarItem(
                    icon = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
                            Spacer(modifier = Modifier.height(4.dp))
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
                    },
                    selected = isSelected,
                    onClick = {
                        navController.navigate(item.screenRoute) {
                            navController.graph.startDestinationRoute?.let {
                                popUpTo(it) { saveState = true }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        indicatorColor = Color.Transparent
                    ),
                )
            }
        }
    }
}