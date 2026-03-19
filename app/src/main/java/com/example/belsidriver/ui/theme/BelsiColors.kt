package com.example.belsidriver.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class BelsiExtendedColors(
    val primaryBlue: Color,
    val primaryBlueDark: Color,
    val primaryBlueLight: Color,
    val successGreen: Color,
    val successGreenDark: Color,
    val successGreenLight: Color,
    val warningAmber: Color,
    val warningAmberLight: Color,
    val warningAmberText: Color,
    val errorRed: Color,
    val errorRedDark: Color,
    val grayPending: Color,
    val grayLight: Color,
    val disabledBg: Color,
    val disabledText: Color,
    val onlineOff: Color,
    val cardBackground: Color,
    val border: Color
)

val LightBelsiColors = BelsiExtendedColors(
    primaryBlue = PrimaryBlue,
    primaryBlueDark = PrimaryBlueDark,
    primaryBlueLight = PrimaryBlueLight,
    successGreen = SuccessGreen,
    successGreenDark = SuccessGreenDark,
    successGreenLight = SuccessGreenLight,
    warningAmber = WarningAmber,
    warningAmberLight = WarningAmberLight,
    warningAmberText = WarningAmberText,
    errorRed = ErrorRed,
    errorRedDark = ErrorRedDark,
    grayPending = GrayPending,
    grayLight = GrayLight,
    disabledBg = GrayDisabledBg,
    disabledText = GrayDisabledText,
    onlineOff = GrayOnlineOff,
    cardBackground = CardLight,
    border = BorderLight
)

val DarkBelsiColors = BelsiExtendedColors(
    primaryBlue = PrimaryBlueDarkTheme,
    primaryBlueDark = PrimaryBlueDarkDark,
    primaryBlueLight = PrimaryBlueLightDark,
    successGreen = SuccessGreenDarkTheme,
    successGreenDark = SuccessGreenDarkTheme,
    successGreenLight = SuccessGreenLightDark,
    warningAmber = WarningAmberDarkTheme,
    warningAmberLight = WarningAmberLightDark,
    warningAmberText = WarningAmberDarkTheme,
    errorRed = ErrorRedDarkTheme,
    errorRedDark = ErrorRedDarkTheme,
    grayPending = GrayPendingDarkTheme,
    grayLight = GrayLightDark,
    disabledBg = GrayLightDark,
    disabledText = GrayPendingDarkTheme,
    onlineOff = GrayPendingDarkTheme,
    cardBackground = CardDark,
    border = Color(0x1FFFFFFF) // rgba(255,255,255,0.12)
)

val LocalBelsiColors = staticCompositionLocalOf { LightBelsiColors }

object BelsiTheme {
    val colors: BelsiExtendedColors
        @Composable
        get() = LocalBelsiColors.current
}
