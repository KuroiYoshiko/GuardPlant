package com.kuroi.guardplant.feature.addplant

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kuroi.guardplant.core.data.PlantRepository
import com.kuroi.guardplant.feature.details.DetailScaffold

@Composable
fun AddPlantScreen(plantRepository: PlantRepository, onBack: () -> Unit) {
    var custom by rememberSaveable { mutableStateOf(false) }
    var name by rememberSaveable { mutableStateOf("") }
    var added by rememberSaveable { mutableStateOf(false) }
    DetailScaffold("Add plant", onBack) {
        Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("How would you like to add it?", style = MaterialTheme.typography.headlineMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(selected = !custom, onClick = { custom = false }, label = { Text("From catalogue") })
                FilterChip(selected = custom, onClick = { custom = true }, label = { Text("Custom plant") })
            }
            Card { Text(if (custom) "Custom plants work without a catalogue link." else "Spider plant is selected for this deterministic demo.", Modifier.padding(16.dp)) }
            OutlinedTextField(name, { name = it }, Modifier.fillMaxWidth(), label = { Text("Plant nickname") }, placeholder = { Text(if (custom) "My mystery plant" else "Scout") })
            Button(
                onClick = { plantRepository.addDemoPlant(custom, name); added = true },
                modifier = Modifier.fillMaxWidth(),
                enabled = !added,
            ) { Text(if (added) "Added to Unassigned" else "Add demo plant") }
            if (added) {
                Text("Added for this session only. Return to My Plants to see it in Unassigned.", color = MaterialTheme.colorScheme.primary)
                Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Done") }
            }
        }
    }
}
