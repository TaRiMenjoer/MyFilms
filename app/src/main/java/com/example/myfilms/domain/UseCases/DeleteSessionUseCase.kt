package com.example.myfilms.domain.UseCases

import com.example.myfilms.domain.MovieRepository.MovieRepository


class DeleteSessionUseCase(private val repository: MovieRepository) {
    suspend operator fun invoke() {
        return repository.deleteSession()
    }
}