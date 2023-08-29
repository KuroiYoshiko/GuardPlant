package com.kuroi.guardplant.api

import com.google.gson.annotations.SerializedName

data class PlantIdentificationResponse(
    @SerializedName("result") val plantIdentResult: PlantIdentResult
)

data class PlantIdentResult(
    val classification: Classification
)

data class Classification(
    val suggestions: List<Suggestion>
)

data class Suggestion(
    val name: String,
    val similar_images: List<SimilarImage>
)

data class SimilarImage(
    val url: String
)