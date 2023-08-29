package com.kuroi.guardplant.scanningpicture

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.kuroi.guardplant.api.Suggestion

class ScannedPictureViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = PlantIdentificationRepository()

    fun identifyPlant(imageBase64: String): LiveData<Suggestion> {
        return repository.identifyPlant(imageBase64)
    }
}