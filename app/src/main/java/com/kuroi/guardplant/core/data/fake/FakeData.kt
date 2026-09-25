package com.kuroi.guardplant.core.data.fake

import com.kuroi.guardplant.core.model.CareTask
import com.kuroi.guardplant.core.model.CareType
import com.kuroi.guardplant.core.model.IdentificationResult
import com.kuroi.guardplant.core.model.PlantRoom
import com.kuroi.guardplant.core.model.UserPlant
import java.time.LocalDate

val DemoToday: LocalDate = LocalDate.of(2026, 9, 21)

object FakeData {
    val rooms = listOf(
        PlantRoom("living_room", "Living room"),
        PlantRoom("bedroom", "Bedroom"),
        PlantRoom("studio", "Studio"),
    )

    val plants = listOf(
        UserPlant("plant_monty", "Monty", "monstera_deliciosa", "living_room", "New leaf unfurling", 0xFF3F7D55),
        UserPlant("plant_pip", "Pip", "epipremnum_aureum", "living_room", "Trailing from the bookshelf", 0xFF7C9A3D),
        UserPlant("plant_sylvie", "Sylvie", "dracaena_trifasciata", "bedroom", "Rotate next week", 0xFF4F6B3C),
        UserPlant("plant_orbit", "Orbit", "goeppertia_orbifolia", "studio", "Likes the humidifier", 0xFF5E8C78),
        UserPlant("plant_cutting", "Mystery cutting", null, null, "Custom plant · identify later", 0xFFB47A52),
    )

    val careTasks = listOf(
        CareTask("task_1", "plant_monty", "Monty", CareType.WATER, DemoToday.minusDays(2)),
        CareTask("task_2", "plant_orbit", "Orbit", CareType.CHECK, DemoToday.minusDays(1)),
        CareTask("task_3", "plant_pip", "Pip", CareType.WATER, DemoToday),
        CareTask("task_4", "plant_sylvie", "Sylvie", CareType.CHECK, DemoToday),
        CareTask("task_5", "plant_monty", "Monty", CareType.FERTILIZE, DemoToday.plusDays(3)),
        CareTask("task_6", "plant_cutting", "Mystery cutting", CareType.CHECK, DemoToday.plusDays(7)),
    )

    val identificationResults = listOf(
        IdentificationResult("monstera_deliciosa", 87),
        IdentificationResult("epipremnum_aureum", 8),
        IdentificationResult("goeppertia_orbifolia", 3),
    )
}
