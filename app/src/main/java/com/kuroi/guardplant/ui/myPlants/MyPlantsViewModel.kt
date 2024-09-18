package com.kuroi.guardplant.ui.myPlants

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyPlantsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "My Plants !"
    }
    val text: LiveData<String> = _text
}