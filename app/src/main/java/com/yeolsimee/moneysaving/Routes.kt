package com.yeolsimee.moneysaving

sealed class BottomNavItem(
    val title: String, val normalResId: Int, val pressedResId: Int, val screenRoute: String
) {

    object Home : BottomNavItem("홈", R.drawable.icon_home_off, R.drawable.icon_home_on, "Home")
    object Recommend :
        BottomNavItem("루틴추천", R.drawable.icon_routine_off, R.drawable.icon_routine_on, "Recommend")

    object MyPage :
        BottomNavItem("내 정보", R.drawable.icon_mypage_off, R.drawable.icon_mypage_on, "MyPage")

}

object Routes {
    const val splash = "splash"
    const val login = "login"
    const val agreement = "agreement"
    const val main = "main"
    const val routine = "routine"
    const val emailLogin = "email_login"

    const val routineAdd = "routine_add"
    const val routineUpdate = "routine_modify"
}