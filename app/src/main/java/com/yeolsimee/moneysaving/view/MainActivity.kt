@file:OptIn(ExperimentalMaterial3Api::class)

package com.yeolsimee.moneysaving.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.yeolsimee.moneysaving.BottomNavItem
import com.yeolsimee.moneysaving.ui.theme.MoneyCatcherTheme
import com.yeolsimee.moneysaving.utils.SetStatusBarColor
import com.yeolsimee.moneysaving.view.calendar.CalendarViewModel
import com.yeolsimee.moneysaving.view.home.HomeScreen
import com.yeolsimee.moneysaving.view.mypage.MyPageScreen
import com.yeolsimee.moneysaving.view.recommend.RecommendScreen

class MainActivity: ComponentActivity() {

    private lateinit var callback: OnBackPressedCallback
    private var pressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MoneyCatcherTheme {
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
        SetStatusBarColor(Color.White)
        Scaffold(
            content = {
                Box(Modifier.padding(it)) {
                    NavHost(navController = navController, startDestination = BottomNavItem.Home.screenRoute) {
                        composable(BottomNavItem.Home.screenRoute) {
                            val calendarViewModel: CalendarViewModel by viewModels()
                            HomeScreen(calendarViewModel)
                        }
                        composable(BottomNavItem.Recommend.screenRoute) {
                            RecommendScreen()
                        }
                        composable(BottomNavItem.MyPage.screenRoute) {
                            MyPageScreen()
                        }
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
            Modifier.background(Color.White),
            contentColor = Color.Black
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            items.forEach { item ->

                val isSelected = currentRoute == item.screenRoute
                val resId = if (isSelected) item.pressedResId else item.normalResId
                val fontWeight = if (isSelected) FontWeight.Bold else FontWeight.W500

                NavigationBarItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = resId),
                            contentDescription = item.title,
                            modifier = Modifier
                                .width(20.dp)
                                .height(20.dp)
                        )
                    },
                    label = {
                        Text(
                            item.title,
                            fontSize = 10.sp,
                            fontWeight = fontWeight,
                            lineHeight = 24.sp,
                            softWrap = false
                        )
                    },
                    selected = isSelected,
                    alwaysShowLabel = true,
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
                        selectedIconColor = Color.Black,
                        unselectedIconColor = Color.Gray
                    ),
                )
            }
        }
    }
}