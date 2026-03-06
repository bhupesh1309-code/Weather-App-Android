package com.example.weatherapp.ui.components

fun getWeatherIcon(code: Int, isDay: Int): String {

    return when (code) {

        // Clear sky
        0 -> if (isDay == 1) "☀️" else "🌙"

        // Partly cloudy / cloudy
        1, 2, 3 -> if (isDay == 1) "⛅" else "🌙☁️"

        // Fog
        45, 48 -> "🌫"

        // Drizzle
        51, 53, 55 -> if (isDay == 1) "🌦" else "🌙🌧"

        // Rain
        61, 63, 65 -> if (isDay == 1) "🌧" else "🌙🌧"

        // Snow
        71, 73, 75 -> "❄️"

        // Thunderstorm
        95 -> "⛈"

        else -> "🌤"
    }
}