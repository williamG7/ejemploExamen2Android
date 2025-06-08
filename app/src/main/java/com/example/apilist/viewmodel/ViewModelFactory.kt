package com.example.apilist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.apilist.data.network.Repository

class MyViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(APIviewmodel::class.java)) {
            return APIviewmodel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}