package com.yeolsimee.moneysaving.view

import com.yeolsimee.moneysaving.BottomNavItem
import kotlinx.coroutines.flow.MutableStateFlow

class Navigator {
    var destination: MutableStateFlow<BottomNavItem> = MutableStateFlow(BottomNavItem.Home)
}