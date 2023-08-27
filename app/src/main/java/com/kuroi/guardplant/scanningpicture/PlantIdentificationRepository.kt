package com.kuroi.guardplant.scanningpicture

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.kuroi.guardplant.api.PlantIdAPI
import com.kuroi.guardplant.api.PlantIdentificationResponse
import com.kuroi.guardplant.api.Suggestion
import com.kuroi.guardplant.api.retrofit.RetrofitPlantIdInstance
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlantIdentificationRepository {
    private val apiService = RetrofitPlantIdInstance.create(PlantIdAPI::class.java)
    fun identifyPlant(imageBase64: String): LiveData<Suggestion> {
        val resultLiveData = MutableLiveData<Suggestion>()
        val imageAsJsonArray = Gson().toJsonTree(arrayListOf(imageBase64))

        val requestBody = RequestBody.create(MediaType.parse("application/json"), imageAsJsonArray.toString())
        apiService.identificationRequest(requestBody).enqueue(object : Callback<PlantIdentificationResponse> {
            override fun onResponse(
                call: Call<PlantIdentificationResponse>,
                response: Response<PlantIdentificationResponse>
            ) {
                if(response.isSuccessful) {
                    val result = response.body()?.plantIdentResult
                    val suggestedPlant = result?.classification?.suggestions?.firstOrNull() ?: Suggestion("", emptyList())
                    resultLiveData.value = suggestedPlant
                    Log.d("onResponse:", response.toString())
                } else {
                    Log.d("onResponse error:", response.toString())
                }
            }

            override fun onFailure(call: Call<PlantIdentificationResponse>, t: Throwable) {
                TODO("Not yet implemented")
                Log.d("onFailure :", t.toString())
            }
        })
        return resultLiveData
    }

}

