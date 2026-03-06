package com.example.weatherapp.data.api

import com.example.weatherapp.data.dto.Weather
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("v1/forecast")
    suspend fun getWeather(

        @Query("latitude") lat: Double,

        @Query("longitude") lon: Double,

        @Query("hourly")
        hourly: String = "temperature_2m,weathercode,is_day",

        @Query("daily")
        daily: String = "temperature_2m_max,temperature_2m_min,weathercode",

        @Query("timezone")
        timezone: String = "auto"

    ): Weather
}