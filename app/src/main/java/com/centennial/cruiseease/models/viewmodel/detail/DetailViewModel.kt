package com.centennial.cruiseease.models.viewmodel.detail

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.centennial.cruiseease.rules.Validator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.UUID

class DetailViewModel : ViewModel() {
    private val TAG = DetailViewModel::class.simpleName

    var detailRegistrationUIState = mutableStateOf(DetailUIState())

    var allDetailValidationPassed = mutableStateOf(false)

    private var bookingInProgress = mutableStateOf(false)

    var bookingSuccess = mutableStateOf(false)

    var bookingFailure = mutableStateOf(false)

    private var onBookingFailure: (() -> Unit)? = null

    fun onEvent(event: DetailUIEvent) {
        when (event) {
            is DetailUIEvent.DateChanged -> {
                detailRegistrationUIState.value = detailRegistrationUIState.value.copy(
                    date = event.date
                )
                printState()
            }

            is DetailUIEvent.EmailChanged -> {
                detailRegistrationUIState.value = detailRegistrationUIState.value.copy(
                    email = event.email
                )
                printState()
            }

            is DetailUIEvent.FullNameChanged -> {
                detailRegistrationUIState.value = detailRegistrationUIState.value.copy(
                    fullName = event.fullName
                )
                printState()
            }

            is DetailUIEvent.PhoneNumberChanged -> {
                detailRegistrationUIState.value = detailRegistrationUIState.value.copy(
                    phone = event.phone
                )
                printState()
            }

            is DetailUIEvent.AddressChanged -> {
                detailRegistrationUIState.value = detailRegistrationUIState.value.copy(
                    address = event.address
                )
            }

            is DetailUIEvent.GenderChanged -> {
                detailRegistrationUIState.value = detailRegistrationUIState.value.copy(
                    gender = event.gender
                )
            }

            is DetailUIEvent.PrefixChanged -> {
                detailRegistrationUIState.value = detailRegistrationUIState.value.copy(
                    prefix = event.prefix
                )
            }

            is DetailUIEvent.RoomTypeChanged -> {
                detailRegistrationUIState.value = detailRegistrationUIState.value.copy(
                    roomType = event.roomType
                )
            }

            is DetailUIEvent.PriceChanged -> {
                detailRegistrationUIState.value = detailRegistrationUIState.value.copy(
                    price = event.price
                )
            }

            is DetailUIEvent.CruiseChanged -> {
                detailRegistrationUIState.value = detailRegistrationUIState.value.copy(
                    cruiseName = event.cruiseName
                )
            }

            is DetailUIEvent.ReservationButtonClicked -> {
                reservation()
            }
        }
        validateDataWithRules()
    }


    private fun reservation() {
        Log.d(TAG, "Inside Reservation")
        val database = Firebase.database

        val myRegRef = database.getReference("bookings")
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val bookingId = UUID.randomUUID().toString()

        val bookingData = BookingData(
            id = bookingId,
            fullName = detailRegistrationUIState.value.fullName,
            email = detailRegistrationUIState.value.email,
            phone = detailRegistrationUIState.value.phone,
            date = detailRegistrationUIState.value.date,
            gender = detailRegistrationUIState.value.gender,
            prefix = detailRegistrationUIState.value.prefix,
            roomType = detailRegistrationUIState.value.roomType,
            price = detailRegistrationUIState.value.price,
            address = detailRegistrationUIState.value.address,
            cruiseName = detailRegistrationUIState.value.cruiseName,
            userId = userId ?: ""
        )

        myRegRef.push().setValue(bookingData)
            .addOnSuccessListener {
                bookingInProgress.value = false
                bookingSuccess.value = true
                Log.d(TAG, "Booking data saved successfully!")
            }
            .addOnFailureListener { e ->
                bookingInProgress.value = false
                bookingFailure.value = true
                onBookingFailure?.invoke()
                Log.e(TAG, "Error saving booking data: ${e.message}")
            }
    }


    private fun validateDataWithRules() {
        val fullNameResult = Validator.validateFullName(
            fullName = detailRegistrationUIState.value.fullName
        )

        val emailResult = Validator.validateEmail(
            email = detailRegistrationUIState.value.email
        )

        val dateResult = Validator.validateDate(
            date = detailRegistrationUIState.value.date
        )

        val phoneResult = Validator.validatePassengerCount(
            passengerCount = detailRegistrationUIState.value.phone
        )

        val addressResult = Validator.validateIsNotEmpty(
            anything = detailRegistrationUIState.value.address
        )

        val genderResult = Validator.validateIsNotEmpty(
            anything = detailRegistrationUIState.value.gender
        )

        val prefixResult = Validator.validateIsNotEmpty(
            anything = detailRegistrationUIState.value.prefix
        )

        val roomTypeResult = Validator.validateIsNotEmpty(
            anything = detailRegistrationUIState.value.roomType
        )

        val priceTypeResult = Validator.validateIsNotEmpty(
            anything = detailRegistrationUIState.value.price
        )

        val cruiseNameResult = Validator.validateIsNotEmpty(
            anything = detailRegistrationUIState.value.cruiseName
        )

        detailRegistrationUIState.value = detailRegistrationUIState.value.copy(
            fullNameError = fullNameResult.status,
            emailError = emailResult.status,
            dateError = dateResult.status,
            phoneError = phoneResult.status,
            genderError = genderResult.status,
            prefixError = prefixResult.status,
            roomTypeError = roomTypeResult.status,
            priceError = priceTypeResult.status,
            addressError = addressResult.status,
            cruiseNameError = cruiseNameResult.status
        )

        allDetailValidationPassed.value =
            fullNameResult.status && emailResult.status && phoneResult.status && addressResult.status
    }


    private fun printState() {
        Log.d(TAG, "Inside_printState")
        Log.d(TAG, detailRegistrationUIState.value.toString())
    }
}

