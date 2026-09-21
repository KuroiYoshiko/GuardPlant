package com.kuroi.guardplant.core.model

import java.time.LocalDate

enum class LightLevel(val label: String) {
    LOW("Low light"),
    MEDIUM("Medium indirect"),
    BRIGHT("Bright indirect"),
    DIRECT("Some direct sun"),
}

enum class HumidityLevel(val label: String) {
    AVERAGE("Average"),
    MODERATE("Moderate"),
    HIGH("High"),
}

enum class GrowthHabit(val label: String) {
    UPRIGHT("Upright"),
    TRAILING("Trailing"),
    CLIMBING("Climbing"),
    ROSETTE("Rosette"),
}

enum class MatureSize(val label: String) {
    COMPACT("Compact"),
    MEDIUM("Medium"),
    LARGE("Large"),
}

data class Species(
    val speciesId: String,
    val scientificName: String,
    val commonNames: List<String>,
    val difficulty: Int,
    val light: LightLevel,
    val watering: String,
    val humidity: HumidityLevel,
    val temperature: String,
    val soil: String,
    val growthHabit: GrowthHabit,
    val matureSize: MatureSize,
    val toxicToPets: Boolean,
    val repotting: String,
    val fertilizing: String,
    val accent: Long,
)

data class PlantRoom(
    val roomId: String,
    val name: String,
)

data class UserPlant(
    val plantId: String,
    val displayName: String,
    val speciesId: String?,
    val roomId: String?,
    val note: String,
    val accent: Long,
)

enum class CareType(val label: String) {
    WATER("Water"),
    CHECK("Check soil"),
    FERTILIZE("Fertilize"),
}

data class CareTask(
    val taskId: String,
    val plantId: String,
    val plantName: String,
    val type: CareType,
    val dueDate: LocalDate,
    val completed: Boolean = false,
)

enum class CareAction {
    WATERED,
    STILL_MOIST,
    REMIND_LATER,
}

data class IdentificationResult(
    val speciesId: String,
    val confidence: Int,
)

data class SpeciesFilters(
    val light: LightLevel? = null,
    val difficulty: Int? = null,
    val toxicToPets: Boolean? = null,
    val growthHabit: GrowthHabit? = null,
    val humidity: HumidityLevel? = null,
    val matureSize: MatureSize? = null,
) {
    val activeCount: Int
        get() = listOf(light, difficulty, toxicToPets, growthHabit, humidity, matureSize).count { it != null }
}
