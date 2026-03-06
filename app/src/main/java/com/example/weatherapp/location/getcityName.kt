package com.example.weatherapp.location

import android.content.Context
import android.location.Geocoder
import java.util.Locale

fun getCityName(
    context: Context,
    lat: Double,
    lon: Double
): String {

    return try {

        val geocoder = Geocoder(context, Locale.getDefault())

        val addresses =
            geocoder.getFromLocation(lat, lon, 1)

        if (!addresses.isNullOrEmpty()) {

            addresses[0].locality ?: "Unknown City"

        } else {

            "Unknown City"

        }

    } catch (e: Exception) {

        "Unknown City"

    }
}