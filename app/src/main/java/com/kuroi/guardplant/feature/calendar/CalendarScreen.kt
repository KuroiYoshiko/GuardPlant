package com.kuroi.guardplant.feature.calendar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuroi.guardplant.core.data.CareRepository
import com.kuroi.guardplant.core.data.fake.DemoToday
import com.kuroi.guardplant.core.designsystem.component.CareTaskCard
import com.kuroi.guardplant.core.designsystem.component.EmptyState
import com.kuroi.guardplant.core.designsystem.component.ScreenHeader
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@Composable
fun CalendarScreen(careRepository: CareRepository) {
    val tasks by careRepository.tasks.collectAsStateWithLifecycle()
    var monthOffset by rememberSaveable { mutableIntStateOf(0) }
    var selectedEpochDay by rememberSaveable { mutableLongStateOf(DemoToday.toEpochDay()) }
    val selectedDate = LocalDate.ofEpochDay(selectedEpochDay)
    val month = YearMonth.from(DemoToday).plusMonths(monthOffset.toLong())
    val firstDayOffset = month.atDay(1).dayOfWeek.value - 1
    val selectedTasks = tasks.filter { it.dueDate == selectedDate }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item { ScreenHeader("Care calendar", "Plan and review your month at a glance.") }
        item {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                IconButton(onClick = { monthOffset-- }) { Icon(Icons.Rounded.ChevronLeft, "Previous month") }
                Text(month.format(DateTimeFormatter.ofPattern("MMMM yyyy")), style = MaterialTheme.typography.titleLarge)
                IconButton(onClick = { monthOffset++ }) { Icon(Icons.Rounded.ChevronRight, "Next month") }
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(Modifier.fillMaxWidth()) {
                    listOf("M", "T", "W", "T", "F", "S", "S").forEach { Text(it, Modifier.weight(1f), textAlign = androidx.compose.ui.text.style.TextAlign.Center, fontWeight = FontWeight.Bold) }
                }
                val cells = firstDayOffset + month.lengthOfMonth()
                repeat((cells + 6) / 7) { week ->
                    Row(Modifier.fillMaxWidth()) {
                        repeat(7) { column ->
                            val day = week * 7 + column - firstDayOffset + 1
                            if (day in 1..month.lengthOfMonth()) {
                                val date = month.atDay(day)
                                val hasTask = tasks.any { it.dueDate == date }
                                val selected = date == selectedDate
                                Surface(
                                    shape = CircleShape,
                                    color = if (selected) MaterialTheme.colorScheme.primary else androidx.compose.ui.graphics.Color.Transparent,
                                    modifier = Modifier.weight(1f).aspectRatio(1f).padding(3.dp).clickable { selectedEpochDay = date.toEpochDay() },
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(day.toString(), color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface)
                                            if (hasTask) Text("•", color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            } else Box(Modifier.weight(1f).aspectRatio(1f))
                        }
                    }
                }
            }
        }
        item { Text(selectedDate.format(DateTimeFormatter.ofPattern("EEEE, MMMM d")), style = MaterialTheme.typography.titleLarge) }
        if (selectedTasks.isEmpty()) {
            item { EmptyState("No care scheduled", "Select a marked day to see its tasks.") }
        } else {
            items(selectedTasks, key = { it.taskId }) { CareTaskCard(it, DemoToday, false) {} }
        }
    }
}
