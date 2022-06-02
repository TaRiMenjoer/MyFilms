package com.example.myfilms.domain.UseCases

import com.example.myfilms.data.model.LoginApprove
import com.example.myfilms.data.model.Session
import com.example.myfilms.domain.MovieRepository.MovieRepository
import retrofit2.Response

class GetResponseSessionUseCase(private val repository: MovieRepository) {
    suspend operator fun invoke(data: LoginApprove): Response<Session> {
        return repository.getResponseSession(data)
    }
}