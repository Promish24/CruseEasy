package com.centennial.cruiseease.ui.screens.auth

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.centennial.cruiseease.constants.Routes
import com.centennial.cruiseease.ui.components.organisms.Header
import com.centennial.cruiseease.ui.components.templates.Layout
import com.centennial.cruiseease.models.datamodel.Cruise
import com.centennial.cruiseease.models.datamodel.PhotoGallery
import com.centennial.cruiseease.utils.SecurePreferences
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.centennial.cruiseease.R
import com.centennial.cruiseease.constants.Strings
import com.centennial.cruiseease.data.gender
import com.centennial.cruiseease.data.prefix
import com.centennial.cruiseease.data.roomTypes
import com.centennial.cruiseease.models.viewmodel.detail.DetailUIEvent
import com.centennial.cruiseease.models.viewmodel.detail.DetailViewModel
import com.centennial.cruiseease.models.viewmodel.home.HomeViewModel
import com.centennial.cruiseease.sealed.CruiseDataState
import com.centennial.cruiseease.ui.components.atoms.ButtonComponent
import com.centennial.cruiseease.ui.components.atoms.MyTextFieldComponent
import com.centennial.cruiseease.ui.components.molecules.KeyValueRow
import com.centennial.cruiseease.ui.components.organisms.EmptyIndicator
import com.centennial.cruiseease.ui.theme.urbanistFont
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun CruiseDetails(navController: NavController, id: String) {
    val viewModel: HomeViewModel = viewModel()

    val filteredCruise = when (val result = viewModel.response.value) {
        is CruiseDataState.Success -> {
            result.data.find { it.id == id }
        }

        else -> null
    }

    Layout(
        header = {
            Header(navController = navController, currentRoute = Routes.DETAIL)
        },
        content = {
            if (filteredCruise != null) {
                DetailsView(filteredCruise)
            } else {
                EmptyIndicator()
            }
        }
    )
}


@Composable
fun DetailsView(selectedCruise: Cruise) {
    val interactionSource = remember { MutableInteractionSource() }
    val securePreferences = SecurePreferences(LocalContext.current)
    val wishlist: List<Cruise> = mutableListOf(selectedCruise)

    val (showSuccessModal, setShowSuccessModal) = remember { mutableStateOf(false) }
    val (showReservationModal, setShowReservationModal) = remember { mutableStateOf(false) }

    val (showReservationSuccessModal, setShowReservationSuccessModal) = remember {
        mutableStateOf(false)
    }

    val closeSuccessModal = { setShowSuccessModal(false) }
    val closeReservationModal = { setShowReservationModal(false) }

    val bookingViewModel: DetailViewModel = viewModel()

    val bookingSuccess by remember {
        bookingViewModel.bookingSuccess
    }

    val closeReservationSuccessModal = {
        bookingViewModel.bookingSuccess.value = false
        setShowReservationSuccessModal(false)
    }


    LazyColumn(
        modifier = Modifier.padding(15.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedCruise.name ?: "",
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = urbanistFont
                    )
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        securePreferences.setWishlist(wishlist)
                        setShowSuccessModal(true)
                    }
                ) {
                    Text(
                        text = Strings.ADD_TO_WISHLIST,
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = urbanistFont,
                            color = Color.Gray
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Image(
                        painter = painterResource(R.drawable.save),
                        contentDescription = "Add to Wishlist",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .padding(vertical = 8.dp)
                    .border(BorderStroke(0.dp, Color.Gray), shape = RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(8.dp))
            ) {
                Image(
                    painter = rememberAsyncImagePainter(selectedCruise.background),
                    contentDescription = "Cruise Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            Text(
                text = selectedCruise.description ?: "", style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = urbanistFont,
                    letterSpacing = 0.5.sp,
                )
            )


            selectedCruise.packages.firstOrNull()?.let { featured ->
                Spacer(modifier = Modifier.height(10.dp))
                KeyValueRow(
                    "Rating:",
                    "${selectedCruise.rating ?: 0} / ${selectedCruise.ratingMax ?: 0}"
                )
                Spacer(modifier = Modifier.height(10.dp))
                KeyValueRow("Starting From:", "$${selectedCruise.startingFrom ?: 0}")
                Spacer(modifier = Modifier.height(10.dp))
                KeyValueRow("Featured Name:", featured.featuredName ?: "")
                Spacer(modifier = Modifier.height(10.dp))
                KeyValueRow("From:", featured.from ?: "")
                Spacer(modifier = Modifier.height(10.dp))
                KeyValueRow("Interior From:", "$${featured.interiorFrom ?: 0}")
                Spacer(modifier = Modifier.height(10.dp))
                KeyValueRow("Photo Gallery:", "")
                Spacer(modifier = Modifier.height(10.dp))
                PhotoGalleryDetails(photoGallery = featured.photoGallery)
                Spacer(modifier = Modifier.height(10.dp))
                KeyValueRow("Ports:", featured.ports ?: "")
                Spacer(modifier = Modifier.height(10.dp))
                KeyValueRow("Way:", featured.way ?: "")
                Spacer(modifier = Modifier.height(10.dp))
            }

            ButtonComponent(
                value = "Make Reservation",
                onButtonClicked = { setShowReservationModal(true) },
                isEnabled = true
            )
        }
    }

    if (showSuccessModal) {
        SuccessModal(onClose = closeSuccessModal)
    }

    if (showReservationModal) {
        ReservationModal(onClose = closeReservationModal, selectedCruise = selectedCruise)
    }

    if (bookingSuccess) {
        setShowReservationSuccessModal(true)
    }

    if (showReservationSuccessModal) {
        BookingSuccessModal(onClose = closeReservationSuccessModal)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationModal(
    onClose: () -> Unit,
    selectedCruise: Cruise
) {
    val securePreferences = SecurePreferences(LocalContext.current)

    val detailViewModel: DetailViewModel = viewModel()
    val submitReservation = {
        onClose()
    }
    val cruiseFromPreferences = securePreferences.getCruiseData()
    val context = LocalContext.current
    val datePickerState = rememberDatePickerState()
    var showDatePicker by remember { mutableStateOf(false) }
    val expanded = remember {
        mutableStateOf(false)
    }
    val selectedRoom = remember {
        mutableStateOf(roomTypes[0])
    }

    val genderDropDownExpanded = remember {
        mutableStateOf(false)
    }
    val selectedGender = remember {
        mutableStateOf(gender[0])
    }

    val prefixDropDownExpanded = remember {
        mutableStateOf(false)
    }

    val selectedPrefix = remember {
        mutableStateOf(prefix[0])
    }

    val selectedRoomUpperCase = selectedRoom.value.uppercase()

    val roomTypeRates = cruiseFromPreferences?.packages?.flatMap { it.roomtypes }?.associateBy(
        { it.name?.uppercase() },
        { it.rate ?: 0 }
    )

    val billingAmount = roomTypeRates?.get(selectedRoomUpperCase) ?: 0

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .background(Color.White, RoundedCornerShape(5.dp))
                .padding(10.dp)
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = Strings.GUEST_DETAILS, style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        letterSpacing = 0.5.sp,
                        fontFamily = urbanistFont
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        ExposedDropdownMenuBox(
                            expanded = genderDropDownExpanded.value, onExpandedChange = {
                                genderDropDownExpanded.value = !genderDropDownExpanded.value
                            }, modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = selectedGender.value,
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = genderDropDownExpanded.value)
                                },
                                textStyle = TextStyle(
                                    color = Color.Black,
                                    fontFamily = urbanistFont,
                                    fontWeight = FontWeight.SemiBold
                                ),
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth(),
                                label = {
                                    Text(
                                        "Gender",
                                        style = TextStyle(
                                            fontFamily = urbanistFont,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium,
                                            letterSpacing = 0.5.sp
                                        )
                                    )
                                }
                            )
                            ExposedDropdownMenu(
                                expanded = genderDropDownExpanded.value,
                                onDismissRequest = { genderDropDownExpanded.value = false }) {
                                gender.forEach {
                                    DropdownMenuItem(text = {
                                        Text(
                                            text = it, style = TextStyle(
                                                fontFamily = urbanistFont,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        )
                                    }, onClick = {
                                        detailViewModel.onEvent((DetailUIEvent.GenderChanged(it)))
                                        selectedGender.value = it
                                        genderDropDownExpanded.value = false
                                    })
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier.weight(1f)
                    ) {
                        ExposedDropdownMenuBox(
                            expanded = prefixDropDownExpanded.value, onExpandedChange = {
                                prefixDropDownExpanded.value = !prefixDropDownExpanded.value
                            }, modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = selectedPrefix.value,
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = prefixDropDownExpanded.value)
                                },
                                label = {
                                    Text(
                                        "Prefix",
                                        style = TextStyle(
                                            fontFamily = urbanistFont,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium,
                                            letterSpacing = 0.5.sp
                                        )
                                    )
                                },
                                textStyle = TextStyle(
                                    color = Color.Black,
                                    fontFamily = urbanistFont,
                                    fontWeight = FontWeight.SemiBold
                                ),
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = prefixDropDownExpanded.value,
                                onDismissRequest = { prefixDropDownExpanded.value = false }) {
                                prefix.forEach {
                                    DropdownMenuItem(text = {
                                        Text(
                                            text = it, style = TextStyle(
                                                fontFamily = urbanistFont,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        )
                                    }, onClick = {
                                        detailViewModel.onEvent((DetailUIEvent.PrefixChanged(it)))
                                        selectedPrefix.value = it
                                        prefixDropDownExpanded.value = false
                                    })
                                }
                            }
                        }
                    }
                }
                MyTextFieldComponent(
                    labelValue = Strings.FULL_NAME,
                    painterResource(id = R.drawable.profile),
                    onTextChanged = {
                        detailViewModel.onEvent((DetailUIEvent.FullNameChanged(it)))
                    },
                    errorStatus = detailViewModel.detailRegistrationUIState.value.fullNameError,
                )

                MyTextFieldComponent(
                    labelValue = Strings.EMAIL,
                    painterResource(id = R.drawable.message),
                    onTextChanged = {
                        detailViewModel.onEvent((DetailUIEvent.EmailChanged(it)))
                    },
                    errorStatus = detailViewModel.detailRegistrationUIState.value.emailError,
                )
                MyTextFieldComponent(
                    labelValue = Strings.PHONE,
                    painterResource(id = R.drawable.phone),
                    onTextChanged = {
                        detailViewModel.onEvent((DetailUIEvent.PhoneNumberChanged(it)))
                    },
                    errorStatus = detailViewModel.detailRegistrationUIState.value.phoneError,
                )

                MyTextFieldComponent(
                    labelValue = Strings.ADDRESS,
                    painterResource(id = R.drawable.address),
                    onTextChanged = {
                        detailViewModel.onEvent((DetailUIEvent.AddressChanged(it)))
                    },
                    errorStatus = detailViewModel.detailRegistrationUIState.value.addressError,
                )

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    ExposedDropdownMenuBox(
                        expanded = expanded.value, onExpandedChange = {
                            expanded.value = !expanded.value
                        }, modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = selectedRoom.value,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value)
                            },
                            label = {
                                Text(
                                    "Room",
                                    style = TextStyle(
                                        fontFamily = urbanistFont,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        letterSpacing = 0.5.sp
                                    )
                                )
                            },
                            textStyle = TextStyle(
                                color = Color.Black,
                                fontFamily = urbanistFont,
                                fontWeight = FontWeight.SemiBold
                            ),
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                        )
                        ExposedDropdownMenu(
                            expanded = expanded.value,
                            onDismissRequest = { expanded.value = false }) {
                            roomTypes.forEach {
                                DropdownMenuItem(text = {
                                    Text(
                                        text = it, style = TextStyle(
                                            fontFamily = urbanistFont,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    )
                                }, onClick = {
                                    detailViewModel.onEvent((DetailUIEvent.RoomTypeChanged(it)))
                                    selectedRoom.value = it
                                    detailViewModel.onEvent(
                                        (DetailUIEvent.PriceChanged(
                                            billingAmount.toString()
                                        ))
                                    )
                                    expanded.value = false
                                })
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(15.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.weight(5f)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.calendar),
                                contentDescription = Strings.CALENDAR_ICON,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = detailViewModel.detailRegistrationUIState.value.date.let {
                                    SimpleDateFormat(
                                        Strings.DATE_FORMAT,
                                        Locale.getDefault()
                                    ).format(it)
                                } ?: "Select Date",
                                modifier = Modifier.padding(horizontal = 10.dp),
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Box(
                        modifier = Modifier.weight(5f)
                    ) {
                        ButtonComponent(
                            value = Strings.SELECT_DATE,
                            onButtonClicked = { showDatePicker = true },
                            isEnabled = true,
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Box {
                    Text(
                        text = "${Strings.BILLING_AMOUNT} $$billingAmount",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = urbanistFont,
                            fontWeight = FontWeight.Bold
                        ),
                    )
                }


                Spacer(modifier = Modifier.height(15.dp))

                ButtonComponent(
                    value = Strings.SUBMIT_RESERVATION,
                    onButtonClicked = {
                        if (detailViewModel.detailRegistrationUIState.value.gender.isBlank()) {
                            detailViewModel.onEvent(DetailUIEvent.GenderChanged(gender[0]))
                        }
                        if (detailViewModel.detailRegistrationUIState.value.prefix.isBlank()) {
                            detailViewModel.onEvent(DetailUIEvent.PrefixChanged(prefix[0]))
                        }
                        if (detailViewModel.detailRegistrationUIState.value.roomType.isBlank()) {
                            detailViewModel.onEvent(DetailUIEvent.RoomTypeChanged(roomTypes[0]))
                        }
                        selectedCruise.name?.let {
                            DetailUIEvent.CruiseChanged(
                                it
                            )
                        }?.let { detailViewModel.onEvent(it) }
                        detailViewModel.onEvent(DetailUIEvent.PriceChanged(billingAmount.toString()))
                        detailViewModel.onEvent(DetailUIEvent.ReservationButtonClicked)
                        submitReservation()
                    },
                    isEnabled = detailViewModel.allDetailValidationPassed.value
                )

                Spacer(modifier = Modifier.height(15.dp))

                ButtonComponent(
                    value = Strings.CANCEL,
                    onButtonClicked = {
                        onClose()
                    },
                    isEnabled = true
                )

                if (showDatePicker) {
                    DatePickerDialog(
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    val selectedDate = Calendar.getInstance().apply {
                                        timeInMillis = datePickerState.selectedDateMillis!!
                                    }
                                    if (selectedDate.after(Calendar.getInstance())) {
                                        detailViewModel.onEvent(
                                            DetailUIEvent.DateChanged(
                                                selectedDate.time
                                            )
                                        )

                                        showDatePicker = false
                                    } else {
                                        Toast.makeText(
                                            context,
                                            Strings.DATE_AFTER_TODAY,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            ) { Text(Strings.OK) }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = {
                                    showDatePicker = false
                                }
                            ) { Text(Strings.CANCEL) }
                        },
                        colors = DatePickerDefaults.colors(
                        ),
                        onDismissRequest = { /*TODO*/ },
                    ) {
                        DatePicker(
                            state = datePickerState,
                            colors = DatePickerDefaults.colors(
                                todayContentColor = Color(0xFF7210FF),
                                todayDateBorderColor = Color(0xFF7210FF),
                                selectedDayContainerColor = Color(0xFF7210FF)
                            )
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun BookingSuccessModal(
    onClose: () -> Unit
) {
    val successComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.success))
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .background(Color.White, RoundedCornerShape(16.dp))
                .padding(32.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LottieAnimation(
                    composition = successComposition,
                    modifier = Modifier
                        .size(100.dp)
                        .padding(bottom = 16.dp),
                )
                Text(
                    text = Strings.SUCCESS,
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontFamily = urbanistFont,
                        letterSpacing = 0.5.sp
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = Strings.RESERVATION_SUCCESS,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = urbanistFont,
                        letterSpacing = 0.5.sp
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                ButtonComponent(
                    value = Strings.OK,
                    onButtonClicked = { onClose() },
                    isEnabled = true
                )
            }
        }
    }
}


@Composable
fun SuccessModal(
    onClose: () -> Unit
) {
    val successComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.success))

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .background(Color.White, RoundedCornerShape(16.dp))
                .padding(32.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LottieAnimation(
                    composition = successComposition,
                    modifier = Modifier
                        .size(100.dp)
                        .padding(bottom = 16.dp),
                )
                Text(
                    text = Strings.SUCCESS,
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontFamily = urbanistFont,
                        letterSpacing = 0.5.sp
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = Strings.ADDED_TO_WISHLIST,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = urbanistFont,
                        letterSpacing = 0.5.sp
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                ButtonComponent(
                    value = Strings.OK,
                    onButtonClicked = { onClose() },
                    isEnabled = true
                )
            }
        }
    }
}


@Composable
fun PhotoGalleryDetails(photoGallery: PhotoGallery) {
    val images = listOfNotNull(
        photoGallery.image1,
        photoGallery.image2,
        photoGallery.image3,
        photoGallery.image4,
        photoGallery.image5,
        photoGallery.image6,
        photoGallery.image7,
        photoGallery.image8,
        photoGallery.image9,
        photoGallery.image10
    )

    if (images.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            item {
                LazyRow(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(images) { imageUrl ->
                        ImageItem(imageUrl)
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }
        }
    }
}


@Composable
fun ImageItem(imageUrl: String) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(200.dp)
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop
            )
        }
    }
}

