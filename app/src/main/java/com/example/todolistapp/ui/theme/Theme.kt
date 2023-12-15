package com.example.todolistapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = PrimaryBlue,
    primaryVariant = PrimaryDarkBlue,
    secondary = SecondaryCoral,
    background = BackgroundDark,
    surface = SurfaceDark,
    error = ErrorRed
)

private val LightColorPalette = lightColors(
    primary = PrimaryBlue,
    primaryVariant = PrimaryDarkBlue,
    secondary = SecondaryCoral,
    background = BackgroundLight,
    surface = SurfaceLight,
    error = ErrorRed
)

@Composable
fun ToDoListAppTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) DarkColorPalette else LightColorPalette

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}