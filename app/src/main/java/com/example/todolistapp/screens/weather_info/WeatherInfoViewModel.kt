package com.example.todolistapp.screens.weather_info

import android.icu.text.SimpleDateFormat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistapp.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class WeatherInfoViewModel @Inject constructor(
    private val weatherService: WeatherService
) : ViewModel() {
    private val _weatherResponse = MutableStateFlow<Response<WeatherResponse>?>(null)
    val weatherResponse: StateFlow<Response<WeatherResponse>?> = _weatherResponse

    private val _forecastResponse = MutableStateFlow<Response<ForecastResponse>?>(null)
    val forecastResponse: StateFlow<Response<ForecastResponse>?> = _forecastResponse

    fun getWeatherInfo(cityName: String, apiKey: String) {
        _weatherResponse.value = Response.Loading
        _forecastResponse.value = Response.Loading

        viewModelScope.launch {
            try {
                val weather = weatherService.getCurrentWeather(cityName, apiKey)
                _weatherResponse.value = Response.Success(weather)

                val forecast = weatherService.getForecast(cityName, apiKey)
                val dailyForecasts = processForecastData(forecast.list)
                _forecastResponse.value = Response.Success(ForecastResponse(dailyForecasts))
            } catch (e: Exception) {
                _weatherResponse.value = Response.Failure(e)
                _forecastResponse.value = Response.Failure(e)
            }
        }
    }

    private fun processForecastData(forecastData: List<ForecastData>): List<ForecastData> {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return forecastData.groupBy {
            dateFormat.format(Date(it.dt * 1000))
        }.map { (_, forecasts) ->
            forecasts.first()
        }
    }
}
