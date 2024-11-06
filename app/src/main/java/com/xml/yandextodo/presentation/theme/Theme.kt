package com.xml.yandextodo.presentation.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    secondary = SecondaryDark,
    tertiary = TertiaryDark,
    background = BackgroundPrimaryDark,
    onBackground = BackgroundSecondaryDark,
    onSurface = BackgroundElevatedDark,
    onErrorContainer = DisableDark,
    errorContainer = RedDark,
    outlineVariant = OverlayDark,
    inverseSurface = GreenDark,
    inversePrimary = SeparatorDark,
    surfaceContainer = GrayLightDark,
    surfaceContainerHighest = GrayDark,
    surface = BlueDark,
)

private val LightColorScheme = lightColorScheme(
    primary = LabelPrimaryLight,
    secondary = LabelSecondaryLight,
    tertiary = LabelTertiaryLight,
    background = BackgroundPrimaryLight,
    onBackground = BackgroundSecondaryLight,
    onErrorContainer = DisableLight,
    errorContainer = RedLight,
    outlineVariant = OverlayLight,
    inverseSurface = GreenLight,
    inversePrimary = SeparatorLight,
    surfaceContainer = GrayLight,
    surfaceContainerHighest = Gray,
    onSurface = BackgroundElevatedLight,
    surface = BlueLight,
)

@Composable
fun YandexToDoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}