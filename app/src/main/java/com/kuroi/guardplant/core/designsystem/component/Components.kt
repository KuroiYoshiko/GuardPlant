package com.kuroi.guardplant.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.LocalFlorist
import androidx.compose.material.icons.rounded.Pets
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kuroi.guardplant.core.designsystem.theme.Amber
import com.kuroi.guardplant.core.model.CareAction
import com.kuroi.guardplant.core.model.CareTask
import com.kuroi.guardplant.core.model.Species
import com.kuroi.guardplant.core.model.UserPlant
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ScreenHeader(
    title: String,
    subtitle: String? = null,
    action: (@Composable () -> Unit)? = null,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.headlineLarge)
            if (subtitle != null) {
                Spacer(Modifier.height(4.dp))
                Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        if (action != null) action()
    }
}

@Composable
fun SectionHeader(title: String, supporting: String? = null) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(title, style = MaterialTheme.typography.titleLarge)
        if (supporting != null) Text(supporting, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
fun PlantArtwork(accent: Long, modifier: Modifier = Modifier, label: String = "Plant") {
    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.large)
            .background(Color(accent).copy(alpha = 0.2f)),
        contentAlignment = Alignment.Center,
    ) {
        Surface(color = Color(accent), shape = CircleShape, modifier = Modifier.size(56.dp)) {
            Box(contentAlignment = Alignment.Center) {
                Icon(Icons.Rounded.LocalFlorist, contentDescription = label, tint = Color.White, modifier = Modifier.size(30.dp))
            }
        }
    }
}

@Composable
fun SearchField(value: String, onValueChange: (String) -> Unit, placeholder: String) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null) },
        placeholder = { Text(placeholder) },
        shape = MaterialTheme.shapes.large,
    )
}

@Composable
fun SpeciesCard(
    species: Species,
    favourite: Boolean,
    onClick: () -> Unit,
    onFavourite: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            PlantArtwork(species.accent, Modifier.size(82.dp), species.commonNames.first())
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(species.commonNames.first(), style = MaterialTheme.typography.titleMedium)
                Text(species.scientificName, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    AssistChip(onClick = {}, label = { Text("Level ${species.difficulty}") })
                    if (!species.toxicToPets) {
                        AssistChip(onClick = {}, label = { Text("Pet safe") }, leadingIcon = { Icon(Icons.Rounded.Pets, null, Modifier.size(16.dp)) })
                    }
                }
            }
            IconButton(onClick = onFavourite) {
                Icon(
                    if (favourite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                    contentDescription = if (favourite) "Remove from favourites" else "Add to favourites",
                    tint = if (favourite) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
fun PlantCard(plant: UserPlant, speciesName: String?, onClick: () -> Unit) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            PlantArtwork(plant.accent, Modifier.size(68.dp), plant.displayName)
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(plant.displayName, style = MaterialTheme.typography.titleMedium)
                Text(speciesName ?: "Custom plant", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
                Text(plant.note, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            Icon(Icons.Rounded.ChevronRight, contentDescription = "Open plant")
        }
    }
}

@Composable
fun CareTaskCard(
    task: CareTask,
    today: LocalDate,
    showActions: Boolean,
    onAction: (CareAction) -> Unit,
) {
    val overdue = task.dueDate.isBefore(today) && !task.completed
    val container = when {
        task.completed -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f)
        overdue -> MaterialTheme.colorScheme.errorContainer
        else -> MaterialTheme.colorScheme.surface
    }
    Card(colors = CardDefaults.cardColors(containerColor = container), modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    color = when {
                        task.completed -> MaterialTheme.colorScheme.primary
                        overdue -> MaterialTheme.colorScheme.error
                        else -> Amber
                    },
                    shape = CircleShape,
                    modifier = Modifier.size(38.dp),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            if (task.completed) Icons.Rounded.CheckCircle else Icons.Rounded.LocalFlorist,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(21.dp),
                        )
                    }
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text("${task.type.label} ${task.plantName}", style = MaterialTheme.typography.titleMedium)
                    val dueText = when {
                        task.completed -> "Completed"
                        overdue -> "Overdue · ${task.dueDate.format(DateTimeFormatter.ofPattern("MMM d"))}"
                        task.dueDate == today -> "Due today"
                        else -> task.dueDate.format(DateTimeFormatter.ofPattern("EEE, MMM d"))
                    }
                    Text(
                        dueText,
                        color = if (overdue) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = if (overdue) FontWeight.Bold else FontWeight.Normal,
                    )
                }
            }
            if (showActions && !task.completed) {
                Spacer(Modifier.height(14.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { onAction(CareAction.WATERED) }, modifier = Modifier.weight(1f)) { Text("Watered") }
                    FilledTonalButton(onClick = { onAction(CareAction.STILL_MOIST) }, modifier = Modifier.weight(1f)) { Text("Still moist") }
                }
                Spacer(Modifier.height(8.dp))
                FilledTonalButton(onClick = { onAction(CareAction.REMIND_LATER) }, modifier = Modifier.fillMaxWidth()) {
                    Text("Remind later")
                }
            }
        }
    }
}

@Composable
fun EmptyState(title: String, message: String, actionLabel: String? = null, onAction: (() -> Unit)? = null) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 36.dp, horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primaryContainer, modifier = Modifier.size(72.dp)) {
            Box(contentAlignment = Alignment.Center) { Icon(Icons.Rounded.LocalFlorist, null, Modifier.size(34.dp)) }
        }
        Spacer(Modifier.height(16.dp))
        Text(title, style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(6.dp))
        Text(message, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        if (actionLabel != null && onAction != null) {
            Spacer(Modifier.height(16.dp))
            Button(onClick = onAction) { Icon(Icons.Rounded.Add, null); Spacer(Modifier.width(6.dp)); Text(actionLabel) }
        }
    }
}
