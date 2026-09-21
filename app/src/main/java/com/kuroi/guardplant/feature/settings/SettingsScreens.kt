package com.kuroi.guardplant.feature.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Backup
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.Devices
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.PrivacyTip
import androidx.compose.material.icons.rounded.Straighten
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.kuroi.guardplant.ThemePreference
import com.kuroi.guardplant.feature.details.DetailScaffold

private data class SettingItem(val title: String, val subtitle: String, val icon: ImageVector)

@Composable
fun SettingsScreen(
    themePreference: ThemePreference,
    onThemePreferenceChanged: (ThemePreference) -> Unit,
    onDevices: () -> Unit,
    onBack: () -> Unit,
) {
    var lastMockAction by rememberSaveable { mutableStateOf<String?>(null) }
    val items = listOf(
        SettingItem("Language", "English · mock", Icons.Rounded.Language),
        SettingItem("Notifications", "Not scheduled in this shell", Icons.Rounded.Notifications),
        SettingItem("Units", "Metric · mock", Icons.Rounded.Straighten),
        SettingItem("Backup / Import", "Local preview only", Icons.Rounded.Backup),
        SettingItem("Privacy", "All demo data stays in memory", Icons.Rounded.PrivacyTip),
        SettingItem("About", "Guard Plant app shell", Icons.Rounded.Info),
    )
    DetailScaffold("Settings", onBack) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                Text("Appearance", style = MaterialTheme.typography.titleLarge)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 8.dp)) {
                    ThemePreference.entries.forEach { mode ->
                        FilterChip(selected = themePreference == mode, onClick = { onThemePreferenceChanged(mode) }, label = { Text(mode.name.lowercase().replaceFirstChar { it.uppercase() }) })
                    }
                }
                Text("Theme choice is not persisted yet.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            item {
                SettingRow("Devices", "Future smart-watering devices · mock", Icons.Rounded.Devices, onDevices)
            }
            items(items, key = { it.title }) { item ->
                SettingRow(item.title, item.subtitle, item.icon) { lastMockAction = "${item.title} is a non-persistent preview." }
            }
            if (lastMockAction != null) item { Card { Text(lastMockAction.orEmpty(), Modifier.padding(16.dp), color = MaterialTheme.colorScheme.primary) } }
        }
    }
}

@Composable
private fun SettingRow(title: String, subtitle: String, icon: ImageVector, onClick: () -> Unit) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.size(14.dp))
            Column(Modifier.weight(1f)) { Text(title, style = MaterialTheme.typography.titleMedium); Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant) }
            Icon(Icons.Rounded.ChevronRight, "Open $title")
        }
    }
}
