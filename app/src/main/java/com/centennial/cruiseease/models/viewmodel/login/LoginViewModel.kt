package com.centennial.cruiseease.models.viewmodel.login


import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.centennial.cruiseease.constants.Strings
import com.centennial.cruiseease.rules.Validator
import com.google.firebase.auth.FirebaseAuth

enum class LoginFailureReason {
    MISMATCHING_CREDENTIALS,
    GENERAL_FAILURE
}


class LoginViewModel : ViewModel() {

    private val tag = LoginViewModel::class.simpleName

    var loginUIState = mutableStateOf(LoginUIState())

    var allValidationsPassed = mutableStateOf(false)

    private var loginInProgress = mutableStateOf(false)

    private var onLoginFailure: (() -> Unit)? = null

    var loginFailed = mutableStateOf(false)

    var loginFailureReason = mutableStateOf<LoginFailureReason?>(null)

    val loginSuccess = mutableStateOf(false)

    fun onEvent(event: LoginUIEvent) {
        when (event) {
            is LoginUIEvent.EmailChanged -> {
                loginUIState.value = loginUIState.value.copy(
                    email = event.email
                )
            }

            is LoginUIEvent.PasswordChanged -> {
                loginUIState.value = loginUIState.value.copy(
                    password = event.password
                )
            }

            is LoginUIEvent.LoginButtonClicked -> {
                login()
            }
        }
        validateLoginUIDataWithRules()
    }

    private fun validateLoginUIDataWithRules() {
        val emailResult = Validator.validateEmail(
            email = loginUIState.value.email
        )


        val passwordResult = Validator.validatePassword(
            password = loginUIState.value.password
        )

        loginUIState.value = loginUIState.value.copy(
            emailError = emailResult.status,
            passwordError = passwordResult.status
        )

        allValidationsPassed.value = emailResult.status && passwordResult.status

    }

    private fun login() {

        loginInProgress.value = true
        val email = loginUIState.value.email
        val password = loginUIState.value.password

        FirebaseAuth
            .getInstance()
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    loginSuccess.value = true
                    loginInProgress.value = false
                } else {
                    loginInProgress.value = false
                    onLoginFailure?.invoke()
                }
            }
            .addOnFailureListener { exception ->
                loginInProgress.value = false
                if (exception.message == Strings.INCORRECT_CREDENTIALS) {
                    loginFailureReason.value = LoginFailureReason.MISMATCHING_CREDENTIALS
                } else {
                    loginFailureReason.value = LoginFailureReason.GENERAL_FAILURE
                }
                Log.d(tag, "Login failed with exception: ${exception.message}")
                onLoginFailure?.invoke()
            }

    }

}


