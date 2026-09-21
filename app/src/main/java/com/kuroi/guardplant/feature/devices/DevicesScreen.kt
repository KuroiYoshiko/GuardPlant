package com.kuroi.guardplant.feature.devices

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Sensors
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kuroi.guardplant.feature.details.DetailScaffold

@Composable
fun DevicesScreen(onBack: () -> Unit) {
    var scanned by rememberSaveable { mutableStateOf(false) }
    DetailScaffold("Devices", onBack) {
        Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Rounded.Sensors, null, Modifier.padding(top = 36.dp).size(72.dp), tint = MaterialTheme.colorScheme.primary)
            Text("Smart watering, later", style = MaterialTheme.typography.headlineMedium)
            Text("Device setup is a visual placeholder. No Bluetooth, Wi-Fi, discovery, or hardware communication is active.", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Button(onClick = { scanned = true }, modifier = Modifier.fillMaxWidth()) { Text("Run mock device scan") }
            if (scanned) Card(Modifier.fillMaxWidth()) { Column(Modifier.padding(18.dp)) { Text("No demo devices found", style = MaterialTheme.typography.titleLarge); Text("That’s expected—this screen performs no real scan.") } }
        }
    }
}
