package com.centennial.cruiseease.models.viewmodel.detail

import java.util.Date

sealed class DetailUIEvent {
    data class FullNameChanged(val fullName: String) : DetailUIEvent()
    data class EmailChanged(val email: String) : DetailUIEvent()
    data class DateChanged(val date: Date) : DetailUIEvent()
    data class PhoneNumberChanged(val phone: String) : DetailUIEvent()
    data class AddressChanged(val address: String) : DetailUIEvent()
    data class GenderChanged(val gender: String) : DetailUIEvent()
    data class PrefixChanged(val prefix: String) : DetailUIEvent()
    data class RoomTypeChanged(val roomType: String) : DetailUIEvent()
    data class PriceChanged(val price: String) : DetailUIEvent()
    data class CruiseChanged(val cruiseName: String): DetailUIEvent()

    data object ReservationButtonClicked : DetailUIEvent()
}