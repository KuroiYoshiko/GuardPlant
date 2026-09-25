package com.kuroi.guardplant.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Species(
    @SerialName("id") val speciesId: String,
    val scientificName: String,
    val commonNames: List<String>,
    val care: SpeciesCare,
    val toxicity: SpeciesToxicity,
    val growth: SpeciesGrowth,
)

@Serializable
data class SpeciesCare(
    val difficulty: Int,
    val watering: WateringGuidance,
    val light: LightLevel,
    val soil: SoilGuidance,
    val humidity: HumidityRange,
    val temperature: TemperatureRange,
    val fertilizing: FertilizingGuidance,
)

@Serializable
data class WateringGuidance(
    val mode: WateringMode,
    val trigger: WateringTrigger?,
    val method: WateringMethod,
    val seasonalContext: SeasonalContext?,
    val intervalDays: Int?,
    val drainageRequired: Boolean,
)

@Serializable
data class SoilGuidance(
    val type: String,
    val components: List<String>,
)

@Serializable
data class HumidityRange(
    val minimumPercent: Int,
    val maximumPercent: Int,
)

@Serializable
data class TemperatureRange(
    val minimumCelsius: Int,
    val maximumCelsius: Int,
)

@Serializable
data class FertilizingGuidance(
    val strategy: FertilizingStrategy,
    val activeGrowth: FertilizingInterval,
    val inactiveGrowth: InactiveGrowthFertilizing,
)

@Serializable
data class FertilizingInterval(
    val minimumIntervalDays: Int?,
    val maximumIntervalDays: Int?,
)

@Serializable
data class InactiveGrowthFertilizing(
    val action: InactiveGrowthAction,
    val minimumIntervalDays: Int?,
    val maximumIntervalDays: Int?,
)

@Serializable
data class SpeciesToxicity(
    val toxicToPets: Boolean,
)

@Serializable
data class SpeciesGrowth(
    val rate: GrowthRate,
    val habit: GrowthHabit,
    val matureHeightCentimeters: CentimeterRange,
    val repotting: RepottingGuidance,
)

@Serializable
data class CentimeterRange(
    val minimum: Int,
    val maximum: Int,
)

@Serializable
data class RepottingGuidance(
    val action: RepottingAction,
    val minimumIntervalMonths: Int?,
    val maximumIntervalMonths: Int?,
    val timing: RepottingTiming?,
)

@Serializable
enum class LightLevel { LOW, MEDIUM_INDIRECT, BRIGHT_INDIRECT, DIRECT }

@Serializable
enum class WateringMode { SUBSTRATE_CHECK, FIXED_INTERVAL }

@Serializable
enum class WateringTrigger {
    SURFACE_DRYING,
    TOP_QUARTER_DRY,
    TOP_THIRD_DRY,
    TOP_HALF_DRY,
    ALMOST_COMPLETELY_DRY,
    COMPLETELY_DRY,
    BARK_ALMOST_DRY,
    MOUNTING_MEDIUM_NEARLY_DRY,
}

@Serializable
enum class WateringMethod {
    THOROUGH,
    BOTTOM_WATER,
    SOAK_ROOTS_AND_DRAIN,
    SOAK_AND_DRAIN,
    SOAK_AND_DRY_WITHIN_4_HOURS,
    WATER_SOIL_AVOID_CROWN,
}

@Serializable
enum class SeasonalContext { ACTIVE_GROWTH }

@Serializable
enum class FertilizingStrategy { SEASONAL, YEAR_ROUND, NONE }

@Serializable
enum class InactiveGrowthAction { PAUSE, REDUCE, SAME }

@Serializable
enum class GrowthRate { SLOW, MEDIUM, FAST }

@Serializable
enum class GrowthHabit { UPRIGHT, TRAILING, CLIMBING, CLUMPING, ROSETTE, BUSHY }

@Serializable
enum class RepottingAction { REPOT, REMOUNT, NONE }

@Serializable
enum class RepottingTiming { AFTER_FLOWERING, BEFORE_ACTIVE_GROWTH }
