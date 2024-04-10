package com.centennial.cruiseease.ui.screens.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.centennial.cruiseease.models.datamodel.Cruise
import com.centennial.cruiseease.models.viewmodel.home.HomeViewModel
import com.centennial.cruiseease.sealed.CruiseDataState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.centennial.cruiseease.constants.Routes
import com.centennial.cruiseease.constants.Strings
import com.centennial.cruiseease.ui.components.molecules.CardItem
import com.centennial.cruiseease.ui.components.organisms.BottomTab
import com.centennial.cruiseease.ui.components.organisms.EmptyIndicator
import com.centennial.cruiseease.ui.components.organisms.ErrorIndicator
import com.centennial.cruiseease.ui.components.organisms.Header
import com.centennial.cruiseease.ui.components.organisms.LoadingIndicator
import com.centennial.cruiseease.ui.components.templates.Layout
import com.centennial.cruiseease.ui.theme.urbanistFont

@Composable
fun Home(navController: NavController) {
    val viewModel: HomeViewModel = viewModel()
    val dataState = viewModel.response.value

    Layout(
        header = {
                Header(navController = navController, currentRoute = Routes.HOME)
        },
        content = {
            if (dataState is CruiseDataState.Loading) {
                LoadingIndicator()
            } else {
                when (dataState) {
                    is CruiseDataState.Success -> SetData(
                        viewModel = viewModel,
                        navController = navController
                    )

                    is CruiseDataState.Empty -> EmptyIndicator()
                    is CruiseDataState.Failure -> ErrorIndicator()
                    is CruiseDataState.Loading -> LoadingIndicator()
                }
            }
        },
        footer = {
                BottomTab(navController = navController, currentScreen = Routes.HOME)
        }
    )
}


@Composable
fun SetData(viewModel: HomeViewModel, navController: NavController) {
    when (val result = viewModel.response.value) {
        is CruiseDataState.Success -> {
            Column {
                ShowLazyList(result.data, navController = navController)
            }
        }

        else -> {
        }
    }
}

@Composable
fun ShowLazyList(cruises: MutableList<Cruise>, navController: NavController) {

    LazyColumn {
        item {
            Column(
                modifier = Modifier.padding(15.dp)
            ) {
                Text(
                    text = Strings.FIND_DREAM_CRUISE,
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = urbanistFont,
                        letterSpacing = 0.5.sp
                    )
                )
                Text(
                    text = Strings.POPULAR_CRUISE,
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

