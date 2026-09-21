package com.kuroi.guardplant.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.runtime.serialization.NavKeySerializer
import androidx.savedstate.compose.serialization.serializers.MutableStateSerializer

class NavigationState(
    val startRoute: NavKey,
    topLevelRoute: MutableState<NavKey>,
    val backStacks: Map<NavKey, NavBackStack<NavKey>>,
) {
    var topLevelRoute: NavKey by topLevelRoute

    val currentBackStack: NavBackStack<NavKey>
        get() = checkNotNull(backStacks[topLevelRoute])

    val currentKey: NavKey
        get() = currentBackStack.last()

    @Composable
    fun decoratedEntries(entryProvider: (NavKey) -> NavEntry<NavKey>): List<NavEntry<NavKey>> {
        val entriesByStack = backStacks.mapValues { (_, stack) ->
            rememberDecoratedNavEntries(
                backStack = stack,
                entryDecorators = listOf(
                    rememberSaveableStateHolderNavEntryDecorator(),
                    rememberViewModelStoreNavEntryDecorator(),
                ),
                entryProvider = entryProvider,
            )
        }
        return if (topLevelRoute == startRoute) {
            entriesByStack[startRoute].orEmpty()
        } else {
            entriesByStack[startRoute].orEmpty() + entriesByStack[topLevelRoute].orEmpty()
        }
    }
}

class Navigator(private val state: NavigationState) {
    fun navigate(key: NavKey) {
        if (key in state.backStacks) state.topLevelRoute = key else state.currentBackStack.add(key)
    }

    fun goBack() {
        if (state.currentBackStack.last() == state.topLevelRoute) {
            if (state.topLevelRoute != state.startRoute) state.topLevelRoute = state.startRoute
        } else {
            state.currentBackStack.removeLastOrNull()
        }
    }
}

@Composable
fun rememberGuardPlantNavigationState(): NavigationState {
    val selectedRoute = rememberSerializable(
        serializer = MutableStateSerializer(NavKeySerializer()),
    ) { mutableStateOf<NavKey>(OverviewKey) }
    val backStacks = TopLevelKeys.associateWith { rememberNavBackStack(it) }
    return remember(backStacks, selectedRoute) {
        NavigationState(OverviewKey, selectedRoute, backStacks)
    }
}
