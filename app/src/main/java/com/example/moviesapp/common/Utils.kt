package com.example.moviesapp.common


fun formatMinutesToHoursAndMinutes(minutes: Int): String {
    val hours = minutes / 60
    val remainingMinutes = minutes % 60
    return if (hours > 0) {
        "$hours h $remainingMinutes min"
    } else {
        "$remainingMinutes min"
    }
}
