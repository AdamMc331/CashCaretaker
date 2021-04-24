package com.androidessence.cashcaretaker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val LightColors = lightColors()
private val DarkColors = darkColors()

@Composable
fun CashCaretakerTheme(
    darkMode: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (darkMode) {
        DarkColors
    } else {
        LightColors
    }

    MaterialTheme(
        colors = colors,
    ) {
        content()
    }
}
