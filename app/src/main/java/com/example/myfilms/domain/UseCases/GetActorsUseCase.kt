package com.example.myfilms.domain.UseCases

import com.example.myfilms.data.model.CreditResponse
import com.example.myfilms.domain.MovieRepository.MovieRepository
import retrofit2.Response

class GetCreditResponseUseCase(private val repository: MovieRepository) {
    suspend operator fun invoke(movieId:Int): Response<CreditResponse> {
        return repository.getCreditResponse(movieId)
    }
}
