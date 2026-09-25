package com.kuroi.guardplant.feature.discover

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kuroi.guardplant.core.data.SpeciesRepository
import com.kuroi.guardplant.R
import com.kuroi.guardplant.core.designsystem.component.EmptyState
import com.kuroi.guardplant.core.designsystem.component.ScreenHeader
import com.kuroi.guardplant.core.designsystem.component.SearchField
import com.kuroi.guardplant.core.designsystem.component.SpeciesCard
import com.kuroi.guardplant.core.model.GrowthHabit
import com.kuroi.guardplant.core.model.HumidityLevel
import com.kuroi.guardplant.core.model.LightLevel
import com.kuroi.guardplant.core.model.MatureSize
import com.kuroi.guardplant.core.model.Species
import com.kuroi.guardplant.core.model.SpeciesFilters
import com.kuroi.guardplant.core.presentation.labelResource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class DiscoverUiState(
    val query: String = "",
    val filters: SpeciesFilters = SpeciesFilters(),
    val species: List<Species> = emptyList(),
    val favouriteIds: Set<String> = emptySet(),
)

class DiscoverViewModel(private val repository: SpeciesRepository) : ViewModel() {
    private val query = MutableStateFlow("")
    private val filters = MutableStateFlow(SpeciesFilters())

    val uiState = combine(repository.species, repository.favouriteSpeciesIds, query, filters) { _, favourites, search, activeFilters ->
        val searchMatches = repository.searchSpecies(search).mapTo(mutableSetOf(), Species::speciesId)
        val filtered = repository.filterSpecies(activeFilters).filter { it.speciesId in searchMatches }
        DiscoverUiState(search, activeFilters, filtered, favourites)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), DiscoverUiState())

    fun setQuery(value: String) { query.value = value }
    fun setFilters(value: SpeciesFilters) { filters.value = value }
    fun clearFilters() { filters.value = SpeciesFilters() }
    fun clearAll() {
        query.value = ""
        filters.value = SpeciesFilters()
    }
    fun toggleFavourite(id: String) = repository.toggleFavourite(id)

    companion object {
        fun factory(repository: SpeciesRepository): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T = DiscoverViewModel(repository) as T
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DiscoverRoute(repository: SpeciesRepository, onSpeciesClick: (String) -> Unit) {
    val viewModel: DiscoverViewModel = viewModel(factory = DiscoverViewModel.factory(repository))
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showFilters by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item { ScreenHeader("Discover", "Explore the offline plant catalogue") }
        item { SearchField(state.query, viewModel::setQuery, "Search common or scientific name") }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { showFilters = true }) {
                    Icon(Icons.Rounded.FilterList, null)
                    Text(if (state.filters.activeCount == 0) " Filters" else " Filters · ${state.filters.activeCount}")
                }
                if (state.filters.activeCount > 0) OutlinedButton(onClick = viewModel::clearFilters) { Text("Clear") }
            }
        }
        item { Text("${state.species.size} plants", style = MaterialTheme.typography.titleMedium) }
        if (state.species.isEmpty()) {
            item { EmptyState("No matches", "Try clearing a filter or searching for another plant.", "Clear search and filters", viewModel::clearAll) }
        } else {
            items(state.species, key = { it.speciesId }) { species ->
                SpeciesCard(
                    species = species,
                    favourite = species.speciesId in state.favouriteIds,
                    onClick = { onSpeciesClick(species.speciesId) },
                    onFavourite = { viewModel.toggleFavourite(species.speciesId) },
                )
            }
        }
    }

    if (showFilters) {
        FilterSheet(
            filters = state.filters,
            onFiltersChanged = viewModel::setFilters,
            onDismiss = { showFilters = false },
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun FilterSheet(filters: SpeciesFilters, onFiltersChanged: (SpeciesFilters) -> Unit, onDismiss: () -> Unit) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(Modifier.padding(horizontal = 20.dp).padding(bottom = 32.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Text("Filter plants", style = MaterialTheme.typography.headlineMedium)
            FilterGroup("Light", LightLevel.entries, filters.light, { stringResource(it.labelResource()) }) { onFiltersChanged(filters.copy(light = it)) }
            FilterGroup("Difficulty", (1..5).toList(), filters.difficulty, { "Level $it" }) { onFiltersChanged(filters.copy(difficulty = it)) }
            FilterGroup(
                "Pet safety",
                listOf(false, true),
                filters.toxicToPets,
                { stringResource(if (it) R.string.pet_toxic else R.string.pet_safe) },
            ) { onFiltersChanged(filters.copy(toxicToPets = it)) }
            FilterGroup("Growth habit", GrowthHabit.entries, filters.growthHabit, { stringResource(it.labelResource()) }) { onFiltersChanged(filters.copy(growthHabit = it)) }
            FilterGroup("Humidity", HumidityLevel.entries, filters.humidity, { stringResource(it.labelResource()) }) { onFiltersChanged(filters.copy(humidity = it)) }
            FilterGroup("Mature size", MatureSize.entries, filters.matureSize, { stringResource(it.labelResource()) }) { onFiltersChanged(filters.copy(matureSize = it)) }
            Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) { Text("Show results") }
        }
    }
}

@Composable
private fun <T> FilterGroup(title: String, options: List<T>, selected: T?, label: @Composable (T) -> String, onSelect: (T?) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(title, style = MaterialTheme.typography.titleMedium)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            options.forEach { option ->
                FilterChip(selected = option == selected, onClick = { onSelect(if (option == selected) null else option) }, label = { Text(label(option)) })
            }
        }
    }
}
