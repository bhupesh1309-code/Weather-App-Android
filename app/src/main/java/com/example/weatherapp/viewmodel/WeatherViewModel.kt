package com.example.weatherapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.dto.Weather
import com.example.weatherapp.data.repository.WeatherRepository
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    private val repository = WeatherRepository()

    var weatherState = mutableStateOf<Weather?>(null)
        private set

    var loading = mutableStateOf(false)
        private set

    fun loadWeather(lat: Double, lon: Double) {

        viewModelScope.launch {

            loading.value = true

            try {

                val result = repository.getWeather(lat, lon)
                weatherState.value = result

            } catch (e: Exception) {

                e.printStackTrace()

            } finally {

                loading.value = false

            }
        }
    }
}