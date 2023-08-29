package com.kuroi.guardplant.api

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface PlantIdAPI {
    @POST("/identification")
    fun identificationRequest(@Body body: RequestBody): Call<PlantIdentificationResponse>

    @POST("/health_assessment")
    fun plantHealthRequest(@Body body: RequestBody): Call<ResponseBody>
}