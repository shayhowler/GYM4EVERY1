package com.gym4every1.database

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

fun formatTimestamp(timestamp: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC") // Ensures UTC parsing
    }
    val outputFormat = SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault()).apply {
        timeZone = TimeZone.getDefault() // Converts to user's local timezone
    }

    return try {
        val date = inputFormat.parse(timestamp)
        date?.let { outputFormat.format(it) } ?: "Unknown time"
    } catch (e: Exception) {
        "$e Invalid date"
    }
}