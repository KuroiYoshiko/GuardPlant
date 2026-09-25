package com.kuroi.guardplant.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.Explore
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.LocalFlorist
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.kuroi.guardplant.ThemePreference
import com.kuroi.guardplant.core.data.AppContainer
import com.kuroi.guardplant.feature.addplant.AddPlantScreen
import com.kuroi.guardplant.feature.calendar.CalendarScreen
import com.kuroi.guardplant.feature.details.PlantDetailScreen
import com.kuroi.guardplant.feature.details.SpeciesDetailScreen
import com.kuroi.guardplant.feature.devices.DevicesScreen
import com.kuroi.guardplant.feature.discover.DiscoverRoute
import com.kuroi.guardplant.feature.overview.OverviewRoute
import com.kuroi.guardplant.feature.plants.MyPlantsScreen
import com.kuroi.guardplant.feature.scan.ScanScreen
import com.kuroi.guardplant.feature.settings.SettingsScreen

private data class TopLevelItem(val key: NavKey, val label: String, val icon: ImageVector)

private val topLevelItems = listOf(
    TopLevelItem(OverviewKey, "Overview", Icons.Rounded.Home),
    TopLevelItem(MyPlantsKey, "My Plants", Icons.Rounded.LocalFlorist),
    TopLevelItem(ScanKey, "Scan", Icons.Rounded.CameraAlt),
    TopLevelItem(CalendarKey, "Calendar", Icons.Rounded.CalendarMonth),
    TopLevelItem(DiscoverKey, "Discover", Icons.Rounded.Explore),
)

@Composable
fun GuardPlantNavigation(
    container: AppContainer,
    themePreference: ThemePreference,
    onThemePreferenceChanged: (ThemePreference) -> Unit,
) {
    val navigationState = rememberGuardPlantNavigationState()
    val navigator = remember(navigationState) { Navigator(navigationState) }
    val showBottomBar = navigationState.currentKey in TopLevelKeys

    val provider = entryProvider<NavKey> {
        entry<OverviewKey> {
            OverviewRoute(container.careRepository, onSettings = { navigator.navigate(SettingsKey) })
        }
        entry<MyPlantsKey> {
            MyPlantsScreen(
                plantRepository = container.plantRepository,
                speciesRepository = container.speciesRepository,
                onPlantClick = { navigator.navigate(PlantDetailKey(it)) },
                onAddPlant = { navigator.navigate(AddPlantKey) },
            )
        }
        entry<ScanKey> { ScanScreen(onSpeciesClick = { navigator.navigate(SpeciesDetailKey(it)) }) }
        entry<CalendarKey> { CalendarScreen(container.careRepository) }
        entry<DiscoverKey> { DiscoverRoute(container.speciesRepository, onSpeciesClick = { navigator.navigate(SpeciesDetailKey(it)) }) }
        entry<SettingsKey> {
            SettingsScreen(
                themePreference = themePreference,
                onThemePreferenceChanged = onThemePreferenceChanged,
                onDevices = { navigator.navigate(DevicesKey) },
                onBack = navigator::goBack,
            )
        }
        entry<DevicesKey> { DevicesScreen(onBack = navigator::goBack) }
        entry<AddPlantKey> { AddPlantScreen(container.plantRepository, onBack = navigator::goBack) }
        entry<SpeciesDetailKey> { key ->
            SpeciesDetailScreen(
                speciesId = key.speciesId,
                repository = container.speciesRepository,
                onBack = navigator::goBack,
                onAddPlant = { navigator.navigate(AddPlantKey) },
            )
        }
        entry<PlantDetailKey> { key ->
            PlantDetailScreen(key.plantId, container.plantRepository, container.speciesRepository, navigator::goBack)
        }
    }

    Scaffold(
        contentWindowInsets = if (showBottomBar) {
            WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal)
        } else {
            WindowInsets(0.dp)
        },
        bottomBar = {
            if (showBottomBar) {
                GuardPlantBottomBar(
                    selected = navigationState.topLevelRoute,
                    onSelect = navigator::navigate,
                )
            }
        },
    ) { innerPadding ->
        NavDisplay(
            entries = navigationState.decoratedEntries(provider),
            onBack = navigator::goBack,
            modifier = Modifier.padding(innerPadding).consumeWindowInsets(innerPadding),
        )
    }
}

@Composable
private fun GuardPlantBottomBar(selected: NavKey, onSelect: (NavKey) -> Unit) {
    Box(modifier = Modifier.fillMaxWidth()) {
        NavigationBar(modifier = Modifier.fillMaxWidth().padding(top = 18.dp)) {
            topLevelItems.forEach { item ->
                val isScan = item.key == ScanKey
                NavigationBarItem(
                    selected = selected == item.key,
                    onClick = { onSelect(item.key) },
                    icon = {
                        if (isScan) Spacer(Modifier.size(52.dp)) else Icon(item.icon, item.label)
                    },
                    label = { Text(item.label) },
                    colors = if (isScan) androidx.compose.material3.NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Transparent,
                        unselectedIconColor = Color.Transparent,
                        indicatorColor = Color.Transparent,
                    ) else androidx.compose.material3.NavigationBarItemDefaults.colors(),
                )
            }
        }
        Surface(
            onClick = { onSelect(ScanKey) },
            color = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            shape = androidx.compose.foundation.shape.CircleShape,
            shadowElevation = 6.dp,
            modifier = Modifier.align(Alignment.TopCenter).size(56.dp),
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(Icons.Rounded.CameraAlt, "Scan", Modifier.size(27.dp))
            }
        }
    }
}
