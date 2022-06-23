package com.example.myfilms.di

import android.content.Context
import android.content.SharedPreferences
import com.example.myfilms.data.ApiFactory
import com.example.myfilms.data.ApiService
import com.example.myfilms.data.model.DataBase
import com.example.myfilms.data.model.MovieDao
import com.example.myfilms.data.repository.MovieRepositoryImpl
import com.example.myfilms.domain.MovieRepository.MovieRepository
import com.example.myfilms.domain.UseCases.*
import com.example.myfilms.presentation.login.ViewModelLogin
import com.example.myfilms.presentation.main.MainViewModel
import com.example.myfilms.presentation.movieDetails.ViewModelDetails
import com.example.myfilms.presentation.movieFavourites.ViewModelFavourites
import com.example.myfilms.presentation.movies.ViewModelMovie
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val networkModule = module {
    single { getRetrofitService() }
    single { getSharedPreferences(context = get()) }
}

val daoModule = module {
    single { getPostDao(context = get()) }
}
val repositoryModule = module{
    single<MovieRepository> {MovieRepositoryImpl(application = get() ,  apiService = get() , movieDao = get() , prefSettings = get())}
}

val viewModelModule = module {
    viewModel { MainViewModel(application = get() , deleteSessionUseCase = get()) }
    viewModel { ViewModelDetails(application = get() , getMovieByIdUseCase = get() ,
        addOrRemoveFavouritesUseCase = get() , postFavouriteMoviesUseCase = get() ,
        getCreditResponseUseCase = get()) }
    viewModel { ViewModelFavourites(application = get() , downloadFavouritesDataUseCase = get()) }
    viewModel { ViewModelLogin(application = get() , getResponseSessionUseCase = get()) }
    viewModel { ViewModelMovie(application = get() , downloadMovieListUseCase = get()) }
}


val useCaseModule = module {
    single { DownloadMovieListUseCase(repository = get()) }
    single { GetMovieByIdUseCase(repository = get()) }
    single { AddOrRemoveFavouritesUseCase(repository = get()) }
    single { PostFavouriteMoviesUseCase(repository = get()) }
    single { DownloadFavouritesDataUseCase(repository = get()) }
    single { GetResponseSessionUseCase(repository = get()) }
    single { DeleteSessionUseCase(repository = get()) }
    single { GetCreditResponseUseCase(repository = get())}
}

val appModule = networkModule + daoModule + repositoryModule + viewModelModule + useCaseModule

private fun getRetrofitService():ApiService = ApiFactory.getInstance()
private fun getPostDao(context : Context): MovieDao = DataBase.getDataBase(context).movieDao()
private fun getSharedPreferences(context: Context) : SharedPreferences{
    return context.getSharedPreferences(
        "Settings",
        Context.MODE_PRIVATE
    ) as SharedPreferences
}