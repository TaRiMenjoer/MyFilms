package com.example.myfilms.presentation.movieDetails

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myfilms.data.model.Movie
import com.example.myfilms.domain.UseCases.AddOrRemoveFavouritesUseCase
import com.example.myfilms.domain.UseCases.GetMovieByIdUseCase
import com.example.myfilms.domain.UseCases.PostFavouriteMoviesUseCase
import com.example.myfilms.presentation.common.Utils.LoadingState
import kotlinx.coroutines.launch

class ViewModelDetails(
    application: Application  ,private val getMovieByIdUseCase: GetMovieByIdUseCase,
    private val addOrRemoveFavouritesUseCase: AddOrRemoveFavouritesUseCase,
    private val postFavouriteMoviesUseCase: PostFavouriteMoviesUseCase
) : AndroidViewModel(application) {


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
            _movie.value = getMovieByIdUseCase.invoke(movieId , session_id)
        }
        _loadingState.value = LoadingState.FINISHED
        _loadingState.value = LoadingState.SUCCESS
    }

    fun addOrRemoveFavourites(movieId: Int, session: String) {
        _loadingState.value = LoadingState.IS_LOADING
        viewModelScope.launch {

            val movie = addOrRemoveFavouritesUseCase.invoke(movieId , session)
            _movie.value = movie

            postFavouriteMoviesUseCase.invoke(movieId , session , movie)

            _loadingState.value = LoadingState.FINISHED
            _loadingState.value = LoadingState.SUCCESS
        }
    }
}