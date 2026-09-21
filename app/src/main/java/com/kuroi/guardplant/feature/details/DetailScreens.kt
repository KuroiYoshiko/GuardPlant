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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuroi.guardplant.core.data.PlantRepository
import com.kuroi.guardplant.core.data.SpeciesRepository
import com.kuroi.guardplant.core.designsystem.component.EmptyState
import com.kuroi.guardplant.core.designsystem.component.PlantArtwork
import com.kuroi.guardplant.core.model.Species

@Composable
fun SpeciesDetailScreen(speciesId: String, repository: SpeciesRepository, onBack: () -> Unit, onAddPlant: () -> Unit) {
    val speciesList by repository.species.collectAsStateWithLifecycle()
    val favourites by repository.favouriteSpeciesIds.collectAsStateWithLifecycle()
    val species = speciesList.firstOrNull { it.speciesId == speciesId }
    DetailScaffold("Species details", onBack) {
        if (species == null) {
            EmptyState("Plant not found", "This demo species is not in the curated shell catalogue.")
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                item { PlantArtwork(species.accent, Modifier.fillMaxWidth().height(220.dp), species.commonNames.first()) }
                item {
                    Row {
                        Column(Modifier.weight(1f)) {
                            Text(species.commonNames.first(), style = MaterialTheme.typography.headlineLarge)
                            Text(species.scientificName, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(species.commonNames.drop(1).joinToString(), style = MaterialTheme.typography.bodyMedium)
                        }
                        IconButton(onClick = { repository.toggleFavourite(species.speciesId) }) {
                            Icon(if (species.speciesId in favourites) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder, "Toggle favourite")
                        }
                    }
                }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        SummaryCard("Difficulty", "${species.difficulty} / 5", Modifier.weight(1f))
                        SummaryCard("Pet safety", if (species.toxicToPets) "Toxic" else "Pet safe", Modifier.weight(1f))
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
    Card {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Text("Care guide", style = MaterialTheme.typography.titleLarge)
            InfoRow("Light", species.light.label)
            InfoRow("Watering", species.watering)
            InfoRow("Humidity", species.humidity.label)
            InfoRow("Temperature", species.temperature)
            InfoRow("Soil", species.soil)
            InfoRow("Growth habit", species.growthHabit.label)
            InfoRow("Mature size", species.matureSize.label)
            InfoRow("Pet toxicity", if (species.toxicToPets) "Toxic to pets" else "Not toxic to pets")
            InfoRow("Repotting", species.repotting)
            InfoRow("Fertilizing", species.fertilizing)
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
