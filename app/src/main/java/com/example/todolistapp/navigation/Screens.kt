package com.example.todolistapp.navigation

import com.example.todolistapp.utils.Constants.CALENDAR_SCREEN
import com.example.todolistapp.utils.Constants.FORGOT_PASSWORD_SCREEN
import com.example.todolistapp.utils.Constants.PROFILE_SCREEN
import com.example.todolistapp.utils.Constants.LOGIN_SCREEN
import com.example.todolistapp.utils.Constants.SIGN_UP_SCREEN
import com.example.todolistapp.utils.Constants.TODO_LIST_SCREEN
import com.example.todolistapp.utils.Constants.VERIFY_EMAIL_SCREEN
import com.example.todolistapp.utils.Constants.WEATHER_INFO_SCREEN

sealed class Screen(val route: String) {
    data object LoginScreen: Screen(LOGIN_SCREEN)
    data object ForgotPasswordScreen: Screen(FORGOT_PASSWORD_SCREEN)
    data object SignUpScreen: Screen(SIGN_UP_SCREEN)
    data object VerifyEmailScreen: Screen(VERIFY_EMAIL_SCREEN)
    data object ProfileScreen: Screen(PROFILE_SCREEN)
    data object CalendarScreen : Screen(CALENDAR_SCREEN)
    data object ToDoListScreen : Screen(TODO_LIST_SCREEN)
    data object WeatherInfoScreen : Screen(WEATHER_INFO_SCREEN)
}