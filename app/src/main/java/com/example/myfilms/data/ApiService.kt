package com.example.myfilms.data

import com.example.myfilms.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("discover/movie")
    suspend fun getMovies(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String = PARAMS_LANGUAGE,
        @Query("sort_by") sort_by: String = SORT_BY_POPULARITY,
        @Query("vote_count.gte") vote_count: Int = MIN_VOTE_COUNT_VALUE,
        @Query("page") page: Int = PARAMS_PAGE
    ):Response<Result>

    @GET("movie/{movie_id}")
    suspend fun getById(
        @Path("movie_id") id: Int,
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String = PARAMS_LANGUAGE
    ): Response<Movie>

    @GET("account/{account_id}/favorite/movies")
    suspend fun getFavoutitsMovies(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("session_id") sessionId: String = SESSION_ID,
        @Query("language") language: String = PARAMS_LANGUAGE
    ): Response<Movie>

    @GET("authentication/token/new")
    suspend fun getToken(
        @Query("api_key") apiKey: String = API_KEY,
    ): Response<Token>

    @POST("authentication/token/validate_with_login")
    suspend fun approveToken(
        @Query("api_key") apiKey: String = API_KEY,
        @Body loginApprove: LoginApprove
    ): Response<Token>

    @POST("authentication/session/new")
    suspend fun createSession(
        @Query("api_key") apiKey: String = API_KEY,
        @Body token: Token
    ): Response<Session>

    @Headers(
        "Accept: application/json;charset=utf-8",
        "Content-type: application/json;charset=utf-8"
    )



    @HTTP(method = "DELETE", path = "authentication/session", hasBody = true)
    suspend fun deleteSession(
        @Query("api_key") apiKey: String = API_KEY,
        @Body sessionId: Session
    )

    @GET("movie/{movie_id}/videos")
    suspend fun getVideos(
        @Path("movie_id") id: Int,
        @Query("api_key") apiKey: String = API_KEY,
        @Query("language") language: String = PARAMS_LANGUAGE
    ): Response<MovieVideos>

    companion object {

        private var SESSION_ID = ""
        private var API_KEY = "a14a376a2704b9c91446a56f236f5b50"
        private var PARAMS_LANGUAGE = "ru"
        private var SORT_BY_POPULARITY = "popularity.desc"
        private var MIN_VOTE_COUNT_VALUE = 1000
        private var PARAMS_PAGE = 1
    }
}