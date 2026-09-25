package com.kuroi.guardplant.core.data

import android.content.Context
import com.kuroi.guardplant.core.data.fake.FakeCareRepository
import com.kuroi.guardplant.core.data.fake.FakePlantRepository
import com.kuroi.guardplant.core.data.local.LocalSpeciesRepository
import com.kuroi.guardplant.core.data.local.SpeciesAssetDataSource

class AppContainer(
    context: Context,
    val speciesRepository: SpeciesRepository = LocalSpeciesRepository(
        SpeciesAssetDataSource(context.assets).load(),
    ),
    val plantRepository: PlantRepository = FakePlantRepository(),
    val careRepository: CareRepository = FakeCareRepository(),
)
