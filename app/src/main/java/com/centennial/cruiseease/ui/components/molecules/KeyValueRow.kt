package com.centennial.cruiseease.ui.components.molecules

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.centennial.cruiseease.ui.theme.urbanistFont


@Composable
fun KeyValueRow(key: String, value: String) {
    Row {
        Text(
            text = key,
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = urbanistFont,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        )
        Text(
            text = value,
            style = TextStyle(fontSize = 16.sp, fontFamily = urbanistFont, letterSpacing = 0.5.sp),
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}