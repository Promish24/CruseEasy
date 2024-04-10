package com.centennial.cruiseease.ui.components.organisms

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.centennial.cruiseease.R
import com.centennial.cruiseease.constants.Strings
import com.centennial.cruiseease.ui.theme.primaryBlack
import com.centennial.cruiseease.ui.theme.urbanistFont

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.centennial.cruiseease.constants.Routes


@Composable
fun Header(navController: NavController, currentRoute: String) {
    if (currentRoute == Routes.HOME) {
        HomeHeader()
    } else {
        OtherHeader(navController)
    }
}

@Composable
fun HomeHeader() {
    val uiColor = primaryBlack

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFBFDBFE))
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Icon(
                modifier = Modifier.size(50.dp),
                painter = painterResource(id = R.drawable.cruise),
                contentDescription = Strings.APP_LOGO,
                tint = uiColor
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = Strings.APP_NAME,
                    style = TextStyle(
                        fontFamily = urbanistFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        letterSpacing = 0.8.sp,
                        color = uiColor
                    )
                )
                Text(
                    text = Strings.SLOGAN,
                    style = TextStyle(
                        fontFamily = urbanistFont,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        letterSpacing = 0.8.sp,
                        color = uiColor
                    )
                )
            }
        }
    }
}

@Composable
fun OtherHeader(navController: NavController) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFBFDBFE))
            .padding(10.dp)
            .height(50.dp)
    ) {
        Icon(
            modifier = Modifier.size(24.dp).clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                navController.popBackStack()
            },
            painter = painterResource(id = R.drawable.back_arrow),
            contentDescription = "Back",
            tint = primaryBlack,
        )
        Text(
            text = "Back",
            style = TextStyle(
                fontFamily = urbanistFont,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                letterSpacing = 0.8.sp,
                color = primaryBlack
            ),
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeHeaderPreview() {
    HomeHeader()
}

@Preview(showBackground = true)
@Composable
fun OtherHeaderPreview() {
    val mockNavController = rememberNavController()
    OtherHeader(navController = mockNavController)
}
