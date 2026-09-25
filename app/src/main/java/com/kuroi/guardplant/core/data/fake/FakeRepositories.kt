package com.kuroi.guardplant.core.data.fake

import com.kuroi.guardplant.core.data.CareRepository
import com.kuroi.guardplant.core.data.PlantRepository
import com.kuroi.guardplant.core.model.CareAction
import com.kuroi.guardplant.core.model.CareTask
import com.kuroi.guardplant.core.model.PlantRoom
import com.kuroi.guardplant.core.model.UserPlant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FakePlantRepository : PlantRepository {
    private val _rooms = MutableStateFlow(FakeData.rooms)
    override val rooms: StateFlow<List<PlantRoom>> = _rooms.asStateFlow()

    private val _plants = MutableStateFlow(FakeData.plants)
    override val plants: StateFlow<List<UserPlant>> = _plants.asStateFlow()

    override fun addDemoPlant(custom: Boolean, name: String) {
        val id = if (custom) "plant_custom_demo" else "plant_scout"
        if (_plants.value.none { it.plantId == id }) {
            _plants.update {
                it + UserPlant(
                    plantId = id,
                    displayName = name.ifBlank { if (custom) "New custom plant" else "Scout" },
                    speciesId = if (custom) null else "chlorophytum_comosum",
                    roomId = null,
                    note = "Added from the mock flow",
                    accent = if (custom) 0xFFB47A52 else 0xFF88A95A,
                )
            }
        }
    }
}
class FakeCareRepository : CareRepository {
    private val _tasks = MutableStateFlow(FakeData.careTasks)
    override val tasks: StateFlow<List<CareTask>> = _tasks.asStateFlow()

    override fun applyAction(taskId: String, action: CareAction) {
        _tasks.update { tasks ->
            tasks.map { task ->
                if (task.taskId != taskId) task else when (action) {
                    CareAction.WATERED -> task.copy(completed = true)
                    CareAction.STILL_MOIST -> task.copy(dueDate = DemoToday.plusDays(2))
                    CareAction.REMIND_LATER -> task.copy(dueDate = DemoToday.plusDays(1))
                }
            }
        }
    }
}
