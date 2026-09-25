package com.kuroi.guardplant.feature.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Pets
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kuroi.guardplant.R
import com.kuroi.guardplant.core.data.PlantRepository
import com.kuroi.guardplant.core.data.SpeciesRepository
import com.kuroi.guardplant.core.designsystem.component.EmptyState
import com.kuroi.guardplant.core.designsystem.component.PlantArtwork
import com.kuroi.guardplant.core.designsystem.component.SpeciesArtwork
import com.kuroi.guardplant.core.model.Species
import com.kuroi.guardplant.core.presentation.fertilizingText
import com.kuroi.guardplant.core.presentation.labelResource
import com.kuroi.guardplant.core.presentation.repottingText
import com.kuroi.guardplant.core.presentation.wateringText
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class SpeciesDetailUiState(
    val species: Species? = null,
    val isFavourite: Boolean = false,
)

class SpeciesDetailViewModel(
    private val speciesId: String,
    private val repository: SpeciesRepository,
) : ViewModel() {
    val uiState = combine(repository.species, repository.favouriteSpeciesIds) { _, favourites ->
        SpeciesDetailUiState(
            species = repository.getSpeciesById(speciesId),
            isFavourite = speciesId in favourites,
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        SpeciesDetailUiState(
            species = repository.getSpeciesById(speciesId),
            isFavourite = speciesId in repository.favouriteSpeciesIds.value,
        ),
    )

    fun toggleFavourite() = repository.toggleFavourite(speciesId)

    companion object {
        fun factory(speciesId: String, repository: SpeciesRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T =
                    SpeciesDetailViewModel(speciesId, repository) as T
            }
    }
}

@Composable
fun SpeciesDetailScreen(speciesId: String, repository: SpeciesRepository, onBack: () -> Unit, onAddPlant: () -> Unit) {
    val viewModel: SpeciesDetailViewModel = viewModel(factory = SpeciesDetailViewModel.factory(speciesId, repository))
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val species = state.species
    DetailScaffold("Species details", onBack) {
        if (species == null) {
            EmptyState("Plant not found", "This species is not in the local catalogue.")
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                item { SpeciesArtwork(species.speciesId, Modifier.fillMaxWidth().height(220.dp), species.commonNames.first()) }
                item {
                    Row {
                        Column(Modifier.weight(1f)) {
                            Text(species.commonNames.first(), style = MaterialTheme.typography.headlineLarge)
                            Text(species.scientificName, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(species.commonNames.drop(1).joinToString(), style = MaterialTheme.typography.bodyMedium)
                        }
                        IconButton(onClick = viewModel::toggleFavourite) {
                            Icon(if (state.isFavourite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder, "Toggle favourite")
                        }
                    }
                }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        SummaryCard("Difficulty", "${species.care.difficulty} / 5", Modifier.weight(1f))
                        SummaryCard(
                            "Pet safety",
                            stringResource(if (species.toxicity.toxicToPets) R.string.pet_toxic else R.string.pet_safe),
                            Modifier.weight(1f),
                        )
                    }
                }
                item { CareInfo(species) }
                item { Button(onClick = onAddPlant, modifier = Modifier.fillMaxWidth()) { Text("Add to My Plants") } }
            }
        }
    }
}

@Composable
private fun CareInfo(species: Species) {
    val resources = LocalContext.current.resources
    val height = species.growth.matureHeightCentimeters.let { range ->
        if (range.minimum == range.maximum) {
            resources.getString(R.string.height_exact, range.minimum)
        } else {
            resources.getString(R.string.height_range, range.minimum, range.maximum)
        }
    }
    Card {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Text("Care guide", style = MaterialTheme.typography.titleLarge)
            InfoRow("Light", resources.getString(species.care.light.labelResource()))
            InfoRow("Watering", wateringText(resources, species.care.watering))
            InfoRow(
                "Humidity",
                resources.getString(
                    R.string.humidity_range,
                    species.care.humidity.minimumPercent,
                    species.care.humidity.maximumPercent,
                ),
            )
            InfoRow(
                "Temperature",
                resources.getString(
                    R.string.temperature_range,
                    species.care.temperature.minimumCelsius,
                    species.care.temperature.maximumCelsius,
                ),
            )
            InfoRow("Soil", species.care.soil.type)
            InfoRow("Soil components", resources.getString(R.string.soil_components, species.care.soil.components.joinToString()))
            InfoRow("Growth rate", resources.getString(species.growth.rate.labelResource()))
            InfoRow("Growth habit", resources.getString(species.growth.habit.labelResource()))
            InfoRow("Mature height", height)
            InfoRow(
                "Pet toxicity",
                resources.getString(
                    if (species.toxicity.toxicToPets) R.string.pet_toxic_guidance else R.string.pet_not_toxic_guidance,
                ),
            )
            InfoRow("Repotting", repottingText(resources, species.growth.repotting))
            InfoRow("Fertilizing", fertilizingText(resources, species.care.fertilizing))
        }
    }
}

@Composable
private fun SummaryCard(label: String, value: String, modifier: Modifier) {
    Card(modifier) { Column(Modifier.padding(14.dp)) { Text(label, style = MaterialTheme.typography.bodyMedium); Text(value, style = MaterialTheme.typography.titleMedium) } }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Column { Text(label, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary); Text(value, style = MaterialTheme.typography.bodyLarge) }
}

@Composable
fun PlantDetailScreen(plantId: String, plantRepository: PlantRepository, speciesRepository: SpeciesRepository, onBack: () -> Unit) {
    val plants by plantRepository.plants.collectAsStateWithLifecycle()
    val species by speciesRepository.species.collectAsStateWithLifecycle()
    val plant = plants.firstOrNull { it.plantId == plantId }
    DetailScaffold("Plant details", onBack) {
        if (plant == null) EmptyState("Plant not found", "This mock plant is no longer in your collection.") else {
            val linked = species.firstOrNull { it.speciesId == plant.speciesId }
            Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                PlantArtwork(plant.accent, Modifier.fillMaxWidth().height(250.dp), plant.displayName)
                Text(plant.displayName, style = MaterialTheme.typography.headlineLarge)
                Text(linked?.scientificName ?: "Custom plant", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleMedium)
                Card { Column(Modifier.padding(18.dp)) { Text("Notes", style = MaterialTheme.typography.titleLarge); Spacer(Modifier.height(8.dp)); Text(plant.note) } }
                Card { Column(Modifier.padding(18.dp)) { Text("Care history", style = MaterialTheme.typography.titleLarge); Spacer(Modifier.height(8.dp)); Text("No recorded events in this non-persistent preview.") } }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DetailScaffold(title: String, onBack: () -> Unit, content: @Composable () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Back") } },
            )
        },
    ) { padding -> Column(Modifier.padding(padding)) { content() } }
}
