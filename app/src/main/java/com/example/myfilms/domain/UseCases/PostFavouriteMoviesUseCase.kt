package com.example.myfilms.domain.UseCases

import com.example.myfilms.data.model.Movie
import com.example.myfilms.domain.MovieRepository.MovieRepository

class PostFavouriteMoviesUseCase(private val repository: MovieRepository) {
    suspend operator fun invoke(movieId: Int, session: String, movieFavourite: Movie) {
        return repository.postFavouriteMovie(movieId , session ,movieFavourite )
    }
}