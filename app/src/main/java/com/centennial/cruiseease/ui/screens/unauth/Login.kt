package com.centennial.cruiseease.ui.screens.unauth

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.centennial.cruiseease.R
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.centennial.cruiseease.constants.Routes
import com.centennial.cruiseease.constants.Strings
import com.centennial.cruiseease.models.viewmodel.login.LoginUIEvent
import com.centennial.cruiseease.models.viewmodel.login.LoginViewModel
import com.centennial.cruiseease.ui.components.atoms.ButtonComponent
import com.centennial.cruiseease.ui.components.atoms.MyTextFieldComponent
import com.centennial.cruiseease.ui.components.atoms.PasswordTextFieldComponent
import com.centennial.cruiseease.ui.components.templates.SocialMediaLogin
import com.centennial.cruiseease.ui.theme.TypoGraphy
import com.centennial.cruiseease.ui.theme.primaryBlack
import com.centennial.cruiseease.ui.theme.urbanistFont
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.centennial.cruiseease.models.viewmodel.login.LoginFailureReason
import com.centennial.cruiseease.utils.SecurePreferences
import kotlinx.coroutines.runBlocking


@Composable
fun Login(navController: NavController) {
    val loginViewModel: LoginViewModel = viewModel()
    val context = LocalContext.current

    val loginFailureReason by remember { loginViewModel.loginFailureReason }
    val loginSuccess by remember {
        loginViewModel.loginSuccess
    }

    if (loginFailureReason != null) {
        val toastMessage = when (loginFailureReason) {
            LoginFailureReason.MISMATCHING_CREDENTIALS -> Strings.INCORRECT_CREDENTIALS
            LoginFailureReason.GENERAL_FAILURE -> Strings.UNEXPECTED_ERROR
            else -> Strings.LOGIN_FAILED
        }

        Toast.makeText(LocalContext.current, toastMessage, Toast.LENGTH_SHORT).show()

        loginViewModel.loginFailureReason.value = null
    }

    if (loginSuccess) {
        val securePreferences = SecurePreferences(context)
        runBlocking { securePreferences.setToken("userhasloggedin") }
        navController.navigate(Routes.AUTH_NAVIGATOR) {
            popUpTo(Routes.UNAUTH_NAVIGATOR) {
                inclusive = true
            }
        }
    }

    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            TopSection()
            Spacer(modifier = Modifier.height(36.dp))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 30.dp)
            ) {
                SocialMediaSection(navController = navController)
                Box(
                    modifier = Modifier
                        .fillMaxHeight(fraction = 0.8f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Bottom(navController = navController)
                }
            }
        }
    }
}

@Composable
private fun Bottom(navController: NavController) {
    Text(
        text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = Color(0xFF94A3BB),
                    fontSize = 14.sp,
                    fontFamily = urbanistFont,
                    fontWeight = FontWeight.Normal,
                )
            ) {
                append(Strings.DONT_HAVE_AN_ACCOUNT)
            }
            withStyle(
                style = SpanStyle(
                    color = primaryBlack,
                    fontSize = 14.sp,
                    fontFamily = urbanistFont,
                    fontWeight = FontWeight.Normal,
                    textDecoration = TextDecoration.Underline
                )
            ) {
                append(Strings.WHITE_SPACE)
                append(Strings.SIGN_UP)
            }
        },
        modifier = Modifier.clickable {
            navController.navigate(Routes.SIGN_UP) {
                popUpTo(Routes.LOGIN) {
                    inclusive = false
                }
            }
        },
        style = TypoGraphy.bodyMedium
    )
}

@Composable
private fun SocialMediaSection(navController: NavController) {
    LoginSection(navController = navController)
//    Spacer(modifier = Modifier.height(30.dp))
//    Column(horizontalAlignment = Alignment.CenterHorizontally) {
//        Text(
//            text = Strings.OR_CONTINUE_WITH, style = TextStyle(
//                fontFamily = urbanistFont,
//                fontWeight = FontWeight.Bold,
//                textAlign = TextAlign.Center,
//                fontSize = 18.sp,
//                letterSpacing = 0.8.sp,
//                color = primaryBlack
//            )
//        )
//        Spacer(modifier = Modifier.height(20.dp))
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            SocialMediaLogin(
//                icon = R.drawable.google,
//                text = Strings.GOOGLE,
//                modifier = Modifier.weight(1f),
//                navController = navController,
//                onClick = {
//                }
//            )
//        }
//    }
}


@Composable
private fun LoginSection(
    navController: NavController,
    loginViewModel: LoginViewModel = viewModel()
) {
    MyTextFieldComponent(
        labelValue = "Email",
        painterResource(id = R.drawable.message),
        onTextChanged = {
            loginViewModel.onEvent(LoginUIEvent.EmailChanged(it))
        },
        errorStatus = loginViewModel.loginUIState.value.emailError
    )

    Spacer(modifier = Modifier.height(15.dp))
    PasswordTextFieldComponent(
        labelValue = "Password",
        painterResource(id = R.drawable.lock),
        onTextSelected = {
            loginViewModel.onEvent(LoginUIEvent.PasswordChanged(it))
        },
        errorStatus = loginViewModel.loginUIState.value.passwordError
    )
    Spacer(modifier = Modifier.height(15.dp))
    ButtonComponent(
        value = Strings.LOGIN,
        onButtonClicked = {
            loginViewModel.onEvent(LoginUIEvent.LoginButtonClicked)
        },
        isEnabled = loginViewModel.allValidationsPassed.value
    )
}


@Composable
private fun TopSection() {
    val uiColor = primaryBlack
    Box(contentAlignment = Alignment.TopCenter) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(fraction = 0.46f),
            painter = painterResource(id = R.drawable.shape),
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )

        Row(
            modifier = Modifier.padding(top = 80.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(50.dp),
                painter = painterResource(id = R.drawable.cruise),
                contentDescription = Strings.APP_LOGO,
                tint = uiColor
            )
            Spacer(modifier = Modifier.width(15.dp))
            Column {
                Text(
                    text = Strings.APP_NAME,
                    style = TextStyle(
                        fontFamily = urbanistFont,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        fontSize = 24.sp,
                        letterSpacing = 0.8.sp,
                        color = uiColor
                    )
                )
                Text(
                    text = Strings.SLOGAN,
                    style = TextStyle(
                        fontFamily = urbanistFont,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp,
                        letterSpacing = 0.8.sp,
                        color = uiColor
                    )
                )
            }
        }

        Text(
            modifier = Modifier
                .padding(bottom = 10.dp)
                .align(alignment = Alignment.BottomCenter),
            text = Strings.LOGIN,
            style = TypoGraphy.headlineLarge,
            color = uiColor,
            letterSpacing = 0.8.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    val mockNavController = rememberNavController()
    Login(navController = mockNavController)
}
