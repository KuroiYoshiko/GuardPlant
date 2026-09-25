package com.kuroi.guardplant.core.data

import com.kuroi.guardplant.core.model.CareAction
import com.kuroi.guardplant.core.model.CareTask
import com.kuroi.guardplant.core.model.PlantRoom
import com.kuroi.guardplant.core.model.Species
import com.kuroi.guardplant.core.model.SpeciesFilters
import com.kuroi.guardplant.core.model.UserPlant
import kotlinx.coroutines.flow.StateFlow

interface SpeciesRepository {
    val species: StateFlow<List<Species>>
    val favouriteSpeciesIds: StateFlow<Set<String>>
    fun getSpeciesById(speciesId: String): Species?
    fun searchSpecies(query: String): List<Species>
    fun filterSpecies(filters: SpeciesFilters): List<Species>
    fun toggleFavourite(speciesId: String)
}

interface PlantRepository {
    val rooms: StateFlow<List<PlantRoom>>
    val plants: StateFlow<List<UserPlant>>
    fun addDemoPlant(custom: Boolean, name: String)
}

interface CareRepository {
    val tasks: StateFlow<List<CareTask>>
    fun applyAction(taskId: String, action: CareAction)
}
