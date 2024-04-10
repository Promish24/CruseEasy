package com.centennial.cruiseease.models.viewmodel.detail

import java.util.Date

data class DetailUIState(
    var fullName: String = "",
    var email: String = "",
    var date: Date = Date(),
    var phone: String = "",
    var address: String = "",
    var prefix: String = "",
    var gender: String = "",
    var roomType: String = "",
    val price: String = "",
    var cruiseName: String = "",

    var fullNameError: Boolean = false,
    var emailError: Boolean = false,
    var dateError: Boolean = false,
    var phoneError: Boolean = false,
    var addressError: Boolean = false,
    var prefixError: Boolean = false,
    var genderError: Boolean = false,
    var roomTypeError: Boolean = false,
    var priceError: Boolean = false,
    var cruiseNameError: Boolean = false
)


data class BookingData(
    var id: String = "",
    var fullName: String = "",
    var email: String = "",
    var date: Date = Date(),
    var phone: String = "",
    var address: String = "",
    var prefix: String = "",
    var gender: String = "",
    var roomType: String = "",
    val price: String = "",
    val cruiseName: String = "",
    val userId: String = "",
) {
    constructor(
        detailUIState: DetailUIState,
        userId: String,
        id: String
    ) : this(
        id = id,
        fullName = detailUIState.fullName,
        email = detailUIState.email,
        date = detailUIState.date,
        phone = detailUIState.phone,
        address = detailUIState.address,
        prefix = detailUIState.prefix,
        gender = detailUIState.gender,
        roomType = detailUIState.roomType,
        price = detailUIState.price,
        cruiseName = detailUIState.cruiseName,
        userId = userId,
    )
}