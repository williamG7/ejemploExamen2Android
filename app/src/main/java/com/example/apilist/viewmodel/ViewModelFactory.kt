package com.example.apilist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.apilist.data.network.SettingsRepository

class MyViewModelFactory(private val repository: SettingsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(APIviewmodel::class.java)) {
            return APIviewmodel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}