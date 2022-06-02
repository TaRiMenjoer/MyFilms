package com.example.myfilms.presentation.movies

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myfilms.data.model.Movie
import com.example.myfilms.domain.UseCases.DownloadMovieListUseCase
import com.example.myfilms.presentation.common.Utils.LoadingState
import kotlinx.coroutines.launch

class ViewModelMovie(

    application: Application,

private val downloadMovieListUseCase: DownloadMovieListUseCase

) : AndroidViewModel(application) {


    private val _movies = MutableLiveData<List<Movie>?>()
    val movies: LiveData<List<Movie>?>
        get() = _movies

    private val _loadingState = MutableLiveData<LoadingState>()
    val loadingState: LiveData<LoadingState>
        get() = _loadingState

    private val _refreshingState = MutableLiveData<LoadingState>()
    val refreshingState: LiveData<LoadingState>
        get() = _refreshingState



    fun downloadData() {
        viewModelScope.launch {

            _loadingState.value = LoadingState.IS_LOADING
            _movies.value = downloadMovieListUseCase.invoke()
          //  _loadingState.value = LoadingState.SUCCESS
            _loadingState.value = LoadingState.FINISHED


        }
    }

    fun refreshingData() {
        viewModelScope.launch {

            _refreshingState.value = LoadingState.IS_LOADING
            _movies.value = downloadMovieListUseCase.invoke()
         //   _refreshingState.value = LoadingState.SUCCESS
            _refreshingState.value = LoadingState.FINISHED

        }
    }


}