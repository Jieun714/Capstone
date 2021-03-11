package com.example.capstone_navi_tab.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ExerciseViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is exercise Fragment"
    }
    val text: LiveData<String> = _text
}