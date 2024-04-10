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
import com.centennial.cruiseease.R
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.centennial.cruiseease.constants.Strings
import com.centennial.cruiseease.ui.theme.TypoGraphy
import com.centennial.cruiseease.ui.theme.primaryBlack
import com.centennial.cruiseease.ui.theme.urbanistFont
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.centennial.cruiseease.constants.Routes
import com.centennial.cruiseease.models.viewmodel.signup.SignUpFailureReason
import com.centennial.cruiseease.models.viewmodel.signup.SignupUIEvent
import com.centennial.cruiseease.models.viewmodel.signup.SignupViewModel
import com.centennial.cruiseease.ui.components.atoms.ButtonComponent
import com.centennial.cruiseease.ui.components.atoms.MyTextFieldComponent
import com.centennial.cruiseease.ui.components.atoms.PasswordTextFieldComponent

@Composable
fun Register(navController: NavController) {

    val registerViewModel: SignupViewModel = viewModel()

    val signUpFailureReason by remember {
        registerViewModel.signUpFailureReason
    }
    val signUpSuccess by remember {
        registerViewModel.signUpSuccess
    }

    if(signUpFailureReason!=null){
        val toastMessage = when(signUpFailureReason){
            SignUpFailureReason.CREDENTIAL_EXITS -> Strings.CREDENTIAL_EXISTS
            SignUpFailureReason.GENERAL_FAILURE -> Strings.UNEXPECTED_ERROR
            else -> Strings.REGISTRATION_FAILED
        }

        Toast.makeText(LocalContext.current, toastMessage, Toast.LENGTH_SHORT).show()

        registerViewModel.signUpFailureReason.value = null
    }

   if (signUpSuccess){
       val toastMessage = Strings.SIGNUP_SUCCESS
       Toast.makeText(LocalContext.current, toastMessage, Toast.LENGTH_SHORT).show()
       navController.navigate(Routes.LOGIN)
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
                RegisterSection()
                Box(
                    modifier = Modifier
                        .fillMaxHeight(fraction = 0.8f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    BottomSection(navController = navController)
                }
            }
        }
    }
}

@Composable
private fun BottomSection(navController: NavController) {
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
                append(Strings.ALREADY_HAVE_AN_ACCOUNT)
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
                append(Strings.LOGIN)
            }
        },
        modifier = Modifier.clickable {
            navController.popBackStack()
        },
        style = TypoGraphy.bodyMedium
    )
}

@Composable
private fun RegisterSection(signupViewModel: SignupViewModel = viewModel()) {

    MyTextFieldComponent(
        labelValue = "First Name",
        painterResource(id = R.drawable.profile),
        onTextChanged = {
            signupViewModel.onEvent(SignupUIEvent.FirstNameChanged(it))
        },
        errorStatus = signupViewModel.registrationUIState.value.firstNameError
    )
    MyTextFieldComponent(
        labelValue = "Last Name",
        painterResource = painterResource(id = R.drawable.profile),
        onTextChanged = {
            signupViewModel.onEvent(SignupUIEvent.LastNameChanged(it))
        },
        errorStatus = signupViewModel.registrationUIState.value.lastNameError
    )
    MyTextFieldComponent(
        labelValue = "Email",
        painterResource = painterResource(id = R.drawable.message),
        onTextChanged = {
            signupViewModel.onEvent(SignupUIEvent.EmailChanged(it))
        },
        errorStatus = signupViewModel.registrationUIState.value.emailError
    )

    PasswordTextFieldComponent(
        labelValue = "Password",
        painterResource = painterResource(id = R.drawable.ic_lock),
        onTextSelected = {
            signupViewModel.onEvent(SignupUIEvent.PasswordChanged(it))
        },
        errorStatus = signupViewModel.registrationUIState.value.passwordError
    )


    Spacer(modifier = Modifier.height(15.dp))
    ButtonComponent(
        value = Strings.SIGN_UP,
        onButtonClicked = {
            signupViewModel.onEvent(SignupUIEvent.RegisterButtonClicked)
        },
        isEnabled = signupViewModel.allValidationsPassed.value
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
            text = Strings.SIGN_UP,
            style = TypoGraphy.headlineLarge,
            color = uiColor,
            letterSpacing = 0.8.sp
        )
    }
}


@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    val mockNavController = rememberNavController()
    Register(navController = mockNavController)
}