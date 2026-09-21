package com.kuroi.guardplant.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable data object OverviewKey : NavKey
@Serializable data object MyPlantsKey : NavKey
@Serializable data object ScanKey : NavKey
@Serializable data object CalendarKey : NavKey
@Serializable data object DiscoverKey : NavKey

@Serializable data object SettingsKey : NavKey
@Serializable data object DevicesKey : NavKey
@Serializable data object AddPlantKey : NavKey
@Serializable data class SpeciesDetailKey(val speciesId: String) : NavKey
@Serializable data class PlantDetailKey(val plantId: String) : NavKey

val TopLevelKeys: Set<NavKey> = linkedSetOf(OverviewKey, MyPlantsKey, ScanKey, CalendarKey, DiscoverKey)
