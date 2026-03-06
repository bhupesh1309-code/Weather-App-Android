package com.example.weatherapp.ui.components

import com.example.weather.R

fun getWeatherBackground(
    code: Int,
    isDay: Int
): Int {

    return when {

        // Thunderstorm
        code == 95 ->
            R.drawable.storm

        // Rain / Drizzle / Showers
        code in listOf(51,53,55,61,63,65,80,81,82) ->
            if (isDay == 1)
                R.drawable.rain
            else
                R.drawable.night_rain

        // Cloudy
        code in listOf(1,2,3,45,48) ->
            if (isDay == 1)
                R.drawable.cloud
            else
                R.drawable.night_cloud

        // Clear sky
        code == 0 ->
            if (isDay == 1)
                R.drawable.day
            else
                R.drawable.night

        // Default fallback
        else ->
            if (isDay == 1)
                R.drawable.day
            else
                R.drawable.night
    }
}