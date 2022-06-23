package com.example.myfilms.presentation.movieDetails

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myfilms.data.model.Cast
import com.example.myfilms.data.model.Movie
import com.example.myfilms.domain.UseCases.*
import com.example.myfilms.presentation.common.Utils.LoadingState
import kotlinx.coroutines.launch

class ViewModelDetails(
    application: Application  ,private val getMovieByIdUseCase: GetMovieByIdUseCase,
    private val addOrRemoveFavouritesUseCase: AddOrRemoveFavouritesUseCase,
    private val postFavouriteMoviesUseCase: PostFavouriteMoviesUseCase ,
    private val getCreditResponseUseCase: GetCreditResponseUseCase
) : AndroidViewModel(application) {



    var context = application

    private val _movie = MutableLiveData<Movie>()
    val movie: LiveData<Movie>
        get() = _movie

    private val _loadingState = MutableLiveData<LoadingState>()
    val loadingState: LiveData<LoadingState>
        get() = _loadingState

    private val _actors = MutableLiveData<List<Cast>>()
    val actors: LiveData<List<Cast>>
        get() = _actors


    fun getMovieById(movieId: Int, session_id: String) {

        _loadingState.value = LoadingState.IS_LOADING

        viewModelScope.launch {
            _movie.value = getMovieByIdUseCase.invoke(movieId , session_id)
        }
        _loadingState.value = LoadingState.FINISHED
        _loadingState.value = LoadingState.SUCCESS
    }

    fun getCreditResponse(movieId: Int){
        viewModelScope.launch {
            _actors.value = getCreditResponseUseCase.invoke(movieId).body()?.cast
        }
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