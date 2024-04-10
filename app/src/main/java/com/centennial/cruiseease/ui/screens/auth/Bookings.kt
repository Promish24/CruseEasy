package com.centennial.cruiseease.ui.screens.auth

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.centennial.cruiseease.R
import com.centennial.cruiseease.constants.Routes
import com.centennial.cruiseease.constants.Strings
import com.centennial.cruiseease.models.datamodel.Booking
import com.centennial.cruiseease.models.viewmodel.mybookings.MyBookingsModel
import com.centennial.cruiseease.sealed.BookingsDataState
import com.centennial.cruiseease.ui.components.atoms.ButtonComponent
import com.centennial.cruiseease.ui.components.molecules.KeyValueRow
import com.centennial.cruiseease.ui.components.organisms.BottomTab
import com.centennial.cruiseease.ui.components.organisms.EmptyIndicator
import com.centennial.cruiseease.ui.components.organisms.ErrorIndicator
import com.centennial.cruiseease.ui.components.organisms.Header
import com.centennial.cruiseease.ui.components.organisms.LoadingIndicator
import com.centennial.cruiseease.ui.components.templates.Layout
import com.centennial.cruiseease.ui.theme.urbanistFont
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.centennial.cruiseease.sealed.Screen
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.Result
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import com.stripe.android.paymentsheet.rememberPaymentSheet

@Composable
fun Bookings(navController: NavController) {
    val viewModel: MyBookingsModel = viewModel()
    val dataState = viewModel.response.value

    Layout(
        header = {
            Header(navController = navController, currentRoute = Routes.SAVED)
        },
        content = {
            if (dataState is BookingsDataState.Loading) {
                LoadingIndicator()
            } else {
                when (dataState) {
                    is BookingsDataState.Success -> SetData(
                        viewModel = viewModel,
                        navController = navController
                    )

                    is BookingsDataState.Empty -> EmptyIndicator()
                    is BookingsDataState.Failure -> ErrorIndicator()
                    is BookingsDataState.Loading -> LoadingIndicator()
                }

            }
        },
        footer = {
            BottomTab(navController = navController, currentScreen = Routes.BOOKINGS)
        }
    )
}

@Composable
fun SetData(viewModel: MyBookingsModel, navController: NavController) {
    when (val result = viewModel.response.value) {
        is BookingsDataState.Success -> {
            Column {
                ShowBookingsLazyList(bookings = result.data, navController = navController)
            }
        }

        else -> {}
    }
}

@Composable
fun ShowBookingsLazyList(bookings: MutableList<Booking>, navController: NavController) {
    LazyColumn {
        item {
            Column(
                modifier = Modifier.padding(15.dp)
            ) {
                Text(
                    text = Strings.BOOKING_HISTORY_TITLE,
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = urbanistFont,
                        letterSpacing = 0.5.sp
                    )
                )
                Text(
                    text = Strings.BOOKING_HISTORY_DESCRIPTION,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = urbanistFont,
                        letterSpacing = 0.5.sp
                    )
                )
            }

        }
        items(bookings) { booking ->
            BookingItemWithLottieTopRight(booking = booking, navController = navController)
        }
    }
}


@Composable
fun BookingItemWithLottieTopRight(booking: Booking, navController: NavController) {
    val lottieComposition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.history)
    )

    val context = LocalContext.current

    val paymentSheet = rememberPaymentSheet(::onPaymentSheetResult)

    var customerConfig by remember { mutableStateOf<PaymentSheet.CustomerConfiguration?>(null) }
    var paymentIntentClientSecret by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(context) {
        val url = "http://192.168.0.19:3000/payment-sheet"

        url.httpPost()
            .header("amount", booking.price).responseJson { _, _, result ->
                Log.d("check the result", "$result")
                if (result is Result.Success) {
                    val responseJson = result.get().obj()
                    paymentIntentClientSecret = responseJson.getString("paymentIntent")
                    customerConfig = PaymentSheet.CustomerConfiguration(
                        responseJson.getString("customer"),
                        responseJson.getString("ephemeralKey")
                    )
                    val publishableKey = responseJson.getString("publishableKey")
                    PaymentConfiguration.init(context, publishableKey)
                }
            }
    }



    val formattedDate = remember {
        val date = Date(booking.date.time)
        SimpleDateFormat("yyyy MMMM dd", Locale.getDefault()).format(date)
    }

    val logText = remember { mutableStateOf("") }



    Box(
        modifier = Modifier
            .padding(horizontal = 15.dp)
            .fillMaxWidth()
            .padding(4.dp)
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
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Column(
                    ) {
                        Text(
                            text = booking.cruiseName, style = TextStyle(
                                fontFamily = urbanistFont,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                letterSpacing = 0.5.sp
                            )
                        )

                        KeyValueRow(key = "Address:", value = booking.address)
                        KeyValueRow(key = "Date:", value = formattedDate)
                        KeyValueRow(key = "Email:", value = booking.email)
                        KeyValueRow(key = "Full Name:", value = booking.fullName)
                        KeyValueRow(key = "Gender:", value = booking.gender)
                        KeyValueRow(key = "Phone:", value = booking.phone)
                        KeyValueRow(key = "Prefix:", value = booking.prefix)
                        KeyValueRow(key = "Price:", value = "$${booking.price}")
                        KeyValueRow(key = "Room Type:", value = booking.roomType)
                    }

                    Spacer(modifier = Modifier.height(8.dp))


                    ButtonComponent(value = "Proceed to pay", onButtonClicked = {

                        val currentConfig = customerConfig
                        val currentClientSecret = paymentIntentClientSecret

                        if (currentConfig != null && currentClientSecret != null) {
                            presentPaymentSheet(paymentSheet, currentConfig, currentClientSecret)
                        }

                    }, isEnabled = true)

                }

                LottieAnimation(
                    composition = lottieComposition,
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.TopEnd),
                    iterations = LottieConstants.IterateForever
                )
            }
        }
    }
    Text(
        text = logText.value,
        style = TextStyle(color = Color.Red),
        modifier = Modifier.padding(16.dp)
    )
}


private fun presentPaymentSheet(
    paymentSheet: PaymentSheet,
    customerConfig: PaymentSheet.CustomerConfiguration,
    paymentIntentClientSecret: String
) {
    Log.d("Customer Configuration", "$customerConfig")
    paymentSheet.presentWithPaymentIntent(
        paymentIntentClientSecret,
        PaymentSheet.Configuration(
            merchantDisplayName = "Cruise Ease",
            customer = customerConfig,
            allowsDelayedPaymentMethods = true
        )
    )
}

private fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {

    when (paymentSheetResult) {
        is PaymentSheetResult.Canceled -> {
            print("Canceled")
        }

        is PaymentSheetResult.Failed -> {
            print("Error: ${paymentSheetResult.error}")
        }

        is PaymentSheetResult.Completed -> {
            print("Completed")
        }
    }
}
