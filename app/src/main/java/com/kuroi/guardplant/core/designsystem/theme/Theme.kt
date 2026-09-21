package com.kuroi.guardplant.core.designsystem.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColors = lightColorScheme(
    primary = Leaf,
    onPrimary = WarmWhite,
    primaryContainer = Sprout,
    onPrimaryContainer = Ink,
    secondary = Moss,
    secondaryContainer = ColorTokens.SageContainer,
    tertiary = SoftBlue,
    background = Cream,
    onBackground = Ink,
    surface = WarmWhite,
    onSurface = Ink,
    surfaceVariant = ColorTokens.LightSurfaceVariant,
    error = Terracotta,
)

private val DarkColors = darkColorScheme(
    primary = LeafLight,
    onPrimary = DeepForest,
    primaryContainer = ColorTokens.DarkLeaf,
    onPrimaryContainer = ColorTokens.PaleLeaf,
    secondary = Sprout,
    secondaryContainer = ColorTokens.DarkSage,
    tertiary = ColorTokens.NightBlue,
    background = DeepForest,
    onBackground = ColorTokens.NightText,
    surface = NightSurface,
    onSurface = ColorTokens.NightText,
    surfaceVariant = NightSurfaceHigh,
    error = ColorTokens.NightError,
)

private object ColorTokens {
    val SageContainer = androidx.compose.ui.graphics.Color(0xFFDDE8D8)
    val LightSurfaceVariant = androidx.compose.ui.graphics.Color(0xFFE9EDE6)
    val DarkLeaf = androidx.compose.ui.graphics.Color(0xFF24543E)
    val PaleLeaf = androidx.compose.ui.graphics.Color(0xFFD7F2DF)
    val DarkSage = androidx.compose.ui.graphics.Color(0xFF344D38)
    val NightBlue = androidx.compose.ui.graphics.Color(0xFF9CCBC7)
    val NightText = androidx.compose.ui.graphics.Color(0xFFE8F0E9)
    val NightError = androidx.compose.ui.graphics.Color(0xFFFFB4A4)
}

@Composable
fun GuardPlantTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) DarkColors else LightColors
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colors,
        typography = GuardPlantTypography,
        shapes = GuardPlantShapes,
        content = content,
    )
}
