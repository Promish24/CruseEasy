package com.centennial.cruiseease.ui.components.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.centennial.cruiseease.ui.theme.urbanistFont

@Composable
fun ButtonComponent(value: String, onButtonClicked: () -> Unit, isEnabled: Boolean = false) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 3.dp
        ),
        onClick = {
            onButtonClicked.invoke()
        },
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isEnabled) Color(0xFF7210FF) else Color(0xFF616161), contentColor = Color.White
        ),
        shape = RoundedCornerShape(size = 4.dp),
        enabled = isEnabled
    ) {
        Text(
            text = value, style = TextStyle(
                fontFamily = urbanistFont,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                letterSpacing = 0.8.sp,
                color= if (isEnabled) Color.White else Color.Black
            )
        )

    }
}