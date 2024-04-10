package com.centennial.cruiseease.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.navigation.compose.NavHost
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.centennial.cruiseease.sealed.Screen
import com.centennial.cruiseease.ui.screens.auth.Bookings
import com.centennial.cruiseease.ui.screens.auth.CruiseDetails
import com.centennial.cruiseease.ui.screens.auth.Home
import com.centennial.cruiseease.ui.screens.auth.Profile
import com.centennial.cruiseease.ui.screens.auth.Saved
import com.centennial.cruiseease.ui.screens.unauth.Login
import com.centennial.cruiseease.ui.screens.unauth.OnboardingScreen
import com.centennial.cruiseease.ui.screens.unauth.Register
import com.centennial.cruiseease.ui.screens.unauth.TermsAndConditionsScreen
import com.centennial.cruiseease.utils.SecurePreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

@Composable
fun Stack() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val securePreferences = remember { SecurePreferences(context) }
    val hasbeenToOnboarding by remember {
        mutableStateOf(runBlocking { securePreferences.getHasBeenToOnboarding() })
    }

    var hasToken by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        hasToken = withContext(Dispatchers.IO) {
            securePreferences.getToken()
        }
    }

    val startDestination = when {
        !hasbeenToOnboarding -> Screen.OnboardNavigatorScreen.route
        hasToken.isNullOrEmpty() -> Screen.UnauthNavigatorScreen.route
        else -> Screen.AuthNavigatorScreen.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.background(
            color = Color.White
        ),
    ) {
        navigation(
            startDestination = Screen.OnboardScreen.route,
            route = Screen.OnboardNavigatorScreen.route
        ) {
            composable(Screen.OnboardScreen.route) {
                AnimatedVisibility(
                    visible = true
                ) {
                    OnboardingScreen(navController = navController)
                }
            }
        }
        navigation(
            startDestination = Screen.LoginScreen.route,
            route = Screen.UnauthNavigatorScreen.route
        ) {
            composable(Screen.LoginScreen.route) {
                AnimatedVisibility(
                    visible = true
                ) {
                    Login(navController = navController)
                }
            }
            composable(Screen.SignUpScreen.route) {
                AnimatedVisibility(
                    visible = true
                ) {
                    Register(navController = navController)
                }
            }
            composable(Screen.TermsAndConditionsScreen.route) {
                AnimatedVisibility(
                    visible = true
                ) {
                    TermsAndConditionsScreen(navController = navController)
                }
            }
        }
        navigation(
            startDestination = Screen.HomeScreen.route,
            route = Screen.AuthNavigatorScreen.route
        ) {

            composable(Screen.HomeScreen.route) {
                AnimatedVisibility(
                    visible = true
                ) {
                    Home(navController = navController)
                }
            }

            composable(
                route = "${Screen.DetailScreen.route}/{id}", arguments = listOf(
                    navArgument("id") {
                        type = NavType.StringType
                    }
                )
            ) {
                AnimatedVisibility(
                    visible = true
                ) {
                    val id = it.arguments!!.getString("id")
                    CruiseDetails(navController = navController, id!!)
                }
            }


            composable(Screen.SavedScreen.route) {
                AnimatedVisibility(
                    visible = true
                ) {
                    Saved(navController = navController)
                }
            }

            composable(Screen.BookingsScreen.route) {
                AnimatedVisibility(
                    visible = true
                ) {
                    Bookings(navController = navController)
                }
            }

            composable(Screen.ProfileScreen.route) {
                AnimatedVisibility(
                    visible = true
                ) {
                    Profile(navController = navController)
                }
            }

        }

    }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavController): T {
    val navGraphRoute = destination.parent?.route ?: return viewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return viewModel(parentEntry)
}