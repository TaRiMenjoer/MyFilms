package com.example.myfilms.domain.UseCases

import com.example.myfilms.data.model.Movie
import com.example.myfilms.domain.MovieRepository.MovieRepository

class GetMovieByIdUseCase(private val repository: MovieRepository) {
    suspend operator fun invoke (movieId: Int, session_id: String): Movie {
        return repository.getMovieById( movieId , session_id)
    }
}