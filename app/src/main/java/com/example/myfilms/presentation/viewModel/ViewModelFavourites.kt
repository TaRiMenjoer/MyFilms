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
import com.example.myfilms.presentation.Utils.LoadingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewModelFavourites(
    val context: Context
) : ViewModel() {

    private val movieDao: MovieDao

    private val apiService = ApiFactory.getInstance()

    private val _movies = MutableLiveData<List<Movie>?>()
    val movies: LiveData<List<Movie>?>
        get() = _movies

    private val _loadingState = MutableLiveData<LoadingState>()
    val loadingState: LiveData<LoadingState>
        get() = _loadingState

    init {
        movieDao = DataBase.getDataBase(context).movieDao()
    }

    fun downloadData(session: String) {

        viewModelScope.launch {

            _loadingState.value = LoadingState.IS_LOADING

            val list = withContext(Dispatchers.IO) {

                try {
                    val response = apiService.getFavorites(session_id = session)
                    if (response.isSuccessful) {

                        val result = response.body()?.movies
                        result
                    } else {
                        throw Exception("Can download Favourites")
                    }
                } catch (e: Exception) {
                    movieDao.getFavouriteMovies()
                }
            }

            _movies.value = list
            _loadingState.value = LoadingState.FINISHED
            _loadingState.value = LoadingState.SUCCESS
        }
    }
}