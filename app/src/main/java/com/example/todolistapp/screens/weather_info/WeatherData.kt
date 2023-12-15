package com.example.todolistapp.screens.weather_info

data class WeatherResponse(
    val main: Main,
    val weather: List<Weather>
)

data class Main(
    val temp: Double
)

data class Weather(
    val main: String,
    val description: String
)

data class ForecastResponse(
    val list: List<ForecastData>
)

data class ForecastData(
    val dt: Long,
    val main: Main,
    val weather: List<Weather>
)
