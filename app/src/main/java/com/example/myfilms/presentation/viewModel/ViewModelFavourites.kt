package com.example.myfilms.presentation.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myfilms.data.model.Movie
import com.example.myfilms.data.repository.MovieRepository
import com.example.myfilms.presentation.Utils.LoadingState
import kotlinx.coroutines.launch

class ViewModelFavourites(
    application: Application
) : AndroidViewModel(application) {


    private val repository = MovieRepository(application)


    private val _movies = MutableLiveData<List<Movie>?>()
    val movies: LiveData<List<Movie>?>
        get() = _movies

    private val _loadingState = MutableLiveData<LoadingState>()
    val loadingState: LiveData<LoadingState>
        get() = _loadingState


    fun downloadData(session: String) {

        viewModelScope.launch {

            _loadingState.value = LoadingState.IS_LOADING
            _movies.value = repository.downloadFavouritesData(session)
            _loadingState.value = LoadingState.FINISHED
            _loadingState.value = LoadingState.SUCCESS
        }
    }
}