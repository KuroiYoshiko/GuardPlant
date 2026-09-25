package com.kuroi.guardplant.core.model

import java.time.LocalDate

enum class HumidityLevel { AVERAGE, MODERATE, HIGH }

enum class MatureSize { COMPACT, MEDIUM, LARGE }

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
