package com.centennial.cruiseease.ui.components.molecules

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.centennial.cruiseease.models.datamodel.Cruise
import com.centennial.cruiseease.sealed.Screen
import com.centennial.cruiseease.ui.theme.urbanistFont
import com.centennial.cruiseease.utils.SecurePreferences

@Composable
fun CardItem(cruise: Cruise, navController: NavController) {
    val interactionSource = remember { MutableInteractionSource() }

    val context = LocalContext.current

    Box(
        modifier = Modifier
            .padding(horizontal = 15.dp)
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                val securePreferences = SecurePreferences(context)
                securePreferences.setCruiseData(cruise)
                navController.navigate("${Screen.DetailScreen.route}/${cruise.id}")
            }
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 5.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFEEEEEE),
            ),
            modifier = Modifier.fillMaxSize()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = cruise.name ?: "",
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = urbanistFont
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = cruise.description ?: "",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = urbanistFont,
                        letterSpacing = 0.5.sp,
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    Text(
                        text = "Starting From: ",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = urbanistFont,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "$${cruise.startingFrom}",
                        style = TextStyle(fontSize = 14.sp, fontFamily = urbanistFont)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    Text(
                        text = "Rating: ",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = urbanistFont,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "${cruise.rating} / ${cruise.ratingMax}",
                        style = TextStyle(fontSize = 14.sp, fontFamily = urbanistFont)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(vertical = 8.dp)
                        .border(BorderStroke(0.dp, Color.Gray), shape = RoundedCornerShape(8.dp))
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(cruise.background),
                        contentDescription = "Cruise Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

            }
        }
    }
}
