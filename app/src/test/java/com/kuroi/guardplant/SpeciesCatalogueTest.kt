package com.kuroi.guardplant

import com.kuroi.guardplant.core.data.local.LocalSpeciesRepository
import com.kuroi.guardplant.core.data.local.SpeciesCatalogueParser
import com.kuroi.guardplant.core.model.FertilizingStrategy
import com.kuroi.guardplant.core.model.GrowthHabit
import com.kuroi.guardplant.core.model.HumidityLevel
import com.kuroi.guardplant.core.model.InactiveGrowthAction
import com.kuroi.guardplant.core.model.LightLevel
import com.kuroi.guardplant.core.model.MatureSize
import com.kuroi.guardplant.core.model.RepottingAction
import com.kuroi.guardplant.core.model.RepottingTiming
import com.kuroi.guardplant.core.model.SeasonalContext
import com.kuroi.guardplant.core.model.Species
import com.kuroi.guardplant.core.model.SpeciesFilters
import com.kuroi.guardplant.core.model.WateringMethod
import com.kuroi.guardplant.core.model.WateringMode
import com.kuroi.guardplant.core.model.WateringTrigger
import java.io.File
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class SpeciesCatalogueTest {
    private val catalogueContent: String by lazy {
        projectFile("app/src/main/assets/data/plant_species.json", "src/main/assets/data/plant_species.json").readText()
    }
    private val catalogue: List<Species> by lazy { SpeciesCatalogueParser.parse(catalogueContent) }
    private val repository by lazy { LocalSpeciesRepository(catalogue) }

    @Test
    fun catalogueParsesWithSeventyUniqueSpecies() {
        assertEquals(70, catalogue.size)
        assertEquals(70, catalogue.map(Species::speciesId).toSet().size)
    }

    @Test
    fun lookupByIdReturnsStableCatalogueSpecies() {
        val species = repository.getSpeciesById("monstera_deliciosa")

        assertNotNull(species)
        assertEquals("Monstera deliciosa", species?.scientificName)
        assertNull(repository.getSpeciesById("not_a_species"))
    }

    @Test
    fun searchMatchesScientificNamesCaseInsensitively() {
        val matches = repository.searchSpecies("PHALAENOPSIS AMABILIS")

        assertEquals(listOf("phalaenopsis_amabilis"), matches.map(Species::speciesId))
    }

    @Test
    fun searchMatchesCommonNamesCaseInsensitively() {
        val matches = repository.searchSpecies("swiss cheese vine")

        assertTrue(matches.any { it.speciesId == "monstera_adansonii" })
    }

    @Test
    fun representativeDiscoverFiltersUseStructuredCatalogueValues() {
        val directLight = repository.filterSpecies(SpeciesFilters(light = LightLevel.DIRECT))
        val clumping = repository.filterSpecies(SpeciesFilters(growthHabit = GrowthHabit.CLUMPING))
        val highHumidity = repository.filterSpecies(SpeciesFilters(humidity = HumidityLevel.HIGH))
        val compact = repository.filterSpecies(SpeciesFilters(matureSize = MatureSize.COMPACT))
        val petSafe = repository.filterSpecies(SpeciesFilters(toxicToPets = false))

        assertTrue(directLight.isNotEmpty() && directLight.all { it.care.light == LightLevel.DIRECT })
        assertTrue(clumping.isNotEmpty() && clumping.all { it.growth.habit == GrowthHabit.CLUMPING })
        assertTrue(highHumidity.isNotEmpty() && highHumidity.all { it.care.humidity.minimumPercent >= 60 })
        assertTrue(compact.isNotEmpty() && compact.all { it.growth.matureHeightCentimeters.maximum <= 60 })
        assertTrue(petSafe.isNotEmpty() && petSafe.all { !it.toxicity.toxicToPets })
    }

    @Test
    fun tillandsiaSpecialGuidanceParsesCorrectly() {
        val species = requireNotNull(repository.getSpeciesById("tillandsia_ionantha"))

        assertEquals(WateringMode.FIXED_INTERVAL, species.care.watering.mode)
        assertNull(species.care.watering.trigger)
        assertEquals(7, species.care.watering.intervalDays)
        assertEquals(WateringMethod.SOAK_AND_DRY_WITHIN_4_HOURS, species.care.watering.method)
        assertEquals(RepottingAction.NONE, species.growth.repotting.action)
        assertEquals(FertilizingStrategy.SEASONAL, species.care.fertilizing.strategy)
        assertEquals(InactiveGrowthAction.PAUSE, species.care.fertilizing.inactiveGrowth.action)
    }

    @Test
    fun phalaenopsisSpecialGuidanceParsesCorrectly() {
        val species = requireNotNull(repository.getSpeciesById("phalaenopsis_amabilis"))

        assertEquals(WateringTrigger.BARK_ALMOST_DRY, species.care.watering.trigger)
        assertEquals(WateringMethod.SOAK_ROOTS_AND_DRAIN, species.care.watering.method)
        assertEquals(7, species.care.fertilizing.activeGrowth.minimumIntervalDays)
        assertEquals(InactiveGrowthAction.REDUCE, species.care.fertilizing.inactiveGrowth.action)
    }

    @Test
    fun caladiumSpecialGuidanceParsesCorrectly() {
        val species = requireNotNull(repository.getSpeciesById("caladium_bicolor"))

        assertEquals(SeasonalContext.ACTIVE_GROWTH, species.care.watering.seasonalContext)
        assertEquals(RepottingTiming.BEFORE_ACTIVE_GROWTH, species.growth.repotting.timing)
        assertEquals(14, species.care.fertilizing.activeGrowth.minimumIntervalDays)
        assertEquals(InactiveGrowthAction.PAUSE, species.care.fertilizing.inactiveGrowth.action)
    }

    @Test
    fun platyceriumSpecialGuidanceParsesCorrectly() {
        val species = requireNotNull(repository.getSpeciesById("platycerium_bifurcatum"))

        assertEquals(WateringTrigger.MOUNTING_MEDIUM_NEARLY_DRY, species.care.watering.trigger)
        assertEquals(WateringMethod.SOAK_AND_DRAIN, species.care.watering.method)
        assertEquals(RepottingAction.REMOUNT, species.growth.repotting.action)
        assertEquals(InactiveGrowthAction.REDUCE, species.care.fertilizing.inactiveGrowth.action)
    }

    @Test
    fun catalogueIdsMatchClassifierStableIds() {
        val classifierIds = Json.decodeFromString<List<String>>(
            projectFile("ml/models/yolo26n_cls/class_names.json", "../ml/models/yolo26n_cls/class_names.json").readText(),
        )

        assertEquals(classifierIds.toSet(), catalogue.map(Species::speciesId).toSet())
        assertEquals(classifierIds.size, classifierIds.toSet().size)
    }

    @Test
    fun favouritesRemainSeparateMutableUserState() {
        val original = requireNotNull(repository.getSpeciesById("epipremnum_aureum"))
        assertFalse(original.speciesId in repository.favouriteSpeciesIds.value)

        repository.toggleFavourite(original.speciesId)

        assertTrue(original.speciesId in repository.favouriteSpeciesIds.value)
        assertEquals(original, repository.getSpeciesById(original.speciesId))
    }

    @Test
    fun unknownEnumValuesAreRejected() {
        val invalid = catalogueContent.replaceFirst("\"BRIGHT_INDIRECT\"", "\"UNKNOWN_LIGHT\"")

        assertThrows(SerializationException::class.java) { SpeciesCatalogueParser.parse(invalid) }
    }

    private fun projectFile(vararg candidates: String): File = candidates
        .asSequence()
        .map(::File)
        .firstOrNull(File::isFile)
        ?: error("Could not locate any of: ${candidates.joinToString()}")
}
