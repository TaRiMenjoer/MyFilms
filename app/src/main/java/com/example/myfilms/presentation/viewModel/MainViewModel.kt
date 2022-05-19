package com.example.myfilms.presentation.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfilms.data.repository.MovieRepository
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MovieRepository(application)

    fun deleteSession() {
        viewModelScope.launch {
           repository.deleteSession()
        }
    }
}