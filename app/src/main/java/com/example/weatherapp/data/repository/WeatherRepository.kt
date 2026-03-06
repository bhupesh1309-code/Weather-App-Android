package com.example.weatherapp.data.repository

import com.example.weatherapp.data.api.RetrofitInstance
import com.example.weatherapp.data.dto.Weather

class WeatherRepository {

    suspend fun getWeather(
        lat: Double,
        lon: Double
    ): Weather {

        return RetrofitInstance.api.getWeather(lat, lon)

    }
}