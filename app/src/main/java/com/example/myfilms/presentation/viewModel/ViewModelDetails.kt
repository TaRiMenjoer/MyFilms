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

class ViewModelDetails(
    application: Application
) : AndroidViewModel(application) {


    private val repository = MovieRepository(application)

    var context = application

    private val _movie = MutableLiveData<Movie>()
    val movie: LiveData<Movie>
        get() = _movie

    private val _loadingState = MutableLiveData<LoadingState>()
    val loadingState: LiveData<LoadingState>
        get() = _loadingState


    fun getMovieById(movieId: Int, session_id: String) {

        _loadingState.value = LoadingState.IS_LOADING

        viewModelScope.launch {
            _movie.value = repository.getMovieById(movieId , session_id)
        }
        _loadingState.value = LoadingState.FINISHED
        _loadingState.value = LoadingState.SUCCESS
    }

    fun addOrRemoveFavourites(movieId: Int, session: String) {
        _loadingState.value = LoadingState.IS_LOADING
        viewModelScope.launch {

            val movie = repository.addOrRemoveFavourites(movieId , session)
            _movie.value = movie

            repository.postFavouriteMovie(movieId , session , movie )

            _loadingState.value = LoadingState.FINISHED
            _loadingState.value = LoadingState.SUCCESS
        }
    }
}