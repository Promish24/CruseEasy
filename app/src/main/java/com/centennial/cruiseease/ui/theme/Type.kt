package com.centennial.cruiseease.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp



val TypoGraphy = Typography(
    bodyLarge = TextStyle(
        fontFamily = urbanistFont,
        fontWeight = FontWeight.Normal,
        textAlign = TextAlign.Center,
        fontSize = 16.sp,
    ),
    headlineLarge = TextStyle(
        fontFamily = urbanistFont,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        fontSize = 35.sp,
        letterSpacing = 0.5.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = urbanistFont,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold
    ),
    titleMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        fontFamily = urbanistFont
    ),
    displayMedium = TextStyle(
        fontFamily = urbanistFont,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        fontSize = 24.sp,
        letterSpacing = 0.5.sp
    ),
    displaySmall = TextStyle(
        fontFamily = urbanistFont,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        fontSize = 20.sp,
        letterSpacing = 0.5.sp
    ),
    labelLarge = TextStyle(
        fontFamily = urbanistFont,
        fontWeight = FontWeight.SemiBold,
        textAlign = TextAlign.Center,
        fontSize = 24.sp,
        letterSpacing = 0.5.sp
    ),
    labelMedium = TextStyle(
        fontFamily = urbanistFont,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        fontSize = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = urbanistFont,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        fontSize = 14.sp,
        letterSpacing = 0.5.sp
    ),
)