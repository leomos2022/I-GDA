package com.example.calculadoraagroecologica.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Esquema de colores para tema claro mejorado
private val LightColors = lightColorScheme(
    primary = ButtonGreen,
    onPrimary = ButtonText,
    primaryContainer = PastelGreen,
    onPrimaryContainer = PastelDark,
    secondary = EcoGreen,
    onSecondary = ButtonText,
    secondaryContainer = PastelBlue,
    onSecondaryContainer = PastelDark,
    background = PastelLight,
    onBackground = PastelDark,
    surface = PastelSurface,
    onSurface = PastelDark,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = MediumContrastText,
    outline = PastelDark.copy(alpha = 0.3f),
    outlineVariant = PastelDark.copy(alpha = 0.2f)
)

// Esquema de colores para tema oscuro mejorado
private val DarkColors = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,
    primaryContainer = DarkPrimary.copy(alpha = 0.2f),
    onPrimaryContainer = PastelLight,
    secondary = EcoGreen,
    onSecondary = DarkOnPrimary,
    secondaryContainer = EcoGreen.copy(alpha = 0.2f),
    onSecondaryContainer = PastelLight,
    background = DarkBackground,
    onBackground = PastelLight,
    surface = DarkSurface,
    onSurface = PastelLight,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = PastelLight.copy(alpha = 0.8f),
    outline = PastelLight.copy(alpha = 0.3f),
    outlineVariant = PastelLight.copy(alpha = 0.2f)
)

@Composable
fun CalculadoraAgroecologicaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors
    val view = LocalView.current
    
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Configurar barra de estado y navegación para que coincidan con el tema
            window.statusBarColor = colorScheme.primary.toArgb()
            window.navigationBarColor = colorScheme.surface.toArgb()
            
            // Configurar iconos de la barra de estado para que sean visibles
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}