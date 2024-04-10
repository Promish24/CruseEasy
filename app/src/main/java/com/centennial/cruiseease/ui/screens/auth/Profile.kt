package com.centennial.cruiseease.ui.screens.auth

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.centennial.cruiseease.R
import com.centennial.cruiseease.constants.Routes
import com.centennial.cruiseease.constants.Strings
import com.centennial.cruiseease.models.datamodel.UserData
import com.centennial.cruiseease.models.viewmodel.home.HomeViewModel
import com.centennial.cruiseease.sealed.ProfileDataState
import com.centennial.cruiseease.sealed.Screen
import com.centennial.cruiseease.ui.components.organisms.BottomTab
import com.centennial.cruiseease.ui.components.organisms.EmptyIndicator
import com.centennial.cruiseease.ui.components.organisms.ErrorIndicator
import com.centennial.cruiseease.ui.components.organisms.Header
import com.centennial.cruiseease.ui.components.organisms.LoadingIndicator
import com.centennial.cruiseease.ui.components.templates.Layout
import com.centennial.cruiseease.ui.theme.urbanistFont
import com.centennial.cruiseease.utils.SecurePreferences
import kotlinx.coroutines.runBlocking

@Composable
fun Profile(navController: NavController) {
    val profileViewModel: HomeViewModel = viewModel()
    var showLogoutConfirmation by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val securePreferences = SecurePreferences(context)

    Layout(
        header = {
            Header(navController = navController, currentRoute = Routes.PROFILE)
        },
        content = {
            when (val userDataState = profileViewModel.userDataState.value) {
                is ProfileDataState.Success -> {
                    val userData: UserData = userDataState.userData
                    Log.d("chceck the userdata", "$userData")
                    UserProfile(
                        userData = userData,
                        onLogoutClick = { showLogoutConfirmation = true })
                }

                is ProfileDataState.Loading -> {
                    LoadingIndicator()
                }

                is ProfileDataState.Empty -> {
                    EmptyIndicator()
                }

                is ProfileDataState.Error -> {
                    ErrorIndicator()
                }

                else -> {}
            }
            if (showLogoutConfirmation) {
                LogoutConfirmationDialog(
                    onConfirm = {
                        securePreferences.removeToken()
                        showLogoutConfirmation = false
                        navController.navigate(Screen.UnauthNavigatorScreen.route) {
                            popUpTo(Screen.AuthNavigatorScreen.route) {
                                inclusive = true
                            }
                        }
                    },
                    onDismiss = { showLogoutConfirmation = false }
                )
            }
        },
        footer = {
            BottomTab(navController = navController, currentScreen = Routes.PROFILE)
        }

    )
}


@Composable
fun UserProfile(userData: UserData, onLogoutClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 5.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFEEEEEE),
            ),
            shape = CircleShape,
            modifier = Modifier
                .size(200.dp)
                .aspectRatio(1f)
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = userData.userPhoto,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${userData.firstName} ${userData.lastName}",
                style = TextStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    fontFamily = urbanistFont
                )
            )
            Text(
                text = userData.email,
                style = TextStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    fontFamily = urbanistFont
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFBFDBFE)
                ),
                shape = RoundedCornerShape(5.dp),
                onClick = { onLogoutClick() },
                modifier = Modifier
                    .width(200.dp)
                    .padding(20.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.logout),
                        contentDescription = "Logout",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = Strings.LOGOUT, style = TextStyle(
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp,
                            fontFamily = urbanistFont,
                            color = Color.Black,
                            letterSpacing = 0.5.sp
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun LogoutConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        containerColor = Color(0xFFEEEEEE),
        onDismissRequest = { onDismiss() },
        title = { Text("Logout", style = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            fontFamily = urbanistFont,
            color=Color.Black
        )) },
        text = { Text("Are you sure you want to logout?",style = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            fontFamily = urbanistFont,
            color=Color.Black
        )) },
        confirmButton = {
            Button(
                onClick = { onConfirm() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            ) {
                Text(
                    "Yes", style = TextStyle(
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        fontFamily = urbanistFont,
                        color=Color.Black
                    )
                )
            }
        },
        dismissButton = {
            Button(
                onClick = { onDismiss() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBFDBFE)),
            ) {
                Text(
                    "No", style = TextStyle(
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        fontFamily = urbanistFont,
                        color=Color.Black
                    )
                )
            }
        }
    )
}
