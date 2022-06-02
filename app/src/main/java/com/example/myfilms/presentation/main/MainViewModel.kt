package com.example.myfilms.presentation.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfilms.data.repository.MovieRepositoryImpl
import com.example.myfilms.domain.UseCases.DeleteSessionUseCase
import kotlinx.coroutines.launch

class MainViewModel( application: Application ,
                   private val deleteSessionUseCase: DeleteSessionUseCase) : AndroidViewModel(application) {


    fun deleteSession() {
        viewModelScope.launch {
           deleteSessionUseCase.invoke()
        }
    }
}