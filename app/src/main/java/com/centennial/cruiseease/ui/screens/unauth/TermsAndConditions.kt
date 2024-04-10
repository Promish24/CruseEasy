package com.centennial.cruiseease.ui.screens.unauth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.centennial.cruiseease.navigation.SystemBackButtonHandler

@Composable
fun TermsAndConditionsScreen(navController: NavController) {
    Surface(modifier = Modifier
        .fillMaxSize()
        .background(color = Color.White)
        .padding(16.dp)) {
    }

    SystemBackButtonHandler {
        navController.popBackStack()
    }
}

@Preview
@Composable
fun TermsAndConditionsScreenPreview(){
    val mockNavController = rememberNavController()
    TermsAndConditionsScreen(navController = mockNavController)
}