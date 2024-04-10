package com.centennial.cruiseease.ui.components.templates

import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.centennial.cruiseease.models.viewmodel.login.SignInViewModel
import com.centennial.cruiseease.services.GoogleAuth
import com.centennial.cruiseease.ui.theme.LightBlueWhite
import com.centennial.cruiseease.ui.theme.primaryBlack
import com.centennial.cruiseease.ui.theme.urbanistFont
import com.google.android.gms.auth.api.identity.Identity

@Composable
fun SocialMediaLogin(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    text: String,
    onClick: () -> Unit,
    navController: NavController
) {
    val context = LocalContext.current

    val viewModel = viewModel<SignInViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.signInError) {
        state.signInError?.let { error ->
            Toast.makeText(
                context,
                error,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    LaunchedEffect(key1 = state.isSignInSuccessful) {
        if(state.isSignInSuccessful) {
            Toast.makeText(
                context,
                "Sign in successful",
                Toast.LENGTH_LONG
            ).show()

            navController.navigate("profile")
            viewModel.resetState()
        }
    }

    val googleAuthUiClient by lazy {
        GoogleAuth(
            context = context,
            oneTapClient = Identity.getSignInClient(context)
        )
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(size = 4.dp))
            .socialMedia()
            .fillMaxWidth()
            .clickable {

            }
            .height(50.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(25.dp)
        )

        Spacer(modifier = Modifier.width(5.dp))

        Text(
            text = text,
            style = TextStyle(
                fontFamily = urbanistFont,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                letterSpacing = 0.8.sp,
                color = primaryBlack
            )
        )
    }
}


fun Modifier.socialMedia(): Modifier {
    return this.then(
        background(LightBlueWhite)
    )
}

