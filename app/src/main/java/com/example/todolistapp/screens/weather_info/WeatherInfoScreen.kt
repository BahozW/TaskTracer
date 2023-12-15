package com.example.todolistapp.screens.weather_info

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.todolistapp.ui.components.BackButton
import com.example.todolistapp.ui.components.SmallSpacer
import com.example.todolistapp.utils.Constants
import com.example.todolistapp.utils.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun WeatherInfoScreen(
    navController: NavController,
    navigateBack: () -> Unit,
    viewModel: WeatherInfoViewModel = hiltViewModel()
) {
    var cityName by remember { mutableStateOf("") }
    val weatherState by viewModel.weatherResponse.collectAsState()
    val forecastState by viewModel.forecastResponse.collectAsState()

    Scaffold(
        topBar = { WeatherTopBar(navigateBack) },
        content = { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                TextField(
                    value = cityName,
                    onValueChange = { cityName = it },
                    label = { Text("Enter City Name") }
                )
                Button(onClick = { viewModel.getWeatherInfo(cityName, "APIKEY") }) {
                    Text("Get Weather")
                }

                weatherState?.let { state ->
                    when (state) {
                        is Response.Loading -> CircularProgressIndicator()
                        is Response.Success -> WeatherDisplay(state.data)
                        is Response.Failure -> Text("Error occurred")
                    }
                }

                forecastState?.let { state ->
                    when (state) {
                        is Response.Loading -> CircularProgressIndicator()
                        is Response.Success -> {
                            val forecastData = (forecastState as Response.Success<ForecastResponse>).data
                            ForecastDisplay(forecastData)
                        }
                        is Response.Failure -> Text("Error occurred")
                    }
                }
            }
        }
    )
}

@Composable
fun WeatherTopBar(navigateBack: () -> Unit) {
    TopAppBar (
        title = {
            Text(
                text = Constants.WEATHER_INFO_SCREEN
            )
        },
        navigationIcon = {
            BackButton(
                navigateBack = navigateBack
            )
        }
    )
}

@Composable
fun WeatherDisplay(weatherData: WeatherResponse) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Current Weather",
                style = MaterialTheme.typography.h6
            )
            SmallSpacer()
            Text(
                text = "${weatherData.main.temp}°C",
                style = MaterialTheme.typography.h4
            )
            SmallSpacer()
            Text(
                text = weatherData.weather.first().main,
                style = MaterialTheme.typography.subtitle1
            )
            Text(
                text = weatherData.weather.first().description,
                style = MaterialTheme.typography.subtitle2
            )
        }
    }
}

@Composable
fun ForecastDisplay(forecastData: ForecastResponse) {
    Column {
        Text(
            text = "Weekly Forecast",
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(16.dp)
        )
        LazyRow {
            items(forecastData.list) { forecast ->
                ForecastItem(forecast)
            }
        }
    }
}

@Composable
fun ForecastItem(forecast: ForecastData) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .padding(8.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Format and display the date
            Text(
                text = SimpleDateFormat("EEE, d MMM", Locale.getDefault()).format(Date(forecast.dt * 1000)),
                style = MaterialTheme.typography.subtitle2
            )
            SmallSpacer()
            Text(
                text = "${forecast.main.temp}°C",
                style = MaterialTheme.typography.h6
            )
            SmallSpacer()
            Text(
                text = forecast.weather.first().main,
                style = MaterialTheme.typography.subtitle1
            )
        }
    }
}



