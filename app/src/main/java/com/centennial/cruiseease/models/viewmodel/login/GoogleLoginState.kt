package com.centennial.cruiseease.models.viewmodel.login

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null
)
