package com.yeolsimee.moneysaving.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private const val designHeight = 812
private const val designWidth = 375

@Composable
fun getReactiveHeight(size: Int): Dp =
    (LocalConfiguration.current.screenHeightDp * (size.toDouble() / designHeight)).dp

@Composable
fun getReactiveWidth(size: Int): Dp =
    (LocalConfiguration.current.screenWidthDp * (size.toDouble() / designWidth)).dp