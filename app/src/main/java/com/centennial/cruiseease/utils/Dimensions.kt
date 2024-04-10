package com.centennial.cruiseease.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration


@Composable
fun getScreenHeightPercentage(percentage: Float): Float {
    val screenHeight = LocalConfiguration.current.screenHeightDp
    return screenHeight * percentage
}

@Composable
fun getScreenWidthPercentage(percentage: Float): Float {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    return screenWidth * percentage
}