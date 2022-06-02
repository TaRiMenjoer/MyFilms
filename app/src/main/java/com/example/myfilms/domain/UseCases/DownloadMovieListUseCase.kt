package com.example.myfilms.domain.UseCases

import com.example.myfilms.data.model.Movie
import com.example.myfilms.domain.MovieRepository.MovieRepository

class DownloadMovieListUseCase(private val repository: MovieRepository) {
    suspend operator fun invoke(): List<Movie>?{
        return repository.downloadData()
    }

}