package com.centennial.cruiseease.sealed

import com.centennial.cruiseease.models.datamodel.Cruise

sealed class CruiseDataState {
    class Success(val data: MutableList<Cruise>): CruiseDataState()
    class Failure(val message: String):CruiseDataState()
    data object Loading : CruiseDataState()
    data object Empty : CruiseDataState()
}