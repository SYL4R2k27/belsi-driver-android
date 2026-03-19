package com.example.belsidriver.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = Color.White,
    primaryContainer = PrimaryBlueLight,
    onPrimaryContainer = PrimaryBlue,
    secondary = PrimaryBlueLight,
    onSecondary = PrimaryBlue,
    secondaryContainer = PrimaryBlueLight,
    onSecondaryContainer = PrimaryBlue,
    tertiary = SuccessGreen,
    onTertiary = Color.White,
    error = ErrorRed,
    onError = Color.White,
    errorContainer = Color(0xFFFCE8E6),
    onErrorContainer = ErrorRed,
    background = BackgroundLight,
    onBackground = ForegroundLight,
    surface = BackgroundLight,
    onSurface = ForegroundLight,
    surfaceVariant = GrayLight,
    onSurfaceVariant = GrayPending,
    outline = BorderLight,
    outlineVariant = BorderLight
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryBlueDarkTheme,
    onPrimary = BackgroundDark,
    primaryContainer = PrimaryBlueLightDark,
    onPrimaryContainer = PrimaryBlueDarkTheme,
    secondary = PrimaryBlueLightDark,
    onSecondary = PrimaryBlueDarkTheme,
    secondaryContainer = PrimaryBlueLightDark,
    onSecondaryContainer = PrimaryBlueDarkTheme,
    tertiary = SuccessGreenDarkTheme,
    onTertiary = BackgroundDark,
    error = ErrorRedDarkTheme,
    onError = BackgroundDark,
    errorContainer = Color(0xFF3C1F1E),
    onErrorContainer = ErrorRedDarkTheme,
    background = BackgroundDark,
    onBackground = ForegroundDark,
    surface = BackgroundDark,
    onSurface = ForegroundDark,
    surfaceVariant = GrayLightDark,
    onSurfaceVariant = GrayPendingDarkTheme,
    outline = Color(0x1FFFFFFF),
    outlineVariant = Color(0x1FFFFFFF)
)

@Composable
fun BELSIDriverTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val belsiColors = if (darkTheme) DarkBelsiColors else LightBelsiColors

    CompositionLocalProvider(LocalBelsiColors provides belsiColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
