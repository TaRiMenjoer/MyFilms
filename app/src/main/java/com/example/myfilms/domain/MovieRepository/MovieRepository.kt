package com.example.myfilms.domain.MovieRepository

import com.example.myfilms.data.model.LoginApprove
import com.example.myfilms.data.model.Movie
import com.example.myfilms.data.model.Session
import retrofit2.Response

interface MovieRepository {
    suspend fun downloadData(): List<Movie>?
    suspend fun getMovieById(movieId: Int, session_id: String): Movie
    suspend fun addOrRemoveFavourites(movieId: Int, session: String): Movie
    suspend fun postFavouriteMovie(movieId: Int, session: String, movieFavourite: Movie)
    suspend fun downloadFavouritesData(session: String): List<Movie>?
    suspend fun getResponseSession(data: LoginApprove): Response<Session>
    suspend fun deleteSession()
}