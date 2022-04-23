package com.example.myfilms.presentation.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfilms.data.ApiFactory
import com.example.myfilms.data.model.DataBase
import com.example.myfilms.data.model.Movie
import com.example.myfilms.data.model.MovieDao
import com.example.myfilms.data.model.MovieVideos
import com.example.myfilms.presentation.Utils.LoadingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewModelDetails(
    private val context: Context
) : ViewModel() {

    private val movieDao: MovieDao
    private val apiService = ApiFactory.getInstance()

    private val _movie = MutableLiveData<Movie>()
    val movie: LiveData<Movie>
        get() = _movie

    private val _videos = MutableLiveData<MovieVideos>()
    val videos: LiveData<MovieVideos>
        get() = _videos

    private val _loadingState = MutableLiveData<LoadingState>()
    val loadingState: LiveData<LoadingState>
        get() = _loadingState


    init {
        movieDao = DataBase.getDataBase(context).movieDao()
    }

//    fun getMovieById(movieId: Int) {
//        _loadingState.value = LoadingState.IS_LOADING
//
//            viewModelScope.launch {
//                //  _loadingState.value = LoadingState.IS_LOADING
//              //  val responseMovie = apiService.getById(movieId)
//                val responseMovie = apiService.getById(movieId)
//                if (responseMovie.isSuccessful) {
//                    _movie.value = responseMovie.body()
//                }
//                val responseVideo = apiService.getVideos(movieId)
//                if (responseVideo.isSuccessful) {
//                    _videos.value = responseVideo.body()
//                }
//                _loadingState.value = LoadingState.FINISHED
//                _loadingState.value = LoadingState.SUCCESS
//            }
//
//    }

    fun getMovieById(movieId: Int) {
        _loadingState.value = LoadingState.IS_LOADING

        viewModelScope.launch {


            val movieForLD = withContext(Dispatchers.IO) {

                try {
                    val movie = movieDao.getMovieById(movieId)

                    //  _movie.value = movie
                    movie
                } catch (e: Exception){
                    movieDao.getMovieById(movieId)
                }
                    // val responseVideo = apiService.getVideos(movieId)
//            if (responseVideo.isSuccessful) {
//                _videos.value = responseVideo.body()
//            }

            }
            _movie.value = movieForLD

            _loadingState.value = LoadingState.FINISHED
            _loadingState.value = LoadingState.SUCCESS
        }

    }


}