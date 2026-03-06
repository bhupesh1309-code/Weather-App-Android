package com.example.weatherapp.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationServices

@SuppressLint("MissingPermission")
fun getCurrentLocation(
    context: Context,
    onLocation: (Location) -> Unit
) {

    val fusedLocationClient =
        LocationServices.getFusedLocationProviderClient(context)

    fusedLocationClient.lastLocation
        .addOnSuccessListener { location ->

            if (location != null) {

                onLocation(location)

            } else {

                val fakeLocation = Location("emulator").apply {
                    latitude = 28.6139
                    longitude = 77.2090
                }

                onLocation(fakeLocation)
            }
        }

        .addOnFailureListener {

            val fakeLocation = Location("fallback").apply {
                latitude = 28.6139
                longitude = 77.2090
            }

            onLocation(fakeLocation)
        }
}