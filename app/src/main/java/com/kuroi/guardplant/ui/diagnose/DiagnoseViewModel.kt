package com.kuroi.guardplant.ui.diagnose

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DiagnoseViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Diagnose !"
    }
    val text: LiveData<String> = _text
}