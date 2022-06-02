package com.example.myfilms.domain.UseCases

import com.example.myfilms.data.model.Movie
import com.example.myfilms.domain.MovieRepository.MovieRepository

class AddOrRemoveFavouritesUseCase(private val repository: MovieRepository) {
    suspend operator fun invoke(movieId: Int, session: String): Movie{
        return repository.addOrRemoveFavourites(movieId, session)
    }

}