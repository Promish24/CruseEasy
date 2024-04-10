package com.centennial.cruiseease.sealed

import com.centennial.cruiseease.models.datamodel.UserData

sealed class ProfileDataState {
    data class Success(val userData: UserData) : ProfileDataState()
    data object Loading : ProfileDataState()
    data class Error(val message: String) : ProfileDataState()
    data object Empty: ProfileDataState()
}
