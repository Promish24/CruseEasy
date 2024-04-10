package com.centennial.cruiseease.sealed

import com.centennial.cruiseease.models.datamodel.Booking


sealed class BookingsDataState {
    class Success(val data: MutableList<Booking>) : BookingsDataState()
    class Failure(val message: String) : BookingsDataState()
    data object Loading : BookingsDataState()
    data object Empty : BookingsDataState()
}