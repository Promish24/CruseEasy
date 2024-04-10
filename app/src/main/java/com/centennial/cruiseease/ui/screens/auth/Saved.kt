package com.centennial.cruiseease.ui.screens.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.centennial.cruiseease.constants.Routes
import com.centennial.cruiseease.constants.Strings
import com.centennial.cruiseease.models.datamodel.Cruise
import com.centennial.cruiseease.ui.components.molecules.CardItem
import com.centennial.cruiseease.ui.components.organisms.BottomTab
import com.centennial.cruiseease.ui.components.organisms.EmptyIndicator
import com.centennial.cruiseease.ui.components.organisms.Header
import com.centennial.cruiseease.ui.components.templates.Layout
import com.centennial.cruiseease.ui.theme.urbanistFont
import com.centennial.cruiseease.utils.SecurePreferences

@Composable
fun Saved(navController: NavController) {
    val securePreferences = SecurePreferences(LocalContext.current)
    val wishlist: List<Cruise>? = securePreferences.getWishlist()

    Layout(
        header = {
            Header(navController = navController, currentRoute = Routes.SAVED)
        },
        content = {
            if (!wishlist.isNullOrEmpty()) {
                ShowSavedLazyList(cruises = wishlist.toMutableList(), navController = navController)
            } else {
                EmptyIndicator()
            }
        },
        footer = {
            BottomTab(navController = navController, currentScreen = Routes.SAVED)
        }
    )
}



@Composable
fun ShowSavedLazyList(cruises: MutableList<Cruise>, navController: NavController) {
    LazyColumn {
        item {
            Column(
                modifier = Modifier.padding(15.dp)
            ) {
                Text(
                    text = Strings.YOUR_SAVED_CRUISE,
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = urbanistFont,
                        letterSpacing = 0.5.sp
                    )
                )
                Text(
                    text = Strings.EXPLORE_YOUR_SAVED_CRUISES,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = urbanistFont,
                        letterSpacing = 0.5.sp
                    )
                )
            }
        }
        items(cruises) { cruise ->
            CardItem(cruise = cruise, navController = navController)
        }
    }
}
