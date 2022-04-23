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
import com.example.myfilms.data.model.Session
import com.example.myfilms.presentation.Utils.LoadingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewModelMovie(
    private val context: Context
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

    fun downloadData(page: Int) {

        viewModelScope.launch {
            _loadingState.value = LoadingState.IS_LOADING
            val list = withContext(Dispatchers.IO) {

                try {

                    //_loadingState.value = LoadingState.IS_LOADING
                    val response = apiService.getMovies(page = page)
                    if (response.isSuccessful) {

                        //_movies.value = response.body()?.movies as List<Movie>


                        val result =  response.body()?.movies


                        if (!result.isNullOrEmpty()) {
                            movieDao.insertAll(result)
                        }

//                        _loadingState.value = LoadingState.FINISHED
//                        _loadingState.value = LoadingState.SUCCESS

                        result
                    }
                    else {
                        movieDao.getAll()
                    }
                } catch (e: Exception){
                    movieDao.getAll()
                }
            }
            _movies.value = list
            _loadingState.value = LoadingState.FINISHED
            _loadingState.value = LoadingState.SUCCESS

        }
    }

    fun deleteSession(session: String) {
        viewModelScope.launch {
            apiService.deleteSession(sessionId = Session(session_id = session))
        }
    }
}