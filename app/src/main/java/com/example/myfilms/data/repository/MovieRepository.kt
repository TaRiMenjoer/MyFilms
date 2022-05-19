package com.example.myfilms.data.repository

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import com.example.myfilms.R
import com.example.myfilms.data.ApiFactory
import com.example.myfilms.data.model.*
import com.example.myfilms.presentation.Utils.LoadingState
import com.example.myfilms.presentation.view.MainActivity
import com.example.myfilms.presentation.viewModel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class MovieRepository(application: Application) {


    private val movieDao: MovieDao = DataBase.getDataBase(application).movieDao()
    private val apiService = ApiFactory.getInstance()
    var context = application

    private var prefSettings: SharedPreferences = application.getSharedPreferences(
        APP_SETTINGS,
        Context.MODE_PRIVATE
    ) as SharedPreferences
    private var editor: SharedPreferences.Editor = prefSettings.edit()


    suspend fun downloadData(): List<Movie>? {

        return withContext(Dispatchers.Default) {

            try {
                val response = apiService.getMovies()

                if (response.isSuccessful && !MainActivity.isFirstDownloaded) {
                    MainActivity.isFirstDownloaded = true
                    val result = response.body()?.movies
                    if (!result.isNullOrEmpty()) {
                        movieDao.insertAll(result)
                    }
                    result
                } else {
                    movieDao.getAll()
                }
            } catch (e: Exception) {
                movieDao.getAll()

            }

        }

    }

    suspend fun getMovieById(movieId: Int, session_id: String): Movie {
        return withContext(Dispatchers.Default) {

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
    }

    suspend fun addOrRemoveFavourites(movieId: Int, session: String): Movie {
        return withContext(Dispatchers.Default) {
            val movie = movieDao.getMovieById(movieId)
            val changedMovie = movie.copy(isLiked = !movie.isLiked)
            movieDao.changeLiked(changedMovie)
            changedMovie
        }
    }

    suspend fun postFavouriteMovie(movieId: Int, session: String, movieFavourite: Movie) {
        withContext(Dispatchers.Default) {
            try {
                val isLiked = movieFavourite.isLiked
                val postMovie = PostMovie(media_id = movieId, favorite = isLiked)
                apiService.addFavorite(session_id = session, postMovie = postMovie)
            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    R.string.no_enternet_connection,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    suspend fun downloadFavouritesData(session: String): List<Movie>? {
        return withContext(Dispatchers.Default) {
            try {
                val response = apiService.getFavorites(session_id = session)
                if (response.isSuccessful) {

                    val result = response.body()?.movies
                    result
                } else {
                    throw Exception(context.getString(R.string.cannot_dawnload_favourite))
                }
            } catch (e: Exception) {
                movieDao.getFavouriteMovies()
            }
        }

    }

    suspend fun getResponseSession(data: LoginApprove): Response<Session> {

        try {
            val responseGet = apiService.getToken()
            if (responseGet.isSuccessful) {
                val loginApprove = LoginApprove(
                    username = data.username,
                    password = data.password,
                    request_token = responseGet.body()?.request_token as String
                )
                val responseApprove = apiService.approveToken(loginApprove = loginApprove)
                if (responseApprove.isSuccessful) {
                    val session =
                        apiService.createSession(token = responseApprove.body() as Token)
                    return session
                }
            }
        } catch (e: Exception) {

        }
        throw Exception("error ResponseSession")
    }


    suspend fun deleteSession() {
        withContext(Dispatchers.Default) {
            SESSION_ID = getSessionId()
            try {
                apiService.deleteSession(sessionId = Session(session_id = SESSION_ID))
            } catch (e: Exception) {
                editor.clear().commit()
            }
        }
    }

    private fun getSessionId(): String {
        var session = ""
        try {
            session =
                prefSettings.getString(SESSION_ID_KEY, "") as String
        } catch (e: Exception) {
        }
        return session
    }

    companion object {

        private var SESSION_ID = ""
        const val APP_SETTINGS = "Settings"
        const val SESSION_ID_KEY = "SESSION_ID"
    }

}