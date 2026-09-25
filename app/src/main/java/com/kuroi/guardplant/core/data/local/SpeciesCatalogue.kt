package com.kuroi.guardplant.core.data.local

import android.content.res.AssetManager
import com.kuroi.guardplant.core.data.SpeciesRepository
import com.kuroi.guardplant.core.model.HumidityLevel
import com.kuroi.guardplant.core.model.MatureSize
import com.kuroi.guardplant.core.model.Species
import com.kuroi.guardplant.core.model.SpeciesFilters
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.json.Json

private const val CatalogueAssetPath = "data/plant_species.json"

object SpeciesCatalogueParser {
    private val json = Json {
        ignoreUnknownKeys = false
        isLenient = false
        explicitNulls = true
    }

    fun parse(content: String): List<Species> = json.decodeFromString(content)
}

class SpeciesAssetDataSource(
    private val assets: AssetManager,
) {
    fun load(): List<Species> = assets.open(CatalogueAssetPath).bufferedReader().use { reader ->
        SpeciesCatalogueParser.parse(reader.readText())
    }
}

class LocalSpeciesRepository(
    catalogue: List<Species>,
) : SpeciesRepository {
    private val catalogue = catalogue.toList()
    private val speciesById = this.catalogue.associateBy(Species::speciesId)
    private val _favouriteSpeciesIds = MutableStateFlow<Set<String>>(emptySet())

    override val species: StateFlow<List<Species>> = MutableStateFlow(this.catalogue).asStateFlow()
    override val favouriteSpeciesIds: StateFlow<Set<String>> = _favouriteSpeciesIds.asStateFlow()

    override fun getSpeciesById(speciesId: String): Species? = speciesById[speciesId]

    override fun searchSpecies(query: String): List<Species> {
        val term = query.trim()
        if (term.isEmpty()) return catalogue
        return catalogue.filter { plant ->
            plant.scientificName.contains(term, ignoreCase = true) ||
                plant.commonNames.any { name -> name.contains(term, ignoreCase = true) }
        }
    }

    override fun filterSpecies(filters: SpeciesFilters): List<Species> = catalogue.filter { plant ->
        (filters.light == null || plant.care.light == filters.light) &&
            (filters.difficulty == null || plant.care.difficulty == filters.difficulty) &&
            (filters.toxicToPets == null || plant.toxicity.toxicToPets == filters.toxicToPets) &&
            (filters.growthHabit == null || plant.growth.habit == filters.growthHabit) &&
            (filters.humidity == null || plant.humidityLevel() == filters.humidity) &&
            (filters.matureSize == null || plant.matureSize() == filters.matureSize)
    }

    override fun toggleFavourite(speciesId: String) {
        if (speciesId !in speciesById) return
        _favouriteSpeciesIds.update { current ->
            if (speciesId in current) current - speciesId else current + speciesId
        }
    }
}

fun Species.humidityLevel(): HumidityLevel = when (care.humidity.minimumPercent) {
    in Int.MIN_VALUE..30 -> HumidityLevel.AVERAGE
    in 31..50 -> HumidityLevel.MODERATE
    else -> HumidityLevel.HIGH
}

fun Species.matureSize(): MatureSize = when (growth.matureHeightCentimeters.maximum) {
    in Int.MIN_VALUE..60 -> MatureSize.COMPACT
    in 61..150 -> MatureSize.MEDIUM
    else -> MatureSize.LARGE
}
