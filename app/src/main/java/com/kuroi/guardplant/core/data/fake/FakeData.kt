package com.kuroi.guardplant.core.data.fake

import com.kuroi.guardplant.core.model.CareTask
import com.kuroi.guardplant.core.model.CareType
import com.kuroi.guardplant.core.model.GrowthHabit
import com.kuroi.guardplant.core.model.HumidityLevel
import com.kuroi.guardplant.core.model.IdentificationResult
import com.kuroi.guardplant.core.model.LightLevel
import com.kuroi.guardplant.core.model.MatureSize
import com.kuroi.guardplant.core.model.PlantRoom
import com.kuroi.guardplant.core.model.Species
import com.kuroi.guardplant.core.model.UserPlant
import java.time.LocalDate

val DemoToday: LocalDate = LocalDate.of(2026, 9, 21)

object FakeData {
    val species = listOf(
        Species(
            "monstera_deliciosa", "Monstera deliciosa", listOf("Swiss cheese plant", "Monstera"),
            2, LightLevel.BRIGHT, "Water when the top 3–5 cm of soil is dry.",
            HumidityLevel.MODERATE, "18–29°C", "Airy, well-draining aroid mix",
            GrowthHabit.CLIMBING, MatureSize.LARGE, true,
            "Every 1–2 years in spring", "Monthly in spring and summer", 0xFF3F7D55,
        ),
        Species(
            "epipremnum_aureum", "Epipremnum aureum", listOf("Golden pothos", "Devil's ivy"),
            1, LightLevel.MEDIUM, "Let the top half of the soil dry before watering.",
            HumidityLevel.AVERAGE, "18–30°C", "Standard mix with added perlite",
            GrowthHabit.TRAILING, MatureSize.MEDIUM, true,
            "When root-bound", "Every 4–6 weeks in the growing season", 0xFF7C9A3D,
        ),
        Species(
            "dracaena_trifasciata", "Dracaena trifasciata", listOf("Snake plant"),
            1, LightLevel.LOW, "Allow the soil to dry completely between waterings.",
            HumidityLevel.AVERAGE, "16–30°C", "Fast-draining succulent mix",
            GrowthHabit.UPRIGHT, MatureSize.MEDIUM, true,
            "Every 2–3 years", "Lightly, twice during spring and summer", 0xFF4F6B3C,
        ),
        Species(
            "chlorophytum_comosum", "Chlorophytum comosum", listOf("Spider plant"),
            1, LightLevel.MEDIUM, "Water when the surface feels dry.",
            HumidityLevel.AVERAGE, "13–27°C", "Loose all-purpose potting mix",
            GrowthHabit.ROSETTE, MatureSize.COMPACT, false,
            "Annually if crowded", "Monthly from spring through early autumn", 0xFF88A95A,
        ),
        Species(
            "goeppertia_orbifolia", "Goeppertia orbifolia", listOf("Calathea orbifolia"),
            4, LightLevel.BRIGHT, "Keep evenly moist, never waterlogged.",
            HumidityLevel.HIGH, "18–27°C", "Moisture-retentive but airy mix",
            GrowthHabit.UPRIGHT, MatureSize.MEDIUM, false,
            "Every 1–2 years", "Half-strength monthly in spring and summer", 0xFF5E8C78,
        ),
        Species(
            "spathiphyllum_wallisii", "Spathiphyllum wallisii", listOf("Peace lily"),
            2, LightLevel.MEDIUM, "Water when leaves begin to soften or the surface dries.",
            HumidityLevel.MODERATE, "18–29°C", "Rich, well-draining potting mix",
            GrowthHabit.UPRIGHT, MatureSize.MEDIUM, true,
            "Every 1–2 years", "Every 6 weeks in spring and summer", 0xFF2F6A54,
        ),
        Species(
            "crassula_ovata", "Crassula ovata", listOf("Jade plant"),
            2, LightLevel.DIRECT, "Soak thoroughly, then let the soil dry fully.",
            HumidityLevel.AVERAGE, "15–24°C", "Gritty cactus and succulent mix",
            GrowthHabit.UPRIGHT, MatureSize.MEDIUM, true,
            "Every 2–3 years", "Every 2 months during active growth", 0xFF6C8F65,
        ),
    )

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
