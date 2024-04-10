package com.centennial.cruiseease.rules

import java.util.Date

object Validator {

    fun validateFirstName(fName: String): ValidationResult {
        return ValidationResult(
            (fName.isNotEmpty() && fName.length >= 2)
        )
    }

    fun validatelastName(lName: String): ValidationResult {
        return ValidationResult(
            (lName.isNotEmpty() && lName.length >= 4)
        )
    }

    fun validateEmail(email: String): ValidationResult {
        val emailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        val isValid = emailRegex.matches(email)
        return ValidationResult(isValid)
    }

    fun validatePassword(password: String): ValidationResult {
        return ValidationResult(
            (password.isNotEmpty() && password.length >= 4)
        )
    }

    fun validateFullName(fullName: String): ValidationResult {
        val nameParts = fullName.split(" ")
        return ValidationResult(
            (nameParts.size == 2 && nameParts[0].isNotEmpty() && nameParts[1].isNotEmpty())
        )
    }


    fun validateDate(date: Date?): ValidationResult {
        return ValidationResult(date != null)
    }

    fun validatePassengerCount(passengerCount: String): ValidationResult {
        return ValidationResult(passengerCount.isNotEmpty())
    }

    fun validateIsNotEmpty(anything: String): ValidationResult {
        return ValidationResult(anything.isNotEmpty())
    }

}

data class ValidationResult(
    val status: Boolean = false
)

