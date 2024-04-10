package com.centennial.cruiseease.utils

import java.text.SimpleDateFormat
import java.util.Date

fun parseDate(dateString: String): Date? {
    val format = SimpleDateFormat("yyyy-MM-dd")
    return try {
        format.parse(dateString)
    } catch (e: Exception) {
        null
    }
}
