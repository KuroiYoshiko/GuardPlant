package com.kuroi.guardplant

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.kuroi.guardplant.core.data.AppContainer
import com.kuroi.guardplant.core.designsystem.theme.GuardPlantTheme
import com.kuroi.guardplant.navigation.GuardPlantNavigation

enum class ThemePreference { SYSTEM, LIGHT, DARK }

@Composable
fun GuardPlantApp() {
    val applicationContext = LocalContext.current.applicationContext
    val container = remember(applicationContext) { AppContainer(applicationContext) }
    var themePreferenceName by rememberSaveable { mutableStateOf(ThemePreference.SYSTEM.name) }
    val themePreference = ThemePreference.valueOf(themePreferenceName)
    val darkTheme = when (themePreference) {
        ThemePreference.SYSTEM -> isSystemInDarkTheme()
        ThemePreference.LIGHT -> false
        ThemePreference.DARK -> true
    }

    GuardPlantTheme(darkTheme = darkTheme) {
        GuardPlantNavigation(
            container = container,
            themePreference = themePreference,
            onThemePreferenceChanged = { themePreferenceName = it.name },
        )
    }
}
