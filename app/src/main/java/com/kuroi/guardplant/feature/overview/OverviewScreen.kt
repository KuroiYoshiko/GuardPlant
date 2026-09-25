package com.kuroi.guardplant.feature.overview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kuroi.guardplant.core.data.CareRepository
import com.kuroi.guardplant.core.data.fake.DemoToday
import com.kuroi.guardplant.core.designsystem.component.CareTaskCard
import com.kuroi.guardplant.core.designsystem.component.EmptyState
import com.kuroi.guardplant.core.designsystem.component.ScreenHeader
import com.kuroi.guardplant.core.designsystem.component.SectionHeader
import com.kuroi.guardplant.core.model.CareAction
import java.time.LocalTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class OverviewViewModel(private val careRepository: CareRepository) : ViewModel() {
    val tasks = careRepository.tasks
    private val _lastAction = MutableStateFlow<String?>(null)
    val lastAction = _lastAction.asStateFlow()

    fun applyAction(taskId: String, plantName: String, action: CareAction) {
        careRepository.applyAction(taskId, action)
        _lastAction.value = when (action) {
            CareAction.WATERED -> "$plantName marked as watered"
            CareAction.STILL_MOIST -> "$plantName moved ahead by two days"
            CareAction.REMIND_LATER -> "$plantName moved to tomorrow"
        }
    }

    companion object {
        fun factory(repository: CareRepository): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T =
                OverviewViewModel(repository) as T
        }
    }
}

@Composable
fun OverviewRoute(careRepository: CareRepository, onSettings: () -> Unit) {
    val viewModel: OverviewViewModel = viewModel(factory = OverviewViewModel.factory(careRepository))
    val tasks by viewModel.tasks.collectAsStateWithLifecycle()
    val lastAction by viewModel.lastAction.collectAsStateWithLifecycle()
    val openTasks = tasks.filterNot { it.completed }
    val overdue = openTasks.filter { it.dueDate.isBefore(DemoToday) }
    val due = openTasks.filter { it.dueDate == DemoToday }
    val upcoming = openTasks.filter { it.dueDate.isAfter(DemoToday) }.sortedBy { it.dueDate }
    val greeting = greetingForHour(LocalTime.now().hour)

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            ScreenHeader(
                title = greeting,
                subtitle = "Here’s what your plants need today.",
                action = { IconButton(onClick = onSettings) { Icon(Icons.Rounded.Settings, "Settings") } },
            )
        }
        if (lastAction != null) {
            item {
                Card { Text(lastAction.orEmpty(), Modifier.padding(14.dp), color = MaterialTheme.colorScheme.primary) }
            }
        }
        item {
            Card {
                Column(Modifier.padding(18.dp)) {
                    Text("Today at a glance", style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(8.dp))
                    Text("${overdue.size} overdue  ·  ${due.size} due today  ·  ${upcoming.size} upcoming")
                }
            }
        }
        if (overdue.isNotEmpty()) {
            item { SectionHeader("Overdue", "Needs attention") }
            items(overdue, key = { it.taskId }) { task ->
                CareTaskCard(task, DemoToday, true) { viewModel.applyAction(task.taskId, task.plantName, it) }
            }
        }
        if (due.isNotEmpty()) {
            item { SectionHeader("Due today", "Quick care") }
            items(due, key = { it.taskId }) { task ->
                CareTaskCard(task, DemoToday, true) { viewModel.applyAction(task.taskId, task.plantName, it) }
            }
        }
        if (overdue.isEmpty() && due.isEmpty()) {
            item { EmptyState("All caught up", "Nothing needs attention today. Enjoy your plants!") }
        }
        item { SectionHeader("Coming up") }
        if (upcoming.isEmpty()) {
            item { EmptyState("A quiet week", "Upcoming care tasks will appear here.") }
        } else {
            items(upcoming, key = { it.taskId }) { task -> CareTaskCard(task, DemoToday, false) {} }
        }
        item {
            Card {
                Column(Modifier.padding(18.dp)) {
                    Text("Smart watering", style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(6.dp))
                    Text("Device insights are coming later. Guard Plant remains fully useful offline without hardware.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        item { Spacer(Modifier.height(12.dp)) }
    }
}

internal fun greetingForHour(hour: Int): String = when (hour) {
    in 5..11 -> "Good morning"
    in 12..17 -> "Good afternoon"
    else -> "Good evening"
}
