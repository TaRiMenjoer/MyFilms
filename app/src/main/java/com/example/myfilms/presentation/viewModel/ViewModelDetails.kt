package com.example.myfilms.presentation.viewModel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfilms.R
import com.example.myfilms.data.ApiFactory
import com.example.myfilms.data.model.*
import com.example.myfilms.presentation.Utils.LoadingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewModelDetails(
    private val context: Context
) : ViewModel() {

    private val movieDao: MovieDao


    private val _movie = MutableLiveData<Movie>()
    val movie: LiveData<Movie>
        get() = _movie

    private val _loadingState = MutableLiveData<LoadingState>()
    val loadingState: LiveData<LoadingState>
        get() = _loadingState

    private val apiService = ApiFactory.getInstance()

    init {
        movieDao = DataBase.getDataBase(context).movieDao()
    }

    fun getMovieById(movieId: Int, session_id: String) {

        _loadingState.value = LoadingState.IS_LOADING

        viewModelScope.launch {

            val movieForLD = withContext(Dispatchers.IO) {

                try {

                    val response = apiService.getFavorites(session_id = session_id)
                    val movies = response.body()?.movies
                    val movie = movies?.find {
                        it.id == movieId
                    }
                    movie!!.isLiked = true
                    movie

                } catch (e: Exception) {
                    movieDao.getMovieById(movieId)
                }
            }

            _movie.value = movieForLD
            _loadingState.value = LoadingState.FINISHED
            _loadingState.value = LoadingState.SUCCESS
        }

    }

    fun addOrRemoveFavourites(movieId: Int, session: String) {
        _loadingState.value = LoadingState.IS_LOADING
        viewModelScope.launch {

            val movieFavouritesForLD = withContext(Dispatchers.IO) {

                val movie = movieDao.getMovieById(movieId)
                val changedMovie = movie.copy(isLiked = !movie.isLiked)
                movieDao.changeLiked(changedMovie)
                changedMovie
            }

            _movie.value = movieFavouritesForLD
            try {
                val isLiked = movieFavouritesForLD.isLiked
                val postMovie = PostMovie(media_id = movieId, favorite = isLiked)
                apiService.addFavorite(session_id = session, postMovie = postMovie)
            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    R.string.no_enternet_connection,
                    Toast.LENGTH_SHORT
                ).show()
            }

            _loadingState.value = LoadingState.FINISHED
            _loadingState.value = LoadingState.SUCCESS
        }
    }
}