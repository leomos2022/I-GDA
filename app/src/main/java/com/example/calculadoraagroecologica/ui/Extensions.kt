package com.example.calculadoraagroecologica.ui

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.luminance

/**
 * Extension function to determine if the current color scheme is light or dark
 * based on the background color luminance.
 */
fun ColorScheme.isLight(): Boolean = this.background.luminance() > 0.5f 