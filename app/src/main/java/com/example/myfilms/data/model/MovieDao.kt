package com.example.myfilms.data.model


import androidx.room.*

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<Movie>)

    @Query("SELECT * FROM movie_table")
    fun getAll(): List<Movie>

    @Query("SELECT * FROM movie_table WHERE id == :movieId ")
    fun getMovieById(movieId: Int): Movie

    @Query("SELECT ALL * FROM movie_table WHERE isLiked == 1 ")
    fun getFavouriteMovies(): List<Movie>

    @Update
    fun changeLiked(movie: Movie)
}