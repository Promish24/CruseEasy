package com.centennial.cruiseease.models.datamodel

data class Booking(
    val id: String="",
    val address: String = "",
    val date: DateData = DateData(),
    val email: String = "",
    val fullName: String = "",
    val gender: String = "",
    val phone: String = "",
    val prefix: String = "",
    val price: String = "",
    val roomType: String = "",
    val cruiseName: String = "",
    val userId: String = "",
) {
    constructor() : this("","", DateData(), "", "", "", "", "", "", "", "", "")
}


data class DateData(
    val date: Int = 0,
    val day: Int = 0,
    val hours: Int = 0,
    val minutes: Int = 0,
    val month: Int = 0,
    val seconds: Int = 0,
    val time: Long = 0,
    val timezoneOffset: Int = 0,
    val year: Int = 0
) {}


data class FirebaseResponse(
    val bookings: Map<String, Booking>
) {}
