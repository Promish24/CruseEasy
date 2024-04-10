package com.centennial.cruiseease.ui.components.organisms

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.centennial.cruiseease.R
import com.centennial.cruiseease.ui.theme.urbanistFont


import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.remember
import com.centennial.cruiseease.constants.Routes

@Composable
fun BottomTab(navController: NavController, currentScreen: String) {
    val homeComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.home))
    val savedComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.saved))
    val bookingsComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.booking))
    val profileCoposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.profile))
    val interactionSource = remember { MutableInteractionSource() }
    val indicatorHeight = 4.dp

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    navController.navigate(Routes.HOME)
                }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(indicatorHeight)
                    .background(if (currentScreen == Routes.HOME) Color(0xFFBFDBFE) else Color.Transparent)
            )
            LottieAnimation(
                modifier = Modifier.size(30.dp),
                composition = homeComposition,
                iterations = LottieConstants.IterateForever
            )
            Text(
                Routes.HOME,
                style = TextStyle(
                    fontFamily = urbanistFont,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    letterSpacing = 0.8.sp,
                    color = Color.Black
                )
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    navController.navigate(Routes.SAVED)
                }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(indicatorHeight)
                    .background(if (currentScreen == Routes.SAVED) Color(0xFFBFDBFE) else Color.Transparent)
            )
            LottieAnimation(
                modifier = Modifier.size(30.dp),
                composition = savedComposition,
                iterations = LottieConstants.IterateForever
            )
            Text(
                Routes.SAVED,
                style = TextStyle(
                    fontFamily = urbanistFont,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    letterSpacing = 0.8.sp,
                    color = Color.Black
                )
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    navController.navigate(Routes.BOOKINGS)
                }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(indicatorHeight)
                    .background(if (currentScreen == Routes.BOOKINGS) Color(0xFFBFDBFE) else Color.Transparent)
            )
            LottieAnimation(
                modifier = Modifier.size(30.dp),
                composition = bookingsComposition,
                iterations = LottieConstants.IterateForever
            )
            Text(
                Routes.BOOKINGS,
                style = TextStyle(
                    fontFamily = urbanistFont,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    letterSpacing = 0.8.sp,
                    color = Color.Black
                )
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    navController.navigate(Routes.PROFILE)
                }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(indicatorHeight)
                    .background(if (currentScreen == Routes.PROFILE) Color(0xFFBFDBFE) else Color.Transparent)
            )
            LottieAnimation(
                modifier = Modifier.size(30.dp),
                composition = profileCoposition,
                iterations = LottieConstants.IterateForever
            )
            Text(
                Routes.PROFILE,
                style = TextStyle(
                    fontFamily = urbanistFont,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    letterSpacing = 0.8.sp,
                    color = Color.Black
                )
            )
        }
    }
}
