package com.example.weatherapp.data.dto

data class Hourly(
    val time: List<String>,
    val temperature_2m: List<Double>,
    val weathercode: List<Int>,
    val is_day: List<Int>
)