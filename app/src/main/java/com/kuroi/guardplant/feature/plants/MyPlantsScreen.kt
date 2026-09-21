package com.kuroi.guardplant.feature.plants

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuroi.guardplant.core.data.PlantRepository
import com.kuroi.guardplant.core.data.SpeciesRepository
import com.kuroi.guardplant.core.designsystem.component.EmptyState
import com.kuroi.guardplant.core.designsystem.component.PlantCard
import com.kuroi.guardplant.core.designsystem.component.ScreenHeader
import com.kuroi.guardplant.core.designsystem.component.SearchField
import com.kuroi.guardplant.core.model.UserPlant

@Composable
fun MyPlantsScreen(
    plantRepository: PlantRepository,
    speciesRepository: SpeciesRepository,
    onPlantClick: (String) -> Unit,
    onAddPlant: () -> Unit,
) {
    val rooms by plantRepository.rooms.collectAsStateWithLifecycle()
    val plants by plantRepository.plants.collectAsStateWithLifecycle()
    val species by speciesRepository.species.collectAsStateWithLifecycle()
    var search by rememberSaveable { mutableStateOf("") }
    var collapsed by rememberSaveable { mutableStateOf(emptyList<String>()) }
    val filtered = plants.filter { it.displayName.contains(search, true) || it.note.contains(search, true) }
    val groups = rooms.map { it.roomId to it.name } + (null to "Unassigned")

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddPlant) { Icon(Icons.Rounded.Add, "Add plant") }
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item { ScreenHeader("My Plants", "${plants.size} plants across ${rooms.size} rooms") }
            item { SearchField(search, { search = it }, "Search your plants") }
            if (filtered.isEmpty()) {
                item { EmptyState("No plants found", "Try a different search, or add a new plant.", "Add plant", onAddPlant) }
            }
            groups.forEach { (roomId, roomName) ->
                val roomPlants: List<UserPlant> = filtered.filter { it.roomId == roomId }
                if (roomPlants.isNotEmpty() || roomId == null) {
                    val groupKey = roomId ?: "unassigned"
                    item(key = "header_$groupKey") {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("$roomName · ${roomPlants.size}", style = androidx.compose.material3.MaterialTheme.typography.titleLarge, modifier = Modifier.weight(1f))
                            IconButton(onClick = {
                                collapsed = if (groupKey in collapsed) collapsed - groupKey else collapsed + groupKey
                            }) {
                                Icon(if (groupKey in collapsed) Icons.Rounded.ExpandMore else Icons.Rounded.ExpandLess, "Toggle $roomName")
                            }
                        }
                    }
                    if (groupKey !in collapsed) {
                        if (roomPlants.isEmpty()) {
                            item(key = "empty_$groupKey") { EmptyState("Nothing unassigned", "Plants without a room will appear here.") }
                        } else {
                            items(roomPlants, key = { it.plantId }) { plant ->
                                val speciesName = species.firstOrNull { it.speciesId == plant.speciesId }?.commonNames?.firstOrNull()
                                PlantCard(plant, speciesName) { onPlantClick(plant.plantId) }
                            }
                        }
                    }
                }
            }
        }
    }
}
