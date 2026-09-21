package com.kuroi.guardplant.feature.scan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.PhotoLibrary
import androidx.compose.material.icons.rounded.SearchOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.kuroi.guardplant.core.data.fake.FakeData
import com.kuroi.guardplant.core.designsystem.component.ScreenHeader

@Composable
fun ScanScreen(onSpeciesClick: (String) -> Unit) {
    var state by rememberSaveable { mutableIntStateOf(0) }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item { ScreenHeader("Identify a plant", "Camera and model inference are simulated in this shell.") }
        item {
            Box(
                modifier = Modifier.fillMaxWidth().height(310.dp).clip(MaterialTheme.shapes.extraLarge).background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Rounded.CameraAlt, null, Modifier.size(72.dp), tint = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.height(12.dp))
                    Text(if (state == 0) "Place a leaf in the frame" else "Demo image captured", style = MaterialTheme.typography.titleLarge)
                    Text("No camera permission is requested", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(onClick = { state = 1 }, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Rounded.CameraAlt, null); Spacer(Modifier.size(6.dp)); Text("Take photo")
                }
                FilledTonalButton(onClick = { state = 1 }, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Rounded.PhotoLibrary, null); Spacer(Modifier.size(6.dp)); Text("Gallery")
                }
            }
        }
        if (state == 1) {
            item { Text("Top 3 matches", style = MaterialTheme.typography.titleLarge) }
            FakeData.identificationResults.forEachIndexed { index, result ->
                item(key = result.speciesId) {
                    Card(onClick = { onSpeciesClick(result.speciesId) }, modifier = Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("${index + 1}", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
                                Spacer(Modifier.size(12.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(result.speciesId.replace('_', ' ').replaceFirstChar { it.uppercase() }, style = MaterialTheme.typography.titleMedium)
                                    Text("${result.confidence}% confidence", color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                            Spacer(Modifier.height(10.dp))
                            LinearProgressIndicator(progress = { result.confidence / 100f }, modifier = Modifier.fillMaxWidth())
                        }
                    }
                }
            }
            item {
                OutlinedButton(onClick = { state = 2 }, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Rounded.SearchOff, null); Spacer(Modifier.size(8.dp)); Text("None of these")
                }
            }
        }
        if (state == 2) {
            item {
                Card {
                    Column(Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("No match selected", style = MaterialTheme.typography.titleLarge)
                        Text("You can add this as a custom plant instead. This action is only a preview.", Modifier.padding(vertical = 8.dp))
                        Button(onClick = { state = 0 }) { Text("Try another photo") }
                    }
                }
            }
        }
    }
}
