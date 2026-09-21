package com.kuroi.guardplant

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.kuroi.guardplant.core.data.fake.AppContainer
import com.kuroi.guardplant.core.designsystem.theme.GuardPlantTheme
import com.kuroi.guardplant.navigation.GuardPlantNavigation

enum class ThemePreference { SYSTEM, LIGHT, DARK }

@Composable
fun GuardPlantApp() {
    val container = remember { AppContainer() }
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
