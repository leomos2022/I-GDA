package com.example.calculadoraagroecologica.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.luminance
import com.example.calculadoraagroecologica.ui.theme.*

private val LightColors = lightColorScheme(
    primary = ButtonGreen,
    onPrimary = ButtonText,
    background = PastelLight,
    surface = PastelSurface,
    onBackground = PastelDark,
    onSurface = PastelDark
)

private val DarkColors = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,
    background = DarkBackground,
    surface = DarkSurface,
    onBackground = PastelLight,
    onSurface = PastelLight
)

@Composable
fun CalculadoraAgroecologicaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}