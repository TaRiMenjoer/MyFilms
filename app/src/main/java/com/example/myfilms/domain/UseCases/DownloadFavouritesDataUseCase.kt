package com.example.myfilms.domain.UseCases

import com.example.myfilms.data.model.Movie
import com.example.myfilms.domain.MovieRepository.MovieRepository

class DownloadFavouritesDataUseCase(private val repository: MovieRepository) {
    suspend operator fun invoke(session: String):List<Movie>? {
        return repository.downloadFavouritesData(session)
    }
}