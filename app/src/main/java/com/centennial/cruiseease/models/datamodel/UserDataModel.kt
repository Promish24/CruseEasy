package com.centennial.cruiseease.models.datamodel

data class UserData(
    val email: String,
    val firstName: String,
    val lastName: String,
    val userPhoto: String
) {
    constructor(map: Map<String, Any>) : this(
        email = map["email"] as String,
        firstName = map["firstName"] as String,
        lastName = map["lastName"] as String,
        userPhoto = map["userPhoto"] as String
    )
}
