package com.example.todolistapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.todolistapp.navigation.Screen.*
import com.example.todolistapp.screens.forgot_password.ForgotPasswordScreen
import com.example.todolistapp.screens.calendar.CalendarScreen
import com.example.todolistapp.screens.profile.ProfileScreen
import com.example.todolistapp.screens.login.LoginScreen
import com.example.todolistapp.screens.sign_up.SignUpScreen
import com.example.todolistapp.screens.verify_email.VerifyEmailScreen
import com.example.todolistapp.screens.todo_list.ToDoListScreen
import com.example.todolistapp.screens.weather_info.WeatherInfoScreen

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = LoginScreen.route) {
        composable(route = LoginScreen.route) {
            LoginScreen(
                navigateToForgotPasswordScreen = {
                    navController.navigate(ForgotPasswordScreen.route)
                },
                navigateToSignUpScreen = {
                    navController.navigate(SignUpScreen.route)
                }
            )
        }

        composable(route = ForgotPasswordScreen.route) {
            ForgotPasswordScreen(
                navigateBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(route = SignUpScreen.route) {
            SignUpScreen(
                navigateBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(route = VerifyEmailScreen.route) {
            VerifyEmailScreen(
                navigateToProfileScreen = {
                    navController.navigate(ProfileScreen.route) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(route = ProfileScreen.route) {
            ProfileScreen(
                navigateToCalendar = {
                    navController.navigate(CalendarScreen.route)
                },
                navigateToWeather = {
                    navController.navigate(WeatherInfoScreen.route)
                }
            )
        }
        composable(route = SignUpScreen.route) {
            SignUpScreen(
                navigateBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(route = CalendarScreen.route) {
            CalendarScreen(
                navController = navController,
                navigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = "${ToDoListScreen.route}/{dateString}",
            arguments = listOf(navArgument("dateString") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val dateString = backStackEntry.arguments?.getString("dateString")
                ?: throw IllegalStateException("No date string provided")

            ToDoListScreen(
                date = dateString,
                navigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = WeatherInfoScreen.route) {
            WeatherInfoScreen(
                navController = navController,
                navigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}