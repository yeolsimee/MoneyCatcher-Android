package com.yeolsimee.moneysaving

sealed class BottomNavItem(
    val title: String, val normalResId: Int, val pressedResId: Int, val screenRoute: String
) {

    object Home : BottomNavItem("홈", R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_foreground, "Home")
    object Recommend :
        BottomNavItem("루틴추천", R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_foreground, "Recommend")

    object MyPage :
        BottomNavItem("내 정보", R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_foreground, "MyPage")

}