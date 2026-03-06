package com.example.weatherapp.location

import android.content.Context
import android.location.Geocoder
import java.util.Locale

fun getCoordinatesFromCity(
    context: Context,
    city: String
): Pair<Double, Double>? {

    return try {

        val geocoder = Geocoder(context, Locale.getDefault())

        val addresses =
            geocoder.getFromLocationName(city, 1)

        if (!addresses.isNullOrEmpty()) {

            val location = addresses[0]

            Pair(location.latitude, location.longitude)

        } else null

    } catch (e: Exception) {

        null
    }
}