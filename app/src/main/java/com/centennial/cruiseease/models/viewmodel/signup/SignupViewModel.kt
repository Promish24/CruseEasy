package com.centennial.cruiseease.models.viewmodel.signup


import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.centennial.cruiseease.constants.Strings
import com.centennial.cruiseease.rules.Validator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage


enum class SignUpFailureReason {
    CREDENTIAL_EXITS,
    GENERAL_FAILURE
}

class SignupViewModel : ViewModel() {

    private val TAG = SignupViewModel::class.simpleName

    var registrationUIState = mutableStateOf(RegistrationUIState())

    var allValidationsPassed = mutableStateOf(false)

    var signUpInProgress = mutableStateOf(false)

    var signUpSuccess = mutableStateOf(false)

    var signUpFailureReason = mutableStateOf<SignUpFailureReason?>(null)

    private var onSignUpFailure: (() -> Unit)? = null

    fun onEvent(event: SignupUIEvent) {
        when (event) {
            is SignupUIEvent.FirstNameChanged -> {
                registrationUIState.value = registrationUIState.value.copy(
                    firstName = event.firstName
                )
                printState()
            }

            is SignupUIEvent.LastNameChanged -> {
                registrationUIState.value = registrationUIState.value.copy(
                    lastName = event.lastName
                )
                printState()
            }

            is SignupUIEvent.EmailChanged -> {
                registrationUIState.value = registrationUIState.value.copy(
                    email = event.email
                )
                printState()

            }


            is SignupUIEvent.PasswordChanged -> {
                registrationUIState.value = registrationUIState.value.copy(
                    password = event.password
                )
                printState()

            }

            is SignupUIEvent.RegisterButtonClicked -> {
                signUp()
            }

        }
        validateDataWithRules()
    }


    private fun signUp() {
        Log.d(TAG, "Inside_signUp")
        printState()
        createUserInFirebase(
            email = registrationUIState.value.email,
            password = registrationUIState.value.password,
            firstName = registrationUIState.value.firstName,
            lastName = registrationUIState.value.lastName
        )
    }

    private fun validateDataWithRules() {
        val fNameResult = Validator.validateFirstName(
            fName = registrationUIState.value.firstName
        )

        val lNameResult = Validator.validatelastName(
            lName = registrationUIState.value.lastName
        )

        val emailResult = Validator.validateEmail(
            email = registrationUIState.value.email
        )


        val passwordResult = Validator.validatePassword(
            password = registrationUIState.value.password
        )




        Log.d(TAG, "Inside_validateDataWithRules")
        Log.d(TAG, "fNameResult= $fNameResult")
        Log.d(TAG, "lNameResult= $lNameResult")
        Log.d(TAG, "emailResult= $emailResult")
        Log.d(TAG, "passwordResult= $passwordResult")

        registrationUIState.value = registrationUIState.value.copy(
            firstNameError = fNameResult.status,
            lastNameError = lNameResult.status,
            emailError = emailResult.status,
            passwordError = passwordResult.status,
        )


        allValidationsPassed.value = fNameResult.status && lNameResult.status &&
                emailResult.status && passwordResult.status

    }


    private fun printState() {
        Log.d(TAG, "Inside_printState")
        Log.d(TAG, registrationUIState.value.toString())
    }


    private fun createUserInFirebase(
        email: String,
        password: String,
        firstName: String,
        lastName: String
    ) {
        signUpInProgress.value = true

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val currentUser = FirebaseAuth.getInstance().currentUser
                    if (currentUser != null) {
                        val storageRef = FirebaseStorage.getInstance().reference
                        val profilePicRef = storageRef.child("userProfilePicture/7309681.jpg")

                        profilePicRef.downloadUrl.addOnSuccessListener { uri ->
                            val userPhoto = uri.toString() // Save the image URL
                            val user = hashMapOf(
                                "firstName" to firstName,
                                "lastName" to lastName,
                                "email" to email,
                                "userPhoto" to userPhoto
                            )
                            val db = FirebaseFirestore.getInstance()
                            db.collection("users").document(currentUser.uid)
                                .set(user)
                                .addOnSuccessListener {
                                    signUpInProgress.value = false
                                    signUpSuccess.value = true
                                }
                                .addOnFailureListener { exception ->
                                    signUpInProgress.value = false
                                    Log.e(TAG, "Error adding user: ", exception)
                                }
                        }.addOnFailureListener { exception ->
                            signUpInProgress.value = false
                            Log.e(TAG, "Error getting download URL: ", exception)
                        }
                    }
                } else {
                    signUpInProgress.value = false
                    onSignUpFailure?.invoke()
                }
            }
            .addOnFailureListener { exception ->
                signUpInProgress.value = false
                if (exception.message == Strings.CREDENTIAL_EXISTS) {
                    signUpFailureReason.value = SignUpFailureReason.CREDENTIAL_EXITS
                } else {
                    signUpFailureReason.value = SignUpFailureReason.GENERAL_FAILURE
                }
            }
    }

}
