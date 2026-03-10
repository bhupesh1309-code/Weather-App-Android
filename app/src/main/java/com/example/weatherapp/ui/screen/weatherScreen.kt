package com.example.weatherapp.ui

import android.Manifest
import android.widget.Toast
import java.util.Calendar

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.weather.R
import com.example.weatherapp.location.getCurrentLocation
import com.example.weatherapp.location.getCityName
import com.example.weatherapp.location.getCoordinatesFromCity
import com.example.weatherapp.viewmodel.WeatherViewModel
import com.example.weatherapp.ui.components.getWeatherIcon
import com.example.weatherapp.ui.components.getWeatherBackground

@Composable
fun WeatherScreen(viewModel: WeatherViewModel = viewModel()) {

    val context = LocalContext.current
    val weather = viewModel.weatherState.value
    val loading = viewModel.loading.value

    var cityName by remember { mutableStateOf("Loading...") }
    var searchCity by remember { mutableStateOf("") }

    val permissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { granted ->

            if (granted) {

                getCurrentLocation(context) { location ->

                    val lat = location.latitude
                    val lon = location.longitude

                    cityName = getCityName(context, lat, lon)

                    viewModel.loadWeather(lat, lon)
                }
            }
        }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    if (loading || weather == null) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }

        return
    }

// -------- FIND CURRENT HOUR INDEX --------

// get city timezone hour
    val calendar = Calendar.getInstance(
        java.util.TimeZone.getTimeZone(weather.timezone)
    )

    val cityHour = calendar.get(Calendar.HOUR_OF_DAY)

// find matching hour in API list
    val currentHourIndex =
        weather.hourly.time.indexOfFirst {

            val apiHour = it.substring(11,13).toIntOrNull()

            apiHour == cityHour

        }.let { if (it == -1) 0 else it }

// -------- CURRENT WEATHER --------

    val currentTemp =
        weather.hourly.temperature_2m[currentHourIndex]

    val currentCode =
        weather.hourly.weathercode[currentHourIndex]

    val isDay =
        weather.hourly.is_day[currentHourIndex]



    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Image(
            painter = painterResource(
                id = getWeatherBackground(currentCode, isDay)
            ),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )


        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(20.dp)
        ) {

            // -------- SEARCH BAR --------

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(55.dp),

                    shape = RoundedCornerShape(30.dp),

                    colors = CardDefaults.cardColors(
                        containerColor =
                            Color.White.copy(alpha = 0.25f)
                    )
                ) {

                    TextField(
                        value = searchCity,
                        onValueChange = { searchCity = it },

                        placeholder = {
                            Text(
                                "Search city",
                                color = Color.White
                            )
                        },

                        singleLine = true,

                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),

                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Button(
                    onClick = {

                        if (searchCity.isBlank()) {

                            Toast.makeText(
                                context,
                                "Enter city name",
                                Toast.LENGTH_SHORT
                            ).show()

                            return@Button
                        }

                        val result =
                            getCoordinatesFromCity(context, searchCity)

                        if (result != null) {

                            cityName = searchCity

                            viewModel.loadWeather(
                                result.first,
                                result.second
                            )

                        } else {

                            Toast.makeText(
                                context,
                                "City not found",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    },

                    shape = RoundedCornerShape(20.dp),

                    colors = ButtonDefaults.buttonColors(
                        containerColor =
                            Color.White.copy(alpha = 0.25f)
                    )
                ) {

                    Text(
                        "Search",
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))


            // -------- CITY --------

            Text(
                text = cityName,
                fontSize = 32.sp,
                color = Color.White
            )

            Text(
                text = "City hour: $cityHour",
                color = Color.Yellow
            )

            Text(
                text = "API hour: ${weather.hourly.time[currentHourIndex]}",
                color = Color.Yellow
            )
            Spacer(modifier = Modifier.height(10.dp))




            // -------- CURRENT TEMP --------

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    getWeatherIcon(currentCode, isDay),
                    fontSize = 45.sp
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    "$currentTemp°C",
                    fontSize = 80.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(30.dp))


            // -------- NEXT 7 HOURS --------

            Card(
                modifier = Modifier.fillMaxWidth(),

                colors = CardDefaults.cardColors(
                    containerColor =
                        Color.White.copy(alpha = 0.20f)
                ),

                shape = RoundedCornerShape(22.dp)
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        "Next 7 Hours",
                        color = Color.White,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    LazyRow {

                        items(7) { index ->

                            val hourIndex = currentHourIndex + index + 1

                            val time =
                                weather.hourly.time[hourIndex]
                                    .substring(11,16)

                            val label = "At $time"

                            val temp =
                                weather.hourly.temperature_2m[hourIndex]

                            val code =
                                weather.hourly.weathercode[hourIndex]

                            val day =
                                weather.hourly.is_day[hourIndex]


                            Card(
                                modifier = Modifier
                                    .padding(end = 10.dp)
                                    .width(100.dp),

                                colors = CardDefaults.cardColors(
                                    containerColor =
                                        Color.White.copy(alpha = 0.25f)
                                ),

                                shape = RoundedCornerShape(18.dp)
                            ) {

                                Column(
                                    horizontalAlignment =
                                        Alignment.CenterHorizontally,

                                    modifier = Modifier.padding(12.dp)
                                ) {

                                    Text(label, color = Color.White)

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Text(
                                        getWeatherIcon(code, day),
                                        fontSize = 24.sp
                                    )

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Text(
                                        "$temp°C",
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))


            // -------- NEXT 7 DAYS --------

            val times = weather.daily.time ?: emptyList()
            val maxTemps = weather.daily.temperature_2m_max ?: emptyList()
            val minTemps = weather.daily.temperature_2m_min ?: emptyList()
            val codes = weather.daily.weathercode ?: emptyList()

            val safeCount = minOf(
                times.size,
                maxTemps.size,
                minTemps.size,
                codes.size,
                7
            )

            Card(
                modifier = Modifier.fillMaxWidth(),

                colors = CardDefaults.cardColors(
                    containerColor =
                        Color.White.copy(alpha = 0.20f)
                ),

                shape = RoundedCornerShape(22.dp)
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        "Next 7 Days",
                        color = Color.White,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    LazyColumn {

                        items(safeCount) { index ->

                            val day = times[index]
                            val max = maxTemps[index]
                            val min = minTemps[index]
                            val code = codes[index]

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 10.dp),

                                horizontalArrangement =
                                    Arrangement.SpaceBetween
                            ) {

                                Text(day, color = Color.White)

                                Text(
                                    getWeatherIcon(code, 1),
                                    fontSize = 20.sp
                                )

                                Text(
                                    "$max° / $min°",
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}