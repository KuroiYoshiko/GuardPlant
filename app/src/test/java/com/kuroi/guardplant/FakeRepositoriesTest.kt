package com.kuroi.guardplant

import com.kuroi.guardplant.core.data.fake.DemoToday
import com.kuroi.guardplant.core.data.fake.FakeCareRepository
import com.kuroi.guardplant.core.data.fake.FakePlantRepository
import com.kuroi.guardplant.core.model.CareAction
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class FakeRepositoriesTest {
    @Test
    fun stillMoistDefersTaskByTwoDays() {
        val repository = FakeCareRepository()

        repository.applyAction("task_1", CareAction.STILL_MOIST)

        assertEquals(DemoToday.plusDays(2), repository.tasks.value.first { it.taskId == "task_1" }.dueDate)
    }

    @Test
    fun wateredCompletesTask() {
        val repository = FakeCareRepository()

        repository.applyAction("task_3", CareAction.WATERED)

        assertTrue(repository.tasks.value.first { it.taskId == "task_3" }.completed)
    }

    @Test
    fun customPlantHasNoCatalogueLinkAndIsUnassigned() {
        val repository = FakePlantRepository()

        repository.addDemoPlant(custom = true, name = "Fern friend")

        val plant = repository.plants.value.first { it.plantId == "plant_custom_demo" }
        assertEquals("Fern friend", plant.displayName)
        assertNull(plant.speciesId)
        assertNull(plant.roomId)
    }
}
